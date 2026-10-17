package com.br.escolar.agenda_escolar.DTO;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record EventoDTO(
        Long id,

        @NotBlank(message = "Título é obrigatório")
        @Size(min = 3, max = 100, message = "Título deve conter entre 3 e 100 caracteres")
        String titulo,

        @Size(max = 500, message = "Descrição pode ter no máximo 500 caracteres")
        String descricao,

        @NotBlank(message = "Responsável é obrigatório")
        @Size(min = 3, max = 100, message = "Nome do responsável deve ter entre 3 e 100 caracteres")
        String responsavel,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String emailResponsavel,

        @NotNull(message = "Data/hora de início é obrigatória")
        LocalDateTime inicio,

        @NotNull(message = "Data/hora de término é obrigatória")
        LocalDateTime termino,

        @NotNull(message = "Público previsto é obrigatório")
        @Min(value = 1, message = "Público previsto deve ser maior que zero")
        Integer publicoPrevisto,

        @NotNull(message = "Espaço é obrigatório")
        Long espacoId,

        String situacao
) {
}
