package com.boco.power.builder;

import com.boco.power.constant.GeneratorConstant;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.DateTimeUtil;
import com.boco.power.utils.GeneratorProperties;
import com.boco.power.utils.StringUtils;
import org.beetl.core.Template;

/**
 * 生成dao层
 * @author sunyu 2016/12/7.
 */
public class DaoBuilder {
    /**
     * 生成dao
     * @param tableName
     * @return
     */
    public String generateDao(String tableName){
        String entitySimpleName = StringUtils.toCapitalizeCamelCase(tableName);//类名
        Template daoTemplate = BeetlTemplateUtil.getByName("Dao.btl");
        daoTemplate.binding(GeneratorConstant.AUTHOR,System.getProperty("user.name"));//作者
        daoTemplate.binding(GeneratorConstant.ENTITY_SIMPLE_NAME,entitySimpleName);//类名
        daoTemplate.binding(GeneratorConstant.BASE_PACKAGE, GeneratorProperties.basePackage());//基包名
        daoTemplate.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
        return daoTemplate.render();
    }
}
