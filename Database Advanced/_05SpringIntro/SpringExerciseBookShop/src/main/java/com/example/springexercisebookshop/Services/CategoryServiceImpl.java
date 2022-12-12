package com.example.springexercisebookshop.Services;

import com.example.springexercisebookshop.Entities.Category;
import com.example.springexercisebookshop.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Set<Category> getRandomCategories() {
        long size = this.categoryRepository.count();
        int categoriesCount = new Random().nextInt((int)size);

        Set<Integer> categoriesId = new HashSet<>();

        for (int i = 0; i < categoriesCount; i++) {
            categoriesId.add(new Random().nextInt((int)size) + 1);
        }

        Set<Category> categories = new HashSet<>(this.categoryRepository.findAllById(categoriesId));

        return categories;
    }
}
