package database;

import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {

        try {
            Connection con = DBConnection.getConnection();

            System.out.println("JDBC CONNECTED TO MYSQL!");
            System.out.println("Database: " +
                con.getCatalog());

            con.close();

        } catch (Exception e) {
            System.out.println("JDBC CONNECTION FAILED!");
            e.printStackTrace();
        }
    }
}