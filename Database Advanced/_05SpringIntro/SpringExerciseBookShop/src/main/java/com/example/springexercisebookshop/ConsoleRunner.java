package com.example.springexercisebookshop;

import com.example.springexercisebookshop.Entities.Author;
import com.example.springexercisebookshop.Entities.Book;
import com.example.springexercisebookshop.Services.SeedService;
import com.example.springexercisebookshop.repositories.AuthorRepository;
import com.example.springexercisebookshop.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Component
public class ConsoleRunner implements CommandLineRunner {



    @Autowired
    private SeedService seedService;

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final AuthorRepository authorRepository;

    public ConsoleRunner(BookRepository bookRepository, SeedService seedService, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.seedService = seedService;
        this.authorRepository = authorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
         //this.seedService.seedAuthors();
        //this.seedService.seedCategories();
       // this.seedService.seedBooks();

           // this.booksAfter2000();
          // this.authorsWithAtLeastOneBookBefore1990();
         // this.authorsOrderedByBooksCount();
        // this.findByAuthor();

    }

    private void booksAfter2000() {
        LocalDate year = LocalDate.of(2000, 1, 1);

        List<Book> books = this.bookRepository.findByReleaseDateAfter(year);

        books.forEach(b -> System.out.println(b.getTitle() + " " + b.getReleaseDate()));
    }


    private void authorsWithAtLeastOneBookBefore1990(){
        final LocalDate year1990 = LocalDate.of(1990, 1, 1);
        List<Author> authors = this.authorRepository.findDistinctByBooksReleaseDateBefore(year1990);

        authors.forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));

    }

    private void authorsOrderedByBooksCount(){
        List<Author> allAuthor = this.authorRepository.findAll();

        allAuthor
                .stream()
                .sorted((a1, a2) -> a2.getBooks().size() - a1.getBooks().size())
                .forEach(a -> System.out.println(a.getFirstName() + " " +
                        a.getLastName() + " - > " + a.getBooks().size()));
    }

    private void findByAuthor(){
        List<Book> books = this.bookRepository.findByAuthorFirstNameOrderByReleaseDateDescTitleAsc("George");

        books.forEach(b -> System.out.println(b.getTitle() + " " + b.getReleaseDate()));

    }

}
