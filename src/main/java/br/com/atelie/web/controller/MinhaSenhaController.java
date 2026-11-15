package br.com.atelie.web.controller;

import br.com.atelie.application.autenticacao.UsuarioLogado;
import br.com.atelie.application.usuarios.UsuarioService;
import br.com.atelie.web.sessao.SessaoUsuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller para troca da própria senha (acessível por Funcionários e Administradoras).
 */
@Controller
@RequestMapping("/minha-senha")
public class MinhaSenhaController {

    private final UsuarioService usuarioService;
    private final SessaoUsuario sessaoUsuario;

    public MinhaSenhaController(UsuarioService usuarioService, SessaoUsuario sessaoUsuario) {
        this.usuarioService = usuarioService;
        this.sessaoUsuario = sessaoUsuario;
    }

    @GetMapping
    public String exibirFormulario(HttpSession sessao, Model model) {
        sessaoUsuario.usuarioAtual(sessao).ifPresent(logado ->
                usuarioService.buscarPorLogin(logado.login())
                        .ifPresent(u -> model.addAttribute("dadosFuncionario", u))
        );
        return "minha-senha";
    }

    @PostMapping
    public String alterarSenha(@RequestParam String senhaAtual,
                               @RequestParam String novaSenha,
                               @RequestParam String confirmacaoSenha,
                               HttpSession sessao,
                               RedirectAttributes redirectAttributes) {
        try {
            String loginLogado = sessaoUsuario.usuarioAtual(sessao)
                    .map(UsuarioLogado::login)
                    .orElse("");
            usuarioService.alterarPropriaSenha(loginLogado, senhaAtual, novaSenha, confirmacaoSenha);
            redirectAttributes.addFlashAttribute("sucesso", "Sua senha de acesso foi alterada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/minha-senha";
    }
}
