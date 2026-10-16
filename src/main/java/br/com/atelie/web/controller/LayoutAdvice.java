package br.com.atelie.web.controller;

import br.com.atelie.application.menu.MenuService;
import br.com.atelie.application.notificacoes.NotificacaoService;
import br.com.atelie.domain.model.Notificacao;
import br.com.atelie.web.sessao.SessaoUsuario;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Coloca em TODAS as páginas os dados do layout (usuário logado, menu
 * lateral, notificações do sino e página atual), para que cada controller
 * não precise repetir isso.
 */
@ControllerAdvice
public class LayoutAdvice {

    private final SessaoUsuario sessaoUsuario;
    private final MenuService menuService;
    private final NotificacaoService notificacaoService;

    public LayoutAdvice(SessaoUsuario sessaoUsuario,
                        MenuService menuService,
                        NotificacaoService notificacaoService) {
        this.sessaoUsuario = sessaoUsuario;
        this.menuService = menuService;
        this.notificacaoService = notificacaoService;
    }

    @ModelAttribute
    public void dadosDoLayout(HttpServletRequest request, Model model) {
        sessaoUsuario.usuarioAtual(request.getSession(false)).ifPresent(usuario -> {
            model.addAttribute("usuarioLogado", usuario);
            model.addAttribute("menu", menuService.itensPara(usuario.perfil()));
            List<Notificacao> notificacoes = notificacaoService.listarTodas();
            model.addAttribute("notificacoesTopo", notificacoes);
            model.addAttribute("totalNotificacoes", notificacoes.size());
        });
        model.addAttribute("caminhoAtual", request.getRequestURI().substring(request.getContextPath().length()));
    }
}

