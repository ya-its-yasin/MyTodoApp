package com.app.mytodo.config;

import com.app.mytodo.constant.FileObjects;
import com.app.mytodo.entity.MyTask;
import com.app.mytodo.entity.MyTaskStatus;
import com.app.mytodo.repository.MyTaskRepository;
import com.app.mytodo.repository.MyTaskStatusRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class DataSaver {

    private static final Logger logger = LoggerFactory.getLogger(DataSaver.class);

    @Autowired
    private MyTaskRepository myTaskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileObjects fileObjects;

    @Autowired
    MyTaskStatusRepository myTaskStatusRepository;

    @Value("${myapp.done.cutoff.days}")
    public Integer DONE_CUTOFF_DAYS;

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> saveTask;

    @Value("${myapp.inactivity.save.file.minutes}")
    private long DELAY_MIN;

    public void saveData() throws IOException {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(DONE_CUTOFF_DAYS);
        List<String> archiveableStatuses = myTaskStatusRepository.findAllByArchivable(true)
                .stream()
                .map(MyTaskStatus::getStatus)
                .toList();
        List<String> nonArchivableStatuses = myTaskStatusRepository.findAllByArchivable(false)
                .stream()
                .map(MyTaskStatus::getStatus)
                .toList();
        List<MyTask> myTasks = myTaskRepository.findAllByStatusIn(nonArchivableStatuses);
        List<MyTask> futureArchivableTasks = myTaskRepository.findAllByStatusInAndUpdatedAtAfter(archiveableStatuses, cutoffDate);
        List<MyTask> unMappedTasks = myTaskRepository.findAllByStatusNotIn(myTaskStatusRepository.findAll().stream().map(MyTaskStatus::getStatus).toList());
        myTasks.addAll(futureArchivableTasks);
        myTasks.addAll(unMappedTasks);
        Path fileDir = Paths.get(fileObjects.BASE_DIR);
        Path tasksFilePath = fileDir.resolve(fileObjects.TASKS_FILE);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(tasksFilePath.toFile(), myTasks);
        logger.info("Saved the tasks to json file: {}", tasksFilePath);

        List<MyTask> archivableTasks = myTaskRepository.findAllByStatusInAndUpdatedAtBefore(archiveableStatuses, cutoffDate);
        if(!archivableTasks.isEmpty()){
            Path archiveFile = fileDir.resolve(fileObjects.ARCHIVE_FILE);
            MyTask[] archived = objectMapper.readValue(archiveFile.toFile(), MyTask[].class);
            List<MyTask> archivedDoneTasks = (archived!=null) ? new ArrayList<>(Arrays.asList(archived)) : new ArrayList<>();
            archivedDoneTasks.addAll(archivableTasks);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(archiveFile.toFile(), archivedDoneTasks);
            logger.info("Updated tasks to archival: {}", archivableTasks);
        }
    }

    // Called after any task change
    public synchronized void scheduleSave() {
        if (saveTask != null && !saveTask.isDone()) {
            saveTask.cancel(false);
        }
        saveTask = scheduler.schedule(() -> {
            try {
                saveData();
            } catch (IOException e) {
                logger.error(String.valueOf(e));
            }
        }, DELAY_MIN , TimeUnit.MINUTES);
    }

    @PreDestroy
    public void cleanup() {
        if (saveTask != null && !saveTask.isDone()) {
            saveTask.cancel(false);
        }
        scheduler.shutdown();
        try {
            saveData();
        } catch (IOException e) {
            logger.error(String.valueOf(e));
        }
    }

    public void saveStatusMenuData() {
        List<MyTaskStatus> myTaskStatuses = myTaskStatusRepository.findAllByOrderById();
        Path fileDir = Paths.get(fileObjects.BASE_DIR);
        Path menuFile = fileDir.resolve(fileObjects.STATUS_MENU_FILE);
        try {
            // Write the JSON with pretty print
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(menuFile.toFile(), myTaskStatuses);
            logger.info("Saved the status menu to JSON file: {}", menuFile);
        } catch (IOException e) {
            logger.error("Failed to save status menu to JSON file: {}", menuFile, e);
        }
    }

}
