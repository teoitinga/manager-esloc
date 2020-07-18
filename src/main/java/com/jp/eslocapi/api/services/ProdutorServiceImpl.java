package com.jp.eslocapi.api.services;

import org.springframework.stereotype.Service;

import com.jp.eslocapi.api.entities.Produtor;
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

}
