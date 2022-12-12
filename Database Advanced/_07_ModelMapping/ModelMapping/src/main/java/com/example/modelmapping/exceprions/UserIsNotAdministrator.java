package com.example.modelmapping.exceprions;

public class UserIsNotAdministrator extends RuntimeException{
    public UserIsNotAdministrator(){
        super("User should be an administrator to do this.");
    }
}
