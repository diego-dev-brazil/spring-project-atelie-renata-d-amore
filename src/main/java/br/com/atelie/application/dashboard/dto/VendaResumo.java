package br.com.atelie.application.dashboard.dto;

/**
 * Linha da tabela "Vendas Recentes", com valores já formatados.
 */
public record VendaResumo(String produto, String cliente, String valor, String data) {
}

