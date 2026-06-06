package thesweetspot.patterns.singleton;

import thesweetspot.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;

    private DBConnection() {
        // Private constructor to prevent instantiation
    }

    public static Connection getInstance() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String url = "jdbc:sqlserver://localhost:1433;databaseName=Bakery;user=sa;password=YOUR_PASSWORD_HERE;encrypt=false;";
            Connection conn = DriverManager.getConnection(url);

            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
