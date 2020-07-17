package com.jp.eslocapi.services;

import org.springframework.stereotype.Service;

import com.jp.eslocapi.api.entities.Produtor;
import com.jp.eslocapi.api.repositories.ProdutorRepository;

@Service
public class ProdutorServiceImpl implements ProdutorService{

	ProdutorRepository produtorRepository;
	
	public ProdutorServiceImpl(ProdutorRepository produtorRepository) {
		this.produtorRepository = produtorRepository;
	}

	@Override
	public Produtor save(Produtor produtor) {
		return produtorRepository.save(produtor);
	}

}
