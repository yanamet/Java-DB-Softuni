package com.example.modelmapping.services;

import com.example.modelmapping.entities.users.LoginDTO;
import com.example.modelmapping.entities.users.RegistrationDTO;
import com.example.modelmapping.entities.users.User;
import com.example.modelmapping.repositories.UserRepository;

import java.util.Optional;

public interface UserService {

    User register(RegistrationDTO registerData);

    Optional<User> login(LoginDTO loginData);

    void logout();

    User getLoggedUser();

    Optional<User> findByEmailAndPassword(String email, String password);
}
