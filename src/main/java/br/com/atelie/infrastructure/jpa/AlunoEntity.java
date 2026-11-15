package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.Aluno;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Tabela "aluno". turma_id e turma_nome são colunas simples, como no domínio atual
 * (o Aluno guarda o id e o nome da turma). Uma evolução natural é trocar por uma
 * chave estrangeira real para "turma".
 */
@Entity
@Table(name = "aluno")
public class AlunoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 150)
    String nome;

    @Column(length = 30)
    String telefone;

    @Column(length = 150)
    String email;

    @Column(name = "turma_id")
    Long turmaId;

    @Column(name = "turma_nome", nullable = false, length = 150)
    String turmaNome;

    @Column(name = "parcelas_pendentes", nullable = false)
    int parcelasPendentes;

    protected AlunoEntity() {
        // exigido pelo JPA
    }

    static AlunoEntity de(Aluno a) {
        AlunoEntity e = new AlunoEntity();
        e.id = a.getId();
        e.nome = a.getNome();
        e.telefone = a.getTelefone();
        e.email = a.getEmail();
        e.turmaId = a.getTurmaId();
        e.turmaNome = a.getTurmaNome();
        e.parcelasPendentes = a.getParcelasPendentes();
        return e;
    }

    Aluno paraDominio() {
        return new Aluno(id, nome, telefone, email, turmaId, turmaNome, parcelasPendentes);
    }
}
