package com.boco.power.builder;

import com.boco.common.util.DateTimeUtil;
import com.boco.common.util.FileUtil;
import com.boco.common.util.StringUtil;
import com.boco.power.constant.ConstVal;
import com.boco.power.constant.GeneratorConstant;
import com.boco.power.database.Column;
import com.boco.power.database.DbProvider;
import com.boco.power.database.TableInfo;
import com.boco.power.factory.DbProviderFactory;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.GeneratorProperties;
import com.boco.power.utils.PathUtil;
import com.boco.power.utils.PropertiesUtils;
import org.beetl.core.Template;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author on 2016/12/7.
 */
public class CodeWriter extends AbstractCodeWriter {


    public void execute() {
        //初始化配置
        initConfig();
        //java代码目录
        mkdirs(config.getPathInfo());
        //创建配置文件路径
        mkdirs(config.getBaseConfigPathInfo());
        //创建配置文件
        writeBaseConfig(config.getBaseConfigFilesPath());
        //创建代码
        writeCode(config);
        //创建项目所需基础类
        writeBaseCode(config);
    }

    public void executeSpringBoot() {
        //初始化配置
        initSpringBootConfig();
        //java代码目录
        mkdirs(config.getPathInfo());
        //创建配置文件路径
        mkdirs(config.getBaseConfigPathInfo());
        //创建配置文件
        writeBaseConfig(config.getBaseConfigFilesPath());
        //创建代码
        writeCode(config);
        //创建项目所需基础类
        writeSpringBootBaseCode(config);

    }

