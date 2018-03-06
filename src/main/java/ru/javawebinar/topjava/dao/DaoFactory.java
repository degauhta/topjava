package ru.javawebinar.topjava.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DaoFactory {
    private static DaoFactory instance = new DaoFactory();
    private static Connection connection;

    static {
        Properties prop = new Properties();
        try (InputStream inputStream = DaoFactory.class.getClassLoader()
                .getResourceAsStream("hsqldb.properties")) {
            prop.load(inputStream);
            Class.forName(prop.getProperty("driver"));
            connection = DriverManager.getConnection(prop.getProperty("url"),
                    prop.getProperty("user"), prop.getProperty("password"));
            connection.createStatement().executeUpdate("CREATE TABLE meals (\n" +
                    "  id INTEGER IDENTITY PRIMARY KEY,\n" +
                    "  description VARCHAR(200) NOT NULL,\n" +
                    "  calories INT NOT NULL,\n" +
                    "  dateTime TIMESTAMP);");
            connection.createStatement().executeUpdate(
                    "INSERT INTO meals VALUES (100,'Learn PHP', 123, NOW())");
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static DaoFactory getInstance() {
        return instance;
    }

    private DaoFactory() {
    }

    public static Connection getConnection() {
        return connection;
    }
}
