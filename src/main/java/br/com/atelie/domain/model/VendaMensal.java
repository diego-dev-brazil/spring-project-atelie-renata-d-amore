package br.com.atelie.domain.model;

import java.math.BigDecimal;

/**
 * Total vendido em um mês (usado no gráfico "Vendas por Mês").
 */
public record VendaMensal(String mes, BigDecimal total) {
}

