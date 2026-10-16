package br.com.atelie.application.formatacao;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Formata valores como moeda brasileira (R$ 1.234,56).
 * No React era a função {@code formatCurrency} dentro do componente.
 */
@Component(FormatadorMoeda.NOME)
public class FormatadorMoeda implements FormatadorValor {

    public static final String NOME = "formatadorMoeda";

    private static final Locale PT_BR = new Locale("pt", "BR");

    @Override
    public String formatar(BigDecimal valor) {
        // NumberFormat não é thread-safe, por isso criamos um a cada chamada
        return NumberFormat.getCurrencyInstance(PT_BR).format(valor);
    }
}

