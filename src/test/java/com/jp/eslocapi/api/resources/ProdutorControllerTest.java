package com.jp.eslocapi.api.resources;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

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
import com.jp.eslocapi.api.dto.ProdutorDto;
import com.jp.eslocapi.api.entities.Produtor;
import com.jp.eslocapi.api.exceptions.ProdutorNotFound;
import com.jp.eslocapi.exceptions.BusinessException;
import com.jp.eslocapi.services.ProdutorService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class ProdutorControllerTest {
	
	static String PRODUTOR_API = "/api/v1/produtores";
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	ProdutorService service;

	@Test
	@DisplayName("Deve criar um registro de novo produtor com sucesso.")
	public void createTest() throws Exception {
		
		ProdutorDto dto = createNewProdutor()
				;
		
		Produtor savedProdutor = Produtor.builder()
				.id(101L)
				.nome("João Paulo")
				.cpf("04459471604")
				.fone("33999065029")
				.build()
				;
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		BDDMockito.given(service.save(Mockito.any(Produtor.class))).willReturn(savedProdutor);
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.post(PRODUTOR_API)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON).content(json);

		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("nome").value("João Paulo"))
		.andExpect(MockMvcResultMatchers.jsonPath("cpf").value("04459471604"))
		.andExpect(MockMvcResultMatchers.jsonPath("fone").value("33999065029"))
		;
	}
	private ProdutorDto createNewProdutor() {
		return ProdutorDto.builder()
				.nome("João Paulo")
				.cpf("04459471604")
				.fone("33999065029")
				.build();
	}
	@Test
	@DisplayName("Deve lançar erro de validação quando houve erros ao criar um registro de novo produtor.")
	public void createInvalidTest() throws Exception {
		
		ProdutorDto dto;

		dto = new ProdutorDto();
		
		String json = new ObjectMapper().writeValueAsString(dto);
				
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.post(PRODUTOR_API)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON).content(json);

		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("errors", org.hamcrest.Matchers.hasSize(3)))
		;
	}
	@Test
	@DisplayName("Deve lançar erro ao tentar cadastrar produtor com cpf existente.")
	public void createProdutorWithDuplicatedCpf()  throws Exception {
		ProdutorDto dto;

		dto = createNewProdutor();
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		String errorMessage = "Este cpf já existe";
		BDDMockito.given(service.save(Mockito.any(Produtor.class)))
					.willThrow(new BusinessException(errorMessage));		
	
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.post(PRODUTOR_API)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON).content(json);

		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("errors", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(errorMessage))
		;

	}
	@Test
	@DisplayName("Deve obter informações do produtor.")
	public void getProdutorDetailTest() throws Exception {
		//cenário (given)
		Long id = 10L;
		Produtor dto = Produtor.builder()
				.id(id)
				.cpf(createNewProdutor().getCpf())
				.nome(createNewProdutor().getNome())
				.fone(createNewProdutor().getFone())
				.build();
		
		BDDMockito.given(service.getById(id)).willReturn(dto);
		
		//execução (when)
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.get(PRODUTOR_API + "/" + id)
		.accept(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("nome").value("João Paulo"))
		.andExpect(MockMvcResultMatchers.jsonPath("cpf").value("04459471604"))
		.andExpect(MockMvcResultMatchers.jsonPath("fone").value("33999065029"))
		;		
	}
	@Test
	@DisplayName("Deve retornar produtor não registrado quando o não houver cadastro do mesmo.")
	public void produtorNotFound() throws Exception {
		//cenário (given)
		
		BDDMockito.given(service.getById(Mockito.anyLong())).willThrow(new ProdutorNotFound());
		
		String id = "1";
		String errorMessage = "Produtor não registrado no banco de dados.";
		
		//execução (when)
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.get(PRODUTOR_API + "/" + id )
		.accept(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(errorMessage))
		;		
	}

}
