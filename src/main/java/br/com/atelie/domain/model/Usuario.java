package br.com.atelie.domain.model;

import java.util.Objects;

/**
 * Usuário do sistema (administradora ou funcionária).
 *
 * Guarda a senha já codificada (hash), nunca a senha em texto puro.
 * Possui flag {@code ativo} para que a administradora possa liberar ou bloquear
 * o acesso de funcionários ao sistema sem necessariamente apagar o histórico.
 */
public class Usuario {

    private final String login;
    private String nome;
    private String cargo;
    private String senhaCodificada;
    private Perfil perfil;
    private boolean ativo;

    public Usuario(String login, String nome, String senhaCodificada, Perfil perfil) {
        this(login, nome, perfil == Perfil.ADMIN ? "Gestora / Proprietária" : "Costureira / Atendente", senhaCodificada, perfil, true);
    }

    public Usuario(String login, String nome, String cargo, String senhaCodificada, Perfil perfil, boolean ativo) {
        this.login = Objects.requireNonNull(login, "login é obrigatório").trim().toLowerCase();
        this.nome = Objects.requireNonNull(nome, "nome é obrigatório").trim();
        this.cargo = (cargo == null || cargo.isBlank()) ? "Funcionária" : cargo.trim();
        this.senhaCodificada = Objects.requireNonNull(senhaCodificada, "senha é obrigatória");
        this.perfil = Objects.requireNonNull(perfil, "perfil é obrigatório");
        this.ativo = ativo;
    }

    public String getLogin() {
        return login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "nome é obrigatório").trim();
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = (cargo == null || cargo.isBlank()) ? "Funcionária" : cargo.trim();
    }

    public String getSenhaCodificada() {
        return senhaCodificada;
    }

    public void setSenhaCodificada(String senhaCodificada) {
        this.senhaCodificada = Objects.requireNonNull(senhaCodificada, "senha é obrigatória");
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = Objects.requireNonNull(perfil, "perfil é obrigatório");
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isAdmin() {
        return perfil == Perfil.ADMIN;
    }
}
