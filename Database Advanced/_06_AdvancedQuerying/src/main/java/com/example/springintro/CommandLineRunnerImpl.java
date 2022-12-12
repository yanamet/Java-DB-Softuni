package com.example.springintro;

import com.example.springintro.model.entity.Book;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
//        seedData();

        //printAllBooksAfterYear(2000);
//        printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
     //   printAllAuthorsAndNumberOfTheirBooks();
//        pritnALlBooksByAuthorNameOrderByReleaseDate("George", "Powell");

//        Scanner scanner = new Scanner(System.in);
//        String restriction = scanner.nextLine();

       // _01_printBooksWithAgeRestriction(restriction);
        //_02_printBooksWithGoldenEdition();
       // _03_printBooksWithPriceCondition();
       // _04_printNotReleasedBooksInYear();
        // _05_printBooksReleasedBefore();
       // _06_printAuthorsEndingWith();
       // _07_printBooksContaining();
        //_08_printBooksWithAuthorsNameStartingWith();
        //_09_printCountBooksWithTitleLengthMoreThan();
        //_10_printTotalCopiesCountPerAuthor();
        _11_printBookInfo();



    }

    private void _11_printBookInfo(){
        Scanner scanner = new Scanner(System.in);
        String title = scanner.nextLine();
        System.out.println(this.bookService.searchBookByTitle(title));
    }

    private void _10_printTotalCopiesCountPerAuthor(){
        this.authorService.findAuthorCopiesCount()
                .forEach(System.out::println);
    }

    private void _09_printCountBooksWithTitleLengthMoreThan(){
        Scanner scanner = new Scanner(System.in);
        int neededLength = Integer.parseInt(scanner.nextLine());
        int booksFound = this.bookService.findBooksWithTitleLengthMoreThan(neededLength);
        System.out.printf("There are %d books with longer title than %d symbols%n", booksFound, neededLength);

    }

    private void _08_printBooksWithAuthorsNameStartingWith(){
        Scanner scanner = new Scanner(System.in);
        String lastNameStarting = scanner.nextLine();
        this.bookService.findBooksWithAuthorsLastName(lastNameStarting)
                .forEach(System.out::println);
    }

    private void _07_printBooksContaining(){
        Scanner scanner = new Scanner(System.in);
        String substring = scanner.nextLine();
        this.bookService.findBooksContaining(substring)
                .forEach(System.out::println);
    }

    private void _06_printAuthorsEndingWith(){
        Scanner scanner = new Scanner(System.in);
        String ending = scanner.nextLine();
        this.authorService.findAutorsWithNamesEndingWith(ending)
                .forEach(System.out::println);
    }

    private void _05_printBooksReleasedBefore(){
        Scanner scanner = new Scanner(System.in);
        String date = scanner.nextLine();
        this.bookService.findBooksReleasedAfter(date)
                .forEach(System.out::println);
    }

    private void _04_printNotReleasedBooksInYear(){
        Scanner scanner = new Scanner(System.in);
        int year = Integer.parseInt(scanner.nextLine());
        this.bookService.findBooksNotReleasedInYear(year)
                .forEach(System.out::println);
    }

    private void _03_printBooksWithPriceCondition(){
        this.bookService.findBooksByPriceGreaterThan5AndLowerThan40()
                .forEach(System.out::println);
    }

    private void _02_printBooksWithGoldenEdition(){
        this.bookService.searchGoldenEditionBooks()
                .forEach(System.out::println);
    }

    private void _01_printBooksWithAgeRestriction(String restriction){
      this.bookService.searchBooksWithAgeRestrriction(restriction)
              .forEach(System.out::println);
    }

    private void pritnALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
