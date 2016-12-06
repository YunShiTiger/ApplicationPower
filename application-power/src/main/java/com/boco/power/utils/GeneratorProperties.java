package com.boco.power.utils;

/**
 * @author sunyu 2016/12/5.
 */
public class GeneratorProperties {
    private static final PropertiesUtils props = new PropertiesUtils("generator.properties");

    /**
     * 获取用户名
     * @return
     */
    public static String authorName(){
        return props.getProperty("generator.author");
    }

    /**
     * 是否需要添加注释
     * @return
     */
    public static Boolean comment(){
        return Boolean.valueOf(props.getProperty("generator.comment"));
    }

    /**
     * 获取基包名
     * @return
     */
    public static String basePackage(){
        return props.getProperty("generator.basePackage");
    }

    /**
     * 获取表前缀
     * @return
     */
    public static String tablePrefix(){
        return props.getProperty("generator.table.prefix");
    }

    /**
     * 获取应用名称
     * @return
     */
    public static String applicationName(){
        return props.getProperty("generator.applicationName");
    }
}
