package com.boco.power.database;

import com.boco.power.utils.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sunyu on 2016/12/6.
 */
public class DataBaseInfo {
    /**
     * 获取表列的名称和类型
     * @param tableName
     * @return
     */
    public Map<String,Column> getColumnsInfo(String tableName) {
        Map<String,Column> colMap = new LinkedHashMap<>();
        Connection connection=null;
        try {
            connection= DbUtil.getConnection();
            DatabaseMetaData meta = DbUtil.getDatabaseMetaData(connection);
            ResultSet colRet = meta.getColumns(null, "%", tableName, "%");
            while (colRet.next()) {
                String columnName = colRet.getString("COLUMN_NAME");
                String isAutoIncrement = colRet.getString("IS_AUTOINCREMENT");
                int digits = colRet.getInt("DECIMAL_DIGITS");
                int dataType = colRet.getInt("DATA_TYPE");
                String remarks = colRet.getString("REMARKS");
                String columnType = TypeConvert.sqlTypeToJavaType(dataType, digits);
                //设置列信息
                Column column = new Column();
                column.setColumnName(columnName);
                column.setColumnType(columnType);
                column.setRemarks(remarks);
                if("YES".equals(isAutoIncrement)){
                    column.setAutoIncrement(true);
                }
                colMap.put(columnName,column);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
           DbUtil.close(connection);
        }
        return colMap;
    }



    /**
     * 根据匹配的表名获取表名
     * @param tablePrefix
     * @return
     */
    public List<String> listOfTablesByPrefix(String tablePrefix){
        List<String> tableList=new ArrayList<>();
        Connection connection=null;
        try {
            connection= DbUtil.getConnection();
            DatabaseMetaData databaseMetaData = DbUtil.getDatabaseMetaData(connection);
            String[] tableType = {"TABLE"};
            ResultSet rs = databaseMetaData.getTables(null, null, "", tableType);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if(StringUtils.isEmpty(tablePrefix)){
                    tableList.add(tableName);
                }else {
                    if(tableName.startsWith(tablePrefix)){
                        tableList.add(tableName);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
           DbUtil.close(connection);
        }
        return tableList;
    }
}
