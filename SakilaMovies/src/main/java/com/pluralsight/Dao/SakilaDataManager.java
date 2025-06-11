package com.pluralsight.Dao;

import com.pluralsight.Models.Actor;
import com.pluralsight.Models.Film;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SakilaDataManager {
    private BasicDataSource dataSource;

    public SakilaDataManager(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Actor> actorByLastName(String lastName){
        List<Actor> actors=new ArrayList<>();
        String query="SELECT actor_id, first_name, last_name FROM actor WHERE last_name LIKE \"%"+lastName+"%\"";
        try(Connection connection=dataSource.getConnection();
        PreparedStatement preparedStatement=connection.prepareStatement(query)){
            try(ResultSet lastNameSet=preparedStatement.executeQuery()){
                while(lastNameSet.next()){
                    int id=lastNameSet.getInt("actor_id");
                    String first=lastNameSet.getString("first_name");
                    String last=lastNameSet.getString("last_name");
                    actors.add(new Actor(id,first,last));
                }
            }catch(SQLException e){
                throw new RuntimeException();
            }
        }catch(SQLException e){
            throw new RuntimeException();
        }
        return actors;
    }

    public List<Film> getFilmsByActor(String actorID){
        List<Film> movies =new ArrayList<>();
        String query= """
                SELECT f.film_id, f.title, f.release_year, f.rating
                FROM film f
                JOIN film_actor fa on f.film_id=fa.film_id
                JOIN actor a on fa.actor_id=a.actor_id
                WHERE a.actor_id="""+actorID;
        try(Connection connection=dataSource.getConnection(); PreparedStatement preparedStatement=connection.prepareStatement(query)){
            try(ResultSet actorFilmSet =preparedStatement.executeQuery()){
                while(actorFilmSet.next()){
                    int id= actorFilmSet.getInt("film_id");
                    String title= actorFilmSet.getString("title");
                    String releaseYear= actorFilmSet.getString("release_year");
                    String rating= actorFilmSet.getString("rating");
                    movies.add(new Film(id,title,releaseYear,rating));
                }
            }catch(SQLException e){
                throw new RuntimeException();
            }
        }catch(SQLException e){
            throw new RuntimeException();
        }
        return movies;
    }

    public void setDataSource(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
