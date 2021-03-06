package Innlevering1.Database;


import com.mysql.jdbc.CommunicationsException;
import com.mysql.jdbc.exceptions.MySQLDataException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnector implements DatabaseInterface{
    private static String hostName;
    private static String dbName;
    private static String userName;
    private static String password;
    private static int port;
    private MysqlDataSource dataSource;

    /**
     * Reads the property file
     */
    public DatabaseConnector(String properties) throws SQLException, IOException{
        try (FileInputStream fileInputStream = new FileInputStream(properties)){
            Properties prop = new Properties();
            prop.load(fileInputStream);
            hostName = prop.getProperty("hostName");
            dbName = prop.getProperty("dbName");
            userName = prop.getProperty("userName");
            password = prop.getProperty("password");
            port = Integer.parseInt(prop.getProperty("port"));
            dropDatabaseIfExist(getConnection());
        }
    }


    /**
     *
     * @return connections to server
     */
    @Override
    public Connection getConnection() throws SQLException{
        try {
            dataSource = new MysqlDataSource();
            dataSource.setServerName(hostName);
            dataSource.setUser(userName);
            dataSource.setPassword(password);
            dataSource.setPort(port);
            createAndSetDatabase(dataSource.getConnection());
            return dataSource.getConnection();
        }catch (SQLException e){
            System.out.println(e.getErrorCode());
            throw new SQLException();
        }
    }

    /**
     * Creats and set the database name
     * @param connection
     */
    private void createAndSetDatabase(Connection connection) throws SQLException{
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            dataSource.setDatabaseName(dbName);
        }catch (SQLException e){
            throw new SQLException("Could not set or create Database");
        }
    }

    private void dropDatabaseIfExist (Connection connection) throws SQLException{
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate("DROP DATABASE IF EXISTS " + dbName);
        }
    }

}