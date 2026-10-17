package com.br.escolar.agenda_escolar.controller;

import com.br.escolar.agenda_escolar.DTO.EventoDTO;
import com.br.escolar.agenda_escolar.enums.SituacaoEvento;
import com.br.escolar.agenda_escolar.exceptions.ConflitoRegraException;
import com.br.escolar.agenda_escolar.exceptions.ValidacaoRegraException;
import com.br.escolar.agenda_escolar.model.Evento;
import com.br.escolar.agenda_escolar.services.EspacoService;
import com.br.escolar.agenda_escolar.services.EventoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/eventos")
public class EventoWebController {

    private final EventoService eventoService;
    private final EspacoService espacoService;

    public EventoWebController(EventoService eventoService, EspacoService espacoService) {
        this.eventoService = eventoService;
        this.espacoService = espacoService;
    }

    @GetMapping
    public String listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) Long espacoId,
            @RequestParam(required = false) SituacaoEvento situacao,
            Model model) {

        model.addAttribute("eventos", eventoService.listarEFiltrar(data, espacoId, situacao));
        model.addAttribute("espacos", espacoService.listarTodos());
        model.addAttribute("dataFiltro", data);
        model.addAttribute("espacoIdFiltro", espacoId);
        model.addAttribute("situacaoFiltro", situacao);
        return "eventos/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("eventoDTO", new EventoDTO(null, "", "", "", "", null, null, null, null, null));
        model.addAttribute("espacos", espacoService.listarTodos());
        return "eventos/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        Evento e = eventoService.buscarPorId(id);
        EventoDTO dto = new EventoDTO(
                e.getId(), e.getTitulo(), e.getDescricao(), e.getResponsavel(),
                e.getEmail_responsavel(), e.getInicio(), e.getFim(),
                e.getPublico_previsto(), e.getEspaco().getId(), e.getSituacao().name()
        );
        model.addAttribute("eventoDTO", dto);
        model.addAttribute("espacos", espacoService.listarTodos());
        return "eventos/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("eventoDTO") EventoDTO dto,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("espacos", espacoService.listarTodos());
            return "eventos/form";
        }
        try {
            if (dto.id() == null) {
                eventoService.agendar(dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Evento agendado com sucesso!");
            } else {
                eventoService.alterar(dto.id(), dto);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Evento alterado com sucesso!");
            }
            return "redirect:/eventos";
        } catch (ValidacaoRegraException | ConflitoRegraException e) {
            model.addAttribute("mensagemErro", e.getMessage());
            model.addAttribute("espacos", espacoService.listarTodos());
            return "eventos/form";
        }
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("evento", eventoService.buscarPorId(id));
        return "eventos/detalhes";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventoService.cancelar(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Evento cancelado com sucesso!");
        } catch (ConflitoRegraException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/eventos/" + id;
    }
}