package com.br.escolar.agenda_escolar.controller;

import com.br.escolar.agenda_escolar.model.Espaco;
import com.br.escolar.agenda_escolar.services.EspacoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/espacos")
public class EspacoRestController {

    private final EspacoService espacoService;

    public EspacoRestController(EspacoService espacoService) {
        this.espacoService = espacoService;
    }

    @PostMapping
    public ResponseEntity<Espaco> cadastrar(@Valid @RequestBody Espaco espaco) {
        Espaco novoEspaco = espacoService.salvar(espaco);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEspaco);
    }

    @GetMapping
    public ResponseEntity<List<Espaco>> listar() {
        return ResponseEntity.ok(espacoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Espaco> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(espacoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Espaco> alterar(@PathVariable Long id, @Valid @RequestBody Espaco espaco) {
        return ResponseEntity.ok(espacoService.atualizar(id, espaco));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        espacoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}