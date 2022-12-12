package com.example.springexercisebookshop.repositories;

import com.example.springexercisebookshop.Entities.Author;
import com.example.springexercisebookshop.Entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
   List<Book> findByReleaseDateAfter(LocalDate releaseDate);

   List<Book> findByAuthorFirstNameOrderByReleaseDateDescTitleAsc(String firstName);
}
