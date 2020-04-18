/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package resource;

import java.io.File;

/**
 *
 * @author Sultan Ahmed Sagor
 * BUET , CSE-08 Batch , Dhaka . 
 */
public class GetFile {
    
    public static File getFile(String fileName){
        File file = new File(fileName);
        return file; 
    }
    
    public static void main(String[] args) {
        File file = new File("dbInfo_Ba.properties");
        if(file == null)
            System.out.println("file is not got ");
        else
            System.out.println("file is got ");
    }

}
