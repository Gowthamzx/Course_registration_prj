package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDB {
    public static void main(String[] args) {
        System.out.println("Testing MySQL JDBC connection...");

        try (Connection con = DBConnection.getConnection()) {
            if (con != null && !con.isClosed()) {
                System.out.println("=========================================");
                System.out.println(" SUCCESS: JDBC CONNECTED TO MYSQL!");
                System.out.println("=========================================");
                System.out.println("Database Name: " + con.getCatalog());

                DatabaseMetaData metaData = con.getMetaData();
                System.out.println("Database Product: " + metaData.getDatabaseProductName() + " " + metaData.getDatabaseProductVersion());
                System.out.println("Driver: " + metaData.getDriverName() + " " + metaData.getDriverVersion());

                try (Statement stmt = con.createStatement();
                     ResultSet rs = stmt.executeQuery("SHOW TABLES")) {
                    System.out.println("\nTables found in database:");
                    boolean hasTables = false;
                    while (rs.next()) {
                        hasTables = true;
                        System.out.println(" - " + rs.getString(1));
                    }
                    if (!hasTables) {
                        System.out.println(" (No tables found yet in '" + con.getCatalog() + "')");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("=========================================");
            System.err.println(" FAILED: JDBC CONNECTION ERROR!");
            System.err.println("=========================================");
            System.err.println("Error details: " + e.getMessage());
            System.err.println("\nTroubleshooting tips:");
            System.err.println("1. Verify MySQL Server is running (e.g. Start 'MySQL267' service or run mysqld).");
            System.err.println("2. Verify credentials in DBConnection.java (user, password, port 3306).");
            System.err.println("3. Ensure database 'course_registration_prj' is created in MySQL.");
            System.err.println("=========================================");
            e.printStackTrace();
        }
    }
}

