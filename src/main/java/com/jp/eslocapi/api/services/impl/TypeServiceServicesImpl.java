package com.jp.eslocapi.api.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.ServiceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jp.eslocapi.api.dto.ServicoDto;
import com.jp.eslocapi.api.entities.TipoServico;
import com.jp.eslocapi.api.exceptions.ServiceNotFound;
import com.jp.eslocapi.api.repositories.TypeServiceRepository;
import com.jp.eslocapi.api.services.TypeServiceService;

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

	@Override
	public List<ServicoDto> findAll() {
		List<TipoServico> servicos = Optional.of(this.repository.findAll()).orElseThrow(()->new ServiceNotFound()); 
		return toListServicoDto(servicos);
	}
	//converte uma lista de TipoServico para Lista de ServicoDto
	private List<ServicoDto> toListServicoDto(List<TipoServico> servicos) {
		return servicos.stream().map(servico->toServicoDto(servico))
						.collect(Collectors.toList());
	}

	//converte um TipoServico para ServicoDto
	private ServicoDto toServicoDto(TipoServico servico) {
		return ServicoDto.builder()
				.descricao(servico.getDescricaoTipo())
				.legenda(servico.getTipo())
				.valorReferencia(String.valueOf(servico.getValorReferencia()))
				.tempoNecessario(String.valueOf(servico.getTempoEstimado()))
				.build();
	}
	public boolean isContaining() {
		return this.repository.findAll().size() > 0 ? true : false;
	}

	@Override
	public void deleteAll() {
		this.repository.deleteAll();
	}

}
