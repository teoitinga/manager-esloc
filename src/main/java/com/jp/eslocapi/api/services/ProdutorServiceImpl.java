package com.jp.eslocapi.api.services;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.jp.eslocapi.api.dto.ProdutorDto;
import com.jp.eslocapi.api.entities.Produtor;
import com.jp.eslocapi.api.exceptions.ProdutorNotFound;
import com.jp.eslocapi.api.repositories.ProdutorRepository;
import com.jp.eslocapi.exceptions.BusinessException;
import com.jp.eslocapi.services.ProdutorService;

@Service
public class ProdutorServiceImpl implements ProdutorService {

	private ProdutorRepository repository;

	public ProdutorServiceImpl(ProdutorRepository repository) {
		this.repository = repository;
	}

	@Override
	public Produtor save(Produtor produtor) {
		if(this.repository.existsByCpf(produtor.getCpf())) {
			throw new BusinessException("Este cpf já existe");
		}
		return repository.save(produtor);
	}

	@Override
	public Produtor getById(Long id) {
		return repository.findById(id).orElseThrow(()-> new ProdutorNotFound());
	}

	@Override
	public void delete(Produtor toDeleted) {
		repository.delete(toDeleted);
	}

	@Override
	public ProdutorDto update(ProdutorDto dto) {
		return null;
	}


}
