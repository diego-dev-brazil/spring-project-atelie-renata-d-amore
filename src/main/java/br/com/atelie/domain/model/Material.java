package br.com.atelie.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidade que representa um Material / Insumo no estoque do ateliê.
 *
 * Ex: Tecido Oxford Azul, Linha branca, Zíper dourado, etc.
 */
public class Material {

    private Long id;
    private String nome;
    private String categoria;
    private int quantidade;
    private int quantidadeMinima;
    private String unidadeMedida; // metros, unidades, rolos, kg
    private BigDecimal precoUnitario;

    public Material(Long id, String nome, String categoria, int quantidade, int quantidadeMinima, String unidadeMedida, BigDecimal precoUnitario) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome, "Nome do material é obrigatório").trim();
        this.categoria = Objects.requireNonNull(categoria, "Categoria é obrigatória").trim();
        this.quantidade = Math.max(0, quantidade);
        this.quantidadeMinima = Math.max(0, quantidadeMinima);
        this.unidadeMedida = (unidadeMedida == null || unidadeMedida.isBlank()) ? "un" : unidadeMedida.trim();
        this.precoUnitario = precoUnitario != null ? precoUnitario : BigDecimal.ZERO;
    }

    public boolean isEstoqueBaixo() {
        return this.quantidade <= this.quantidadeMinima;
    }

    // Getters e Setters para edição
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
        this.nome = Objects.requireNonNull(nome, "Nome do material é obrigatório").trim();
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = Objects.requireNonNull(categoria, "Categoria é obrigatória").trim();
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = Math.max(0, quantidade);
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = Math.max(0, quantidadeMinima);
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = (unidadeMedida == null || unidadeMedida.isBlank()) ? "un" : unidadeMedida.trim();
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario != null ? precoUnitario : BigDecimal.ZERO;
    }
}

