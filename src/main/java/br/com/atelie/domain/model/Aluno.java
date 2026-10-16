package br.com.atelie.domain.model;

import java.util.Objects;

/**
 * Representa um Aluno(a) matriculado(a) em uma turma do ateliê.
 */
public class Aluno {

    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private Long turmaId;
    private String turmaNome;
    private int parcelasPendentes;

    public Aluno(Long id, String nome, String telefone, String email, Long turmaId, String turmaNome, int parcelasPendentes) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome, "Nome do aluno é obrigatório").trim();
        this.telefone = telefone != null ? telefone.trim() : "";
        this.email = email != null ? email.trim() : "";
        this.turmaId = turmaId;
        this.turmaNome = (turmaNome == null || turmaNome.isBlank()) ? "Sem turma" : turmaNome.trim();
        this.parcelasPendentes = Math.max(0, parcelasPendentes);
    }

    public boolean isInadimplente() {
        return parcelasPendentes > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "Nome do aluno é obrigatório").trim();
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone != null ? telefone.trim() : "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim() : "";
    }

    public Long getTurmaId() {
        return turmaId;
    }

    public void setTurmaId(Long turmaId) {
        this.turmaId = turmaId;
    }

    public String getTurmaNome() {
        return turmaNome;
    }

    public void setTurmaNome(String turmaNome) {
        this.turmaNome = (turmaNome == null || turmaNome.isBlank()) ? "Sem turma" : turmaNome.trim();
    }

    public int getParcelasPendentes() {
        return parcelasPendentes;
    }

    public void setParcelasPendentes(int parcelasPendentes) {
        this.parcelasPendentes = Math.max(0, parcelasPendentes);
    }
}

