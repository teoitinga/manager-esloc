package com.jp.eslocapi.api.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.BeanProperty.Bogus;
import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.entities.EnumStatus;
import com.jp.eslocapi.api.entities.Persona;
import com.jp.eslocapi.api.entities.Tarefa;
import com.jp.eslocapi.api.repositories.TarefaRepository;
import com.jp.eslocapi.services.ProdutorService;
import com.jp.eslocapi.services.TarefaService;

@Service
public class TarefaServiceImpl implements TarefaService{
	@Autowired
	ProdutorService ProdutorService;
	@Autowired
	TarefaRepository TarefaRepository;

	@Override
	public Tarefa save(Tarefa tarefa) {
		return this.TarefaRepository.save(tarefa);
	}

	@Override
	public Tarefa getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tarefa toTarefa(TarefaPostDto dto) {
		
		Persona produtor = this.ProdutorService.getByCpf(dto.getCpfProdutor());
		BigDecimal valorDoServico = new BigDecimal(dto.getValorDoServico());
		Boolean emitiuDae = Boolean.parseBoolean(dto.getEmitiuDAE());
		Boolean emitiuArt = Boolean.parseBoolean(dto.getEmitiuART());
		EnumStatus status = EnumStatus.ABERTO;
		Tarefa tarefa = Tarefa.builder()
				.produtor(produtor)
				.valorDoServico(valorDoServico)
				.emitiuDAE(emitiuDae)
				.emitiuART(emitiuArt)
				.statusTarefa(status)
				.observacoes(dto.getObservacoes())
				.build();
		return tarefa;
	}

}
