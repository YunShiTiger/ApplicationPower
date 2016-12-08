package com.boco.power.builder;

import com.boco.power.database.DataBaseInfo;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.FileUtils;
import com.boco.power.utils.GeneratorProperties;
import com.boco.power.utils.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author  on 2016/12/7.
 */
public class CodeWriter {

    /**
     * 更具数据库生成代码
     */
    public void writeCodeFromDataBase(){




        DirBuilder dirBuilder = new DirBuilder();
        Map<String,String> dirMap = dirBuilder.initAndGetDirs(GeneratorProperties.outDir());

        //pom文件路径
        String pomDir = dirMap.get("pom");
        if(StringUtils.isNotEmpty(pomDir)){
            String pomCode = new PomBuilder().generatePom();
            System.out.println("pomDir:"+pomDir);
            FileUtils.writeFileNotAppend(pomCode,pomDir+"\\pom.xml");
        }
        //web.xml文件路径
        String webConfig = dirMap.get("webConfig");
        if(StringUtils.isNotEmpty(webConfig)){
            String webXmlCode = new WebXmlBuilder().generateWebXml();
            FileUtils.writeFileNotAppend(webXmlCode,webConfig+"\\web.xml");
        }
        //recource路径
        String resource = dirMap.get("resources");
        if(StringUtils.isNotEmpty(resource)){
            String springMvcCfg = new SpringMvcCfgBuilder().generateSpringMvcCfg();
            FileUtils.writeFileNotAppend(springMvcCfg,resource+"\\spring-mvc.xml");
            String springMybatis = new SpringMybatisCfgBuilder().generateSpringMyBatisCfg();
            FileUtils.writeFileNotAppend(springMybatis,resource+"\\spring-mybatis.xml");
            String currentPath = Thread.currentThread().getContextClassLoader().getResource("jdbc.properties").getPath();
            FileUtils.nioTransferCopy(new File(currentPath),new File(resource+"\\jdbc.properties"));

            String log4j = BeetlTemplateUtil.getByName("log4j.btl").render();
            FileUtils.writeFileNotAppend(log4j,resource+"\\log4j.properties");

            String mybatisCfg = BeetlTemplateUtil.getByName("mybatis-config.btl").render();
            FileUtils.writeFileNotAppend(mybatisCfg,resource+"\\mybatis-config.xml");
        }

        DataBaseInfo info = new DataBaseInfo();
        List<String> tables = info.listOfTablesByPrefix("");
        for(String table:tables){
            String entityName = StringUtils.toCapitalizeCamelCase(table);
            for(Map.Entry<String,String> entry:dirMap.entrySet()){
                String value = entry.getValue();
                String key = entry.getKey();
                if("dao".equals(key)){
                    String daoCode = new DaoBuilder().generateDao(table);
                    FileUtils.writeFileNotAppend(daoCode,value+"\\"+entityName+"Dao.java");
                }
                if("model".equals(key)){
                    String modelCode = new ModelBuilder().generateModel(table);
                    FileUtils.writeFileNotAppend(modelCode,value+"\\"+entityName+".java");
                }
                if("service".equals(key)){
                    String serviceCode = new ServiceBuilder().generateService(table);
                    FileUtils.writeFileNotAppend(serviceCode,value+"\\"+entityName+"Service.java");
                    String serviceImplCode = new ServiceImplBuilder().generateServiceImpl(table);
                    FileUtils.writeFileNotAppend(serviceImplCode,value+"\\impl\\"+entityName+"ServiceImpl.java");
                }
                if("serviceTest".equals(key)){
                    String serviceTestCode = new ServiceTestBuilder().generateServiceTest(table);
                    FileUtils.writeFileNotAppend(serviceTestCode,value+"\\"+entityName+"ServiceTest.java");
                }
                if("controller".equals(key)){
                    String controllerCode = new ControllerBuilder().generateController(table);
                    FileUtils.writeFileNotAppend(controllerCode,value+"\\"+entityName+"Controller.java");
                }
                if("controllerTest".equals(key)){
                    String controllerCode = new ControllerTestBuilder().generateControllerTest(table);
                    FileUtils.writeFileNotAppend(controllerCode,value+"\\"+entityName+"ControllerTest.java");
                }

                if("mapper".equals(key)){
                    String mapperCode = new MapperBuilder().generateMapper(table);
                    FileUtils.writeFileNotAppend(mapperCode,value+"\\"+entityName+"Dao.xml");
                }
            }
        }
    }

}
