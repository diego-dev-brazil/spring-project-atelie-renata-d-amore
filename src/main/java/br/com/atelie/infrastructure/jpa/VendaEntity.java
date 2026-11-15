package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.Venda;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Tabela "venda". {@link Venda} é um record (imutável), por isso não pode ser a entidade.
 */
@Entity
@Table(name = "venda")
public class VendaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 150)
    String produto;

    @Column(nullable = false, length = 150)
    String cliente;

    @Column(nullable = false, precision = 12, scale = 2)
    BigDecimal valor;

    @Column(name = "data_venda", nullable = false)
    LocalDate data;

    protected VendaEntity() {
        // exigido pelo JPA
    }

    static VendaEntity de(Venda v) {
        VendaEntity e = new VendaEntity();
        e.id = v.id();
        e.produto = v.produto();
        e.cliente = v.cliente();
        e.valor = v.valor();
        e.data = v.data() != null ? v.data() : LocalDate.now();
        return e;
    }

    Venda paraDominio() {
        return new Venda(id, produto, cliente, valor, data);
    }
}
