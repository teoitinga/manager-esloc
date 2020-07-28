package com.jp.eslocapi.api.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jp.eslocapi.Configuration;
import com.jp.eslocapi.api.dto.AtendimentoBasicDto;
import com.jp.eslocapi.api.entities.Atendimento;
import com.jp.eslocapi.api.repositories.AtendimentosRepository;
import com.jp.eslocapi.api.services.AtendimentoService;

@Service
public class AtendimentoServiceImpl implements AtendimentoService{

	@Autowired
	private AtendimentosRepository repository;
	
	@Autowired
	private Configuration dateFormat;
	
	@Override
	public Atendimento save(Atendimento atendimento) {
		return this.repository.save(atendimento);
	}

	@Override
	public List<AtendimentoBasicDto> findAll() {
		List<Atendimento> atd = this.repository.findAll();
		return atd.stream().map(atendimento->toAtendimentoBasicDo(atendimento)).collect(Collectors.toList());
	}

	private AtendimentoBasicDto toAtendimentoBasicDo(Atendimento atendimento) {
		return AtendimentoBasicDto.builder()
				.cadastro(atendimento.getDataCadastro().format(dateFormat.viewDateTimeFormater()))
				.produtor(atendimento.getProdutor().getNome())
				.descricao(atendimento.getTarefaDescricao())
				.observacao(atendimento.getObservacoes())
				.servico(atendimento.getTiposervico().getDescricaoTipo())
				.build();
	}

}
