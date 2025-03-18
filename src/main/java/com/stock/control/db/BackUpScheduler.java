package com.stock.control.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BackUpScheduler {

    private static final Logger log = LoggerFactory.getLogger(BackUpScheduler.class);

    private static final String DB_PATH = "stock_control.db",
    DB_BACKUP_PATH = System.getProperty("user.home") + "/AppData/StockControlBackUp/stock_control.bak"; //backup se guarda en el appdata del usuario

    public static void startBackUpScheduler(){
        Timer timer = new Timer(true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    log.info("CREATING BACKUP");
                    SQLiteBackUp.backUpDataBase(DB_PATH, DB_BACKUP_PATH);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 7200000); //back up programado cada 2 horas
    }
}
