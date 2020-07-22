package com.jp.eslocapi.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.eslocapi.api.entities.Persona;

public interface ProdutorRepository extends JpaRepository<Persona, Long> {

	boolean existsByCpf(String cpf);

}
