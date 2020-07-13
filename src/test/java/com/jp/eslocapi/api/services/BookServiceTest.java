package com.jp.eslocapi.api.services;

import com.jp.eslocapi.api.entities.Book;
import com.jp.eslocapi.repositories.BookRepository;
import com.jp.eslocapi.services.impl.BookServiceImpl;
import com.jp.eslocapi.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService bookService;

    BookRepository repository;

    @BeforeEach
    public void setUp(){
        this.bookService = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar o registro de livro")
    public void saveBookTest(){
        //cenario
        Book book = Book.builder()
                .author("João Paulo")
                .title("Meu livro")
                .isbn("1213221")
                .build();
        //execução
        Mockito.when(repository.save(book)).thenReturn(Book.builder()
                .id(10L)
                .author("João Paulo")
                .title("Meu livro")
                .isbn("1213221")
                .build());

        Book savedBook = bookService.save(book);

        //verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("1213221");
        assertThat(savedBook.getTitle()).isEqualTo("Meu livro");
        assertThat(savedBook.getAuthor()).isEqualTo("João Paulo");

    }
}
