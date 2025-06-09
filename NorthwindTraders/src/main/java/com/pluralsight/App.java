package com.pluralsight;

import java.sql.*;

public class App {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3305/northwind",
                "root",
                "yearup")) {
            PreparedStatement preparedStatement=connection.prepareStatement("SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products ");

            ResultSet results =preparedStatement.executeQuery();
            System.out.println("ID   Product"+" ".repeat(28)+"Price     UnitsInStock");
            System.out.println("%".repeat(62));
            while (results.next()) {
                String id= results.getString("ProductID");
                String product = results.getString("ProductName");
                Double price=Double.parseDouble(results.getString("UnitPrice"));
                String stock= results.getString("UnitsInStock");
                System.out.print(id+".".repeat(5-id.length()));
                System.out.print(product);
                System.out.print(".".repeat(35-product.length()));
                System.out.printf("$%.2f",price);
                System.out.print(".".repeat(9-String.format("%.2f",price).length()));
                System.out.println(stock+".".repeat(12-stock.length()));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
