package com.br.escolar.agenda_escolar.controller;
import com.br.escolar.agenda_escolar.exceptions.ConflitoRegraException;
import com.br.escolar.agenda_escolar.model.Espaco;
import com.br.escolar.agenda_escolar.services.EspacoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/espacos")
public class EspacoWebController {

    private final EspacoService espacoService;

    public EspacoWebController(EspacoService espacoService) {
        this.espacoService = espacoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("espacos", espacoService.listarTodos());
        return "espacos/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("espaco", new Espaco());
        return "espacos/form";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("espaco", espacoService.buscarPorId(id));
        return "espacos/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("espaco") Espaco espaco,
                         BindingResult result,
                         RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "espacos/form";
        }
        try {
            if (espaco.getId() == null) {
                espacoService.salvar(espaco);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Espaço cadastrado com sucesso!");
            } else {
                espacoService.atualizar(espaco.getId(), espaco);
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Espaço atualizado com sucesso!");
            }
            return "redirect:/espacos";
        } catch (ConflitoRegraException e) {
            result.rejectValue("nome", "error.espaco", e.getMessage());
            return "espacos/form";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            espacoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Espaço excluído com sucesso!");
        } catch (ConflitoRegraException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        }
        return "redirect:/espacos";
    }
}