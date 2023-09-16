package utilities;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtils {


    //This method establishes a connection with the ManagementOnSchool database and returns Connection data
    public static Connection connectToDatabase() {
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://managementonschools.com:5432/school_management", "select_user", "43w5ijfso");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }


    //This method creates and returns a statement object by calling the connectToDatabase() method
    public static Statement createStatement() {
        Statement statement;
        try {
            statement = connectToDatabase().createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statement;
    }


    //This method runs a SQL query and returns true if data is returned, false otherwise
    public static boolean execute(String sql) {

        try {
            return createStatement().execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    //This method runs a SQL query and returns the result as ResultSet
    public static ResultSet executeQuery(String sql) {

        try {
            return createStatement().executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //This method returns a column of any table we want as a list
    public static List<Object> getColumnList(String tableName, String columnName) throws SQLException {

        List<Object> list = new ArrayList<>();

        ResultSet resultSet = executeQuery("select " + columnName + " from " + tableName);

        while (resultSet.next()) {
            list.add(resultSet.getObject(columnName));
        }

        return list;

    }

    //This method closes the connection
    public static void closeConnection() {

        try {
            connectToDatabase().close();
            createStatement().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
