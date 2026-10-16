package br.com.atelie.web.controller;

import br.com.atelie.application.autenticacao.AutenticacaoService;
import br.com.atelie.application.autenticacao.UsuarioLogado;
import br.com.atelie.web.sessao.SessaoUsuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * Tela de login (no React: app/page.tsx).
 *
 * SRP: o controller só cuida de HTTP — recebe o formulário, chama o serviço
 *      e decide para qual página ir. A regra de "senha confere?" está no
 *      {@link AutenticacaoService}.
 */
@Controller
public class LoginController {

    private final AutenticacaoService autenticacaoService;
    private final SessaoUsuario sessaoUsuario;

    public LoginController(AutenticacaoService autenticacaoService, SessaoUsuario sessaoUsuario) {
        this.autenticacaoService = autenticacaoService;
        this.sessaoUsuario = sessaoUsuario;
    }

    @GetMapping("/")
    public String telaLogin(HttpSession sessao) {
        if (sessaoUsuario.usuarioAtual(sessao).isPresent()) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String entrar(@RequestParam(defaultValue = "") String usuario,
                         @RequestParam(defaultValue = "") String senha,
                         HttpServletRequest request,
                         Model model) {
        Optional<UsuarioLogado> autenticado = autenticacaoService.autenticar(usuario, senha);

        if (autenticado.isEmpty()) {
            model.addAttribute("erro", "Usuário ou senha inválidos.");
            model.addAttribute("usuarioDigitado", usuario);
            return "login";
        }

        // Troca o ID da sessão após o login (evita "session fixation")
        HttpSession antiga = request.getSession(false);
        if (antiga != null) {
            antiga.invalidate();
        }
        sessaoUsuario.iniciar(request.getSession(true), autenticado.get());
        return "redirect:/dashboard";
    }

    @PostMapping("/logout")
    public String sair(HttpSession sessao) {
        sessaoUsuario.encerrar(sessao);
        return "redirect:/";
    }
}

