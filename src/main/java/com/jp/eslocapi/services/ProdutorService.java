package com.jp.eslocapi.services;

import javax.validation.Valid;

import com.jp.eslocapi.api.dto.ProdutorDto;
import com.jp.eslocapi.api.entities.Produtor;

public interface ProdutorService {

	Produtor save(Produtor produtor);

	Produtor getById(Long id);

	void delete(Produtor toDeleted);

	ProdutorDto update(@Valid ProdutorDto dto);

}
