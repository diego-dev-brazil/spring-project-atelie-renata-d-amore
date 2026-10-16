package br.com.atelie.domain.model;

import java.math.BigDecimal;

/**
 * Variação percentual de um indicador (ex.: +12%, -3%, 0%).
 *
 * A regra de "é positiva? é negativa? como escrevo isso?" mora aqui,
 * em vez de ficar espalhada no HTML como estava no React.
 */
public record Variacao(BigDecimal percentual) {

    public static Variacao de(double percentual) {
        return new Variacao(BigDecimal.valueOf(percentual));
    }

    public boolean isPositiva() {
        return percentual.signum() > 0;
    }

    public boolean isNegativa() {
        return percentual.signum() < 0;
    }

    public String getTexto() {
        String sinal = isPositiva() ? "+" : "";
        return sinal + percentual.stripTrailingZeros().toPlainString() + "%";
    }
}

