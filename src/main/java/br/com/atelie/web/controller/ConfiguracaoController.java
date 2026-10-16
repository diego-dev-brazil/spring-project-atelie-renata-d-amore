package br.com.atelie.web.controller;

import br.com.atelie.application.configuracoes.ConfiguracaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

/**
 * Controller para o módulo de Configurações do Ateliê (exclusivo Admin).
 */
@Controller
@RequestMapping("/configuracoes")
public class ConfiguracaoController {

    private final ConfiguracaoService configuracaoService;

    public ConfiguracaoController(ConfiguracaoService configuracaoService) {
        this.configuracaoService = configuracaoService;
    }

    @GetMapping
    public String exibir(Model model) {
        model.addAttribute("config", configuracaoService.obter());
        return "configuracoes";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam String nomeAtelie,
                         @RequestParam String proprietaria,
                         @RequestParam String cnpj,
                         @RequestParam String telefone,
                         @RequestParam String emailContato,
                         @RequestParam String endereco,
                         @RequestParam BigDecimal metaVendasMensal,
                         @RequestParam(defaultValue = "false") boolean alertasEstoqueAtivos,
                         RedirectAttributes redirectAttributes) {
        configuracaoService.atualizar(nomeAtelie, proprietaria, cnpj, telefone, emailContato, endereco, metaVendasMensal, alertasEstoqueAtivos);
        redirectAttributes.addFlashAttribute("sucesso", "Configurações do ateliê salvas com sucesso!");
        return "redirect:/configuracoes";
    }
}

