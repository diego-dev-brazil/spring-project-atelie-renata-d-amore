package br.com.atelie.web.controller;

import br.com.atelie.application.estoque.EstoqueService;
import br.com.atelie.application.formatacao.FormatadorMoeda;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.application.modelos.ModeloBolsaService;
import br.com.atelie.domain.model.ModeloBolsa;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsável pelo módulo de Modelos de Bolsas e Ficha Técnica.
 *
 * SRP: Trata a interface web para listar modelos, cadastrar novos com seus materiais,
 *      editar e excluir modelos.
 * DIP: Injeta serviços da camada de aplicação (ModeloBolsaService e EstoqueService).
 */
@Controller
@RequestMapping("/modelos")
public class ModeloBolsaController {

    private final ModeloBolsaService modeloBolsaService;
    private final EstoqueService estoqueService;
    private final FormatadorValor formatadorMoeda;

    public ModeloBolsaController(ModeloBolsaService modeloBolsaService,
                                 EstoqueService estoqueService,
                                 @Qualifier(FormatadorMoeda.NOME) FormatadorValor formatadorMoeda) {
        this.modeloBolsaService = modeloBolsaService;
        this.estoqueService = estoqueService;
        this.formatadorMoeda = formatadorMoeda;
    }

    @GetMapping
    public String listar(Model model, @RequestParam(name = "editar", required = false) Long idEdicao) {
        model.addAttribute("modelos", modeloBolsaService.listarTodos());
        model.addAttribute("totalModelos", modeloBolsaService.totalModelos());
        model.addAttribute("materiaisDisponiveis", estoqueService.listarTodos());
        model.addAttribute("formatadorMoeda", formatadorMoeda);

        if (idEdicao != null) {
            Optional<ModeloBolsa> editando = modeloBolsaService.buscarPorId(idEdicao);
            editando.ifPresent(m -> model.addAttribute("modeloEmEdicao", m));
        }

        return "modelos";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam(required = false) Long id,
                         @RequestParam String nome,
                         @RequestParam(required = false) String descricao,
                         @RequestParam String categoria,
                         @RequestParam(required = false) BigDecimal precoVenda,
                         @RequestParam(defaultValue = "0") int tempoProducaoMinutos,
                         @RequestParam(name = "materialId", required = false) List<Long> materialIds,
                         @RequestParam(name = "quantidade", required = false) List<Integer> quantidades,
                         RedirectAttributes redirectAttributes) {
        try {
            modeloBolsaService.cadastrarOuAtualizar(id, nome, descricao, categoria, precoVenda, tempoProducaoMinutos, materialIds, quantidades);
            redirectAttributes.addFlashAttribute("sucesso", id == null ? "Modelo de bolsa cadastrado com sucesso!" : "Modelo atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/modelos";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean removido = modeloBolsaService.excluir(id);
        if (removido) {
            redirectAttributes.addFlashAttribute("sucesso", "Modelo de bolsa removido com sucesso.");
        } else {
            redirectAttributes.addFlashAttribute("erro", "Modelo não encontrado para exclusão.");
        }
        return "redirect:/modelos";
    }
}

