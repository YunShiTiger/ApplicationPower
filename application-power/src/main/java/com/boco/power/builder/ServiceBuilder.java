package com.boco.power.builder;

import com.boco.power.constant.GeneratorConstant;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.DateTimeUtil;
import com.boco.power.utils.GeneratorProperties;
import com.boco.power.utils.StringUtils;
import org.beetl.core.Template;

/**
 * 生成service层代码
 * @author sunyu 2016/12/7.
 */
public class ServiceBuilder {
    /**
     * 生成service代码
     * @param tableName 数据库表名
     * @return
     */
    public String generateService(String tableName){
        String entitySimpleName = StringUtils.toCapitalizeCamelCase(tableName);//类名
        Template serviceTemplate = BeetlTemplateUtil.getByName("service.btl");
        serviceTemplate.binding(GeneratorConstant.AUTHOR,System.getProperty("user.name"));//作者
        serviceTemplate.binding(GeneratorConstant.ENTITY_SIMPLE_NAME,entitySimpleName);//类名
        serviceTemplate.binding(GeneratorConstant.BASE_PACKAGE, GeneratorProperties.basePackage());//基包名
        serviceTemplate.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
        return serviceTemplate.render();
    }
}
