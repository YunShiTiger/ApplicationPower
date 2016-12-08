package com.boco.power.builder;

import com.boco.power.constant.GeneratorConstant;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.GeneratorProperties;
import org.beetl.core.Template;

/**
 * maven 项目生成器
 * @author sunyu 2016/12/5.
 */
public class PomBuilder {
    public String generatePom(){
        Template template = BeetlTemplateUtil.getByName("pom.btl");
        template.binding(GeneratorConstant.BASE_PACKAGE, GeneratorProperties.basePackage());
        template.binding(GeneratorConstant.APPLICATION_NAME,GeneratorProperties.applicationName());
        template.binding("projectVersion","${project.version}");
        template.binding("springVersion","${spring.version}");
        template.binding("mybatisVersion","${mybatis.version}");
        template.binding("jacksonVersion","${jackson.version}");
        template.binding("slf4jVersion","${slf4j.version}");
        return template.render();
    }
}
