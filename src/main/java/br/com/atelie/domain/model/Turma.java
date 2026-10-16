package br.com.atelie.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Representa uma Turma de curso de costura/artesanato oferecida pelo ateliê.
 */
public class Turma {

    private Long id;
    private String nome;
    private String instrutora;
    private String horario;
    private int vagasTotais;
    private BigDecimal valorMensalidade;
    private boolean ativa;

    public Turma(Long id, String nome, String instrutora, String horario, int vagasTotais, BigDecimal valorMensalidade, boolean ativa) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome, "Nome da turma é obrigatório").trim();
        this.instrutora = (instrutora == null || instrutora.isBlank()) ? "Renata D'Amore" : instrutora.trim();
        this.horario = (horario == null || horario.isBlank()) ? "A definir" : horario.trim();
        this.vagasTotais = Math.max(1, vagasTotais);
        this.valorMensalidade = valorMensalidade != null ? valorMensalidade : BigDecimal.ZERO;
        this.ativa = ativa;
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
        this.nome = Objects.requireNonNull(nome, "Nome da turma é obrigatório").trim();
    }

    public String getInstrutora() {
        return instrutora;
    }

    public void setInstrutora(String instrutora) {
        this.instrutora = (instrutora == null || instrutora.isBlank()) ? "Renata D'Amore" : instrutora.trim();
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = (horario == null || horario.isBlank()) ? "A definir" : horario.trim();
    }

    public int getVagasTotais() {
        return vagasTotais;
    }

    public void setVagasTotais(int vagasTotais) {
        this.vagasTotais = Math.max(1, vagasTotais);
    }

    public BigDecimal getValorMensalidade() {
        return valorMensalidade;
    }

    public void setValorMensalidade(BigDecimal valorMensalidade) {
        this.valorMensalidade = valorMensalidade != null ? valorMensalidade : BigDecimal.ZERO;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }
}

