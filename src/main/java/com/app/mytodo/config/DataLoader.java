package com.app.mytodo.config;

import com.app.mytodo.constant.FileObjects;
import com.app.mytodo.entity.MyTask;
import com.app.mytodo.entity.MyTaskStatus;
import com.app.mytodo.repository.MyTaskRepository;
import com.app.mytodo.repository.MyTaskStatusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private MyTaskRepository myTaskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileObjects fileObjects;

    @Autowired
    private MyTaskStatusRepository myTaskStatusRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() throws IOException {
        Path fileDir = Paths.get(fileObjects.BASE_DIR);
        Path tasksFilePath = fileDir.resolve(fileObjects.TASKS_FILE);
        if (Files.exists(tasksFilePath)) {
            logger.info("Loading tasks from file: {}", tasksFilePath.toAbsolutePath());
            List<MyTask> myTasks = Arrays.asList(objectMapper.readValue(Files.newBufferedReader(tasksFilePath), MyTask[].class));
            myTaskRepository.saveAll(myTasks);
        } else {
            logger.warn("Task file not found, creating new: {}", tasksFilePath.toAbsolutePath());
        }

        Path statusMenuFilePath = fileDir.resolve(fileObjects.STATUS_MENU_FILE);
        if (Files.exists(statusMenuFilePath)) {
            logger.info("Loading status menu from file: {}", statusMenuFilePath.toAbsolutePath());
            List<MyTaskStatus> myTaskStatuses = Arrays.asList(objectMapper.readValue(Files.newBufferedReader(statusMenuFilePath), MyTaskStatus[].class));
            myTaskStatusRepository.saveAll(myTaskStatuses);
        } else {
            logger.warn("Status menu file not found, creating new: {}", statusMenuFilePath.toAbsolutePath());
        }
    }


}