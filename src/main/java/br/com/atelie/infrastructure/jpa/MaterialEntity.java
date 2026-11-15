package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.Material;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * Representação JPA da tabela "material". Existe só na infraestrutura:
 * o domínio ({@link Material}) continua sem nenhuma anotação de persistência.
 */
@Entity
@Table(name = "material")
public class MaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 150)
    String nome;

    @Column(nullable = false, length = 100)
    String categoria;

    @Column(nullable = false)
    int quantidade;

    @Column(name = "quantidade_minima", nullable = false)
    int quantidadeMinima;

    @Column(name = "unidade_medida", nullable = false, length = 20)
    String unidadeMedida;

    @Column(name = "preco_unitario", nullable = false, precision = 12, scale = 2)
    BigDecimal precoUnitario;

    protected MaterialEntity() {
        // exigido pelo JPA
    }

    static MaterialEntity de(Material m) {
        MaterialEntity e = new MaterialEntity();
        e.id = m.getId();
        e.nome = m.getNome();
        e.categoria = m.getCategoria();
        e.quantidade = m.getQuantidade();
        e.quantidadeMinima = m.getQuantidadeMinima();
        e.unidadeMedida = m.getUnidadeMedida();
        e.precoUnitario = m.getPrecoUnitario();
        return e;
    }

    Material paraDominio() {
        return new Material(id, nome, categoria, quantidade, quantidadeMinima, unidadeMedida, precoUnitario);
    }
}
