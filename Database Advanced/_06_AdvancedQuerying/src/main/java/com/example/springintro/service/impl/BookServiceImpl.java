package com.example.springintro.service.impl;

import com.example.springintro.model.entity.*;
import com.example.springintro.repository.BookRepository;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private static final String BOOKS_FILE_PATH = "src/main/resources/files/books.txt";

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (bookRepository.count() > 0) {
            return;
        }

        Files
                .readAllLines(Path.of(BOOKS_FILE_PATH))
                .forEach(row -> {
                    String[] bookInfo = row.split("\\s+");

                    Book book = createBookFromInfo(bookInfo);

                    bookRepository.save(book);
                });
    }

    @Override
    public List<Book> findAllBooksAfterYear(int year) {
        return bookRepository
                .findAllByReleaseDateAfter(LocalDate.of(year, 12, 31));
    }

    @Override
    public List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year) {
        return bookRepository
                .findAllByReleaseDateBefore(LocalDate.of(year, 1, 1))
                .stream()
                .map(book -> String.format("%s %s", book.getAuthor().getFirstName(),
                        book.getAuthor().getLastName()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName) {
       return bookRepository
                .findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(firstName, lastName)
                .stream()
                .map(book -> String.format("%s %s %d",
                        book.getTitle(),
                        book.getReleaseDate(),
                        book.getCopies()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> searchBooksWithAgeRestrriction(String restriction) {
        AgeRestriction ageRestriction = AgeRestriction.valueOf(restriction.toUpperCase());
        return this.bookRepository.findByAgeRestriction(ageRestriction)
                .stream().map(Book::getTitle).collect(Collectors.toList());
    }

    @Override
    public List<String> searchGoldenEditionBooks() {
        EditionType editionType = EditionType.GOLD;
        return this.bookRepository.findByEditionTypeAndCopiesLessThan(editionType, 5000)
                .stream().map(Book::getTitle).collect(Collectors.toList());
    }

    @Override
    public List<String> findBooksByPriceGreaterThan5AndLowerThan40() {
        BigDecimal min = BigDecimal.valueOf(5);
        BigDecimal max = BigDecimal.valueOf(40);
        return this.bookRepository.findByPriceLessThanOrPriceGreaterThan(min, max)
                .stream().map(b -> b.getTitle() + " -> " + b.getPrice())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findBooksNotReleasedInYear(int year) {

        LocalDate before = LocalDate.of(year, 1, 1);
        LocalDate after = LocalDate.of(year, 12, 31);

        return this.bookRepository.findByReleaseDateBeforeOrReleaseDateAfter(before, after)
                .stream().map(Book::getTitle).collect(Collectors.toList());
    }

    @Override
    public List<String> findBooksReleasedAfter(String date) {
        LocalDate releaseDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return this.bookRepository.findAllByReleaseDateBefore(releaseDate)
                .stream().map(b -> b.getTitle() + " " + b.getEditionType() + " " + b.getPrice())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findBooksContaining(String substring) {
        return this.bookRepository.findByTitleContaining(substring)
                .stream().map(Book::getTitle).collect(Collectors.toList());
    }

    @Override
    public List<String> findBooksWithAuthorsLastName(String lastNameStarting) {
        return this.bookRepository.findByAuthorLastNameStartingWith(lastNameStarting)
                .stream().map(b -> b.getTitle() + " (" + b.getAuthor().getFirstName() +
                        " " + b.getAuthor().getLastName() + ")")
                .collect(Collectors.toList());
    }

    @Override
    public int findBooksWithTitleLengthMoreThan(int neededLength) {
        return this.bookRepository.findByNameLongerThan(neededLength);
    }

    @Override
    public String searchBookByTitle(String title) {
        BookSummary summary = this.bookRepository.findSummaryForTitle(title);
        return String.format("%s %s %s %.2f", summary.getTitle(), summary.getEditionType(), summary.getAgeRestriction(),
                summary.getPrice());
    }

    private Book createBookFromInfo(String[] bookInfo) {
        EditionType editionType = EditionType.values()[Integer.parseInt(bookInfo[0])];
        LocalDate releaseDate = LocalDate
                .parse(bookInfo[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        Integer copies = Integer.parseInt(bookInfo[2]);
        BigDecimal price = new BigDecimal(bookInfo[3]);
        AgeRestriction ageRestriction = AgeRestriction
                .values()[Integer.parseInt(bookInfo[4])];
        String title = Arrays.stream(bookInfo)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService
                .getRandomCategories();

        return new Book(editionType, releaseDate, copies, price, ageRestriction, title, author, categories);

    }
}
