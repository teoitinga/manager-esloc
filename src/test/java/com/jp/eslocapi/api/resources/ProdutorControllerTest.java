package com.jp.eslocapi.api.resources;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
import com.jp.eslocapi.api.entities.Persona;
import com.jp.eslocapi.api.exceptions.ProdutorNotFound;
import com.jp.eslocapi.api.services.ProdutorService;
import com.jp.eslocapi.exceptions.BusinessException;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class ProdutorControllerTest {
	
	static String PRODUTOR_API = "/api/v1/produtores";
	static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	ProdutorService service;

	@Autowired
	private ProdutorService repository;
	@Test
	@DisplayName("Deve atualizar um registro de produtor")
	public void updateProdutorTest() throws Exception {
		//cenário (given)
		Long idProdutor = 1L;
		
		ProdutorDto dto;
		dto = createNewProdutorDto();
		
		ProdutorDto response  = createNewProdutorDto();
		response.setId(idProdutor);
		
		Persona responseProdutor  = createNewProdutor();
		response.setId(idProdutor);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		BDDMockito.given(service.getById(idProdutor)).willReturn(responseProdutor);
		
		BDDMockito.given(service.update(dto)).willReturn(response);
		
		//execução (when)
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(String.format("%s%s%x",PRODUTOR_API,"/",idProdutor))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);

		//verificação
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("nome").value("João Paulo"))
		.andExpect(MockMvcResultMatchers.jsonPath("cpf").value("04459471604"))
		.andExpect(MockMvcResultMatchers.jsonPath("fone").value("33999065029"))
		;		
	}
	@Test
	@DisplayName("Deve retornar erro ao atualizar um registro de produtor não registrado")
	public void updateNotFoundProdutorTest() throws Exception {
		//cenário (given)
		Long idProdutor = 1L;
		
		ProdutorDto dto;
		dto = createNewProdutorDto();
		
		ProdutorDto response  = createNewProdutorDto();
		response.setId(idProdutor);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		BDDMockito.given(service.getById(Mockito.anyLong())).willThrow(new ProdutorNotFound());
		
		BDDMockito.given(service.update(dto)).willReturn(response);
		
		//execução (when)
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(String.format("%s%s%x",PRODUTOR_API,"/",idProdutor))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);
		
		//verificação
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		;		
	}
	@Test
	@DisplayName("Deve criar um registro de novo produtor com sucesso.")
	public void createTest() throws Exception {
		//cenário (given)

		Long idProdutor = 12L;
		
		ProdutorDto dto;
		dto = createNewProdutorDto();
		
		Persona response  = createNewProdutor();
		response.setId(idProdutor);
		
		ProdutorDto responseProdutor  = createNewProdutorDto();
		responseProdutor.setId(idProdutor);
		
		String json = new ObjectMapper().writeValueAsString(dto);
		
		BDDMockito.given(service.save(Mockito.any(Persona.class))).willReturn(response);
		BDDMockito.given(service.toProdutor(Mockito.any(ProdutorDto.class))).willReturn(response);
		BDDMockito.given(service.toProdutorDto(Mockito.any(Persona.class))).willReturn(responseProdutor);
		
		//execução (when)		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.post(PRODUTOR_API)
		.accept(MediaType.APPLICATION_JSON)
		.contentType(MediaType.APPLICATION_JSON)
		.content(json);
		
		//verificação
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("nome").value("João Paulo"))
		.andExpect(MockMvcResultMatchers.jsonPath("cpf").value("04459471604"))
		.andExpect(MockMvcResultMatchers.jsonPath("fone").value("33999065029"))
		;
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
		.accept(MediaType.APPLICATION_JSON)
		.content(json);

		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.jsonPath("errors", org.hamcrest.Matchers.hasSize(4)))
		;
	}
	@Test
	@DisplayName("Deve lançar erro ao tentar cadastrar produtor com cpf existente.")
	public void createProdutorWithDuplicatedCpf()  throws Exception {
		//cenário (given)

		Long idProdutor = 12L;
		
		ProdutorDto dto;
		dto = createNewProdutorDto();
		
		Persona response  = createNewProdutor();
		response.setId(idProdutor);
		
		ProdutorDto responseProdutor  = createNewProdutorDto();
		responseProdutor.setId(idProdutor);
		
		String json = new ObjectMapper().writeValueAsString(dto);

		
		String errorMessage = "Este cpf já existe";
		
		BDDMockito.given(service.toProdutor(Mockito.any(ProdutorDto.class))).willReturn(response);
		BDDMockito.given(service.toProdutorDto(Mockito.any(Persona.class))).willReturn(responseProdutor);
		BDDMockito.given(service.save(Mockito.any(Persona.class)))
					.willThrow(new BusinessException(errorMessage));	
		
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.post(PRODUTOR_API)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON).content(json);

		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("errors", org.hamcrest.Matchers.hasSize(1)))
		.andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(errorMessage))
		;

	}
	@Test
	@DisplayName("Deve obter informações do produtor.")
	public void getProdutorDetailTest() throws Exception {
		//cenário (given)
		Long id = 1L;

		ProdutorDto dto = this.createNewProdutorDto();
		dto.setId(id);
		Persona produtor = this.createNewProdutor();
		dto.setId(id);
		
		BDDMockito.given(service.getById(id)).willReturn(produtor);
		
		BDDMockito.given(service.toProdutorDto(Mockito.any(Persona.class))).willReturn(dto);
		
		//execução (when)
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.get((String.format("%s%s%x",PRODUTOR_API,"/",id)))
		.accept(MediaType.APPLICATION_JSON);
		
		//verificação
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
		.andExpect(MockMvcResultMatchers.jsonPath("nome").value("João Paulo"))
		.andExpect(MockMvcResultMatchers.jsonPath("cpf").value("04459471604"))
		.andExpect(MockMvcResultMatchers.jsonPath("fone").value("33999065029"))
		.andExpect(MockMvcResultMatchers.jsonPath("dataNascimento").value("04/01/1979"))
		.andReturn().getResponse().getContentAsString()
		;
		
	}
	@Test
	@DisplayName("Deve retornar produtor não registrado quando o não houver cadastro do mesmo.")
	public void produtorNotFound() throws Exception {
		//cenário (given)
		
		BDDMockito.given(service.getById(Mockito.anyLong())).willThrow(new ProdutorNotFound());
		
		Long id = 1L;
		String errorMessage = "Produtor não registrado no banco de dados.";
		
		//execução (when)
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
		.get((String.format("%s%s%x",PRODUTOR_API,"/",id)))
		.accept(MediaType.APPLICATION_JSON);

		//verificação
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		.andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value(errorMessage))
		;		
	}
	@Test
	@DisplayName("Deve deletar um registro de produtor")
	public void deleteProdutorTest() throws Exception {
		//cenário (given)
		Long idProdutor = 1L;
		
		Persona produtor = Persona.builder()
				.id(Long.valueOf(idProdutor))
				.build();
		BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(produtor);
		
		//execução (when)
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(String.format("%s%s%x",PRODUTOR_API,"/",idProdutor))
				.accept(MediaType.APPLICATION_JSON);

		//verificação
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isNoContent())
		;		
	}

	@Test
	@DisplayName("Deve retornar produtor não registrado ao não encontrar o Produtor para deletar")
	public void deleteNotFoundProdutorTest() throws Exception {
		//cenário (given)
		Long idProdutor = 1L;

		BDDMockito.given(service.getById(Mockito.anyLong())).willThrow(new ProdutorNotFound());
		
		//execução (when)
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(String.format("%s%s%x",PRODUTOR_API,"/",idProdutor))
				.accept(MediaType.APPLICATION_JSON);
		
		mvc.perform(request)
		.andExpect(MockMvcResultMatchers.status().isNotFound())
		;		
	}
	private ProdutorDto createNewProdutorDto() {

		return ProdutorDto.builder()
				.nome("João Paulo")
				.cpf("04459471604")
				.fone("33999065029")
				.dataNascimento(LocalDate.parse("04/01/1979", formatter).format(formatter))
				.build();
	}
	private Persona createNewProdutor() {
		
		return Persona.builder()
				.nome("João Paulo")
				.cpf("04459471604")
				.fone("33999065029")
				.dataNascimento(LocalDate.parse("04/01/1979", formatter))
				.build();
	}
}
