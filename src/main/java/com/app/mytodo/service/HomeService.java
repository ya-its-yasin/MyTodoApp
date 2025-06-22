package com.app.mytodo.service;

import com.app.mytodo.bean.MyTaskBean;
import com.app.mytodo.config.DataSaver;
import com.app.mytodo.entity.MyTask;
import com.app.mytodo.entity.MyTaskStatus;
import com.app.mytodo.repository.MyTaskRepository;
import com.app.mytodo.repository.MyTaskStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Transactional
@Service
public class HomeService {

    private static final Logger logger = LoggerFactory.getLogger(HomeService.class);

    @Autowired
    MyTaskRepository myTaskRepository;

    @Autowired
    MyTaskStatusRepository myTaskStatusRepository;

    @Autowired
    DataSaver dataSaver;

    @Value("${myapp.done.cutoff.days}")
    public Integer DONE_CUTOFF_DAYS;

    public Map<MyTaskStatus, List<MyTaskBean>> getAllTasks() {
        List<MyTaskStatus> statuses = myTaskStatusRepository.findAllByOrderByDisplayOrderId();
        List<MyTaskBean> taskBeans = MyTaskBean.mapAllToBeans(myTaskRepository.findAll());
        List<MyTaskBean> unMappedTasks = MyTaskBean.mapAllToBeans(
                myTaskRepository.findAllByStatusNotIn(
                        myTaskStatusRepository.findAll().stream().map(MyTaskStatus::getStatus).toList()
                )
        );
        Map<MyTaskStatus, List<MyTaskBean>> map = new LinkedHashMap<>();
        for (MyTaskStatus status : statuses) {
            map.put(status, new ArrayList<>());
        }
        taskBeans.forEach(taskBean -> {
            List<MyTaskBean> list = map.get(
                    statuses
                    .stream()
                    .filter(status -> status.getStatus().equals(taskBean.getStatus()))
                    .findFirst().orElse(null)
            );
            if (list != null) {
                list.add(taskBean);
            }
        });
        MyTaskStatus unmappedTasks = new MyTaskStatus();
        unmappedTasks.setStatus("Unmapped Tasks");
        if(!unMappedTasks.isEmpty()){
            map.put(unmappedTasks, unMappedTasks);
        }
        dataSaver.scheduleSave();
        return map;
    }

    public void addTask(MyTaskBean myTaskBean){
        MyTask myTask = MyTaskBean.mapToEntity(myTaskBean);
        myTask.setCreatedAt(LocalDateTime.now());
        myTask.setUpdatedAt(LocalDateTime.now());
        logger.info("Adding new task {}", myTask);
        myTaskRepository.save(myTask);
    }

    public void moveStatusRight(Integer id) {
        MyTask myTask = myTaskRepository.findById(id).orElseThrow();
        if (myTask.getStatus() != null) {
            Optional<MyTaskStatus> myTaskStatus = myTaskStatusRepository.findByStatus(myTask.getStatus());
            myTaskStatus.ifPresent(status -> myTask.setStatus(status.getRightStatus()));
            myTask.setUpdatedAt(LocalDateTime.now());
            logger.info("Task moving right  {}", myTask);
            myTaskRepository.save(myTask);
        }
    }

    public void moveStatusLeft(Integer id) {
        MyTask myTask = myTaskRepository.findById(id).orElseThrow();
        if (myTask.getStatus() != null) {
            Optional<MyTaskStatus> myTaskStatus = myTaskStatusRepository.findByStatus(myTask.getStatus());
            myTaskStatus.ifPresent(status -> myTask.setStatus(status.getLeftStatus()));
            myTask.setUpdatedAt(LocalDateTime.now());
            logger.info("Task moving left {}", myTask);
            myTaskRepository.save(myTask);
        }
    }

    public void updateDescription(Integer id, String description) {
        Optional<MyTask> optional = myTaskRepository.findById(id);
        if (optional.isPresent()) {
            MyTask myTask = optional.get();
            myTask.setDescription(description);
            myTask.setUpdatedAt(LocalDateTime.now());
            logger.info("Updating Description {}", myTask);
            myTaskRepository.save(myTask);
        }
    }

    public void deleteTask(Integer id) {
        if (id!= null) {
            logger.info("Deleting task {}", id);
            myTaskRepository.deleteById(id);
        }
    }

    public void jumpTask(Integer id) {
        MyTask myTask = myTaskRepository.findById(id).orElseThrow();
        if (myTask.getStatus() != null) {
            Optional<MyTaskStatus> myTaskStatus = myTaskStatusRepository.findByStatus(myTask.getStatus());
            myTaskStatus.ifPresent(status -> myTask.setStatus(status.getJumpToStatus()));
            myTask.setUpdatedAt(LocalDateTime.now());
            logger.info("Task jumping {}", myTask);
            myTaskRepository.save(myTask);
        }
    }

/*    public void moveAllReadyToDone() {
        logger.info("Moving all in hand tasks to Done");
        myTaskRepository.updateAllReadyToDone(LocalDateTime.now());
    }*/

}
