package br.com.atelie.application.formatacao;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Formata quantidades inteiras com separador de milhar (1.247).
 */
@Component(FormatadorQuantidade.NOME)
public class FormatadorQuantidade implements FormatadorValor {

    public static final String NOME = "formatadorQuantidade";

    private static final Locale PT_BR = new Locale("pt", "BR");

    @Override
    public String formatar(BigDecimal valor) {
        return NumberFormat.getIntegerInstance(PT_BR).format(valor);
    }
}

