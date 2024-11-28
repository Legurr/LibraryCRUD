package com.library.api.services;

import com.library.api.dao.BookDao;
import com.library.api.models.Book;
import com.library.api.models.Person;
import com.library.api.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookDao bookDao;
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository, BookDao bookDao) {
        this.bookRepository = bookRepository;
        this.bookDao = bookDao;
    }

    public List<Book> findAll(boolean sort) {
        if (sort) {
            return bookRepository.findAll(Sort.by("year"));
        } else
            return bookRepository.findAll();
    }

    public List<Book> findWithPagination(Integer page, Integer booksOnPage, boolean sort) {
        if (sort) {
            return bookRepository.findAll(PageRequest.of(page,booksOnPage,Sort.by("year"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page,booksOnPage)).getContent();
        }
    }

    public Book findById(int id) {
        Optional<Book> book = bookRepository.findById(id);

        return book.orElse(null);
    }

    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitleStartingWith(title);
    }

    public Person getBookOwner(int id)
    {
        return bookRepository.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToUpdate = bookRepository.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToUpdate.getOwner());

        bookRepository.save(updatedBook);
    }


    @Transactional
    public void releaseTheBook(int bookId) {
        bookRepository.findById(bookId).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakenAt(null);
                    bookRepository.save(book);
                });
    }

    @Transactional
    public void assignTheBook(int Id, Person Person ) {
        bookRepository.findById(Id).ifPresent(
                book -> {
                    book.setOwner(Person);
                    book.setTakenAt(new Date());
                }
        );
    }

}
