package br.com.atelie.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teste de ponta a ponta: sobe a aplicação inteira e simula o navegador.
 */
@SpringBootTest
@AutoConfigureMockMvc
class FluxoLoginDashboardTest {

    @Autowired
    private MockMvc mvc;

    private MockHttpSession logar(String usuario, String senha) throws Exception {
        MvcResult resultado = mvc.perform(post("/login").param("usuario", usuario).param("senha", senha))
                .andExpect(redirectedUrl("/dashboard"))
                .andReturn();
        return (MockHttpSession) resultado.getRequest().getSession(false);
    }

    @Test
    void paginaDeLoginAbre() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Digite seu usuário")));
    }

    @Test
    void dashboardSemLoginRedirecionaParaLogin() throws Exception {
        mvc.perform(get("/dashboard")).andExpect(redirectedUrl("/"));
    }

    @Test
    void senhaErradaMostraMensagem() throws Exception {
        mvc.perform(post("/login").param("usuario", "admin").param("senha", "xxx"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Usuário ou senha inválidos")));
    }

    @Test
    void adminVeDashboardCompleto() throws Exception {
        MockHttpSession sessao = logar("admin", "admin123");

        mvc.perform(get("/dashboard").session(sessao))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Renata D&#39;Amore")))
                .andExpect(content().string(containsString("Materiais em Estoque")))
                .andExpect(content().string(containsString("1.247")))
                .andExpect(content().string(containsString("Bolsa Artesanal Premium")))
                .andExpect(content().string(containsString("Relatórios")));
    }

    @Test
    void funcionariaNaoVeNemAcessaAreaAdmin() throws Exception {
        MockHttpSession sessao = logar("funcionaria", "func123");

        mvc.perform(get("/dashboard").session(sessao))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Relatórios"))));

        mvc.perform(get("/modulos/relatorios").session(sessao))
                .andExpect(status().isForbidden());

        mvc.perform(get("/estoque").session(sessao))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Estoque de Materiais")));
    }

    @Test
    void logoutEncerraSessao() throws Exception {
        MockHttpSession sessao = logar("admin", "admin123");

        mvc.perform(post("/logout").session(sessao)).andExpect(redirectedUrl("/"));
        mvc.perform(get("/dashboard").session(sessao)).andExpect(redirectedUrl("/"));
    }
}

