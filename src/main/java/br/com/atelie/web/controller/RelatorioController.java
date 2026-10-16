package br.com.atelie.web.controller;

import br.com.atelie.application.formatacao.FormatadorMoeda;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.application.relatorios.RelatorioService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller para o módulo de Relatórios Gerenciais (exclusivo Admin).
 */
@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final FormatadorValor formatadorMoeda;

    public RelatorioController(RelatorioService relatorioService,
                               @Qualifier(FormatadorMoeda.NOME) FormatadorValor formatadorMoeda) {
        this.relatorioService = relatorioService;
        this.formatadorMoeda = formatadorMoeda;
    }

    @GetMapping
    public String exibir(Model model) {
        model.addAttribute("valorEstoque", formatadorMoeda.formatar(relatorioService.calcularValorPatrimonialEstoque()));
        model.addAttribute("faturamentoSemestral", formatadorMoeda.formatar(relatorioService.calcularFaturamentoSemestral()));
        model.addAttribute("receitaTurmas", formatadorMoeda.formatar(relatorioService.calcularReceitaPotencialTurmas()));
        model.addAttribute("pecasProduzidas", relatorioService.contarPecasProduzidas());
        model.addAttribute("totalInadimplentes", relatorioService.totalInadimplentes());
        model.addAttribute("totalMateriaisCriticos", relatorioService.totalMateriaisCriticos());
        model.addAttribute("totalModelosCatalogo", relatorioService.totalModelosCatalogo());
        model.addAttribute("historicoVendas", relatorioService.historicoVendasMensais());
        model.addAttribute("formatadorMoeda", formatadorMoeda);
        return "relatorios";
    }
}

