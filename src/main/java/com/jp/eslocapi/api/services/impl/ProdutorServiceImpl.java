package com.jp.eslocapi.api.services.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jp.eslocapi.api.dto.ProdutoPostMinDto;
import com.jp.eslocapi.api.dto.ProdutorDto;
import com.jp.eslocapi.api.entities.Persona;
import com.jp.eslocapi.api.exceptions.ProdutorNotFound;
import com.jp.eslocapi.api.repositories.ProdutorRepository;
import com.jp.eslocapi.exceptions.BusinessException;
import com.jp.eslocapi.services.ProdutorService;

@Service
public class ProdutorServiceImpl implements ProdutorService {

	@Value("${esloc.date.view}")
	private String DATA_FORMAT_VIEW;
	@Value("${esloc.date.form}")
	private String DATA_FORMAT_FORM;
	
	private ProdutorRepository repository;

	public ProdutorServiceImpl(ProdutorRepository repository) {
		this.repository = repository;
	}

	@Override
	public Persona save(Persona produtor) {
		
		if(this.repository.existsByCpf(produtor.getCpf())) {
			throw new BusinessException("Já existe um registro com este cpf.");
		}
		
		return repository.save(produtor);
	}

	@Override
	public Persona getById(Long id) {
		return repository.findById(id).orElseThrow(()-> new ProdutorNotFound());
	}

	@Override
	public void delete(Persona toDeleted) {
		if(this.repository.existsByCpf(toDeleted.getCpf())) {
			throw new BusinessException("Este registro não existe.");
		}
		repository.delete(toDeleted);
	}

	@Override
	public ProdutorDto update(ProdutorDto dto) {
		return null;
	}

	/** Obtem uma inância de <b>Produtor</b> a partir de uma de <b>ProdutorDto</b> informada.
	 * @author João Paulo Santana Gusmão
	 * @param produtorDto
	 * @return Produtor 
	 * @see Persona
	 */
	@Override
	public Persona toProdutor(ProdutorDto produtorDto) {
		String dataNascimento;
		LocalDate localDateNascimento = null;
		System.out.println("Nascimento: " + produtorDto.getDataNascimento().toString());
		System.out.println("Formato: " + DATA_FORMAT_VIEW);

		try {
			localDateNascimento = LocalDate.parse(produtorDto.getDataNascimento(),DateTimeFormatter.ofPattern(DATA_FORMAT_FORM));
			
		}catch(java.time.format.DateTimeParseException e) {
			dataNascimento = produtorDto.getDataNascimento();
			localDateNascimento.parse(dataNascimento, DateTimeFormatter.ofPattern(DATA_FORMAT_VIEW));
		}
		
		return Persona.builder()
				.nome(produtorDto.getNome())
				.cpf(produtorDto.getCpf())
				.fone(produtorDto.getFone())
				.dataNascimento(localDateNascimento)//LocalDate.parse(produtorDto.getDataNascimento()))//LocalDate.parse(produtorDto.getDataNascimento(), DateTimeFormatter.ofPattern(DATA_FORMAT_VIEW)))
				.build();
	}
	
	/** Obtem uma inância de <b>ProdutorDto</b> a partir de uma de <b>Produtor</b> informada.
	 * @author João Paulo Santana Gusmão
	 * @param produtor
	 * @return ProdutorDto 
	 * @see ProdutorDto
	 */
	@Override
	public ProdutorDto toProdutorDto(Persona persona) {
		if(persona == null) {
			return null;
		}

		String dataDeNascimento = null;
		try {
			dataDeNascimento = String.valueOf(persona.getDataNascimento().format(DateTimeFormatter.ofPattern(DATA_FORMAT_VIEW)));
		}catch(Exception e) {
			
		}
		
		return ProdutorDto.builder()
				.id(persona.getId())
				.nome(persona.getNome())
				.cpf(persona.getCpf())
				.fone(persona.getFone())
				.dataNascimento(dataDeNascimento)
				.build();
	}

	@Override
	public Persona getByCpf(String cpf) {
		return this.repository.findByCpf(cpf).orElseThrow(()-> new ProdutorNotFound());
	}

	@Override
	public Persona whatIsCpf(String cpf) {
		Optional<Persona> produtor = this.repository.findByCpf(cpf);
		if(produtor.isPresent()) {
			return produtor.get();
		} else {
			return null;
		}

	}

	@Override
	public Persona toProdutor(ProdutoPostMinDto produtor) {
		return Persona.builder()
				.nome(produtor.getNome())
				.cpf(produtor.getCpf())
				.build();
	}
	
	@Override
	public Persona saveMin(ProdutoPostMinDto produtorMin) {
		Persona produtor = Persona.builder()
				.nome(produtorMin.getNome())
				.cpf(produtorMin.getCpf())
				.build();
		return this.repository.save(produtor);
	}


}
