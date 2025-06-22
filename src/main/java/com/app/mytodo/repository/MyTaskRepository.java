package com.app.mytodo.repository;

import com.app.mytodo.entity.MyTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MyTaskRepository extends JpaRepository<MyTask, Integer> {

    List<MyTask> findAllByStatusIn(List<String> statuses);

    List<MyTask> findAllByStatusInAndUpdatedAtBefore(List<String> status, LocalDateTime dateTime);

    List<MyTask> findAllByStatusInAndUpdatedAtAfter(List<String> statuses, LocalDateTime dateTime);

    List<MyTask> findAllByStatusNotIn(List<String> statuses);

    @Modifying
    @Transactional
    @Query("UPDATE MyTask t SET t.status =:newStatus, t.updatedAt =CURRENT_TIMESTAMP WHERE t.status =:oldStatus")
    int updateStatusToNew(String oldStatus, String newStatus);

/*    @Modifying
    @Transactional
    @Query("UPDATE MyTask t SET t.status = 4, t.updatedAt =:dateTime WHERE t.status = 2")
    int updateAllReadyToDone(@Param("dateTime") LocalDateTime dateTime);*/

}



