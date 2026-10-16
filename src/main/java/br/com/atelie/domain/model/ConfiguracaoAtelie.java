package br.com.atelie.domain.model;

import java.math.BigDecimal;

/**
 * Configurações gerais e parâmetros operacionais do Ateliê.
 */
public class ConfiguracaoAtelie {

    private String nomeAtelie;
    private String proprietaria;
    private String cnpj;
    private String telefone;
    private String emailContato;
    private String endereco;
    private BigDecimal metaVendasMensal;
    private boolean alertasEstoqueAtivos;

    public ConfiguracaoAtelie(String nomeAtelie, String proprietaria, String cnpj, String telefone,
                              String emailContato, String endereco, BigDecimal metaVendasMensal, boolean alertasEstoqueAtivos) {
        this.nomeAtelie = nomeAtelie;
        this.proprietaria = proprietaria;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.emailContato = emailContato;
        this.endereco = endereco;
        this.metaVendasMensal = metaVendasMensal != null ? metaVendasMensal : new BigDecimal("50000.00");
        this.alertasEstoqueAtivos = alertasEstoqueAtivos;
    }

    public String getNomeAtelie() { return nomeAtelie; }
    public void setNomeAtelie(String nomeAtelie) { this.nomeAtelie = nomeAtelie; }

    public String getProprietaria() { return proprietaria; }
    public void setProprietaria(String proprietaria) { this.proprietaria = proprietaria; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmailContato() { return emailContato; }
    public void setEmailContato(String emailContato) { this.emailContato = emailContato; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public BigDecimal getMetaVendasMensal() { return metaVendasMensal; }
    public void setMetaVendasMensal(BigDecimal metaVendasMensal) { this.metaVendasMensal = metaVendasMensal; }

    public boolean isAlertasEstoqueAtivos() { return alertasEstoqueAtivos; }
    public void setAlertasEstoqueAtivos(boolean alertasEstoqueAtivos) { this.alertasEstoqueAtivos = alertasEstoqueAtivos; }
}

