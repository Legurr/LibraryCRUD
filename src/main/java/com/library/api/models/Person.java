package com.library.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "name")
    private String name;

    @Min(value = 1900 , message = "Year should be grater than 1900")
    @Column(name = "year")
    private int year;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {}

    public Person(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotEmpty(message = "Name should not be empty") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Name should not be empty") String name) {
        this.name = name;
    }

    @Min(value = 1900, message = "Year should be grater than 1900")
    public int getYear() {
        return year;
    }

    public void setYear(@Min(value = 1900, message = "Year should be grater than 1900") int year) {
        this.year = year;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }


}
