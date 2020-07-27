package com.jp.eslocapi.api.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jp.eslocapi.api.entities.Atendimento;
import com.jp.eslocapi.api.repositories.AtendimentosRepository;
import com.jp.eslocapi.services.AtendimentoService;

@Service
public class AtendimentoServiceImpl implements AtendimentoService{

	@Autowired
	private AtendimentosRepository repository;
	
	@Override
	public Atendimento save(Atendimento atendimento) {
		return this.repository.save(atendimento);
	}

}
