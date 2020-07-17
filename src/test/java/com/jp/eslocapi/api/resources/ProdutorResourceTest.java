package com.jp.eslocapi.api.resources;

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
import com.jp.eslocapi.api.entities.Produtor;
import com.jp.eslocapi.dto.ProdutorDto;
import com.jp.eslocapi.services.ProdutorService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class ProdutorResourceTest {
	
	static String PRODUTOR_API = "/api/produtor";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	ProdutorService service;	
	
	@Test
	@DisplayName("Deve inserir um registro de novo produtor")
	public void createBookTest() throws Exception {
		
		ProdutorDto dto = ProdutorDto.builder()
				.nome("João Paulo")
				.cpf("04459471604")
				.fone("33999065029")
				.build();
		
		Produtor savedProdutor = Produtor.builder()
				.id(10L)
				.nome("João Paulo")
				.cpf("04459471604")
				.fone("33999065029")
				.build();
		
		BDDMockito.given(service.save(Mockito.any(Produtor.class))).willReturn(savedProdutor);

		String json = new ObjectMapper().writeValueAsString(dto);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.post(PRODUTOR_API)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON)
		.content(json);
		
		mvc
			.perform(request)
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
			.andExpect(MockMvcResultMatchers.jsonPath("nome").value(dto.getNome()))
			.andExpect(MockMvcResultMatchers.jsonPath("cpf").value(dto.getCpf()))
			.andExpect(MockMvcResultMatchers.jsonPath("fone").value(dto.getFone()))
			;
			
	}
	@Test
	@DisplayName("Deve lançar erro de validação  quando não houver dados suficientes para criação de um novo livro")
	public void createInvalidBookTest() {
		
	}
}
