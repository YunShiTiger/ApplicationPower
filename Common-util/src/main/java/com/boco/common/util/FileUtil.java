package com.boco.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by yu on 2017/3/9.
 */
public class FileUtil {

    /**
     *write by BufferedWriter
     * @param source String source
     * @param file File
     * @return boolean
     */
    public static boolean writeFile(String source,File file){
        boolean flag = true;
        BufferedWriter output = null;
        try {
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file,true));
            output.write(source);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        }finally{
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
}
