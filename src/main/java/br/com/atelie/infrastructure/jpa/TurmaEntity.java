package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.Turma;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "turma")
public class TurmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 150)
    String nome;

    @Column(nullable = false, length = 100)
    String instrutora;

    @Column(nullable = false, length = 100)
    String horario;

    @Column(name = "vagas_totais", nullable = false)
    int vagasTotais;

    @Column(name = "valor_mensalidade", nullable = false, precision = 12, scale = 2)
    BigDecimal valorMensalidade;

    @Column(nullable = false)
    boolean ativa;

    protected TurmaEntity() {
        // exigido pelo JPA
    }

    static TurmaEntity de(Turma t) {
        TurmaEntity e = new TurmaEntity();
        e.id = t.getId();
        e.nome = t.getNome();
        e.instrutora = t.getInstrutora();
        e.horario = t.getHorario();
        e.vagasTotais = t.getVagasTotais();
        e.valorMensalidade = t.getValorMensalidade();
        e.ativa = t.isAtiva();
        return e;
    }

    Turma paraDominio() {
        return new Turma(id, nome, instrutora, horario, vagasTotais, valorMensalidade, ativa);
    }
}
