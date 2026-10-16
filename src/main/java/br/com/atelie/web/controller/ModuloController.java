package br.com.atelie.web.controller;

import br.com.atelie.application.autenticacao.UsuarioLogado;
import br.com.atelie.application.menu.MenuService;
import br.com.atelie.domain.model.ItemMenu;
import br.com.atelie.web.sessao.SessaoUsuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

/**
 * Páginas dos módulos que ainda serão desenvolvidos (Estoque, Vendas...).
 * No React esses links levavam a um erro 404; aqui mostram "em desenvolvimento".
 *
 * Também garante que a funcionária não acesse módulos só de admin,
 * mesmo digitando a URL direto no navegador.
 */
@Controller
public class ModuloController {

    private final MenuService menuService;
    private final SessaoUsuario sessaoUsuario;

    public ModuloController(MenuService menuService, SessaoUsuario sessaoUsuario) {
        this.menuService = menuService;
        this.sessaoUsuario = sessaoUsuario;
    }

    @GetMapping("/modulos/{slug}")
    public String modulo(@PathVariable String slug, HttpSession sessao, Model model) {
        ItemMenu modulo = menuService.buscarModulo(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Módulo não encontrado"));

        UsuarioLogado usuario = sessaoUsuario.usuarioAtual(sessao)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!modulo.permitidoPara(usuario.perfil())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso restrito à administração");
        }

        model.addAttribute("modulo", modulo);
        return "modulo";
    }
}

