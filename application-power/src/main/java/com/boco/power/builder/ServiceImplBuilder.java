package com.boco.power.builder;

import com.boco.common.util.DateTimeUtil;
import com.boco.common.util.StringUtil;
import com.boco.power.constant.ConstVal;
import com.boco.power.constant.GeneratorConstant;
import com.boco.power.database.Column;
import com.boco.power.database.TableInfo;
import com.boco.power.utils.BeetlTemplateUtil;

import com.boco.power.utils.GeneratorProperties;
import org.beetl.core.Template;

import java.util.Map;

/**
 * 生成service层实现模板
 *
 * @author sunyu on 2016/12/7.
 */
public class ServiceImplBuilder implements IBuilder {

    @Override
    public String generateTemplate(TableInfo tableInfo, Map<String, Column> columnMap) {
        String tableTemp = StringUtil.removePrefix(tableInfo.getName(), GeneratorProperties.tablePrefix());
        String entityName = StringUtil.toCapitalizeCamelCase(tableTemp);
        String entitySimpleName = StringUtil.toCapitalizeCamelCase(entityName);//类名
        String firstLowName = StringUtil.firstToLowerCase(entitySimpleName);
        Template serviceImplTemplate = BeetlTemplateUtil.getByName(ConstVal.TPL_SERVICEIMPL);
        serviceImplTemplate.binding(GeneratorConstant.COMMON_VARIABLE);//作者
        serviceImplTemplate.binding(GeneratorConstant.FIRST_LOWER_NAME, firstLowName);
        serviceImplTemplate.binding(GeneratorConstant.ENTITY_SIMPLE_NAME, entitySimpleName);//类名
        serviceImplTemplate.binding(GeneratorProperties.getGenerateMethods());//过滤方法
        return serviceImplTemplate.render();
    }
}
