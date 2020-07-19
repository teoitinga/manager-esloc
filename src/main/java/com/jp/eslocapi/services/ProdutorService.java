package com.jp.eslocapi.services;

import com.jp.eslocapi.api.entities.Produtor;

public interface ProdutorService {

	Produtor save(Produtor produtor);

	Produtor getById(Long id);

}
