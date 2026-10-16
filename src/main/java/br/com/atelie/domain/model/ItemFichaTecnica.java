package br.com.atelie.domain.model;

import java.util.Objects;

/**
 * Item de composição da ficha técnica do modelo (Material + Quantidade necessária).
 * Exemplo: 2 metros de Tecido Oxford Azul, 1 unidade de Zíper Dourado 30cm.
 */
public class ItemFichaTecnica {

    private Long materialId;
    private String materialNome;
    private int quantidadeNecessaria;
    private String unidadeMedida;

    public ItemFichaTecnica(Long materialId, String materialNome, int quantidadeNecessaria, String unidadeMedida) {
        this.materialId = Objects.requireNonNull(materialId, "materialId é obrigatório");
        this.materialNome = Objects.requireNonNull(materialNome, "materialNome é obrigatório").trim();
        this.quantidadeNecessaria = Math.max(1, quantidadeNecessaria);
        this.unidadeMedida = (unidadeMedida == null || unidadeMedida.isBlank()) ? "un" : unidadeMedida.trim();
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getMaterialNome() {
        return materialNome;
    }

    public void setMaterialNome(String materialNome) {
        this.materialNome = materialNome;
    }

    public int getQuantidadeNecessaria() {
        return quantidadeNecessaria;
    }

    public void setQuantidadeNecessaria(int quantidadeNecessaria) {
        this.quantidadeNecessaria = Math.max(1, quantidadeNecessaria);
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
}

