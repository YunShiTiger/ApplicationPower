package com.boco.power.builder;

import com.boco.power.constant.GeneratorConstant;
import com.boco.power.database.Column;
import com.boco.power.database.DataBaseInfo;
import com.boco.power.utils.BeetlTemplateUtil;
import com.boco.power.utils.GeneratorProperties;
import com.boco.power.utils.StringUtils;
import org.beetl.core.Template;

import java.util.Map;

/**
 * 创建mybatis mapper文件
 *
 * @author sunyu on 2016/12/7.
 */
public class MapperBuilder {

    public String generateMapper(String tableName) {
        String entitySimpleName = StringUtils.toCapitalizeCamelCase(tableName);//类名
        String firstLowName = StringUtils.firstToLowerCase(entitySimpleName);
        DataBaseInfo tableInfo = new DataBaseInfo();
        Map<String, Column> columnMap = tableInfo.getColumnsInfo(tableName);
        String insertSql = generateInsertSql(columnMap, tableName);
        String updateSql = generateUpdateSql(columnMap, tableName);
        String selectSql = generateSelectSql(columnMap, tableName);
        String results = generateResultMap(columnMap);
        Template mapper = BeetlTemplateUtil.getByName("Mapper.btl");
        mapper.binding(GeneratorConstant.FIRST_LOWER_NAME, firstLowName);
        mapper.binding(GeneratorConstant.ENTITY_SIMPLE_NAME, entitySimpleName);//类名
        mapper.binding(GeneratorConstant.BASE_PACKAGE, GeneratorProperties.basePackage());//基包名
        mapper.binding(GeneratorConstant.INSERT_SQL, insertSql);
        mapper.binding(GeneratorConstant.UPDATE_SQL, updateSql);
        mapper.binding(GeneratorConstant.SELECT_SQL, selectSql);
        mapper.binding(GeneratorConstant.RESULT_MAP, results);
        mapper.binding(GeneratorConstant.TABLE_NAME, tableName);
        return mapper.render();
    }

    /**
     * 生成insert语句
     *
     * @param columnMap
     * @param tableName
     * @return
     */
    private String generateInsertSql(Map<String, Column> columnMap, String tableName) {
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("insert into ").append(tableName).append("(\n");

        StringBuilder insertValues = new StringBuilder();
        int i = 0;
        int size = columnMap.size();
        for (Map.Entry<String, Column> entry : columnMap.entrySet()) {
            if (i < size - 1) {
                insertSql.append("			").append(entry.getKey()).append(",\n");
                insertValues.append("			#{").append(StringUtils.underlineToCamel(entry.getKey())).append("},\n");
            } else {
                insertSql.append("			").append(entry.getKey()).append("\n");
                insertValues.append("			#{").append(StringUtils.underlineToCamel(entry.getKey())).append("}\n");
            }
            i++;
        }
        insertSql.append("		) values (\n");
        insertSql.append(insertValues);
        insertSql.append("		)");
        return insertSql.toString();
    }

    /**
     * 生成update语句
     *
     * @param columnMap
     * @param tableName
     * @return
     */
    private String generateUpdateSql(Map<String, Column> columnMap, String tableName) {
        StringBuilder updateSql = new StringBuilder();
        updateSql.append(" update ").append(tableName).append(" set\n");
        int i = 0;
        int size = columnMap.size();
        for (Map.Entry<String, Column> entry : columnMap.entrySet()) {
            if (i < size - 1) {
                updateSql.append("			").append(entry.getKey()).append(" = #{");
                updateSql.append(StringUtils.underlineToCamel(entry.getKey())).append("},\n");
            } else {
                updateSql.append("			").append(entry.getKey()).append(" = #{");
                updateSql.append(StringUtils.underlineToCamel(entry.getKey())).append("}");
            }
            i++;
        }
        return updateSql.toString();
    }

    /**
     * 生成查询语句
     *
     * @param columnMap
     * @param tableName
     * @return
     */
    private String generateSelectSql(Map<String, Column> columnMap, String tableName) {
        StringBuilder selectSql = new StringBuilder();
        selectSql.append(" select \n");
        int i = 0;
        int size = columnMap.size();
        for (Map.Entry<String, Column> entry : columnMap.entrySet()) {
            if (i < size - 1) {
                selectSql.append("			").append(entry.getKey()).append(",\n");
            } else {
                selectSql.append("			").append(entry.getKey()).append("\n");
            }
            i++;
        }
        selectSql.append(" 		 from ").append(tableName);
        return selectSql.toString();
    }

    /**
     * mapper映射文件中resultMap下的result
     *
     * @param columnMap
     * @return
     */
    private String generateResultMap(Map<String, Column> columnMap) {
        StringBuilder results = new StringBuilder();
        String property;
        for (Map.Entry<String, Column> entry : columnMap.entrySet()) {
            property = StringUtils.underlineToCamel(entry.getKey());
            results.append("\t\t<result property=\"").append(property).append("\" column=\"");
            results.append(entry.getKey()).append("\" />\n");
        }
        return results.toString();
    }
}
