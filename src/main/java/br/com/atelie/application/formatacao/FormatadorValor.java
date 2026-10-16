package br.com.atelie.application.formatacao;

import java.math.BigDecimal;

/**
 * Estratégia de formatação de um valor numérico para exibição.
 *
 * OCP / Strategy: para criar um novo formato (porcentagem, peso em kg...)
 * basta criar uma nova implementação — nenhuma classe existente é alterada.
 */
public interface FormatadorValor {

    String formatar(BigDecimal valor);
}

