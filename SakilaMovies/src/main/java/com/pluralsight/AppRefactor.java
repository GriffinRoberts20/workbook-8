package com.pluralsight;

import com.pluralsight.Dao.SakilaDataManager;
import com.pluralsight.Models.Actor;
import com.pluralsight.Models.Film;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AppRefactor {
    static Scanner input=new Scanner(System.in);
    public static void main(String[] args) {
        String url="jdbc:mysql://localhost:3305/sakila";
        String username="root";
        String password="yearup";
        try(BasicDataSource dataSource=new BasicDataSource()){
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
            SakilaDataManager sdm=new SakilaDataManager(dataSource);
            System.out.print("Enter last name of actors to look up: ");
            String actorLastName=input.nextLine();
            List<Actor> lastNameList=sdm.actorByLastName(actorLastName);
            System.out.println();
            if(lastNameList.isEmpty()){
                System.out.println("No actors found");
                System.exit(0);
            }
            System.out.println("ID    First"+" ".repeat(9)+"Last");
            for(Actor actor:lastNameList){
                actor.printBasic();
            }
            System.out.println();
            System.out.print("Enter ID of actor to lookup movies: ");
            String ActorId =input.nextLine();
            boolean idFound=false;
            for(Actor actor:lastNameList){
                if (Integer.toString(actor.getActorId()).matches(ActorId)) {
                    idFound = true;
                    break;
                }
            }
            if(idFound){
                List<Film> films=sdm.getFilmsByActor(ActorId);
                System.out.println("ID    Title"+" ".repeat(25)+"ReleaseDate  Rating");
                for(Film film:films){
                    film.printBasic();
                }
            } else System.out.println("Invalid actor ID.");
        } catch (SQLException e){
            throw new RuntimeException();
        }
    }
}
