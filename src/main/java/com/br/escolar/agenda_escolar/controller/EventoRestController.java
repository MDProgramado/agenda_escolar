package com.br.escolar.agenda_escolar.controller;


import com.br.escolar.agenda_escolar.DTO.EventoDTO;
import com.br.escolar.agenda_escolar.enums.SituacaoEvento;
import com.br.escolar.agenda_escolar.model.Evento;
import com.br.escolar.agenda_escolar.services.EventoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoRestController {

    private final EventoService eventoService;

    public EventoRestController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<Evento> agendar(@Valid @RequestBody EventoDTO dto) {
        Evento eventoSalvo = eventoService.agendar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Evento>> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Long espacoId,
            @RequestParam(required = false) SituacaoEvento situacao) {
        return ResponseEntity.ok(eventoService.listarEFiltrar(data, espacoId, situacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evento> consultarDetalhes(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evento> alterar(@PathVariable Long id, @Valid @RequestBody EventoDTO dto) {
        return ResponseEntity.ok(eventoService.alterar(id, dto));
    }

    @PatchMapping("/{id}/cancelamento")
    public ResponseEntity<Evento> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(eventoService.cancelar(id));
    }
}
