package br.com.atelie.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa uma Ordem de Produção de um Modelo de Bolsa no ateliê.
 */
public class OrdemProducao {

    public enum StatusProducao {
        EM_ANDAMENTO("Em Andamento"),
        CONCLUIDA("Concluída"),
        AGUARDANDO_MATERIAIS("Aguardando Materiais");

        private final String descricao;

        StatusProducao(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }

    private Long id;
    private Long modeloId;
    private String modeloNome;
    private int quantidadePecas;
    private String responsavel;
    private LocalDate dataPedido;
    private StatusProducao status;

    public OrdemProducao(Long id, Long modeloId, String modeloNome, int quantidadePecas,
                         String responsavel, LocalDate dataPedido, StatusProducao status) {
        this.id = id;
        this.modeloId = Objects.requireNonNull(modeloId, "Modelo é obrigatório");
        this.modeloNome = Objects.requireNonNull(modeloNome, "Nome do modelo é obrigatório").trim();
        this.quantidadePecas = Math.max(1, quantidadePecas);
        this.responsavel = (responsavel == null || responsavel.isBlank()) ? "Equipe do Ateliê" : responsavel.trim();
        this.dataPedido = dataPedido != null ? dataPedido : LocalDate.now();
        this.status = status != null ? status : StatusProducao.EM_ANDAMENTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getModeloId() {
        return modeloId;
    }

    public String getModeloNome() {
        return modeloNome;
    }

    public int getQuantidadePecas() {
        return quantidadePecas;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public StatusProducao getStatus() {
        return status;
    }

    public void setStatus(StatusProducao status) {
        this.status = Objects.requireNonNull(status);
    }

    public boolean isConcluida() {
        return status == StatusProducao.CONCLUIDA;
    }
}

