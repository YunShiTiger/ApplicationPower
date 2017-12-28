package com.boco.power.builder;

import com.boco.common.util.DateTimeUtil;
import com.boco.power.constant.ConstVal;
import com.boco.power.constant.GeneratorConstant;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.GeneratorProperties;
import org.beetl.core.Template;

import java.util.Set;

public class DataSourceKeyBuilder {

    public String builderDataSourceKey(Set<String> sourceKeys){
        String basePackage = GeneratorProperties.basePackage();
        Template template = BeetlTemplateUtil.getByName(ConstVal.TPL_DATASOURCE_KEY);
        template.binding(GeneratorConstant.BASE_PACKAGE, basePackage);
        template.binding(GeneratorConstant.AUTHOR, System.getProperty("user.name"));
        template.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
        template.binding("datasource",buildDataSource(sourceKeys));
        return template.render();
    }

    private String buildDataSource(Set<String> sourceKeys){
        StringBuilder builder = new StringBuilder();
        for(String str:sourceKeys){
            builder.append("    /** 数据库源").append(str).append("*/\n");
            builder.append("    public static final String ").append(str.toUpperCase());
            builder.append("= \"").append(str).append("\";\n\n");
        }
        return builder.toString();
    }
}
