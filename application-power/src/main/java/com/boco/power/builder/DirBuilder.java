package com.boco.power.builder;

import com.boco.power.utils.FileUtils;
import com.boco.power.utils.GeneratorProperties;

import java.io.File;
import java.util.*;

/**
 * 创建工程目录
 * @author sunyu 2016/12/7.
 */
public class DirBuilder {

    /**
     * 初始化生成工程目录并返回
     */
    public Map<String,String> initAndGetDirs(String rootDir){
        String fileSeparator =  System.getProperty("file.separator");
        Map<String,String> dirsMap = new HashMap<>();
        String applicationName = GeneratorProperties.applicationName();
        String basePackage = GeneratorProperties.basePackage();
        String newPackage =  basePackage.replaceAll("[.]","\\\\");

        String mainDir = rootDir+fileSeparator+applicationName+fileSeparator+"src"+fileSeparator+"main";
        String javaDir = mainDir +fileSeparator+"java";
        //根据基础包创建目录
        String basePackageDir = javaDir +fileSeparator+newPackage;
        File file = new File(basePackageDir);
        file.mkdirs();
        //pom的路径
        dirsMap.put("pom",rootDir+fileSeparator+applicationName);

        //生成resource dir
        String resourceDir = mainDir+fileSeparator+"resources";
        FileUtils.mkdir(resourceDir);
        dirsMap.put("resources",resourceDir);
        //
        String testRootDir = rootDir+fileSeparator+applicationName+"\\src\\test\\java";
        FileUtils.mkdirs(testRootDir);
        //service层单元测试的路径
        String testServiceDir = testRootDir+fileSeparator+newPackage+"\\service";

        //controller层单元测试的路径
        String testControllerDir = testRootDir+fileSeparator+newPackage+"\\controller";

        //webApp
        String webappDir = mainDir+fileSeparator+"webapp";
        String webInfoDir = webappDir+fileSeparator+"WEB-INF";
        FileUtils.mkdirs(webInfoDir);
        dirsMap.put("webConfig",webInfoDir);

        //更具指定的代码层创建目录
        String layers = GeneratorProperties.layers();
        String[] layerArr = layers.split(",");
        Set<String> layerSet = new HashSet<>();
        layerSet.addAll(Arrays.asList(layerArr));
        for(String str:layerSet){
            String targetDir = basePackageDir+fileSeparator+str;
            if("service".equals(str)){
                FileUtils.mkdir(targetDir);
                dirsMap.put(str,targetDir);
                targetDir = targetDir+fileSeparator+"\\impl";
                FileUtils.mkdir(targetDir);
                dirsMap.put("serviceImpl",targetDir);
            }else if("serviceTest".equals(str)){
                File testServiceFile = new File(testServiceDir);
                testServiceFile.mkdirs();
                dirsMap.put("serviceTest",testServiceDir);
            }else if("controllerTest".equals(str)){
                File testControllerFile = new File(testControllerDir);
                testControllerFile.mkdirs();
                dirsMap.put("controllerTest",testControllerDir);
            }else if("dao".equals(str)){
                FileUtils.mkdir(targetDir);
                dirsMap.put(str,targetDir);
            }else if("model".equals(str)){
                FileUtils.mkdir(targetDir);
                dirsMap.put(str,targetDir);
            }else if("mapper".equals(str)){
                targetDir = resourceDir+fileSeparator+newPackage+fileSeparator+"mapping";
                File mapping = new File(targetDir);
                mapping.mkdirs();
                dirsMap.put(str,targetDir);
            }else if("controller".equals(str)){
                FileUtils.mkdir(targetDir);
                dirsMap.put(str,targetDir);
            }
        }
        return dirsMap;
    }

    public static void main(String[] args) {
        new DirBuilder().initAndGetDirs("e:\\test");
    }
}
