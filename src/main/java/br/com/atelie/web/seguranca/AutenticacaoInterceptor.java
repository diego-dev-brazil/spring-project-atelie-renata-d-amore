package br.com.atelie.web.seguranca;

import br.com.atelie.web.sessao.SessaoUsuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Barra o acesso às páginas internas de quem não fez login,
 * redirecionando para a tela de login.
 *
 * SRP: a regra "precisa estar logado" fica num lugar só, em vez de ser
 *      repetida em cada controller.
 */
@Component
public class AutenticacaoInterceptor implements HandlerInterceptor {

    private final SessaoUsuario sessaoUsuario;

    public AutenticacaoInterceptor(SessaoUsuario sessaoUsuario) {
        this.sessaoUsuario = sessaoUsuario;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (sessaoUsuario.usuarioAtual(request.getSession(false)).isPresent()) {
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/");
        return false;
    }
}

