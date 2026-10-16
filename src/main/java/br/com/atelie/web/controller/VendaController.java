package br.com.atelie.web.controller;

import br.com.atelie.application.formatacao.FormatadorMoeda;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.application.modelos.ModeloBolsaService;
import br.com.atelie.application.vendas.VendaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Controller para o módulo de Vendas.
 */
@Controller
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;
    private final ModeloBolsaService modeloBolsaService;
    private final FormatadorValor formatadorMoeda;

    public VendaController(VendaService vendaService,
                           ModeloBolsaService modeloBolsaService,
                           @Qualifier(FormatadorMoeda.NOME) FormatadorValor formatadorMoeda) {
        this.vendaService = vendaService;
        this.modeloBolsaService = modeloBolsaService;
        this.formatadorMoeda = formatadorMoeda;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vendas", vendaService.listarTodas());
        model.addAttribute("totalMes", formatadorMoeda.formatar(vendaService.totalVendasMesAtual()));
        model.addAttribute("modelos", modeloBolsaService.listarTodos());
        model.addAttribute("formatadorMoeda", formatadorMoeda);
        model.addAttribute("hoje", LocalDate.now());
        return "vendas";
    }

    @PostMapping("/registrar")
    public String registrar(@RequestParam String produto,
                            @RequestParam String cliente,
                            @RequestParam BigDecimal valor,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                            RedirectAttributes redirectAttributes) {
        try {
            vendaService.registrarVenda(produto, cliente, valor, data);
            redirectAttributes.addFlashAttribute("sucesso", "Venda registrada com sucesso! O painel e os gráficos foram atualizados.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/vendas";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        vendaService.excluir(id);
        redirectAttributes.addFlashAttribute("sucesso", "Registro de venda removido.");
        return "redirect:/vendas";
    }
}

