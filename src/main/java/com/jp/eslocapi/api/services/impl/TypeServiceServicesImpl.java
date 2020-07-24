package com.jp.eslocapi.api.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jp.eslocapi.api.entities.TipoServico;
import com.jp.eslocapi.api.repositories.TypeServiceRepository;
import com.jp.eslocapi.services.TypeServiceService;

@Service
public class TypeServiceServicesImpl implements TypeServiceService{
	
	@Autowired
	TypeServiceRepository repository;
	
	@Override
	public TipoServico save(TipoServico servicos) {
		return this.repository.save(servicos);
	}

	@Override
	public Optional<TipoServico> getByType(String tipoServico) {
		return this.repository.findByTipo(tipoServico);
	}

}
