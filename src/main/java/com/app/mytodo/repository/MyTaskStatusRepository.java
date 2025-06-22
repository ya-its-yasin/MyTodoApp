package com.app.mytodo.repository;

import com.app.mytodo.entity.MyTaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyTaskStatusRepository extends JpaRepository<MyTaskStatus, Integer> {

    List<MyTaskStatus> findAllByArchivable(boolean isArchiveable);

    List<MyTaskStatus> findAllByOrderById();

    Optional<MyTaskStatus> findByStatus(String status);

    Optional<MyTaskStatus> findByDisplayOrderId(Integer displayOrderId);

    @Modifying
    @Transactional
    @Query("UPDATE MyTaskStatus s SET s.displayOrderId = s.displayOrderId+1, s.updatedAt =CURRENT_TIMESTAMP WHERE s.displayOrderId >=:newDisplayOrderId")
    int checkAndMoveOrderForward(Integer newDisplayOrderId);

    @Modifying
    @Transactional
    @Query("UPDATE MyTaskStatus s SET s.displayOrderId = s.displayOrderId-1, s.updatedAt =CURRENT_TIMESTAMP WHERE s.displayOrderId >=:displayOrderIdToBeDeleted")
    int checkAndMoveOrderBackward(Integer displayOrderIdToBeDeleted);

    List<MyTaskStatus> findAllByOrderByDisplayOrderId();
}