    /**
     * 处理输出目录
     *
     * @param pathInfo 路径信息
     */
    private void mkdirs(Map<String, String> pathInfo) {
        for (Map.Entry<String, String> entry : pathInfo.entrySet()) {
            File dir = new File(entry.getValue());
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    /**
     * 创建工程所需配置文件
     *
     * @param baseConfigFiles web工程所需的配置文件
     */
    private void writeBaseConfig(Map<String, String> baseConfigFiles) {
        String basePackage = GeneratorProperties.basePackage();
        PropertiesUtils dbProp = new PropertiesUtils("jdbc.properties");
        Template template;
        String key;
        for (Map.Entry<String, String> entry : baseConfigFiles.entrySet()) {
            key = entry.getKey();
            if (ConstVal.TPL_JDBC.equals(key)) {
                String currentPath = Thread.currentThread().getContextClassLoader().getResource(ConstVal.JDBC).getPath();
                FileUtil.nioTransferCopy(new File(currentPath), new File(entry.getValue()));
            } else {
                template = BeetlTemplateUtil.getByName(key);
                template.binding(GeneratorConstant.BASE_PACKAGE, basePackage);
                template.binding(GeneratorConstant.APPLICATION_NAME, GeneratorProperties.applicationName());
                //spring config
                template.binding("mappingDir", basePackage.replaceAll("[.]", "/"));
                template.binding("jdbcUrl", "${jdbc.url}");
                template.binding("jdbcUserName", "${jdbc.username}");
                template.binding("jdbcPassword", "${jdbc.password}");
                //pom
                template.binding("projectVersion", "${project.version}");
                template.binding("springVersion", "${spring.version}");
                template.binding("mybatisVersion", "${mybatis.version}");
                template.binding("jacksonVersion", "${jackson.version}");
                template.binding("slf4jVersion", "${slf4j.version}");
                template.binding("log4j2Version", "${log4j2.version}");
                //log4j2
                template.binding("LOG_HOME", "${LOG_HOME}");
                template.binding("CATALINA_HOME", "${CATALINA_HOME}");
                //mybatis config
                template.binding("cacheEnabled", GeneratorProperties.enableCache());

                //SpringBoot yml
                template.binding("dbUrl", dbProp.getProperty("jdbc.url"));
                template.binding("dbUserName", dbProp.getProperty("jdbc.username"));
                template.binding("dbPassword", dbProp.getProperty("jdbc.password"));
                template.binding("dbDriver", dbProp.getProperty("jdbc.driver"));
                FileUtil.writeFileNotAppend(template.render(), entry.getValue());
            }
        }
    }

    /**
     * base code是项目的一些基本类，包括单元测基类
     * 时间转换类
     *
     * @param config
     */
    private void writeBaseCode(ConfigBuilder config) {
        String basePackage = GeneratorProperties.basePackage();
        Map<String, String> dirMap = config.getPathInfo();
        for (Map.Entry<String, String> entry : dirMap.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();
            if (ConstVal.SERVICE_TEST_PATH.equals(key)) {
                Template template = BeetlTemplateUtil.getByName(ConstVal.TPL_SERVICE_BASE_TEST);
                template.binding(GeneratorConstant.BASE_PACKAGE, basePackage);
                template.binding(GeneratorConstant.AUTHOR, System.getProperty("user.name"));//作者
                template.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
                FileUtil.writeFileNotAppend(template.render(), value + "\\ServiceBaseTest.java");
            }
            if (ConstVal.CONTROLLER_TEST_PATH.equals(key)) {
                Template template = BeetlTemplateUtil.getByName(ConstVal.TPL_CONTROLLER_BASE_TEST);
                template.binding(GeneratorConstant.BASE_PACKAGE, basePackage);
                template.binding(GeneratorConstant.AUTHOR, System.getProperty("user.name"));//作者
                template.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
                FileUtil.writeFileNotAppend(template.render(), value + "\\ControllerBaseTest.java");
            }
            if (ConstVal.DATE_CONVERTER_PATH.equals(key)) {
                Template template = BeetlTemplateUtil.getByName(ConstVal.TPL_DATE_CONVERTER);
                template.binding(GeneratorConstant.BASE_PACKAGE, basePackage);
                template.binding(GeneratorConstant.AUTHOR, System.getProperty("user.name"));//作者
                template.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
                FileUtil.writeFileNotAppend(template.render(), value + "\\DateConverter.java");
            }
        }
    }

    /**
     * 创建SpringBoot的基础类代码
     *
     * @param config
     */
    private void writeSpringBootBaseCode(ConfigBuilder config) {
        String basePackage = GeneratorProperties.basePackage();
        Map<String, String> dirMap = config.getPathInfo();
        for (Map.Entry<String, String> entry : dirMap.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();
            if (ConstVal.CONTROLLER_TEST_PATH.equals(key)) {
                Template template = BeetlTemplateUtil.getByName(ConstVal.TPL_SPRING_BOOT_CONTROLLER_BASE_TEST);
                template.binding(GeneratorConstant.BASE_PACKAGE, basePackage);
                template.binding(GeneratorConstant.AUTHOR, System.getProperty("user.name"));
                template.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
                FileUtil.writeFileNotAppend(template.render(), value + "\\ControllerBaseTest.java");
            }
        }
        //创建启动的主类
        Template template = BeetlTemplateUtil.getByName(ConstVal.TPL_SPRING_BOOT_MAIN);
        template.binding(GeneratorConstant.BASE_PACKAGE, basePackage);
        template.binding(GeneratorConstant.AUTHOR, System.getProperty("user.name"));
        template.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
        String basePackagePath = PathUtil.joinPath(config.getProjectPath().getJavaSrcPath(), basePackage);
        FileUtil.writeFileNotAppend(template.render(), basePackagePath + "\\SpringBootMainApplication.java");
    }

    /**
     * 生成model,dao,service,controller,controllerTest,serviceTest代码
     *
     * @param config
     */
    private void writeCode(ConfigBuilder config) {
        Map<String, String> dirMap = config.getPathInfo();
        List<TableInfo> tables = config.getTableInfo();
        DbProvider dbProvider = new DbProviderFactory().getInstance();
        for (TableInfo tableInfo : tables) {
            String table = tableInfo.getName();
            Map<String, Column> columnMap = dbProvider.getColumnsInfo(table);
            //实体名需要移除表前缀
            String tableTemp = StringUtil.removePrefix(table, GeneratorProperties.tablePrefix());
            String entityName = StringUtil.toCapitalizeCamelCase(tableTemp);
            for (Map.Entry<String, String> entry : dirMap.entrySet()) {
                String value = entry.getValue();
                String key = entry.getKey();
                if (ConstVal.DAO_PATH.equals(key)) {
                    String daoCode = new DaoBuilder().generateDao(entityName);
                    FileUtil.writeFileNotAppend(daoCode, value + "\\" + entityName + "Dao.java");
                }
                if (ConstVal.ENTITY_PATH.equals(key)) {
                    String modelCode = new ModelBuilder().generateModel(tableInfo,columnMap);
                    FileUtil.writeFileNotAppend(modelCode, value + "\\" + entityName + ".java");
                }
                if (ConstVal.SERVICE_PATH.equals(key)) {
                    String serviceCode = new ServiceBuilder().generateService(entityName);
                    FileUtil.writeFileNotAppend(serviceCode, value + "\\" + entityName + "Service.java");
                    String serviceImplCode = new ServiceImplBuilder().generateServiceImpl(entityName);
                    FileUtil.writeFileNotAppend(serviceImplCode, value + "\\impl\\" + entityName + "ServiceImpl.java");
                }
                if (ConstVal.SERVICE_TEST_PATH.equals(key)) {
                    String serviceTestCode = new ServiceTestBuilder().generateServiceTest(entityName);
                    FileUtil.writeFileNotAppend(serviceTestCode, value + "\\" + entityName + "ServiceTest.java");
                }
                if (ConstVal.CONTROLLER_PATH.equals(key)) {
                    String controllerCode = new ControllerBuilder().generateController(entityName);
                    FileUtil.writeFileNotAppend(controllerCode, value + "\\" + entityName + "Controller.java");
                }
                if (ConstVal.CONTROLLER_TEST_PATH.equals(key)) {
                    String controllerCode = new ControllerTestBuilder().generateControllerTest(entityName,columnMap);
                    FileUtil.writeFileNotAppend(controllerCode, value + "\\" + entityName + "ControllerTest.java");
                }

                if (ConstVal.MAPPER_PATH.equals(key)) {
                    String mapperCode = new MapperBuilder().generateMapper(table,columnMap);
                    FileUtil.writeFileNotAppend(mapperCode, value + "\\" + entityName + "Dao.xml");
                }

            }
        }
    }
}
