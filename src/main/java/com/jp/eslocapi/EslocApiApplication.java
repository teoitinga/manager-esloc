package com.jp.eslocapi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.jp.eslocapi.api.entities.DocumentType;
import com.jp.eslocapi.api.entities.TipoServico;
import com.jp.eslocapi.services.DocumentTypeService;
import com.jp.eslocapi.services.TypeServiceService;

@SpringBootApplication
public class EslocApiApplication extends SpringBootServletInitializer {
	@Autowired
	private TypeServiceService service;

	@Autowired
	private DocumentTypeService documentTypeService;
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(EslocApiApplication.class, args);
	}

	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {



		@Override
		public void run(String... args) throws Exception {
			createServicesIfNotExists();
			createDocumentTypeIfNotExists();

		}

		private void createServicesIfNotExists() {
			List<TipoServico> services = new ArrayList<>();

			TipoServico srv;

			srv = TipoServico.builder().tempoEstimado(7).descricaoTipo("Elaboração de projeto de crédito rural")
					.tipo("CR")
					.valorReferencia(new BigDecimal("150"))
					.build();
			services.add(srv);
			
			srv = TipoServico.builder().tempoEstimado(2).descricaoTipo("Emissão de Declaração de aptidão ao pronaf")
					.tipo("DAP")
					.valorReferencia(new BigDecimal("0"))			
					.build();
			services.add(srv);

			srv = TipoServico.builder().tempoEstimado(2).descricaoTipo("Elaboração de limite de crédito para Banco do Brasil")
					.tipo("LM")
					.valorReferencia(new BigDecimal("150"))			
					.build();
			services.add(srv);

			srv = TipoServico.builder().tempoEstimado(2).descricaoTipo("Elaboração de limite de crédito para Sicoob")
					.tipo("CAD")
					.valorReferencia(new BigDecimal("150"))			
					.build();
			services.add(srv);

			srv = TipoServico.builder().tempoEstimado(2).descricaoTipo("Cadastro ambiental Rural - Emissão")
					.tipo("CAR")
					.valorReferencia(new BigDecimal("200"))			
					.build();
			services.add(srv);

			srv = TipoServico.builder().tempoEstimado(2).descricaoTipo("Gleba - elaboração de Geo referenciamento")
					.tipo("GLB")
					.valorReferencia(new BigDecimal("50"))			
					.build();
			services.add(srv);
			
			services.forEach(servicos-> service.save(servicos));
		}
		private void createDocumentTypeIfNotExists() {
			List<DocumentType> documentType = new ArrayList<>();
			
			DocumentType dt;
			
			dt = DocumentType.builder().abreviatura("CRT").descricao("Certidão da propriedade")
					.informacoes("Refere-se a emissão de certidão com número de matrícula.")
					.build();
			documentType.add(dt);
			
			dt = DocumentType.builder().abreviatura("REL").descricao("Relatório de Assistência técnica")
					.informacoes("Refere-se a emissão de Relatório de Assistência técnica.")
					.build();
			documentType.add(dt);
			
			dt = DocumentType.builder().abreviatura("RG").descricao("Identidade")
					.informacoes("Refere-se a emissão de Carteira de Identidade válida ou Habilitação.")
					.build();
			documentType.add(dt);
			
			dt = DocumentType.builder().abreviatura("CPF_TIT").descricao("CPF do titular")
					.informacoes("Refere-se a emissão de CPF do titular.")
					.build();
			documentType.add(dt);
			
			dt = DocumentType.builder().abreviatura("CPF_CNJ").descricao("CPF do conjugue")
					.informacoes("Refere-se a emissão de CPF do conjugue.")
					.build();
			documentType.add(dt);
			
			dt = DocumentType.builder().abreviatura("CAR").descricao("Cadastro Ambiental Rural")
					.informacoes("Refere-se a emissão de CAR da área informada.")
					.build();
			documentType.add(dt);
			
			dt = DocumentType.builder().abreviatura("DAP").descricao("Declaração de aptidão ao PRONAF")
					.informacoes("Refere-se a emissão de DAP para o produtor. Só enviar se estiver todos devidamente assinados.")
					.build();
			documentType.add(dt);
			
			dt = DocumentType.builder().abreviatura("FSN").descricao("Ficha sanitária do rebanho")
					.informacoes("Refere-se a emissão de ficha sanitária emitida pelo IMA.")
					.build();
			documentType.add(dt);
			
			dt = DocumentType.builder().abreviatura("CCIR").descricao("Certidão de Inscrição de Imóvel Rural no INCRA")
					.informacoes("Refere-se a emissão de Certidão de Inscrição de Imóvel Rural no INCRA.")
					.build();
			documentType.add(dt);
			
			documentType.forEach(documento-> documentTypeService.save(documento));
		}

	}
}
