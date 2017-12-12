package com.boco.power.utils;

import com.boco.power.database.Column;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类用于缓存包查出的字段信息
 */
public class ColumnInfoCacheUtil {

    private static Map<String, Column> columnMap = new HashMap<>();

    public static void setColumn(String key,Column column){
        columnMap.put(key,column);
    }

    public static Map<String, Column> getColumnMap(){
        return columnMap;
    }
}
