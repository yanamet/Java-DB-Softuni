package com.example.modelmapping.services.impl;

import com.example.modelmapping.entities.users.LoginDTO;
import com.example.modelmapping.entities.users.RegistrationDTO;
import com.example.modelmapping.entities.users.User;
import com.example.modelmapping.exceprions.UserNotLoggedInException;
import com.example.modelmapping.repositories.UserRepository;
import com.example.modelmapping.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private User currentUser;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentUser = null;
    }

    @Override
    public User register(RegistrationDTO registerData) {
        ModelMapper mapper = new ModelMapper();
        User toRegister = mapper.map(registerData, User.class);

        long count = this.userRepository.count();

        if(count == 0){
            toRegister.setAdministrator(true);
        }

        return this.userRepository.save(toRegister);
    }

    @Override
    public Optional<User> login(LoginDTO loginData) {
        Optional<User> user = this.userRepository.findByEmailAndPassword(
                loginData.getEmail(), loginData.getPassword());

        if(user.isPresent()){
            this.currentUser = user.get();
        }

        return user;
    }

    @Override
    public void logout() {
            this.currentUser = null;

    }

    @Override
    public User getLoggedUser(){
        if(currentUser == null){
            throw new UserNotLoggedInException("Execute login command first.");

        }
        return this.currentUser;
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return this.userRepository.findByEmailAndPassword(email, password);
    }

}
