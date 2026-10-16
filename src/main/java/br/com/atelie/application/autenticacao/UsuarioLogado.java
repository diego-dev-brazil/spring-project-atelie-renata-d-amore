package br.com.atelie.application.autenticacao;

import br.com.atelie.domain.model.Perfil;

import java.io.Serializable;

/**
 * Dados mínimos do usuário autenticado que ficam guardados na sessão HTTP.
 * Não carrega a senha — só o necessário para a tela.
 */
public record UsuarioLogado(String login, String nome, Perfil perfil) implements Serializable {

    public String getIniciais() {
        return nome.isBlank() ? "?" : nome.substring(0, 1).toUpperCase();
    }

    public boolean isAdmin() {
        return perfil == Perfil.ADMIN;
    }
}

