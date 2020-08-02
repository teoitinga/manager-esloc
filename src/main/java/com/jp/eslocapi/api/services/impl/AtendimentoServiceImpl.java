package com.jp.eslocapi.api.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jp.eslocapi.Configuration;
import com.jp.eslocapi.api.dto.AtendimentoBasicDto;
import com.jp.eslocapi.api.dto.AtendimentosBasicGetDto;
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
	public List<AtendimentosBasicGetDto> findAll(Pageable pageable) {
		Page<Atendimento> atd = this.repository.findAll(pageable);
		return atd.stream().map(atendimento->toAtendimentosBasicGetDto(atendimento)).collect(Collectors.toList());
	}

	private AtendimentoBasicDto toAtendimentoBasicDo(Atendimento atendimento) {
		String dataDoAtendimento = null;
		try {
			dataDoAtendimento = atendimento.getDataAtendimento().format(dateFormat.viewDateTimeFormater());
		}catch (NullPointerException e) {
			dataDoAtendimento = null;
		}
		return AtendimentoBasicDto.builder()
				.descricaoDoServico(atendimento.getTarefaDescricao())
				.CodDoServico(atendimento.getTiposervico().getDescricaoTipo())
				.build();
	}
	private AtendimentosBasicGetDto toAtendimentosBasicGetDto(Atendimento atendimento) {
		String dataDoAtendimento = null;
		try {
			dataDoAtendimento = atendimento.getDataAtendimento().format(dateFormat.viewDateTimeFormater());
		}catch (NullPointerException e) {
			dataDoAtendimento = null;
		}
		return AtendimentosBasicGetDto.builder()
				.id(String.valueOf(atendimento.getId()))
				.dataDoAtendimento(dataDoAtendimento )
				.produtor(atendimento.getProdutor().getNome())
				.descricaoDoServico(atendimento.getTarefaDescricao())
				.observacao(atendimento.getObservacoes())
				.emitiuArt(atendimento.getEmitiuART().toString())
				.emitiuDae(atendimento.getEmitiuDAE().toString())
				.CodDoServico(atendimento.getTiposervico().getDescricaoTipo())
				.build();
	}

}
