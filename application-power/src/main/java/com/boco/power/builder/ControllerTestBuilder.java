package com.boco.power.builder;

import com.boco.common.util.DateTimeUtil;
import com.boco.common.util.RandomUtil;
import com.boco.common.util.StringUtil;
import com.boco.power.constant.ConstVal;
import com.boco.power.constant.GeneratorConstant;
import com.boco.power.database.Column;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.ColumnInfoCacheUtil;
import com.boco.power.utils.GeneratorProperties;
import org.beetl.core.Template;

import java.util.Map;

/**
 * 创建controller层接口测试
 *
 * @author sunyu on 2016/12/7.
 */
public class ControllerTestBuilder {

    private static final String controllerTestParams = "params";
    /**
     * 表名
     *
     * @param tableName
     * @return
     */
    public String generateControllerTest(String tableName) {
        String entitySimpleName = StringUtil.toCapitalizeCamelCase(tableName);//类名
        String firstLowName = StringUtil.firstToLowerCase(entitySimpleName);//类实例变量名
        Template controllerTemplate = BeetlTemplateUtil.getByName(ConstVal.TPL_CONTROLLER_TEST);
        controllerTemplate.binding(GeneratorConstant.AUTHOR, System.getProperty("user.name"));//作者
        controllerTemplate.binding(GeneratorConstant.FIRST_LOWER_NAME, firstLowName);
        controllerTemplate.binding(GeneratorConstant.ENTITY_SIMPLE_NAME, entitySimpleName);//类名
        controllerTemplate.binding(GeneratorConstant.BASE_PACKAGE, GeneratorProperties.basePackage());//基包名
        controllerTemplate.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
        controllerTemplate.binding(controllerTestParams,generateParams());
        controllerTemplate.binding(GeneratorProperties.getGenerateMethods());
        return controllerTemplate.render();
    }

    /**
     *
     * @return
     */
    private String generateParams(){
        Map<String,Column> columnMap = ColumnInfoCacheUtil.getColumnMap();
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Column> entry : columnMap.entrySet()) {
            Column column = entry.getValue();
            builder.append("\n");
            builder.append("            .param(\"").append(StringUtil.underlineToCamel(column.getColumnName()))
                    .append("\",\"").append(RandomUtil.randomValueByType(column.getColumnType()))
                    .append("\")");

        }
        return builder.toString();
    }
}
