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
 * 创建service层测试框架
 *
 * @author sunyu on 2016/12/7.
 */
public class ServiceTestBuilder implements IBuilder {

    @Override
    public String generateTemplate(TableInfo tableInfo, Map<String, Column> columnMap) {
        String tableTemp = StringUtil.removePrefix(tableInfo.getName(), GeneratorProperties.tablePrefix());
        String entitySimpleName = StringUtil.toCapitalizeCamelCase(tableTemp);//类名
        String firstLowName = StringUtil.firstToLowerCase(entitySimpleName);
        Template serviceTestTemplate = BeetlTemplateUtil.getByName(ConstVal.TPL_SERVICE_TEST);
        serviceTestTemplate.binding(GeneratorConstant.COMMON_VARIABLE);//作者
        serviceTestTemplate.binding(GeneratorConstant.FIRST_LOWER_NAME, firstLowName);
        serviceTestTemplate.binding(GeneratorConstant.ENTITY_SIMPLE_NAME, entitySimpleName);//类名
        serviceTestTemplate.binding(GeneratorProperties.getGenerateMethods());//过滤方法
        return serviceTestTemplate.render();
    }
}
