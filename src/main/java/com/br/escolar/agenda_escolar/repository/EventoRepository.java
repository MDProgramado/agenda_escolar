package com.br.escolar.agenda_escolar.repository;

import com.br.escolar.agenda_escolar.enums.SituacaoEvento;
import com.br.escolar.agenda_escolar.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    boolean existsByEspacoId(Long espacoId);

    @Query("SELECT COUNT(e) > 0 FROM Evento e WHERE e.espaco.id = :espacoId " +
            "AND e.situacao = 'AGENDADO' " +
            "AND (:id IS NULL OR e.id != :id) " +
            "AND (e.inicio < :termino AND e.termino > :inicio)")
    boolean existeConflitoHorario(@Param("espacoId") Long espacoId,
                                   @Param("inicio") LocalDateTime inicio,
                                   @Param("termino") LocalDateTime termino,
                                   @Param("id") Long id);

    @Query("SELECT MAX(e.publicoPrevisto) FROM Evento e WHERE e.espaco.id = :espacoId AND e.situacao = 'AGENDADO'")
    Integer findMaxPublicoAgendadoByEspacoId(@Param("espacoId") Long espacoId);

    @Query("SELECT e FROM Evento e WHERE " +
            "(:dataInicio IS NULL OR e.inicio >= :dataInicio AND e.inicio < :dataFim) AND " +
            "(:espacoId IS NULL OR e.espaco.id = :espacoId) AND " +
            "(:situacao IS NULL OR e.situacao = :situacao) " +
            "ORDER BY e.inicio ASC")
    List<Evento> filtrarEventos(@Param("dataInicio") LocalDateTime dataInicio,
                                @Param("dataFim") LocalDateTime dataFim,
                                @Param("espacoId") Long espacoId,
                                @Param("situacao") SituacaoEvento situacao);
}
