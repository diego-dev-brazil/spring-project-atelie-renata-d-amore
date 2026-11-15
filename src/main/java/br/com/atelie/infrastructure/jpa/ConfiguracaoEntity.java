package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.ConfiguracaoAtelie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * Tabela "configuracao_atelie": sempre uma única linha (id = 1).
 */
@Entity
@Table(name = "configuracao_atelie")
public class ConfiguracaoEntity {

    static final Long ID_UNICO = 1L;

    @Id
    Long id;

    @Column(name = "nome_atelie", length = 150)
    String nomeAtelie;

    @Column(length = 150)
    String proprietaria;

    @Column(length = 30)
    String cnpj;

    @Column(length = 30)
    String telefone;

    @Column(name = "email_contato", length = 150)
    String emailContato;

    @Column(length = 255)
    String endereco;

    @Column(name = "meta_vendas_mensal", precision = 12, scale = 2)
    BigDecimal metaVendasMensal;

    @Column(name = "alertas_estoque_ativos", nullable = false)
    boolean alertasEstoqueAtivos;

    protected ConfiguracaoEntity() {
        // exigido pelo JPA
    }

    static ConfiguracaoEntity de(ConfiguracaoAtelie c) {
        ConfiguracaoEntity e = new ConfiguracaoEntity();
        e.id = ID_UNICO;
        e.nomeAtelie = c.getNomeAtelie();
        e.proprietaria = c.getProprietaria();
        e.cnpj = c.getCnpj();
        e.telefone = c.getTelefone();
        e.emailContato = c.getEmailContato();
        e.endereco = c.getEndereco();
        e.metaVendasMensal = c.getMetaVendasMensal();
        e.alertasEstoqueAtivos = c.isAlertasEstoqueAtivos();
        return e;
    }

    ConfiguracaoAtelie paraDominio() {
        return new ConfiguracaoAtelie(nomeAtelie, proprietaria, cnpj, telefone, emailContato, endereco,
                metaVendasMensal, alertasEstoqueAtivos);
    }
}
