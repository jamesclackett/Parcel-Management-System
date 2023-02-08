package com.jimboidin.database;

import java.sql.*;

/**
 * Class for handling the execution of MySQL queries and updates.
 * Contains default SQL queries that will be useful within the
 * P.M.S. application.
 */
public class Database {
    public static final String JDBC_URL =
            "jdbc:h2:./src/main/resources/com.jimboidin/database/parceldb";
    public static final String MYDB =
            "jdbc:mysql://localhost:3306/parcel_management_sys";
    public static final String DEFAULT = JDBC_URL;

    public static final String SELECT_ALL = "SELECT * FROM parcel_management_sys.parcels " +
            "ORDER BY arrival_date DESC;";
    public static final String SELECT_COLLECTED = "SELECT * FROM parcel_management_sys.parcels " +
            "WHERE collected_date is not null " +
            "ORDER BY collected_date DESC;";
    public static final String SELECT_EXPIRED = "SELECT * FROM parcel_management_sys.parcels " +
            "WHERE arrival_date < NOW() - interval '1' month AND collected_date is null" +
            " ORDER BY arrival_date DESC;";
    public static final String SELECT_UNEXPIRED = "SELECT * FROM parcel_management_sys.parcels " +
            "WHERE arrival_date > NOW() - interval '1' month AND collected_date is null" +
            " ORDER BY arrival_date DESC;";


    /**
     *
     * Uses the DriverManager service in order to make a connection
     * with the given database and an execute the given SQL query.
     *
     * @param database a database url that conforms to jdbc standards
     * @param sql the string-value SQL query to be executed
     * @return a Result built from a Statement's ResultSet
     */
    public static Result executeQuery(String database, String sql) {
        Connection connection = null;
        Result result = null;

        try {
            connection = DriverManager.getConnection
                    (database, "root", "password");
            System.out.println("connected to db: " + database);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            result = new Result(resultSet);

        } catch (SQLException e){
            e.printStackTrace();

        } finally {
                try {
                    if (connection != null) {
                        connection.close();
                        System.out.println("disconnected from db: " + database);
                    }
                }
                catch (SQLException ignored){}
        }

        return result;
    }

    /**
     * Uses the DriverManager service in order to make a connection
     * with the given database and update according to the given
     * SQL query.
     *
     * @param database a database url that conforms to jdbc standards
     * @param sql the string-value SQL query to be executed
     * @return returns the integer result of Statement::executeUpdate
     */
    public static int executeUpdate(String database, String sql) {
        Connection connection = null;
        int rowsReturned = 0;

        try {
            connection = DriverManager.getConnection
                    (database, "root", "password");
            System.out.println("connected to db: " + database);

            Statement statement = connection.createStatement();
            rowsReturned = statement.executeUpdate(sql);

        } catch (SQLException e){
            e.printStackTrace();

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("disconnected from db: " + database);
                }
            }
            catch (SQLException ignored){}
        }

        return rowsReturned;
    }

}
