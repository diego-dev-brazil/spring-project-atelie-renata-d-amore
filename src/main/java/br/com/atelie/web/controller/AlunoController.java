package br.com.atelie.web.controller;

import br.com.atelie.application.alunos.AlunoService;
import br.com.atelie.application.turmas.TurmaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller para o módulo de Alunos.
 */
@Controller
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;
    private final TurmaService turmaService;

    public AlunoController(AlunoService alunoService, TurmaService turmaService) {
        this.alunoService = alunoService;
        this.turmaService = turmaService;
    }

    @GetMapping
    public String listar(Model model, @RequestParam(name = "editar", required = false) Long idEdicao) {
        model.addAttribute("alunos", alunoService.listarTodos());
        model.addAttribute("totalAlunos", alunoService.totalAlunos());
        model.addAttribute("turmas", turmaService.listarTodas());

        if (idEdicao != null) {
            alunoService.buscarPorId(idEdicao).ifPresent(a -> model.addAttribute("alunoEmEdicao", a));
        }
        return "alunos";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam(required = false) Long id,
                         @RequestParam String nome,
                         @RequestParam(required = false) String telefone,
                         @RequestParam(required = false) String email,
                         @RequestParam(required = false) Long turmaId,
                         @RequestParam(defaultValue = "0") int parcelasPendentes,
                         RedirectAttributes redirectAttributes) {
        try {
            alunoService.salvar(id, nome, telefone, email, turmaId, parcelasPendentes);
            redirectAttributes.addFlashAttribute("sucesso", id == null ? "Aluna(o) matriculada(o) com sucesso!" : "Cadastro da(o) aluna(o) atualizado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/alunos";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        alunoService.excluir(id);
        redirectAttributes.addFlashAttribute("sucesso", "Aluna(o) removida(o) do cadastro.");
        return "redirect:/alunos";
    }
}

