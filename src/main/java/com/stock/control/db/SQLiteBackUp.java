package com.stock.control.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class SQLiteBackUp {

    private static final Logger log = LoggerFactory.getLogger(SQLiteBackUp.class);

    public static void backUpDataBase(String dbPath, String bkUpPath) throws IOException {
        File dbFile = new File(dbPath);
        File bkUpFile = new File(bkUpPath);

        createDirIfNotExists(bkUpPath);
        try {
            Files.copy(dbFile.toPath(), bkUpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            log.info("BACKUP DONE");
        } catch (IOException e) {
            log.error("ERROR WHILE DATA BASE BUCK UP: {}", e.getMessage());
        }
    }

    private static void createDirIfNotExists(String bkUpPath) throws IOException {
        Path backupDir = Path.of(bkUpPath);
        if (!Files.exists(backupDir)) {
            Files.createDirectories(backupDir);
            log.info("Back up dir just created at {}", backupDir);
        }
    }
}
