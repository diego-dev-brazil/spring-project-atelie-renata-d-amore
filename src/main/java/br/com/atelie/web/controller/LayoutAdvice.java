package br.com.atelie.web.controller;

import br.com.atelie.application.menu.MenuService;
import br.com.atelie.web.sessao.SessaoUsuario;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Coloca em TODAS as páginas os dados do layout (usuário logado, menu
 * lateral e página atual), para que cada controller não precise repetir isso.
 *
 * No React, o menu e o usuário eram declarados dentro da própria página.
 */
@ControllerAdvice
public class LayoutAdvice {

    private final SessaoUsuario sessaoUsuario;
    private final MenuService menuService;

    public LayoutAdvice(SessaoUsuario sessaoUsuario, MenuService menuService) {
        this.sessaoUsuario = sessaoUsuario;
        this.menuService = menuService;
    }

    @ModelAttribute
    public void dadosDoLayout(HttpServletRequest request, Model model) {
        sessaoUsuario.usuarioAtual(request.getSession(false)).ifPresent(usuario -> {
            model.addAttribute("usuarioLogado", usuario);
            model.addAttribute("menu", menuService.itensPara(usuario.perfil()));
        });
        model.addAttribute("caminhoAtual", request.getRequestURI().substring(request.getContextPath().length()));
    }
}

