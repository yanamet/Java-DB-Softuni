package com.example.tryy.productShop.services;

import com.example.tryy.productShop.entities.users.User;
import com.example.tryy.productShop.entities.users.UserWithSoldProductsDTO;
import com.example.tryy.productShop.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

        mapper = new ModelMapper();
    }

    @Override
    @Transactional
    public List<UserWithSoldProductsDTO> getUsersWithSoldProducts() {
        List<User> usersWithSoldProducts = this.userRepository.findAllWithSoldProducts();

        return usersWithSoldProducts
                .stream()
                .map(u -> this.mapper.map(u, UserWithSoldProductsDTO.class))
                .collect(Collectors.toList());

    }


}
