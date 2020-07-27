package com.jp.eslocapi.api.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jp.eslocapi.api.dto.DetailServiceDto;
import com.jp.eslocapi.api.dto.DetailsServiceResportDto;
import com.jp.eslocapi.api.dto.ProdutoPostMinDto;
import com.jp.eslocapi.api.dto.TarefaGetDto;
import com.jp.eslocapi.api.dto.TarefaPostDto;
import com.jp.eslocapi.api.entities.DetalheServico;
import com.jp.eslocapi.api.entities.EnumStatus;
import com.jp.eslocapi.api.entities.Persona;
import com.jp.eslocapi.api.entities.Tarefa;
import com.jp.eslocapi.api.entities.TipoServico;
import com.jp.eslocapi.api.exceptions.ProdutorNotFound;
import com.jp.eslocapi.api.exceptions.ServiceIsEmptyException;
import com.jp.eslocapi.api.exceptions.ServiceNotFound;
import com.jp.eslocapi.api.exceptions.TarefaNotFoundException;
import com.jp.eslocapi.api.repositories.TarefaRepository;
import com.jp.eslocapi.services.DetalheService;
import com.jp.eslocapi.services.ProdutorService;
import com.jp.eslocapi.services.TarefaService;
import com.jp.eslocapi.services.TypeServiceService;
import com.jp.eslocapi.util.FileUtil;
import com.jp.eslocapi.util.Gerenciamento;

@Service
public class TarefaServiceImpl implements TarefaService{

	@Autowired
	TarefaRepository TarefaRepository;
	
	@Autowired
	ProdutorService produtorService;

	@Autowired
	TypeServiceService typeService;
	

	@Autowired
	DetalheService detalheService;
	
	@Autowired
	private DateTimeFormatter dateTimeFormater;

	@Autowired
	private FileUtil file;

	@Autowired
	private Gerenciamento gerenciamento;

	
	@Override
	public Tarefa save(Tarefa tarefa) {
		return this.TarefaRepository.save(tarefa);
	}

	@Override
	public Tarefa getById(Long id) {
		return this.TarefaRepository.findById(id).orElseThrow(()-> new TarefaNotFoundException());
	}
	
	@Override
	@Transactional
	public Tarefa managerDto(TarefaPostDto dto) {
		List<Tarefa> tarefas = toListTarefa(dto);
		List<Tarefa> response = new ArrayList<>();
		
		tarefas.forEach(tarefa->response.add(this.TarefaRepository.save(tarefa)));
		//return this.TarefaRepository.save();//toTarefa(dto));
		return response.get(0);
	}
	
//	@Override
//	public Tarefa toTarefa(TarefaPostDto dto) {
//		//busca informações do solicitante
//		Persona produtor = this.ProdutorService.getByCpf(dto.getCpfProdutor());
//
//		//Obtem lista de serviços
//		List<DetalheServico> detalheServico = toListServices(dto.getTipoServico());
//
//		if(detalheServico.isEmpty()) {
//			throw new ServiceIsEmptyException();
//		}
//		//salvando os servicos prestados
//		detalheServico.forEach(atendimento->this.detalheService.save(atendimento));
//		
//		Tarefa tarefa = Tarefa.builder()
//				.produtor(produtor)
//				.detalhes(detalheServico)
//				.build();
//
//		return tarefa;
//	}
	//Le os solicitantes do dto e retorna a lista de tarefas
	@Override
	public List<Tarefa> toListTarefa(TarefaPostDto dto) {
		List<Tarefa> tarefas = new ArrayList<>();
		
		//a mesma tarefa será gerada para cada cliente
		List<ProdutoPostMinDto> clientes = dto.getProdutorInfo();
		
		for(int i = 0; i<clientes.size();i++) {
			/**
			 * Registra Produtor no banco de dados, caso o cpf esteja definido e o nome esteja NULO.
			 * Isso garante a busca pelo CPF
			 */
//			if(clientes.get(i).getNome()==null && clientes.get(i).getCpf()!=null) {
//				Persona toSaved = produtorService.toProdutor(clientes.get(i));
//				toSaved = produtorService.save(toSaved);
//
//			}
			Persona toSaved = null;
			try {
				toSaved = this.produtorService.getByCpf(clientes.get(i).getCpf());
				
			}catch(ProdutorNotFound ex) {
				toSaved = produtorService.toProdutor(clientes.get(i));
				toSaved = produtorService.save(toSaved);
				
			}
			//busca informações do solicitante
			Persona produtor = toSaved;//this.produtorService.getByCpf(clientes.get(i).getCpf());

			//Obtem lista de serviços
			List<DetalheServico> detalheServico = toListServices(dto.getTipoServico());
			
			if(detalheServico.isEmpty()) {
				throw new ServiceIsEmptyException();
			}
			
			//salvando os servicos prestados
			detalheServico.forEach(atendimento->this.detalheService.save(atendimento));
			
			Tarefa tarefa = Tarefa.builder()
					.produtor(produtor)
					.detalhes(detalheServico)
					.build();
			tarefas.add(tarefa);
		}
		
		return tarefas;
	}

