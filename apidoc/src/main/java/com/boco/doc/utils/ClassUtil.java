package com.boco.doc.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassUtil {

    /**
     *  读取源代码获取类名
     * @param filePath
     * @param simpleClassName 和简单的类名进行对比，避免再往下读取
     * @return
     */
    public static String getClassName(String filePath,String simpleClassName) {
        final String  regex = "(class (.*?)\\{)|(interfase (.*?)\\{)";
        String curLine;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            while ((curLine = reader.readLine()) != null) {
                Matcher matcher = Pattern.compile(regex, Pattern.DOTALL | Pattern.MULTILINE).matcher(curLine);
                if (matcher.find()) {
                    String selector = matcher.group();
                    String[] strs = selector.split(" ");
                    for(String str:strs){
                        if(str.contains(simpleClassName)){
                            return str;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
