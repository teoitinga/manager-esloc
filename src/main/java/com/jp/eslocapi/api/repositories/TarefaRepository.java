package com.jp.eslocapi.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.eslocapi.api.entities.Tarefa;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {


}
