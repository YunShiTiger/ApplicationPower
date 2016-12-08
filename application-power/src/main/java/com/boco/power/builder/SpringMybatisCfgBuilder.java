package com.boco.power.builder;

import com.boco.power.constant.GeneratorConstant;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.GeneratorProperties;
import org.beetl.core.Template;

/**
 * @author sunyu on 2016/12/7.
 */
public class SpringMybatisCfgBuilder {

    /**
     * spring-mybatis集成配置文件
     * @return
     */
    public String generateSpringMyBatisCfg(){
        String basePackage = GeneratorProperties.basePackage();
        Template template = BeetlTemplateUtil.getByName("spring-mybatis.btl");
        template.binding(GeneratorConstant.BASE_PACKAGE, GeneratorProperties.basePackage());
        template.binding("mappingDir",basePackage.replaceAll("[.]","/"));
        template.binding("jdbcUrl","${jdbc.url}");
        template.binding("jdbcUserName","${jdbc.username}");
        template.binding("jdbcPassword","${jdbc.password}");
        return template.render();
    }
}
