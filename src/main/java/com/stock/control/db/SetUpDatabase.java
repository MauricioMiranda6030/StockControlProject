package com.stock.control.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SetUpDatabase {

    private static Logger log = LoggerFactory.getLogger(SetUpDatabase.class);

    public static void setUp(){
        String userHome = System.getProperty("user.home");
        String dbFolder = userHome + "/AppData/Roaming/StockControl/";
        String dbPath = dbFolder + "stock_control.db";

        File dbFile = new File(dbPath);

        if(!dbFile.exists()){
            File folder = new File(dbFolder);
            createDataBaseFolder(folder, dbFolder);
            if(folder.exists())
                createDataBaseFile(dbPath, dbFolder);
        }
    }

    private static void createDataBaseFolder(File folder, String dbFolder) {
        if(!folder.exists())
            if(folder.mkdirs())
                log.info("db folder created at {}", dbFolder);
            else
                log.error("Error while creating db folder: {}", dbFolder);
    }

    private static void createDataBaseFile(String dbPath, String dbFolder) {
        String url = "jdbc:sqlite:" + dbPath;

        try{
            Connection conn = DriverManager.getConnection(url);
            if(conn != null)
                log.info("db successfully created at {}", dbFolder);
        } catch (SQLException e) {
            log.error("Error while creating the db: {}", e.getMessage());
        }
    }
}
