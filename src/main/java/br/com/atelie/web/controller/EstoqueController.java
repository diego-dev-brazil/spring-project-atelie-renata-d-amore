package br.com.atelie.web.controller;

import br.com.atelie.application.estoque.EstoqueService;
import br.com.atelie.domain.model.Material;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Controller responsável pelo módulo de Estoque e Materiais.
 *
 * SRP: Responsável unicamente pela interação HTTP para listar, cadastrar,
 *      editar e excluir materiais de estoque.
 * DIP: Depende de EstoqueService (camada de aplicação), sem acoplamento com
 *      implementações de armazenamento.
 */
@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public String listar(Model model, @RequestParam(name = "editar", required = false) Long idEdicao) {
        model.addAttribute("materiais", estoqueService.listarTodos());
        model.addAttribute("totalItens", estoqueService.totalItensEmEstoque());

        if (idEdicao != null) {
            Optional<Material> materialEditando = estoqueService.buscarPorId(idEdicao);
            materialEditando.ifPresent(m -> model.addAttribute("materialEmEdicao", m));
        }

        return "estoque";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam(required = false) Long id,
                         @RequestParam String nome,
                         @RequestParam String categoria,
                         @RequestParam int quantidade,
                         @RequestParam int quantidadeMinima,
                         @RequestParam String unidadeMedida,
                         @RequestParam(required = false) BigDecimal precoUnitario,
                         RedirectAttributes redirectAttributes) {
        try {
            estoqueService.cadastrarOuAtualizar(id, nome, categoria, quantidade, quantidadeMinima, unidadeMedida, precoUnitario);
            redirectAttributes.addFlashAttribute("sucesso", id == null ? "Material cadastrado com sucesso!" : "Material atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/estoque";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean removido = estoqueService.excluir(id);
        if (removido) {
            redirectAttributes.addFlashAttribute("sucesso", "Material removido do estoque.");
        } else {
            redirectAttributes.addFlashAttribute("erro", "Material não encontrado para exclusão.");
        }
        return "redirect:/estoque";
    }
}

