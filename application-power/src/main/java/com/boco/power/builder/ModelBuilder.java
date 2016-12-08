package com.boco.power.builder;


import com.boco.power.constant.GeneratorConstant;
import com.boco.power.database.Column;
import com.boco.power.database.DataBaseInfo;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.DateTimeUtil;
import com.boco.power.utils.GeneratorProperties;
import com.boco.power.utils.StringUtils;
import org.beetl.core.Template;

import java.util.Map;
import java.util.UUID;

/**
 * @author sunyu on 2016/12/6.
 */
public class ModelBuilder {


    /**
     * 生成model
     * @param tableName
     * @return
     */
    public String generateModel(String tableName){
        String entitySimpleName = StringUtils.toCapitalizeCamelCase(tableName);//类名
        DataBaseInfo tableInfo = new DataBaseInfo();
        Map<String,Column> columnMap = tableInfo.getColumnsInfo(tableName);
        String fields = generateFields(columnMap);
        String gettersAndSetters = generateSetAndGetMethods(columnMap);
        String imports = generateImport(columnMap);
        Template template = BeetlTemplateUtil.getByName("model.btl");
        template.binding(GeneratorConstant.AUTHOR,System.getProperty("user.name"));//作者
        template.binding(GeneratorConstant.ENTITY_SIMPLE_NAME,entitySimpleName);//类名
        template.binding(GeneratorConstant.BASE_PACKAGE,GeneratorProperties.basePackage());//基包名
        template.binding(GeneratorConstant.FIELDS,fields);//字段
        template.binding(GeneratorConstant.GETTERS_AND_SETTERS,gettersAndSetters);//get和set方法
        template.binding(GeneratorConstant.CREATE_TIME, DateTimeUtil.getTime());//创建时间
        template.binding("SerialVersionUID", String.valueOf(UUID.randomUUID().getLeastSignificantBits()));
        template.binding("modelImports",imports);
        return template.render();
    }

    /**
     * 生成model的字段
     * @param columnMap
     * @return
     */
    private String generateFields(Map<String,Column> columnMap){
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String,Column> entry:columnMap.entrySet()){
            Column column = entry.getValue();
            builder.append("	//").append(column.getRemarks()).append("\n");
            builder.append("	private ").append(column.getColumnType()).append(" ");
            builder.append(StringUtils.underlineToCamel(column.getColumnName())).append(";\n");
        }
        return builder.toString();
    }

    /**
     * 生成model导包块
     * @param columnMap
     * @return
     */
    private String generateImport(Map<String,Column> columnMap){
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String,Column> entry:columnMap.entrySet()){
           String type = entry.getValue().getColumnType();
            if("BigDecimal".equals(type)){
                builder.append("import java.math.BigDecimal;\n");
            }
            if("Date".equals(type)){
                builder.append("import java.sql.Date;\n");
            }
            if("Timestamp".equals(type)){
                builder.append("import java.sql.Timestamp;\n");
            }
            if("Time".equals(type)){
                builder.append("import java.sql.Time;\n");
            }
        }
        return builder.toString();
    }

    /**
     * 生成get和set方法
     * @param columnMap
     * @return
     */
    private String generateSetAndGetMethods(Map<String,Column> columnMap){
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String,Column> entry:columnMap.entrySet()){
            Column column = entry.getValue();
            String columnName = column.getColumnName();
            //get
            builder.append("	public ").append(column.getColumnType()).append(" get");
            builder.append(StringUtils.toCapitalizeCamelCase(column.getColumnName())).append("(){\n");
            builder.append("		return ").append(StringUtils.underlineToCamel(columnName)).append(";\n");
            builder.append("	}\n");
            //set
            builder.append("	public void set").append(StringUtils.toCapitalizeCamelCase(columnName));
            builder.append("(").append(column.getColumnType()).append(" ").append(StringUtils.underlineToCamel(columnName));
            builder.append("){\n");
            builder.append("		this.").append(StringUtils.underlineToCamel(columnName));
            builder.append(" = ").append(StringUtils.underlineToCamel(columnName)).append(";\n");
            builder.append("	}\n");
        }
        return builder.toString();
    }

}
