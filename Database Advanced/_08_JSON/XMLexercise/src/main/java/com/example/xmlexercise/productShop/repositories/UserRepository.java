package com.example.xmlexercise.productShop.repositories;

import com.example.xmlexercise.productShop.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u From User u " +
            " WHERE (SELECT COUNT(p) " +
            " FROM Product p  " +
            " WHERE p.seller = u AND p.buyer IS NOT NULL) > 0 " +
            " ORDER BY u.lastName, u.firstName")
    List<User> findAllUsersWithSoldProducts();
}
