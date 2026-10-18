package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.OrdemProducao;
import br.com.atelie.domain.model.OrdemProducao.StatusProducao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "ordem_producao")
public class OrdemProducaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "modelo_id", nullable = false)
    Long modeloId;

    @Column(name = "modelo_nome", nullable = false, length = 150)
    String modeloNome;

    @Column(name = "quantidade_pecas", nullable = false)
    int quantidadePecas;

    @Column(nullable = false, length = 100)
    String responsavel;

    @Column(name = "data_pedido", nullable = false)
    LocalDate dataPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    StatusProducao status;

    protected OrdemProducaoEntity() {
        // exigido pelo JPA
    }

    static OrdemProducaoEntity de(OrdemProducao o) {
        OrdemProducaoEntity e = new OrdemProducaoEntity();
        e.id = o.getId();
        e.modeloId = o.getModeloId();
        e.modeloNome = o.getModeloNome();
        e.quantidadePecas = o.getQuantidadePecas();
        e.responsavel = o.getResponsavel();
        e.dataPedido = o.getDataPedido();
        e.status = o.getStatus();
        return e;
    }

    OrdemProducao paraDominio() {
        return new OrdemProducao(id, modeloId, modeloNome, quantidadePecas, responsavel, dataPedido, status);
    }
}
