package com.br.escolar.agenda_escolar.repository;

import com.br.escolar.agenda_escolar.model.Espaco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EspacoRepostory extends JpaRepository<Espaco, Long> {
    List<Espaco> findAllByOrderByNomeAsc();
    boolean existsByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);
}
