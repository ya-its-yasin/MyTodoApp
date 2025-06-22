package com.app.mytodo.constant;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileObjects {

    private static final Logger logger = LoggerFactory.getLogger(FileObjects.class);

    @Value("${myapp.current.working.directory}")
    public String BASE_DIR;

    public String BACKUP_DIR ;
    public String TASKS_FILE ;
    public String ARCHIVE_FILE;
    public String STATUS_MENU_FILE ;

    @PostConstruct
    public void init() {
        BACKUP_DIR = BASE_DIR + File.separator + "backup";
        TASKS_FILE = "my_tasks.json";
        ARCHIVE_FILE = "archived_tasks.json";
        STATUS_MENU_FILE = "status_menu.json";
        logger.info("Initiated BASE_DIR : {}", BASE_DIR);
    }
}
