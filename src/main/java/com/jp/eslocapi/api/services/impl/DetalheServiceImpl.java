package com.jp.eslocapi.api.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jp.eslocapi.api.entities.DetalheServico;
import com.jp.eslocapi.api.repositories.DetalheRepository;
import com.jp.eslocapi.services.DetalheService;

@Service
public class DetalheServiceImpl implements DetalheService{
	
	@Autowired
	private DetalheRepository repository;

	@Override
	public DetalheServico save(DetalheServico detalheServico) {
		return this.repository.save(detalheServico);
	}

}
