package com.app.mytodo.config;

import com.app.mytodo.constant.FileObjects;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Component
public class StartupLoader {

    private static final Logger logger = LoggerFactory.getLogger(StartupLoader.class);

    @Value("${myapp.file.backup.days}")
    private Integer BACKUP_DAYS;

    @Autowired
    private FileObjects fileObjects;

    @PostConstruct
    public void loadTasksOnStartup() {
        try {
            Path fileDir = Paths.get(fileObjects.BASE_DIR);
            Path taskFile = fileDir.resolve(fileObjects.TASKS_FILE);
            Path archiveFile = fileDir.resolve(fileObjects.ARCHIVE_FILE);
            Path statusMenuFile = fileDir.resolve(fileObjects.STATUS_MENU_FILE);

            // Step 2: Ensure backup dir exists
            Path backupDir = Paths.get(fileObjects.BACKUP_DIR);
            Files.createDirectories(backupDir);
            String timestamp = LocalDateTime.now().toLocalDate().toString().replace(":", "-");
            Path backupDirectory = Paths.get(fileObjects.BACKUP_DIR, "backup-" + timestamp);
            Path tasksFileBackup = backupDirectory.resolve(fileObjects.TASKS_FILE);
            Path archiveFileBackup = backupDirectory.resolve(fileObjects.ARCHIVE_FILE);
            Path statusMenuFileBackup = backupDirectory.resolve(fileObjects.STATUS_MENU_FILE);

            // Create backup directory and copy files
            if (!Files.exists(backupDirectory) || !Files.exists(tasksFileBackup) || !Files.exists(archiveFileBackup) || !Files.exists(statusMenuFileBackup)) {
                Files.createDirectories(backupDirectory); // Safe to call even if exists
                Files.copy(taskFile, tasksFileBackup, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(archiveFile, archiveFileBackup, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(statusMenuFile, statusMenuFileBackup, StandardCopyOption.REPLACE_EXISTING);
                logger.info("Backup completed");
            }

            // Delete older backup directories
            try (Stream<Path> backupDirs = Files.list(Paths.get(fileObjects.BACKUP_DIR))) {
                List<Path> sortedDirs = backupDirs
                        .filter(Files::isDirectory)
                        .sorted(Comparator.comparing(Path::getFileName)) // Assumes lexicographic order like "backup-2024-01-01"
                        .toList();
                if (sortedDirs.size() > BACKUP_DAYS) {
                    for (int i = 0; i < sortedDirs.size() - BACKUP_DAYS; i++) {
                        Path oldDir = sortedDirs.get(i);
                        deleteDirectoryRecursively(oldDir);
                        logger.info("Deleted old backup: {}", oldDir.getFileName());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error during startup file load: {}", e.getMessage());
        }
    }

    private void deleteDirectoryRecursively(Path dir) throws IOException {
        if (Files.exists(dir)) {
            try (Stream<Path> stream = Files.walk(dir)) {
                stream.sorted(Comparator.reverseOrder()) // Delete children before parents
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete " + path, e);
                        }
                    });
            }
        }
    }

}
