package com.jp.eslocapi.api.services.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.jp.eslocapi.api.dto.ProdutorDto;
import com.jp.eslocapi.api.entities.Persona;
import com.jp.eslocapi.api.exceptions.ProdutorNotFound;
import com.jp.eslocapi.api.repositories.ProdutorRepository;
import com.jp.eslocapi.exceptions.BusinessException;
import com.jp.eslocapi.services.ProdutorService;

@Service
public class ProdutorServiceImpl implements ProdutorService {

	private ProdutorRepository repository;

	public ProdutorServiceImpl(ProdutorRepository repository) {
		this.repository = repository;
	}

	@Override
	public Persona save(Persona produtor) {
		if(this.repository.existsByCpf(produtor.getCpf())) {
			throw new BusinessException("Este cpf já existe");
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
		return Persona.builder()
				.nome(produtorDto.getNome())
				.cpf(produtorDto.getCpf())
				.fone(produtorDto.getFone())
				.dataNascimento(LocalDate.parse(produtorDto.getDataNascimento(), DateTimeFormatter.ofPattern("dd/MM/yyyy")))
				.build();
	}
	
	/** Obtem uma inância de <b>ProdutorDto</b> a partir de uma de <b>Produtor</b> informada.
	 * @author João Paulo Santana Gusmão
	 * @param produtor
	 * @return ProdutorDto 
	 * @see ProdutorDto
	 */
	@Override
	public ProdutorDto toProdutorDto(Persona toSaved) {
		return ProdutorDto.builder()
				.id(toSaved.getId())
				.nome(toSaved.getNome())
				.cpf(toSaved.getCpf())
				.fone(toSaved.getFone())
				.dataNascimento(String.valueOf(toSaved.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
				.build();
	}

	@Override
	public Persona getByCpf(String cpf) {
		return this.repository.findByCpf(cpf).orElseThrow(()-> new ProdutorNotFound());
	}


}
