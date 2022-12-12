package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.BookSummary;

import java.io.IOException;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> searchBooksWithAgeRestrriction(String restriction);

    List<String> searchGoldenEditionBooks();

    List<String> findBooksByPriceGreaterThan5AndLowerThan40();

    List<String> findBooksNotReleasedInYear(int year);

    List<String> findBooksReleasedAfter(String date);

    List<String> findBooksContaining(String substring);

    List<String> findBooksWithAuthorsLastName(String lastNameEnding);

    int findBooksWithTitleLengthMoreThan(int neededLength);

    String searchBookByTitle(String title);
}
