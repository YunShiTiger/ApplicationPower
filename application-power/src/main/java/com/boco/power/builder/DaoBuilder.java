package com.boco.power.builder;

import com.boco.common.util.DateTimeUtil;
import com.boco.common.util.StringUtil;
import com.boco.power.constant.ConstVal;
import com.boco.power.constant.GeneratorConstant;
import com.boco.power.utils.BeetlTemplateUtil;

import com.boco.power.utils.GeneratorProperties;

import org.beetl.core.Template;

/**
 * 生成dao层
 *
 * @author sunyu 2016/12/7.
 */
public class DaoBuilder {
    /**
     * 实体名
     *
     * @param entityName
     * @return
     */
    public String generateDao(String entityName) {
        String entitySimpleName = StringUtil.toCapitalizeCamelCase(entityName);//类名
        Template daoTemplate = BeetlTemplateUtil.getByName(ConstVal.TPL_DAO);
        daoTemplate.binding(GeneratorConstant.AUTHOR, System.getProperty("user.name"));//作者
        daoTemplate.binding(GeneratorConstant.ENTITY_SIMPLE_NAME, entitySimpleName);//类名
        daoTemplate.binding(GeneratorConstant.BASE_PACKAGE, GeneratorProperties.basePackage());//基包名
        daoTemplate.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
        daoTemplate.binding(GeneratorProperties.getGenerateMethods());//过滤方法
        return daoTemplate.render();
    }
}
