package com.jp.eslocapi.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.eslocapi.api.entities.Produtor;

public interface ProdutorRepository extends JpaRepository<Produtor, Long> {

	boolean existsByCpf(String cpf);

}
