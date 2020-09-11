package com.era.admin;
 
import java.io.File;
import java.io.FileInputStream; 
import java.io.IOException;
import java.io.InputStream; 
import java.util.Properties;

public class GetDBInfo {

    public static DBInfo getDbInfo() {

        Properties prop = new Properties();
        InputStream inputStream = null;
        DBInfo dbInfo = new DBInfo();

        try {
            File file =new File("dbInfo_Ba.properties");
            inputStream = new FileInputStream(file);
            prop.load(inputStream); 
            dbInfo.ip = prop.getProperty("ip");
            dbInfo.portNo = prop.getProperty("portNo");
            dbInfo.serviceName = prop.getProperty("serviceName");
            dbInfo.userName = prop.getProperty("userName");
            dbInfo.password = prop.getProperty("password");
            inputStream.close();
            return dbInfo; 
        } catch (IOException e) {
            System.out.println("dbInfo_Ba.properties file read error.");
            return null;
        } 
    }
}
