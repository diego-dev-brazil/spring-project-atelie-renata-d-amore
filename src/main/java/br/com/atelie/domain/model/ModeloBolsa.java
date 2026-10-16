package br.com.atelie.domain.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade que representa um Modelo de Bolsa.
 *
 * Contém informações do produto e sua Ficha Técnica (BOM - Bill of Materials),
 * detalhando quais materiais e em que quantidades são necessários para produzi-lo.
 */
public class ModeloBolsa {

    private Long id;
    private String nome;
    private String descricao;
    private String categoria;
    private BigDecimal precoVenda;
    private int tempoProducaoMinutos;
    private List<ItemFichaTecnica> fichaTecnica;

    public ModeloBolsa(Long id, String nome, String descricao, String categoria, BigDecimal precoVenda, int tempoProducaoMinutos, List<ItemFichaTecnica> fichaTecnica) {
        this.id = id;
        this.nome = Objects.requireNonNull(nome, "Nome do modelo é obrigatório").trim();
        this.descricao = descricao != null ? descricao.trim() : "";
        this.categoria = Objects.requireNonNull(categoria, "Categoria é obrigatória").trim();
        this.precoVenda = precoVenda != null ? precoVenda : BigDecimal.ZERO;
        this.tempoProducaoMinutos = Math.max(0, tempoProducaoMinutos);
        this.fichaTecnica = fichaTecnica != null ? new ArrayList<>(fichaTecnica) : new ArrayList<>();
    }

    public void adicionarMaterial(ItemFichaTecnica item) {
        this.fichaTecnica.removeIf(i -> i.getMaterialId().equals(item.getMaterialId()));
        this.fichaTecnica.add(item);
    }

    public void removerMaterial(Long materialId) {
        this.fichaTecnica.removeIf(i -> i.getMaterialId().equals(materialId));
    }

    // Getters e Setters
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
        this.nome = Objects.requireNonNull(nome, "Nome do modelo é obrigatório").trim();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao != null ? descricao.trim() : "";
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = Objects.requireNonNull(categoria, "Categoria é obrigatória").trim();
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda != null ? precoVenda : BigDecimal.ZERO;
    }

    public int getTempoProducaoMinutos() {
        return tempoProducaoMinutos;
    }

    public void setTempoProducaoMinutos(int tempoProducaoMinutos) {
        this.tempoProducaoMinutos = Math.max(0, tempoProducaoMinutos);
    }

    public List<ItemFichaTecnica> getFichaTecnica() {
        return fichaTecnica;
    }

    public void setFichaTecnica(List<ItemFichaTecnica> fichaTecnica) {
        this.fichaTecnica = fichaTecnica != null ? new ArrayList<>(fichaTecnica) : new ArrayList<>();
    }
}

