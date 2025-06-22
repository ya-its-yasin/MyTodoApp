package com.app.mytodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class MyTaskStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;
    @Version
    @JsonIgnore
    private Long version;
    private Integer displayOrderId;
    private String status;
    private String leftStatus;
    private String rightStatus;
    private String jumpToStatus;
    private boolean addable;         // Indicates if the item can be added
    private boolean editable;        // Indicates if the item can be edited
    private boolean deletable;       // Indicates if the item can be deleted
    private boolean archivable;      // Indicates if the item can be archived
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getDisplayOrderId() {
        return displayOrderId;
    }

    public void setDisplayOrderId(Integer displayOrderId) {
        this.displayOrderId = displayOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRightStatus() {
        return rightStatus;
    }

    public void setRightStatus(String rightStatus) {
        this.rightStatus = rightStatus;
    }

    public String getLeftStatus() {
        return leftStatus;
    }

    public void setLeftStatus(String leftStatus) {
        this.leftStatus = leftStatus;
    }

    public String getJumpToStatus() {
        return jumpToStatus;
    }

    public void setJumpToStatus(String jumpToStatus) {
        this.jumpToStatus = jumpToStatus;
    }

    public boolean isAddable() {
        return addable;
    }

    public void setAddable(boolean addable) {
        this.addable = addable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public boolean isArchivable() {
        return archivable ;
    }

    public void setArchivable(boolean archivable ) {
        this.archivable  = archivable ;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "MyTaskStatus{" +
                "id=" + id +
                ", version=" + version +
                ", displayOrderId=" + displayOrderId +
                ", status='" + status + '\'' +
                ", leftStatus='" + leftStatus + '\'' +
                ", rightStatus='" + rightStatus + '\'' +
                ", jumpToStatus='" + jumpToStatus + '\'' +
                ", addable=" + addable +
                ", editable=" + editable +
                ", deletable=" + deletable +
                ", archivable=" + archivable +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
