package com.app.mytodo.bean;

import com.app.mytodo.entity.MyTaskStatus;

import java.util.ArrayList;
import java.util.List;

public class MyTaskStatusBean {

    private Integer id;
    private Integer displayOrderId;
    private String status;
    private String right;
    private String left;
    private String jumpTo;
    private boolean addable;         // Indicates if the item can be added
    private boolean editable;        // Indicates if the item can be edited
    private boolean deletable;       // Indicates if the item can be deleted
    private boolean archivable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getJumpTo() {
        return jumpTo;
    }

    public void setJumpTo(String jumpTo) {
        this.jumpTo = jumpTo;
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
        return archivable;
    }

    public void setArchivable(boolean archivable) {
        this.archivable = archivable;
    }

    public static MyTaskStatusBean mapToBean(MyTaskStatus myTaskStatus) {
        if (myTaskStatus == null) return null;
        MyTaskStatusBean bean = new MyTaskStatusBean();
        bean.setId(myTaskStatus.getId());
        bean.setDisplayOrderId(myTaskStatus.getDisplayOrderId());
        bean.setStatus(myTaskStatus.getStatus());
        bean.setAddable(myTaskStatus.isAddable());
        bean.setEditable(myTaskStatus.isEditable());
        bean.setDeletable(myTaskStatus.isDeletable());
        bean.setArchivable(myTaskStatus.isArchivable());
        bean.setRight(myTaskStatus.getRightStatus());
        bean.setLeft(myTaskStatus.getLeftStatus());
        bean.setJumpTo(myTaskStatus.getJumpToStatus());
        return bean;
    }

    public static List<MyTaskStatusBean> mapAllToBeans(List<MyTaskStatus> myTaskStatuses){
        List<MyTaskStatusBean> beans = new ArrayList<>();
        myTaskStatuses.forEach(task -> beans.add(mapToBean(task)));
        return beans;
    }

    public static MyTaskStatus mapToEntity(MyTaskStatusBean bean) {
        if (bean == null) return null;
        MyTaskStatus entity = new MyTaskStatus();
        entity.setDisplayOrderId(bean.getDisplayOrderId());
        entity.setStatus(bean.getStatus());
        entity.setAddable(bean.isAddable());
        entity.setEditable(bean.isEditable());
        entity.setDeletable(bean.isDeletable());
        entity.setArchivable(bean.isArchivable());
        entity.setRightStatus(bean.getRight());
        entity.setLeftStatus(bean.getLeft());
        entity.setJumpToStatus(bean.getJumpTo());
        return entity;
    }

    public static List<MyTaskStatus> mapAllToEntities(List<MyTaskStatusBean> myTaskStatusBeans){
        List<MyTaskStatus> myTaskStatuses = new ArrayList<>();
        myTaskStatusBeans.forEach(bean -> myTaskStatuses.add(mapToEntity(bean)));
        return myTaskStatuses;
    }

}