package com.stock.control.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BackUpScheduler {

    private static final Logger log = LoggerFactory.getLogger(BackUpScheduler.class);

    private static final String BASE_PATH = System.getProperty("user.home") + "/AppData/Roaming/",
    DB_PATH = BASE_PATH + "StockControl/stock_control.db",
    DB_BACKUP_PATH = BASE_PATH + "StockControlBackUp/"; //backup se guarda en el appdata del usuario

    private static final int HOURS_FOR_BACKUP = 7200000,
    DAYS_FOR_DEADLINE = 5; //2 Horas en milisegundos

    public static void startBackUpScheduler(){
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    deleteOldBackUps(DAYS_FOR_DEADLINE);

                    log.info("Creating a backup at {} ...", DB_BACKUP_PATH);
                    String timestamp = new SimpleDateFormat("dd-MM-yyyy_HHmm").format(new Date());
                    String dbName = DB_BACKUP_PATH + "stock_control_" + timestamp + ".bak";

                    SQLiteBackUp.backUpDataBase(DB_PATH, dbName);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 10, HOURS_FOR_BACKUP); //back up programado cada 2 horas
    }

    private static void deleteOldBackUps(int days){
        File folder = new File(DB_BACKUP_PATH);
        Long deadLine = System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L); //Dias a milisegundos

        for(File file : folder.listFiles()){
            if (file.lastModified() < deadLine){
                file.delete();
                log.info("Old back up deleted");
            }
        }
    }
}
