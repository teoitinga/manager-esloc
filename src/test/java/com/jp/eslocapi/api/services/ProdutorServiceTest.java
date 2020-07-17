package com.jp.eslocapi.api.services;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jp.eslocapi.api.entities.Produtor;
import com.jp.eslocapi.api.repositories.ProdutorRepository;
import com.jp.eslocapi.services.ProdutorService;
import com.jp.eslocapi.services.ProdutorServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
public class ProdutorServiceTest {

    ProdutorService produtorService;
    
	@MockBean
	ProdutorRepository repository;

    @BeforeEach
    public void setUp(){
        this.produtorService = new ProdutorServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar o registro de novo produtor")
    public void saveBookTest(){
        //cenario
        Produtor produtor = Produtor.builder()
				.nome("João Paulo")
				.cpf("04459471604")
				.fone("33999065029")
                .build();
        //execução
        Mockito.when(repository.save(produtor)).thenReturn(produtor.builder()
                .id(10L)
				.nome("João Paulo")
				.cpf("04459471604")
				.fone("33999065029")
                .build());

        Produtor savedProdutor = produtorService.save(produtor);

        //verificação
        assertThat(savedProdutor.getId()).isNotNull();
        assertThat(savedProdutor.getNome()).isEqualTo("João Paulo");
        assertThat(savedProdutor.getCpf()).isEqualTo("04459471604");
        assertThat(savedProdutor.getFone()).isEqualTo("33999065029");

    }
}
