package com.boco.power.builder;

import com.boco.power.constant.GeneratorConstant;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.DateTimeUtil;
import com.boco.power.utils.GeneratorProperties;
import com.boco.power.utils.StringUtils;
import org.beetl.core.Template;

/**
 * 生成service层实现模板
 * @author sunyu on 2016/12/7.
 */
public class ServiceImplBuilder {
    /**
     *
     * @param tableName
     * @return
     */
    public String generateServiceImpl(String tableName){
        String entitySimpleName = StringUtils.toCapitalizeCamelCase(tableName);//类名
        String firstLowName = StringUtils.firstToLowerCase(entitySimpleName);
        Template serviceImplTemplate = BeetlTemplateUtil.getByName("serviceImpl.btl");
        serviceImplTemplate.binding(GeneratorConstant.AUTHOR,System.getProperty("user.name"));//作者
        serviceImplTemplate.binding(GeneratorConstant.FIRST_LOWER_NAME,firstLowName);
        serviceImplTemplate.binding(GeneratorConstant.ENTITY_SIMPLE_NAME,entitySimpleName);//类名
        serviceImplTemplate.binding(GeneratorConstant.BASE_PACKAGE, GeneratorProperties.basePackage());//基包名
        serviceImplTemplate.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
        return serviceImplTemplate.render();
    }
}
