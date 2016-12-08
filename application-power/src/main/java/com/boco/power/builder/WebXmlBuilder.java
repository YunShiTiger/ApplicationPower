package com.boco.power.builder;

import com.boco.power.constant.GeneratorConstant;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.GeneratorProperties;
import org.beetl.core.Template;

/**
 * 生成web xml配置文件
 * @author sunyu on 2016/12/7.
 */
public class WebXmlBuilder {
    /**
     * 生成web xml配置文件
     * @return
     */
    public String generateWebXml(){
        Template template = BeetlTemplateUtil.getByName("web.btl");
        template.binding(GeneratorConstant.APPLICATION_NAME, GeneratorProperties.applicationName());
        return template.render();
    }
}
