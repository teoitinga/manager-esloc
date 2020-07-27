package com.jp.eslocapi.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.eslocapi.api.entities.Atendimento;

public interface AtendimentosRepository extends JpaRepository<Atendimento, Long> {

}
