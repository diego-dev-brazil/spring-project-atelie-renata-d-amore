package br.com.atelie.web.controller;

import br.com.atelie.application.modelos.ModeloBolsaService;
import br.com.atelie.application.producao.ProducaoService;
import br.com.atelie.application.usuarios.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller para o módulo de Produção.
 */
@Controller
@RequestMapping("/producao")
public class ProducaoController {

    private final ProducaoService producaoService;
    private final ModeloBolsaService modeloBolsaService;
    private final UsuarioService usuarioService;

    public ProducaoController(ProducaoService producaoService,
                              ModeloBolsaService modeloBolsaService,
                              UsuarioService usuarioService) {
        this.producaoService = producaoService;
        this.modeloBolsaService = modeloBolsaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ordens", producaoService.listarTodas());
        model.addAttribute("modelos", modeloBolsaService.listarTodos());
        model.addAttribute("funcionarios", usuarioService.listarTodos());
        return "producao";
    }

    @PostMapping("/nova")
    public String abrirOrdem(@RequestParam Long modeloId,
                             @RequestParam int quantidadePecas,
                             @RequestParam String responsavel,
                             RedirectAttributes redirectAttributes) {
        try {
            var ordem = producaoService.abrirOrdem(modeloId, quantidadePecas, responsavel);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Ordem de produção #" + ordem.getId() + " aberta (" + ordem.getStatus().getDescricao() + ").");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/producao";
    }

    @PostMapping("/concluir/{id}")
    public String concluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            producaoService.concluirOrdem(id);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Produção concluída! Os materiais da ficha técnica foram descontados automaticamente do estoque.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/producao";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        producaoService.excluir(id);
        redirectAttributes.addFlashAttribute("sucesso", "Ordem de produção removida.");
        return "redirect:/producao";
    }
}

