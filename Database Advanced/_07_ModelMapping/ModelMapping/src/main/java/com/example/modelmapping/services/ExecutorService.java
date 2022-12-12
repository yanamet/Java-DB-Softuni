package com.example.modelmapping.services;

public interface ExecutorService {

     final String REGISTER_USER_COMMAND = "RegisterUser";
     final String LOGIN_USER_COMMAND = "LoginUser";
     final String LOGOUT_USER_COMMAND = "Logout";
     final String ADD_GAME_COMMAND = "AddGame";
     final String EDIT_GAME_COMMAND = "EditGame";
     final String DELETE_GAME_COMMAND = "DeleteGame";
     final String ALL_GAMES_COMMAND = "AllGames";
     final String GAME_DETAILS_COMMAND = "DetailGame";

    String execute(String command);

}
