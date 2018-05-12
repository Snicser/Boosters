package io.github.jordieh.boosters.framework.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    private HikariDataSource dataSource;

    public Database(String username, String password, String host, String database, int size) {
        host = host.contains(":") ? host : (host + ":3306");

        this.dataSource = new HikariDataSource();
        this.dataSource.setJdbcUrl("jdbc:mysql://" + host + "/" + database);
        this.dataSource.addDataSourceProperty("maximumPoolSize", size);
//        this.dataSource.setUsername(username);
//        this.dataSource.setPassword(password);
        this.dataSource.addDataSourceProperty("username", username);
        this.dataSource.addDataSourceProperty("password", password);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
