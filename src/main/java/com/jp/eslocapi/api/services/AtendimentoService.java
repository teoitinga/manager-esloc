package com.jp.eslocapi.api.services;

import java.util.List;

import com.jp.eslocapi.api.dto.AtendimentoBasicDto;
import com.jp.eslocapi.api.entities.Atendimento;

public interface AtendimentoService {

	Atendimento save(Atendimento atendimento);

	List<AtendimentoBasicDto> findAll();

}