	public void printFile(Tarefa tarefa) {
		file.createFolder(this.gerenciamento.obtemNomeDePastaDoProdutor(tarefa));
	}

	private List<DetalheServico> toListServices(List<DetailServiceDto> tipoServico) {

		return tipoServico.stream().map(servico->toService(servico)).collect(Collectors.toList());
	}

	private DetalheServico toService(DetailServiceDto dto) {
		BigDecimal valorDoServico;
		try {
			valorDoServico = new BigDecimal(dto.getValorDoServico());
			
		}catch(NullPointerException ex) {
			valorDoServico = BigDecimal.ZERO;
		}
		
		TipoServico typeService = this.typeService.getByType(dto.getTipoServico()).orElseThrow(()-> new ServiceNotFound());
		Boolean emitiuDae = Boolean.parseBoolean(dto.getEmitiuDAE());
		Boolean emitiuArt = Boolean.parseBoolean(dto.getEmitiuART());
		
		return DetalheServico.builder()
				.emitiuART(emitiuArt)
				.emitiuDAE(emitiuDae)
				.tiposervico(typeService)
				.valorDoServico(valorDoServico)
				.tarefaDescricao(dto.getTarefaDescricao())
				.statusTarefa(EnumStatus.INICIADA)
				.dataConclusaoPrevista(LocalDate.now().plusDays(typeService.getTempoEstimado()).toString())
				.build();
	}

	@Override
	public TarefaGetDto toTarefaGetDto(Tarefa tarefa) {
		List<DetalheServico> detalhes = tarefa.getDetalhes();
		List<DetailsServiceResportDto> report = toListDetailsServiceResportDto(detalhes);
		
		return TarefaGetDto.builder()
				.atendimentos(report)
				.cpfProdutor(tarefa.getProdutor().getCpf())
				.nomeDoProdutor(tarefa.getProdutor().getNome())
				.id(tarefa.getId())
				.dataSolicitacao(tarefa.getDataCadastro().format(dateTimeFormater))
				.build();
	}

	private List<DetailsServiceResportDto> toListDetailsServiceResportDto(List<DetalheServico> detalhes) {
		return detalhes.stream().map(detalhe->toDetailsServiceResportDto(detalhe)).collect(Collectors.toList());
	}

	private DetailsServiceResportDto toDetailsServiceResportDto(DetalheServico detalhe) {
		return DetailsServiceResportDto.builder()
				.valorDoServico(String.valueOf(detalhe.getValorDoServico()))
				.emitiuDAE(detalhe.getEmitiuDAE().toString())
				.emitiuART(detalhe.getEmitiuART().toString())
				.tarefaDescricao(detalhe.getTiposervico().getDescricaoTipo())
				.dataConclusaoPrevista(detalhe.getDataConclusaoPrevista())
				.situacao(detalhe.getStatusTarefa().toString())
				.build();
	}

	@Override
	public Tarefa getById(String idTarefa) {
		Long id = Long.parseLong(idTarefa);
		return this.getById(id);
	}

}
