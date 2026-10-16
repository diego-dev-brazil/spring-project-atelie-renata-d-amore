package br.com.atelie.web.config;

import br.com.atelie.web.seguranca.AdminAutorizacaoInterceptor;
import br.com.atelie.web.seguranca.AutenticacaoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configura quais URLs exigem login e quais exigem perfil de Administradora.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AutenticacaoInterceptor autenticacaoInterceptor;
    private final AdminAutorizacaoInterceptor adminAutorizacaoInterceptor;

    public WebConfig(AutenticacaoInterceptor autenticacaoInterceptor,
                     AdminAutorizacaoInterceptor adminAutorizacaoInterceptor) {
        this.autenticacaoInterceptor = autenticacaoInterceptor;
        this.adminAutorizacaoInterceptor = adminAutorizacaoInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(autenticacaoInterceptor)
                .addPathPatterns(
                        "/dashboard",
                        "/notificacoes", "/notificacoes/**",
                        "/estoque", "/estoque/**",
                        "/modelos", "/modelos/**",
                        "/producao", "/producao/**",
                        "/vendas", "/vendas/**",
                        "/turmas", "/turmas/**",
                        "/alunos", "/alunos/**",
                        "/relatorios", "/relatorios/**",
                        "/usuarios", "/usuarios/**",
                        "/configuracoes", "/configuracoes/**",
                        "/modulos/**"
                );

        registry.addInterceptor(adminAutorizacaoInterceptor)
                .addPathPatterns(
                        "/relatorios", "/relatorios/**",
                        "/usuarios", "/usuarios/**",
                        "/configuracoes", "/configuracoes/**"
                );
    }
}
