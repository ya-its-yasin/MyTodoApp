package com.app.mytodo.service;

import com.app.mytodo.bean.MyTaskStatusBean;
import com.app.mytodo.config.DataSaver;
import com.app.mytodo.entity.MyTaskStatus;
import com.app.mytodo.repository.MyTaskRepository;
import com.app.mytodo.repository.MyTaskStatusRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
public class StatusMenuService {

    private static final Logger logger = LoggerFactory.getLogger(StatusMenuService.class);

    @Autowired
    private MyTaskStatusRepository myTaskStatusRepository;

    @Autowired
    private DataSaver dataSaver;

    @Autowired
    private MyTaskRepository myTaskRepository;

    public List<MyTaskStatusBean> getAllStatuses() {
        return MyTaskStatusBean.mapAllToBeans(myTaskStatusRepository.findAllByOrderByDisplayOrderId());
    }

    public void updateStatus(MyTaskStatusBean statusBean) {
        MyTaskStatus entity = myTaskStatusRepository.findById(statusBean.getId()).orElse(null);
        if (entity != null) {
            String oldStatus = null;
            if(!entity.getStatus().equals(statusBean.getStatus())){
                oldStatus = entity.getStatus();
                myTaskRepository.updateStatusToNew(entity.getStatus(), statusBean.getStatus());
            }
            entity.setStatus(statusBean.getStatus());
            entity.setAddable(statusBean.isAddable());
            entity.setEditable(statusBean.isEditable());
            entity.setDeletable(statusBean.isDeletable());
            entity.setArchivable(statusBean.isArchivable());
            entity.setRightStatus(statusBean.getRight());
            entity.setLeftStatus(statusBean.getLeft());
            entity.setJumpToStatus(statusBean.getJumpTo());
            entity.setUpdatedAt(LocalDateTime.now());
            if(!entity.getDisplayOrderId().equals(statusBean.getDisplayOrderId())){
                checkAndMoveOrder(entity.getDisplayOrderId(), statusBean.getDisplayOrderId());
                entity.setDisplayOrderId(statusBean.getDisplayOrderId());
                entity.setId(null);
                addStatus(entity);
            }else{
                myTaskStatusRepository.save(entity);
            }
            if(oldStatus!=null)
                updateInStatusTable(oldStatus, statusBean.getStatus());
            logger.info("Updated the task {}", statusBean);
            dataSaver.saveStatusMenuData();
        }
    }

    private void updateInStatusTable(String oldStatus, String newStatus) {
        List<MyTaskStatus> statuses = myTaskStatusRepository.findAll();
        List<MyTaskStatus> updatedStatuses = new ArrayList<>();
        for (MyTaskStatus myTaskStatus : statuses) {
            boolean updated = false;
            if (oldStatus.equals(myTaskStatus.getLeftStatus())) {
                myTaskStatus.setLeftStatus(newStatus);
                updated = true;
            }
            if (oldStatus.equals(myTaskStatus.getRightStatus())) {
                myTaskStatus.setRightStatus(newStatus);
                updated = true;
            }
            if (oldStatus.equals(myTaskStatus.getJumpToStatus())) {
                myTaskStatus.setJumpToStatus(newStatus);
                updated = true;
            }
            if (updated) {
                updatedStatuses.add(myTaskStatus);
            }
        }
        myTaskStatusRepository.saveAll(updatedStatuses);
    }

    private void checkAndMoveOrder(Integer oldOrderId, Integer newOrderId) {
        List<MyTaskStatus> statuses = myTaskStatusRepository.findAllByOrderByDisplayOrderId();
        for(MyTaskStatus myTaskStatus : statuses){
            if(Objects.equals(myTaskStatus.getDisplayOrderId(), oldOrderId)){
                deleteStatus(myTaskStatus.getId());
            }
        }
    }

    public void deleteStatus(Integer id) {
        Optional<MyTaskStatus> myTaskStatus = myTaskStatusRepository.findById(id);
        myTaskStatusRepository.deleteById(id);
        myTaskStatus.ifPresent(status -> myTaskStatusRepository.checkAndMoveOrderBackward(status.getDisplayOrderId()));
        logger.info("Deleting the status {}", myTaskStatus);
        dataSaver.saveStatusMenuData();
    }

    public void createStatus(MyTaskStatusBean statusBean) {
        MyTaskStatus myTaskStatus = MyTaskStatusBean.mapToEntity(statusBean);
        myTaskStatus.setCreatedAt(LocalDateTime.now());
        myTaskStatus.setUpdatedAt(LocalDateTime.now());
        addStatus(myTaskStatus);
    }

    public void addStatus(MyTaskStatus myTaskStatus) {
        Optional<MyTaskStatus> existingMyTaskStatus = myTaskStatusRepository.findByDisplayOrderId(myTaskStatus.getDisplayOrderId());
        if(existingMyTaskStatus.isPresent())
            myTaskStatusRepository.checkAndMoveOrderForward(myTaskStatus.getDisplayOrderId());
        myTaskStatusRepository.save(myTaskStatus);
        logger.info("Adding new status {}", myTaskStatus);
        dataSaver.saveStatusMenuData();
    }
}
