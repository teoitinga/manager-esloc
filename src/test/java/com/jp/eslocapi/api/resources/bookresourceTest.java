package com.jp.eslocapi.api.resources;

import com.jp.eslocapi.repositories.BookRepository;
import com.jp.eslocapi.services.impl.BookServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jp.eslocapi.api.entities.Book;
import com.jp.eslocapi.dto.BookDto;
import com.jp.eslocapi.services.BookService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class bookresourceTest {
	
	static String BOOK_API = "/api/books";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	BookService service;

	@MockBean
	BookRepository repository;

	@BeforeEach
	public void setUp(){
		this.service = new BookServiceImpl(repository);
	}

	@Test
	@DisplayName("Deve criar um novo livro")
	public void createBookTest() throws Exception {
		BookDto dto = BookDto.builder().
				author("João Paulo")
				.title("Meu livro")
				.isbn("1213221")
				.build();

		Book savedBook = Book.builder()
				.id(10L)
				.author("João Paulo")
				.title("Meu livro")
				.isbn("1213221")
				.build();
		
		BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);

		String json = new ObjectMapper().writeValueAsString(dto);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.post(this.BOOK_API)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(json);

		mvc
			.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").isNotEmpty())
			.andExpect(jsonPath("title").value(dto.getTitle()))
			.andExpect(jsonPath("author").value(dto.getAuthor()))
			.andExpect(jsonPath("isbn").value(dto.getIsbn()))
			;

	}
	@Test
	@DisplayName("Deve lançar erro de validação  quando não houver dados suficientes para criação de um novo livro")
	public void createInvalidBookTest() throws Exception{

        String json = new ObjectMapper().writeValueAsString(new BookDto());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(this.BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                //.andExpect( jsonPath("errors", Matchers.hasSize(3)))
        ;
	}
}
