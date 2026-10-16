package br.com.atelie.web.controller;

import br.com.atelie.application.notificacoes.NotificacaoService;
import br.com.atelie.domain.model.Notificacao.Categoria;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller da Central de Notificações (/notificacoes).
 */
@Controller
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    public NotificacaoController(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false, defaultValue = "TODAS") String categoria,
                         Model model) {
        model.addAttribute("categoriaSelecionada", categoria.toUpperCase());
        model.addAttribute("notificacoes", notificacaoService.listarPorCategoria(categoria));
        model.addAttribute("qtdFaltaMaterial", notificacaoService.contarPorCategoria(Categoria.FALTA_MATERIAL));
        model.addAttribute("qtdPagamentoAtrasado", notificacaoService.contarPorCategoria(Categoria.PAGAMENTO_ATRASADO));
        model.addAttribute("qtdAniversarios", notificacaoService.contarPorCategoria(Categoria.ANIVERSARIO_FUNCIONARIO));
        model.addAttribute("qtdUrgentes", notificacaoService.contarUrgentes());
        return "notificacoes";
    }
}

