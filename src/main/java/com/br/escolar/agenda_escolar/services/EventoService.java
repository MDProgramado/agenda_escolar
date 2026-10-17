package com.br.escolar.agenda_escolar.services;

import com.br.escolar.agenda_escolar.DTO.EventoDTO;
import com.br.escolar.agenda_escolar.enums.SituacaoEvento;
import com.br.escolar.agenda_escolar.exceptions.ConflitoRegraException;
import com.br.escolar.agenda_escolar.exceptions.RecursoNaoEncontradoException;
import com.br.escolar.agenda_escolar.exceptions.ValidacaoRegraException;
import com.br.escolar.agenda_escolar.model.Espaco;
import com.br.escolar.agenda_escolar.model.Evento;
import com.br.escolar.agenda_escolar.repository.EventoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final EspacoService espacoService;

    public EventoService(EventoRepository eventoRepository, EspacoService espacoService) {
        this.eventoRepository = eventoRepository;
        this.espacoService = espacoService;
    }

    @Transactional(readOnly = true)
    public List<Evento> listarEFiltrar(LocalDate data, Long espacoId, SituacaoEvento situacao) {
        LocalDateTime dataInicio = data != null ? data.atStartOfDay() : null;
        LocalDateTime dataFim = data != null ? data.atTime(LocalTime.MAX) : null;
        return eventoRepository.filtrarEventos(dataInicio, dataFim, espacoId, situacao);
    }

    @Transactional(readOnly = true)
    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Evento não encontrado com ID: " + id));
    }

    @Transactional
    public Evento agendar(EventoDTO dto) {
        Espaco espaco = espacoService.buscarPorId(dto.espacoId());

        validarRegrasEvento(dto, espaco, null);

        Evento evento = new Evento();
        converterDtoParaEntidade(dto, evento, espaco);
        evento.setSituacao(SituacaoEvento.AGENDADO);

        return eventoRepository.save(evento);
    }

    @Transactional
    public Evento alterar(Long id, EventoDTO dto) {
        Evento eventoExistente = buscarPorId(id);


        if (eventoExistente.getSituacao() == SituacaoEvento.CANCELADO) {
            throw new ConflitoRegraException("Eventos cancelados não podem ser alterados.");
        }

        Espaco espaco = espacoService.buscarPorId(dto.espacoId());
        validarRegrasEvento(dto, espaco, id);

        converterDtoParaEntidade(dto, eventoExistente, espaco);
        return eventoRepository.save(eventoExistente);
    }

    @Transactional
    public Evento cancelar(Long id) {
        Evento evento = buscarPorId(id);


        if (evento.getSituacao() == SituacaoEvento.CANCELADO) {
            throw new ConflitoRegraException("Este evento já está cancelado.");
        }

        evento.setSituacao(SituacaoEvento.CANCELADO);
        return eventoRepository.save(evento);
    }

    private void validarRegrasEvento(EventoDTO dto, Espaco espaco, Long eventoIdAtual) {
        LocalDateTime agora = LocalDateTime.now();


        if (dto.inicio().isBefore(agora)) {
            throw new ValidacaoRegraException("A data/hora de início do evento deve estar no futuro.");
        }

        // RN03: Término posterior ao início
        if (!dto.termino().isAfter(dto.inicio())) {
            throw new ValidacaoRegraException("A data/hora de término deve ser posterior à data/hora de início.");
        }


        if (dto.publicoPrevisto() > espaco.getCapacidade()) {
            throw new ValidacaoRegraException("O público previsto (" + dto.publicoPrevisto() +
                    ") excede a capacidade do espaço " + espaco.getNome() + " (" + espaco.getCapacidade() + ").");
        }


        boolean conflito = eventoRepository.existeConflitoHorario(
                espaco.getId(), dto.inicio(), dto.termino(), eventoIdAtual);

        if (conflito) {
            throw new ConflitoRegraException("O espaço '" + espaco.getNome() + "' já possui um evento agendado com horário conflitante neste período.");
        }
    }

    private void converterDtoParaEntidade(EventoDTO dto, Evento evento, Espaco espaco) {
        evento.setTitulo(dto.titulo());
        evento.setDescricao(dto.descricao());
        evento.setResponsavel(dto.responsavel());
        evento.setEmail_responsavel(dto.emailResponsavel());
        evento.setInicio(dto.inicio());
        evento.setFim(dto.termino());
        evento.setPublico_previsto(dto.publicoPrevisto());
        evento.setEspaco(espaco);
    }
}
