package com.example.springexercisebookshop.Services;

import com.example.springexercisebookshop.Entities.Author;
import com.example.springexercisebookshop.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author getRandomAuthor() {
        long size = this.authorRepository.count();
        int id = new Random().nextInt((int)size) + 1;
        return this.authorRepository.findById(id).get();
    }
}
