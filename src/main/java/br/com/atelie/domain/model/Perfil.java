package br.com.atelie.domain.model;

/**
 * Perfis de acesso do sistema.
 * No React era o tipo {@code UserRole = "admin" | "employee"}.
 */
public enum Perfil {

    ADMIN("Administradora"),
    FUNCIONARIO("Funcionária");

    private final String descricao;

    Perfil(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

