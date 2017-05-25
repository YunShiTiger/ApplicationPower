package com.boco.power.factory;

import com.boco.power.database.DbProperties;
import com.boco.power.database.DbProvider;
import com.boco.power.database.MySqlProvider;
import com.boco.power.database.OracleProvider;

/**
 * @author sunyu 2016/12/11.
 */
public class DbProviderFactory {
    /**
     * 数据库属性
     */
    private DbProperties properties;

    public DbProviderFactory() {
        properties = new DbProperties();
    }

    public DbProvider getInstance() {
        String driverName = this.properties.getDriver();
        DbProvider provider = null;
        if ("com.mysql.jdbc.Driver".equals(driverName)) {
            provider = new MySqlProvider();
        }
        if ("com.mysql.cj.jdbc.Driver".equals(driverName)) {
            provider = new MySqlProvider();
        }
        if ("oracle.jdbc.driver.OracleDriver".equals(driverName)) {
            provider = new OracleProvider();
        }
        return provider;
    }
}
