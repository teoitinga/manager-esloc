package com.jp.eslocapi.services.impl;

import com.jp.eslocapi.api.entities.Book;
import com.jp.eslocapi.repositories.BookRepository;
import com.jp.eslocapi.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {


    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }
}
