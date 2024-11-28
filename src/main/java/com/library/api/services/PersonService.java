package com.library.api.services;

import com.library.api.dao.BookDao;
import com.library.api.models.Book;
import com.library.api.models.Person;
import com.library.api.repositories.BookRepository;
import com.library.api.repositories.PersonRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final BookDao bookDao;

    @Autowired
    public PersonService(PersonRepository personRepository, BookRepository bookRepository, BookDao bookDao) {
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
        this.bookDao = bookDao;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findById(int id) {
        Optional<Person> person = personRepository.findById(id);

        return person.orElse(null);
    }

    @Transactional
    public Person save(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        personRepository.save(person);
    }

    public Optional<Person> getPersonByName(String name) {
        return personRepository.findByName(name);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = personRepository.findById(id);

        if(person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());

            //checking for overdue
            person.get().getBooks().forEach(book -> {
                if (book.getTakenAt() != null) {
                    long diffInMiles = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                    if (diffInMiles > 864000000) {
                        book.setExpired(true);
                    } else {
                        book.setExpired(false);
                    }
                }
            });

            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }
}
