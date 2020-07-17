package com.jp.eslocapi.api.resources;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jp.eslocapi.api.dto.ProdutorDto;
import com.jp.eslocapi.api.entities.Produtor;
import com.jp.eslocapi.services.ProdutorService;

@RestController
@RequestMapping("/api/v1/produtores")
public class ProdutorController {
	
	private ProdutorService service;
	
	public ProdutorController(ProdutorService service) {
		this.service = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutorDto create(@RequestBody ProdutorDto dto) {
		
		Produtor toSaved = Produtor.builder()
				.nome(dto.getNome())
				.cpf(dto.getCpf())
				.fone(dto.getFone())
				.build();
		toSaved = service.save(toSaved);
		
		ProdutorDto response = ProdutorDto.builder()
				.id(toSaved.getId())
				.nome(toSaved.getNome())
				.cpf(toSaved.getCpf())
				.fone(toSaved.getFone())
				.build();
		return response;
	}
}
