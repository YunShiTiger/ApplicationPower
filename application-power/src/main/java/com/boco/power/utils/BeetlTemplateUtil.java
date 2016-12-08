package com.boco.power.utils;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.IOException;

/**
 * 获取模板
 * @author sunyu on 2016/12/6.
 */
public class BeetlTemplateUtil {
    public static Template getByName(String templateName){
        try{
            ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("/template/");
            Configuration cfg = Configuration.defaultConfiguration();
            GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
            return gt.getTemplate(templateName);
        }catch (IOException e){
            throw new RuntimeException("获取模板异常");

        }
    }

    public static void main(String[] args) throws IOException {
        Template t = getByName("pom.btl");
        t.binding("projectName","boco-test");
        t.binding("projectVersion","${project.version}");
        t.binding("springVersion","${spring.version}");
        t.binding("mybatisVersion","${mybatis.version}");
        t.binding("jacksonVersion","${jackson.version}");
        t.binding("slf4jVersion","${slf4j.version}");
        //System.out.println(t.render());

        initSpringMvcConfig();
    }

    public static void initSpringMvcConfig() throws IOException{
        Template mvc = getByName("spring-mvc.btl");
        mvc.binding("basePackage","com.boco.gms");

        System.out.println(mvc.render());
    }

    public static void initSpringMybatisConfig() throws IOException{
        Template springMybatis = getByName("spring-mybatis.btl");
        springMybatis.binding("basePackage","com.boco.gms");
        springMybatis.binding("mappingDir","com/boco/gms");
    }
}
