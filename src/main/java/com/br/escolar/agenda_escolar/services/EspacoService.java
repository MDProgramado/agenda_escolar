package com.br.escolar.agenda_escolar.services;


import com.br.escolar.agenda_escolar.exceptions.ConflitoRegraException;
import com.br.escolar.agenda_escolar.exceptions.RecursoNaoEncontradoException;
import com.br.escolar.agenda_escolar.model.Espaco;
import com.br.escolar.agenda_escolar.repository.EspacoRepostory;
import com.br.escolar.agenda_escolar.repository.EventoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EspacoService {

    private final EspacoRepostory espacoRepository;
    private final EventoRepository eventoRepository;

    public EspacoService(EspacoRepostory espacoRepository, EventoRepository eventoRepository) {
        this.espacoRepository = espacoRepository;
        this.eventoRepository = eventoRepository;
    }

    @Transactional(readOnly = true)
    public List<Espaco> listarTodos() {
        return espacoRepository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public Espaco buscarPorId(Long id) {
        return espacoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Espaço não encontrado com o ID: " + id));
    }

    @Transactional
    public Espaco salvar(Espaco espaco) {

        if (espacoRepository.existsByNomeIgnoreCase(espaco.getNome())) {
            throw new ConflitoRegraException("Já existe um espaço cadastrado com o nome '" + espaco.getNome() + "'.");
        }
        return espacoRepository.save(espaco);
    }

    @Transactional
    public Espaco atualizar(Long id, Espaco espacoAtualizado) {
        Espaco espacoExistente = buscarPorId(id);

        if (espacoRepository.existsByNomeIgnoreCaseAndIdNot(espacoAtualizado.getNome(), id)) {
            throw new ConflitoRegraException("Já existe outro espaço com o nome '" + espacoAtualizado.getNome() + "'.");
        }


        Integer maxPublico = eventoRepository.findMaxPublicoAgendadoByEspacoId(id);
        if (maxPublico != null && espacoAtualizado.getCapacidade() < maxPublico) {
            throw new ConflitoRegraException("Não é possível alterar a capacidade para " + espacoAtualizado.getCapacidade() +
                    ", pois existe evento agendado para este espaço com público previsto de " + maxPublico + ".");
        }

        espacoExistente.setNome(espacoAtualizado.getNome());
        espacoExistente.setCapacidade(espacoAtualizado.getCapacidade());
        return espacoRepository.save(espacoExistente);
    }

    @Transactional
    public void excluir(Long id) {
        Espaco espaco = buscarPorId(id);


        if (eventoRepository.existsByEspacoId(id)) {
            throw new ConflitoRegraException("Não é possível excluir o espaço pois existem eventos vinculados a ele.");
        }

        espacoRepository.delete(espaco);
    }
}
