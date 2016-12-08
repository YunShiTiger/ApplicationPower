package com.boco.power.builder;

import com.boco.power.constant.GeneratorConstant;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.GeneratorProperties;
import org.beetl.core.Template;

/**
 * 生成spring mvc配置文件
 * @author sunyu 2016/12/7.
 */
public class SpringMvcCfgBuilder {
    public String generateSpringMvcCfg(){
        Template template = BeetlTemplateUtil.getByName("spring-mvc.btl");
        template.binding(GeneratorConstant.BASE_PACKAGE, GeneratorProperties.basePackage());
        return template.render();
    }
}
