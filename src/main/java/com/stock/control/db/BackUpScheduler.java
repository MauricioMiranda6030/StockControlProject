package com.stock.control.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BackUpScheduler {

    private static final Logger log = LoggerFactory.getLogger(BackUpScheduler.class);

    private static final String BASE_PATH = System.getProperty("user.home") + "/AppData/Roaming/",
    DB_PATH = BASE_PATH + "StockControl/stock_control.db",
    DB_BACKUP_PATH = BASE_PATH + "StockControlBackUp/stock_control.bak"; //backup se guarda en el appdata del usuario
    private static final int HOURS_FOR_BACKUP = 7200000; //2 Horas en milisegundos

    public static void startBackUpScheduler(){
        Timer timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    log.info("Creating a backup at {} ...", DB_BACKUP_PATH);
                    SQLiteBackUp.backUpDataBase(DB_PATH, DB_BACKUP_PATH);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, HOURS_FOR_BACKUP); //back up programado cada 2 horas
    }
}
