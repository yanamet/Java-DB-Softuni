package com.example.xmlexercise.productShop.services;

import com.example.xmlexercise.productShop.entities.users.ExportSellersDTO;
import com.example.xmlexercise.productShop.entities.users.ExportUsersWithSoldItemsDTO;
import com.example.xmlexercise.productShop.entities.users.User;
import com.example.xmlexercise.productShop.repositories.UserRepository;
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
        this.mapper = new ModelMapper();
    }

    @Override
    @Transactional
    public ExportSellersDTO findAllWithSoldProducts() {
        List<User> usersWithSold = this.userRepository.findAllUsersWithSoldProducts();

       List<ExportUsersWithSoldItemsDTO> users = usersWithSold
                .stream()
                .map(user -> this.mapper.map(user, ExportUsersWithSoldItemsDTO.class))
               .collect(Collectors.toList());

        return new ExportSellersDTO(users);
    }


}
