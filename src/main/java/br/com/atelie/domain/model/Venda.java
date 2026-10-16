package br.com.atelie.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Uma venda realizada pelo ateliê.
 * No React era um objeto solto dentro do array {@code mockRecentSales}.
 */
public record Venda(Long id, String produto, String cliente, BigDecimal valor, LocalDate data) {
}

