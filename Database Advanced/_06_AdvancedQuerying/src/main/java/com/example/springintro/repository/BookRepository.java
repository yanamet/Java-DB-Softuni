package com.example.springintro.repository;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.BookSummary;
import com.example.springintro.model.entity.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDateAfter);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String author_firstName, String author_lastName);

    List<Book> findByAgeRestriction(AgeRestriction restriction);

    List<Book> findByEditionTypeAndCopiesLessThan(EditionType editionType, int i);

    List<Book> findByPriceLessThanOrPriceGreaterThan(BigDecimal min, BigDecimal max);


    List<Book> findByReleaseDateBeforeOrReleaseDateAfter(LocalDate before, LocalDate after);

    List<Book> findByTitleContaining(String substring);

    List<Book> findByAuthorLastNameStartingWith(String lastNameStarting);

    @Query("select count(b.id) FROM Book b where LENGTH(b.title) > :neededLength")
    int findByNameLongerThan(int neededLength);

    @Query("SELECT b.title AS title, b.editionType AS editionType, b.ageRestriction AS ageRestriction, b.price AS price FROM Book b " +
            " where b.title = :title")
    BookSummary findSummaryForTitle(String title);
}
