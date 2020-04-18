package com.era.model;

/**
 *
 * @author roman
 */
public class FingerDataWithName {
    private String fingerName; 
    private byte[] fingerData ; 

    public FingerDataWithName(String fingerName, byte[] fingerData) {
        this.fingerName = fingerName;
        this.fingerData = fingerData;
    } 
    
    public String getFingerName() {
        return fingerName;
    }

    public void setFingerName(String fingerName) {
        this.fingerName = fingerName;
    }

    public byte[] getFingerData() {
        return fingerData;
    }

    public void setFingerData(byte[] fingerData) {
        this.fingerData = fingerData;
    }  
}
