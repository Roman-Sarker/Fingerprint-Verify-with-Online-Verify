/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.era.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sultan Ahmed Sagor
 * BUET , CSE-08 Batch , Dhaka . 
 */
public class UserFingerData {
    private long custNo ; 
    List<FingerDataWithName> fingerDataWithNameList;
     
    public UserFingerData(){
        fingerDataWithNameList = new ArrayList<FingerDataWithName>();
    }

    public long getCustNo() {
        return custNo;
    }

    public void setCustNo(long custNo) {
        this.custNo = custNo;
    } 

    public List<FingerDataWithName> getFingerDataWithName() {
        return fingerDataWithNameList;
    }

    public void addFingerDataWithName(FingerDataWithName fingerDataWithName) {
        fingerDataWithNameList.add(fingerDataWithName);
    }
    
    
}
