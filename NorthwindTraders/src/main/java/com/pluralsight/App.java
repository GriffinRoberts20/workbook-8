package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Scanner;

public class App {
    static Scanner input=new Scanner(System.in);

    public static void main(String[] args) {
        boolean running=true;
        try(BasicDataSource dataSource=new BasicDataSource()) {
            dataSource.setUrl("jdbc:mysql://localhost:3305/northwind");
            dataSource.setUsername("root");
            dataSource.setPassword("yearup");
            Connection connection=dataSource.getConnection();
            while (running) {
                printMenu();
                String choice = input.nextLine().trim();
                switch (choice) {
                    case "1":
                        getProducts(connection,"");
                        break;
                    case "2":
                        allCustomers(connection);
                        break;
                    case "3":
                        categories(connection);
                        break;
                    case "0":
                        running = false;
                        continue;
                    default:
                        System.out.println("Invalid choice.");
                }
                enterContinue();
                clearScreen();
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printMenu(){
        System.out.println("Northwind DB");
        System.out.println("%".repeat(35));
        System.out.println("What would you like to do?");
        System.out.println("   1) Display all products");
        System.out.println("   2) Display all customers");
        System.out.println("   3) Search products by category");
        System.out.println("   0) Exit");
        System.out.print("Select an option: ");
    }

    public static void enterContinue(){
        System.out.print("Press enter to continue ");
        input.nextLine();
    }

    public static void clearScreen(){
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static void getProducts(Connection connection, String where){
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products " + where);
            ResultSet results = preparedStatement.executeQuery()) {

            System.out.println("ID   Product" + " ".repeat(28) + "Price     UnitsInStock");
            System.out.println("%".repeat(62));
            while (results.next()) {
                String id = results.getString("ProductID");
                String product = results.getString("ProductName");
                Double price = Double.parseDouble(results.getString("UnitPrice"));
                String stock = results.getString("UnitsInStock");
                System.out.print(id + ".".repeat(5 - id.length()));
                System.out.print(product);
                System.out.print(".".repeat(35 - product.length()));
                System.out.printf("$%.2f", price);
                System.out.print(".".repeat(9 - String.format("%.2f", price).length()));
                System.out.println(stock + ".".repeat(12 - stock.length()));
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static void allCustomers(Connection connection){
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT ContactName, CompanyName, City, Country, Phone FROM customers Order BY Country ");
            ResultSet results = preparedStatement.executeQuery()){
            System.out.println("Contact Name" + " ".repeat(14) + "Company" + " ".repeat(31) + "City" + " ".repeat(14) + "Country       Phone");
            System.out.println("%".repeat(116));
            while (results.next()) {
                String name = results.getString("ContactName");
                String company = results.getString("CompanyName");
                String city = results.getString("City");
                String country = results.getString("Country");
                String phone = results.getString("Phone");
                System.out.print(name + ".".repeat(26 - name.length()));
                System.out.print(company);
                System.out.print(".".repeat(38 - company.length()));
                System.out.print(city);
                if (city != null) System.out.print(".".repeat(18 - city.length()));
                else System.out.print(".".repeat(14));
                System.out.print(country);
                if (country != null) System.out.print(".".repeat(14 - country.length()));
                else System.out.print(".".repeat(10));
                System.out.print(phone);
                if (phone != null) System.out.println(".".repeat(20 - phone.length()));
                else System.out.println(".".repeat(16));
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static void categories(Connection connection) {
        while(true) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT CategoryID, CategoryName FROM categories Order BY CategoryID ");
                 ResultSet results = preparedStatement.executeQuery()) {

                System.out.println("ID  Category");
                System.out.println("%".repeat(22));
                while (results.next()) {
                    String id = results.getString("CategoryID");
                    String product = results.getString("CategoryName");
                    System.out.print(id + ".".repeat(5 - id.length()));
                    System.out.print(product);
                    System.out.println(".".repeat(17 - product.length()));
                }
                System.out.print("Enter the ID of the category your like to\nfilter by, or 0 to return to home screen: ");
                String choice = input.nextLine();
                if (choice.matches("\\d+")) {
                    int id = Integer.parseInt(choice);
                    try (PreparedStatement maxIdStatement = connection.prepareStatement("SELECT max(CategoryID) as maxID FROM categories ");
                         ResultSet maxIdRe = maxIdStatement.executeQuery()) {
                        if (maxIdRe.next()) {
                            int maxId = maxIdRe.getInt("maxID");
                            if (id == 0) {
                                System.out.println("Returning to home screen");
                            } else if (id > 0 && id <= maxId) {
                                getProducts(connection, "WHERE CategoryID=" + id);
                            } else {
                                System.out.println("Invalid input");
                                enterContinue();
                                continue;
                            }
                        } else System.out.println("Could not retrieve category information");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Invalid input");
                    enterContinue();
                    continue;
                }
                break;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
