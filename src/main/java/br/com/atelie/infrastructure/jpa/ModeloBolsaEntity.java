package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.ItemFichaTecnica;
import br.com.atelie.domain.model.ModeloBolsa;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Tabela "modelo_bolsa" + tabela "ficha_tecnica" (itens do modelo).
 *
 * A ficha técnica guarda material_id e material_nome como colunas simples (sem chave
 * estrangeira), igual ao domínio atual, que também guarda o nome do material dentro do item.
 */
@Entity
@Table(name = "modelo_bolsa")
public class ModeloBolsaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 150)
    String nome;

    @Column(length = 1000)
    String descricao;

    @Column(nullable = false, length = 100)
    String categoria;

    @Column(name = "preco_venda", nullable = false, precision = 12, scale = 2)
    BigDecimal precoVenda;

    @Column(name = "tempo_producao_minutos", nullable = false)
    int tempoProducaoMinutos;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ficha_tecnica", joinColumns = @JoinColumn(name = "modelo_id"))
    @OrderColumn(name = "posicao")
    List<ItemFicha> fichaTecnica = new ArrayList<>();

    protected ModeloBolsaEntity() {
        // exigido pelo JPA
    }

    @Embeddable
    public static class ItemFicha {

        @Column(name = "material_id", nullable = false)
        Long materialId;

        @Column(name = "material_nome", nullable = false, length = 150)
        String materialNome;

        @Column(name = "quantidade_necessaria", nullable = false)
        int quantidadeNecessaria;

        @Column(name = "unidade_medida", nullable = false, length = 20)
        String unidadeMedida;

        protected ItemFicha() {
            // exigido pelo JPA
        }

        static ItemFicha de(ItemFichaTecnica item) {
            ItemFicha e = new ItemFicha();
            e.materialId = item.getMaterialId();
            e.materialNome = item.getMaterialNome();
            e.quantidadeNecessaria = item.getQuantidadeNecessaria();
            e.unidadeMedida = item.getUnidadeMedida();
            return e;
        }

        ItemFichaTecnica paraDominio() {
            return new ItemFichaTecnica(materialId, materialNome, quantidadeNecessaria, unidadeMedida);
        }
    }

    static ModeloBolsaEntity de(ModeloBolsa m) {
        ModeloBolsaEntity e = new ModeloBolsaEntity();
        e.id = m.getId();
        e.nome = m.getNome();
        e.descricao = m.getDescricao();
        e.categoria = m.getCategoria();
        e.precoVenda = m.getPrecoVenda();
        e.tempoProducaoMinutos = m.getTempoProducaoMinutos();
        for (ItemFichaTecnica item : m.getFichaTecnica()) {
            e.fichaTecnica.add(ItemFicha.de(item));
        }
        return e;
    }

    ModeloBolsa paraDominio() {
        List<ItemFichaTecnica> itens = fichaTecnica.stream().map(ItemFicha::paraDominio).toList();
        return new ModeloBolsa(id, nome, descricao, categoria, precoVenda, tempoProducaoMinutos, itens);
    }
}
