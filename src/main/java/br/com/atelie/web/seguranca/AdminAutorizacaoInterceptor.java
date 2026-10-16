package br.com.atelie.web.seguranca;

import br.com.atelie.application.autenticacao.UsuarioLogado;
import br.com.atelie.web.sessao.SessaoUsuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

/**
 * Interceptor que protege rotas exclusivas da Administradora
 * (/usuarios, /relatorios, /configuracoes).
 *
 * SRP: A verificação de autorização por perfil fica isolada aqui, sem
 *      poluir os controllers.
 */
@Component
public class AdminAutorizacaoInterceptor implements HandlerInterceptor {

    private final SessaoUsuario sessaoUsuario;

    public AdminAutorizacaoInterceptor(SessaoUsuario sessaoUsuario) {
        this.sessaoUsuario = sessaoUsuario;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Optional<UsuarioLogado> usuarioOpt = sessaoUsuario.usuarioAtual(request.getSession(false));
        if (usuarioOpt.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return false;
        }
        if (!usuarioOpt.get().isAdmin()) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Acesso restrito à administração");
            return false;
        }
        return true;
    }
}

