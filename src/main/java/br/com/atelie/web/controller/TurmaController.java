package br.com.atelie.web.controller;

import br.com.atelie.application.formatacao.FormatadorMoeda;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.application.turmas.TurmaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

/**
 * Controller para o módulo de Turmas.
 */
@Controller
@RequestMapping("/turmas")
public class TurmaController {

    private final TurmaService turmaService;
    private final FormatadorValor formatadorMoeda;

    public TurmaController(TurmaService turmaService,
                           @Qualifier(FormatadorMoeda.NOME) FormatadorValor formatadorMoeda) {
        this.turmaService = turmaService;
        this.formatadorMoeda = formatadorMoeda;
    }

    @GetMapping
    public String listar(Model model, @RequestParam(name = "editar", required = false) Long idEdicao) {
        model.addAttribute("turmas", turmaService.listarTodas());
        model.addAttribute("totalAtivas", turmaService.totalTurmasAtivas());
        model.addAttribute("formatadorMoeda", formatadorMoeda);

        if (idEdicao != null) {
            turmaService.buscarPorId(idEdicao).ifPresent(t -> model.addAttribute("turmaEmEdicao", t));
        }
        return "turmas";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam(required = false) Long id,
                         @RequestParam String nome,
                         @RequestParam String instrutora,
                         @RequestParam String horario,
                         @RequestParam int vagasTotais,
                         @RequestParam BigDecimal valorMensalidade,
                         @RequestParam(defaultValue = "false") boolean ativa,
                         RedirectAttributes redirectAttributes) {
        try {
            turmaService.salvar(id, nome, instrutora, horario, vagasTotais, valorMensalidade, ativa);
            redirectAttributes.addFlashAttribute("sucesso", id == null ? "Turma criada com sucesso!" : "Turma atualizada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/turmas";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        turmaService.excluir(id);
        redirectAttributes.addFlashAttribute("sucesso", "Turma removida.");
        return "redirect:/turmas";
    }
}

