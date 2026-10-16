package br.com.atelie.web.controller;

import br.com.atelie.application.autenticacao.UsuarioLogado;
import br.com.atelie.application.usuarios.UsuarioService;
import br.com.atelie.domain.model.Perfil;
import br.com.atelie.web.sessao.SessaoUsuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * Controller para o gerenciamento de Usuários e Funcionários (exclusivo Admin).
 */
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final SessaoUsuario sessaoUsuario;

    public UsuarioController(UsuarioService usuarioService, SessaoUsuario sessaoUsuario) {
        this.usuarioService = usuarioService;
        this.sessaoUsuario = sessaoUsuario;
    }

    @GetMapping
    public String listar(Model model, @RequestParam(name = "editar", required = false) String loginEdicao) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("perfis", Perfil.values());

        if (loginEdicao != null && !loginEdicao.isBlank()) {
            usuarioService.buscarPorLogin(loginEdicao)
                    .ifPresent(u -> model.addAttribute("usuarioEmEdicao", u));
        }
        return "usuarios";
    }

    @PostMapping("/novo")
    public String cadastrar(@RequestParam String login,
                            @RequestParam String nome,
                            @RequestParam String cargo,
                            @RequestParam String senha,
                            @RequestParam Perfil perfil,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataNascimento,
                            RedirectAttributes redirectAttributes) {
        try {
            usuarioService.cadastrarNovo(login, nome, cargo, senha, perfil, dataNascimento);
            redirectAttributes.addFlashAttribute("sucesso", "Funcionário(a) '" + nome + "' cadastrado(a) com acesso liberado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/atualizar")
    public String atualizar(@RequestParam String login,
                            @RequestParam String nome,
                            @RequestParam String cargo,
                            @RequestParam(required = false) String novaSenha,
                            @RequestParam Perfil perfil,
                            @RequestParam(defaultValue = "false") boolean ativo,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataNascimento,
                            HttpSession sessao,
                            RedirectAttributes redirectAttributes) {
        try {
            String loginLogado = sessaoUsuario.usuarioAtual(sessao).map(UsuarioLogado::login).orElse("");
            usuarioService.atualizar(login, nome, cargo, novaSenha, perfil, ativo, dataNascimento, loginLogado);
            redirectAttributes.addFlashAttribute("sucesso", "Dados e permissões de '" + nome + "' atualizados!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/alternar-acesso/{login}")
    public String alternarAcesso(@PathVariable String login,
                                 HttpSession sessao,
                                 RedirectAttributes redirectAttributes) {
        try {
            String loginLogado = sessaoUsuario.usuarioAtual(sessao).map(UsuarioLogado::login).orElse("");
            usuarioService.alternarAcesso(login, loginLogado);
            redirectAttributes.addFlashAttribute("sucesso", "Permissão de acesso ao sistema alterada com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/excluir/{login}")
    public String excluir(@PathVariable String login,
                          HttpSession sessao,
                          RedirectAttributes redirectAttributes) {
        try {
            String loginLogado = sessaoUsuario.usuarioAtual(sessao).map(UsuarioLogado::login).orElse("");
            usuarioService.excluir(login, loginLogado);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário removido do sistema.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
