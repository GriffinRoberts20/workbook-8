package com.pluralsight;

import java.sql.*;

public class App {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3305/northwind",
                "root",
                "yearup")) {
            Statement statement = connection.createStatement();
            String query = "SELECT ProductName, UnitPrice FROM products";
            ResultSet results = statement.executeQuery(query);
            System.out.println("Product"+" ".repeat(28)+"Price");
            System.out.println("%".repeat(41));
            while (results.next()) {
                String product = results.getString("ProductName");
                Double price=Double.parseDouble(results.getString("UnitPrice"));
                System.out.print(product);
                System.out.print(".".repeat(35-product.length()));
                System.out.printf("$%.2f\n",price);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
