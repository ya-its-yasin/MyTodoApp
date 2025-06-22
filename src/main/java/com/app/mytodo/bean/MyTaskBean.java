package com.app.mytodo.bean;

import com.app.mytodo.entity.MyTask;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyTaskBean {

    private int id;
    private String description;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static MyTaskBean mapToBean(MyTask myTask){
        MyTaskBean bean = new MyTaskBean();
        bean.setId(myTask.getId());
        bean.setDescription(myTask.getDescription());
        bean.setStatus(myTask.getStatus());
        bean.setCreatedAt(Date.from(myTask.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        bean.setUpdatedAt(Date.from(myTask.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        return bean;
    }

    public static List<MyTaskBean> mapAllToBeans(List<MyTask> myTasks){
        List<MyTaskBean> beans = new ArrayList<>();
        myTasks.forEach(task -> beans.add(mapToBean(task)));
        return beans;
    }

    public static MyTask mapToEntity(MyTaskBean bean) {
        MyTask myTask = new MyTask();
        myTask.setDescription(bean.getDescription());
        myTask.setStatus(bean.getStatus());
        return myTask;
    }

}
