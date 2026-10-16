package br.com.atelie.application.dashboard.dto;

/**
 * Uma barra do gráfico "Vendas por Mês", com a altura já calculada.
 */
public record BarraGrafico(String mes, int alturaPx, String valorFormatado) {
}

