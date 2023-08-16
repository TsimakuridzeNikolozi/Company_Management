import properties.DBProperties;
import service.DataManager;

import java.sql.*;

public class main {

//    private static Connection connection;

    public static void main(String[] args) {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + DBProperties.DB_SERVER,
                    DBProperties.USERNAME,
                    DBProperties.PASSWORD);
            // Use the specified database
            Statement statement = connection.createStatement();
            statement.execute("USE " + DBProperties.SCHEMA_NAME);

            String query = "SELECT * FROM person";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String string = resultSet.getString("last_name");
                System.out.println(string);
            }

        } catch (Exception e) {
            // Throw a runtime exception if an error occurs during initialization
            throw new RuntimeException("Error initializing database: " + e.getMessage());
        }
    }
}

