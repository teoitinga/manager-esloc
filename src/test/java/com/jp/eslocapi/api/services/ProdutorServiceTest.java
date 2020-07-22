package com.jp.eslocapi.api.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jp.eslocapi.api.entities.Persona;
import com.jp.eslocapi.api.repositories.ProdutorRepository;
import com.jp.eslocapi.exceptions.BusinessException;
import com.jp.eslocapi.services.ProdutorService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ProdutorServiceTest {
	ProdutorService service;
	
	@MockBean
	private ProdutorRepository repository;
	
	@BeforeEach
	public void setUp() {
		this.service = new ProdutorServiceImpl(repository);
	}
	@Test
	@DisplayName("Deve salvar o registro de produtor")
	public void saveProdutor() {
		//cenário
		Persona produtor = createValidProdutor();
		
		Persona responseProdutor = Persona.builder()
				.id(10L)
				.nome("João Paulo")
				.cpf("04459471604")
				.fone("33999065029")
				.build();
		
		Mockito.when(repository.existsByCpf(Mockito.anyString())).thenReturn(false);		
		Mockito.when(repository.save(produtor)).thenReturn(responseProdutor);
		
		//execução
		Persona savedProdutor = service.save(produtor);
		
		//verificação
		assertThat(savedProdutor.getId()).isNotNull();
		assertThat(savedProdutor.getNome()).isEqualTo("João Paulo");
		assertThat(savedProdutor.getCpf()).isEqualTo("04459471604");
		assertThat(savedProdutor.getFone()).isEqualTo("33999065029");
	}
	private Persona createValidProdutor() {
		return Persona.builder()
				.nome("João Paulo")
				.cpf("04459471604")
				.fone("33999065029")
				.build();
	}
	@Test
	@DisplayName("Deve lançar erro de negocio ao tentar salvar um registro com cpf duplicado")
	public void shouldNotSaveProdutorWithDuplicatedCpf() {
		//cenário
		Persona produtor = createValidProdutor();
		
		String errorMessage = "Este cpf já existe";
		
		Mockito.when(repository.existsByCpf(Mockito.anyString())).thenReturn(true);
		
		//execução
		Throwable exception = Assertions.catchThrowable(()-> service.save(produtor));
		
		//verificações
		assertThat(exception).isInstanceOf(BusinessException.class)
					.hasMessage(errorMessage);
		Mockito.verify(repository, Mockito.never()).save(produtor);
		
	}
}
