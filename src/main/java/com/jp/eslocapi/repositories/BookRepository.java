package com.jp.eslocapi.repositories;

import com.jp.eslocapi.api.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
