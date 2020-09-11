
package com.era.OldFingerMatching;

import com.era.admin.DBInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author roman
 */
public class IpPortRead {
    public static IpPortInfo getIpPortInfo() {

        Properties prop = new Properties();
        InputStream inputStream = null;
        IpPortInfo ipPortInfo = new IpPortInfo();
        try {
            File file =new File("OldFpApiIpPort_Ba.properties");
            inputStream = new FileInputStream(file);
            prop.load(inputStream); 
            ipPortInfo.setIp(prop.getProperty("IP"));
            ipPortInfo.setPort(prop.getProperty("PORT"));
            
            inputStream.close();
            return ipPortInfo; 
        } catch (IOException e) {
            System.out.println("OldFpApiIpPort_Ba.properties.");
            return null;
        } 
    }
}
