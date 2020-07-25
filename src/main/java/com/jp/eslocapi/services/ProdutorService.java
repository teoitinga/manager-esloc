package com.jp.eslocapi.services;

import com.jp.eslocapi.api.dto.ProdutorDto;
import com.jp.eslocapi.api.entities.Persona;

public interface ProdutorService {

	Persona save(Persona produtor);

	Persona getById(Long id);

	void delete(Persona toDeleted);

	ProdutorDto update(ProdutorDto dto);

	Persona toProdutor(ProdutorDto dto);

	ProdutorDto toProdutorDto(Persona toSaved);

	Persona getByCpf(String cpf);

	Persona whatIsCpf(String cpf);

}
