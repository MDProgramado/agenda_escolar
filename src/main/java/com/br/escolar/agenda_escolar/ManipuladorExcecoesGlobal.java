package com.br.escolar.agenda_escolar;

import com.br.escolar.agenda_escolar.exceptions.ConflitoRegraException;
import com.br.escolar.agenda_escolar.exceptions.RecursoNaoEncontradoException;
import com.br.escolar.agenda_escolar.exceptions.ValidacaoRegraException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManipuladorExcecoesGlobal {
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(RecursoNaoEncontradoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 404);
        body.put("erro", "Recurso não encontrado");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ConflitoRegraException.class)
    public ResponseEntity<Map<String, Object>> handleConflict(ConflitoRegraException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 409);
        body.put("erro", "Conflito de regra de negócio");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(ValidacaoRegraException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(ValidacaoRegraException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("erro", "Requisição inválida");
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        Map<String, String> errosCampos = new HashMap<>();

        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errosCampos.put(fe.getField(), fe.getDefaultMessage());
        }

        body.put("timestamp", LocalDateTime.now());
        body.put("status", 400);
        body.put("erro", "Falha de validação dos campos");
        body.put("erros", errosCampos);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
