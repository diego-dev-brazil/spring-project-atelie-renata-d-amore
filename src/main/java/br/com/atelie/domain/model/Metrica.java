package br.com.atelie.domain.model;

import java.math.BigDecimal;

/**
 * Um número do negócio (ex.: total de materiais) e quanto ele variou
 * em relação ao mês anterior, em porcentagem.
 */
public record Metrica(BigDecimal valor, Variacao variacao) {
}

