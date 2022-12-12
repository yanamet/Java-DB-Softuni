package com.example.springexercisebookshop.Services;

import com.example.springexercisebookshop.Entities.Category;

import java.util.Set;


public interface CategoryService {
     Set<Category> getRandomCategories();
}