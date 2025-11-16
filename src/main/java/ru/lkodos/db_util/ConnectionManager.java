package ru.lkodos.db_util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.lkodos.exception.DbAccessException;

import java.sql.Connection;

public class ConnectionManager {

    private static HikariDataSource dataSource;

    private static final String URL = "db.url";
    private static final String DRIVER = "db.driver";
    private static final String MAX_POOL_SIZE = "max.pool.size";
    private static final String MIN_IDLE = "min.idle";
    private static final String CONNECTION_TIMEOUT = "connection.timeout";
    private static final String IDLE_TIMEOUT = "idle.timeout";
    private static final String MAX_LIFETIME = "max.lifetime";

    private ConnectionManager() {
    }

    public static Connection getConnection() {
        try {
            if (dataSource == null) {
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(PropertiesUtil.getProperties(URL));
                config.setDriverClassName(PropertiesUtil.getProperties(DRIVER));
                config.setMaximumPoolSize(Integer.parseInt(PropertiesUtil.getProperties(MAX_POOL_SIZE)));
                config.setMinimumIdle(Integer.parseInt(PropertiesUtil.getProperties(MIN_IDLE)));
                config.setConnectionTimeout(Integer.parseInt(PropertiesUtil.getProperties(CONNECTION_TIMEOUT)));
                config.setIdleTimeout(Integer.parseInt(PropertiesUtil.getProperties(IDLE_TIMEOUT)));
                config.setMaxLifetime(Integer.parseInt(PropertiesUtil.getProperties(MAX_LIFETIME)));

                dataSource = new HikariDataSource(config);
            }
            return dataSource.getConnection();
        } catch (Exception e) {
            throw new DbAccessException("Database connection failed!", e);
        }
    }
}