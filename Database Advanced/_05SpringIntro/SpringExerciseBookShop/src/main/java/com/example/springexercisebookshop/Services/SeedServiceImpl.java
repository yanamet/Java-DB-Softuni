package com.example.springexercisebookshop.Services;

import com.example.springexercisebookshop.Entities.*;
import com.example.springexercisebookshop.repositories.AuthorRepository;
import com.example.springexercisebookshop.repositories.BookRepository;
import com.example.springexercisebookshop.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeedServiceImpl implements SeedService {
    private final static String RESOURCE_PATH = "src\\main\\resources\\files";
    private final static String AUTHORS_FILE_PATH = RESOURCE_PATH + "\\authors.txt";
    private final static String BOOKS_FILE_PATH = RESOURCE_PATH + "\\books.txt";
    private final static String CATEGORIES_FILE_PATH = RESOURCE_PATH + "\\categories.txt";

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void seedAuthors() throws IOException {
        Files.readAllLines(Path.of(AUTHORS_FILE_PATH))
                .stream()
                .filter(a -> !a.isBlank())
                .map(names -> names.split(" "))
                .map(a -> new Author(a[0], a[1]))
                .forEach(a -> authorRepository.save(a));
    }

    @Override
    public void seedCategories() throws IOException {
        Files.readAllLines(Path.of(CATEGORIES_FILE_PATH))
                .stream()
                .filter(c -> !c.isBlank())
                .map(Category::new)
                .forEach(c -> categoryRepository.save(c));
    }

    @Override
    public void seedBooks() throws IOException {
        Files.readAllLines(Path.of(BOOKS_FILE_PATH))
                .stream()
                .filter(a -> !a.isBlank())
                .map(this::getBookObject)
                .forEach(b -> bookRepository.save(b));
    }

    private Book getBookObject(String line){
        String[] bookInfo = line.split(" ");

        EditionType editionType = EditionType.values()[Integer.parseInt(bookInfo[0])];

        LocalDate releaseDate = LocalDate.parse(bookInfo[1], DateTimeFormatter.ofPattern("d/M/yyyy"));

        int copies = Integer.parseInt(bookInfo[2]);

        BigDecimal price = new BigDecimal(Double.parseDouble(bookInfo[3]));

        AgeRestriction ageRestriction = AgeRestriction.values()[Integer.parseInt(bookInfo[4])];

        String title = Arrays.stream(bookInfo)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService.getRandomCategories();

        return new Book(title, editionType, price,copies,  releaseDate,
                ageRestriction, author, categories);
    }

}
