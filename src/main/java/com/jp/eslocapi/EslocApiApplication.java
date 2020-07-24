package com.jp.eslocapi;

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

import com.jp.eslocapi.api.entities.TipoServico;
import com.jp.eslocapi.services.TypeServiceService;

@SpringBootApplication
public class EslocApiApplication extends SpringBootServletInitializer {
	@Autowired
	private TypeServiceService service;
	
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

		}

		private void createServicesIfNotExists() {
			List<TipoServico> services = new ArrayList<>();

			TipoServico srv;

			srv = TipoServico.builder().tempoEstimado(7).DescricaoTipo("Elaboração de projeto de crédito rural")
					.tipo("CR").build();
			services.add(srv);
			
			srv = TipoServico.builder().tempoEstimado(2).DescricaoTipo("Emissão de Declaração de aptidão ao pronaf")
					.tipo("DAP").build();
			
			services.add(srv);
			
			services.forEach(servicos-> service.save(servicos));
		}

	}
}
