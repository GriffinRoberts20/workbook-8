package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    static Scanner input=new Scanner(System.in);
    public static void main(String[] args) {
        try (BasicDataSource dataSource = new BasicDataSource()) {
            dataSource.setUrl("jdbc:mysql://localhost:3305/sakila");
            dataSource.setUsername("root");
            dataSource.setPassword("yearup");
            Connection connection = dataSource.getConnection();

            System.out.print("Enter last name of actors to look up: ");
            String actorLastName=input.nextLine();
            List<String[]> lastNameList=findActors("",actorLastName,connection);
            System.out.println();
            if(lastNameList.isEmpty()){
                System.out.println("No actors found");
                System.exit(0);
            }
            System.out.println("ID    First"+" ".repeat(9)+"Last");
            for(String[] actor:lastNameList){
                System.out.println(actor[0]+" ".repeat(6-actor[0].length())+actor[1]+" ".repeat(14-actor[1].length())+actor[2]);
            }
            System.out.println();
            System.out.print("Enter ID of actor to lookup movies: ");
            String ActorId =input.nextLine();
            boolean idFound=false;
            for(String[] actor:lastNameList){
                if(ActorId.matches(actor[0])) idFound=true;
            }
            System.out.println();
            if(idFound){
                List<String[]> movies=getMovies(ActorId,connection);
                System.out.println("ID    Title"+" ".repeat(25)+"ReleaseDate  Rating");
                for(String[] movie:movies){
                    System.out.println(movie[0]+" ".repeat(6-movie[0].length())+movie[1]+" ".repeat(30-movie[1].length())+movie[2]+" ".repeat(3)+movie[3]);
                }
            } else System.out.println("Invalid actor ID.");
        } catch(SQLException e){
            throw new RuntimeException();
        }
    }

    public static List<String[]> findActors(String firstName,String lastName, Connection connection){
        List<String[]> actors=new ArrayList<>();
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT actor_id, first_name, last_name FROM actor WHERE first_name LIKE \"%"+firstName+"%\" AND last_name LIKE \"%"+lastName+"%\"")){
            try(ResultSet lastNameSet=preparedStatement.executeQuery()){
                while(lastNameSet.next()){
                    actors.add(new String[]{
                       lastNameSet.getString("actor_id"),
                       lastNameSet.getString("first_name"),
                       lastNameSet.getString("last_name")
                    });
                }
            }catch(SQLException e){
                throw new RuntimeException();
            }
        }catch(SQLException e){
            throw new RuntimeException();
        }
        return actors;
    }

    public static List<String[]> getMovies(String actorID,Connection connection){
        List<String[]> movies =new ArrayList<>();
        try(PreparedStatement preparedStatement =
                    connection.prepareStatement("""
                            SELECT f.film_id, f.title, f.release_year, f.rating
                            FROM film f
                            JOIN film_actor fa on f.film_id=fa.film_id
                            JOIN actor a on fa.actor_id=a.actor_id
                            WHERE a.actor_id="""+actorID)){
            try(ResultSet actorMovieSet =preparedStatement.executeQuery()){
                while(actorMovieSet.next()){
                    movies.add(new String[]{
                            actorMovieSet.getString("film_id"),
                            actorMovieSet.getString("title"),
                            actorMovieSet.getString("release_year"),
                            actorMovieSet.getString("rating")
                    });
                }
            }catch(SQLException e){
                throw new RuntimeException();
            }
        }catch(SQLException e){
            throw new RuntimeException();
        }
        return movies;
    }
}
