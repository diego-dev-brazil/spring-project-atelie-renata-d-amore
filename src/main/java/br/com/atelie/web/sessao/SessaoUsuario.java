package br.com.atelie.web.sessao;

import br.com.atelie.application.autenticacao.UsuarioLogado;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Único lugar que sabe COMO o usuário logado é guardado na sessão HTTP.
 *
 * SRP: controllers e interceptor não manipulam atributos de sessão "na mão";
 *      se um dia trocarmos sessão por token JWT, só esta classe muda.
 */
@Component
public class SessaoUsuario {

    private static final String CHAVE = "usuarioLogado";

    public void iniciar(HttpSession sessao, UsuarioLogado usuario) {
        sessao.setAttribute(CHAVE, usuario);
    }

    public Optional<UsuarioLogado> usuarioAtual(HttpSession sessao) {
        if (sessao == null) {
            return Optional.empty();
        }
        return Optional.ofNullable((UsuarioLogado) sessao.getAttribute(CHAVE));
    }

    public void encerrar(HttpSession sessao) {
        if (sessao != null) {
            sessao.invalidate();
        }
    }
}

