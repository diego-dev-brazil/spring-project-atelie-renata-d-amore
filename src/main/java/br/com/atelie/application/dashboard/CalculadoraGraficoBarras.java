package br.com.atelie.application.dashboard;

import br.com.atelie.application.dashboard.dto.BarraGrafico;
import br.com.atelie.application.formatacao.FormatadorMoeda;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.domain.model.VendaMensal;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Calcula a altura (em pixels) de cada barra do gráfico de vendas.
 *
 * SRP: no React essa conta ({@code (value / max) * 200}) ficava no meio do
 *      JSX. Agora é uma classe pequena, fácil de testar isoladamente.
 */
@Component
public class CalculadoraGraficoBarras {

    static final int ALTURA_MAXIMA_PX = 200;

    private final FormatadorValor formatadorMoeda;

    public CalculadoraGraficoBarras(@Qualifier(FormatadorMoeda.NOME) FormatadorValor formatadorMoeda) {
        this.formatadorMoeda = formatadorMoeda;
    }

    public List<BarraGrafico> calcular(List<VendaMensal> totais) {
        BigDecimal maior = totais.stream()
                .map(VendaMensal::total)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return totais.stream()
                .map(mes -> new BarraGrafico(mes.mes(), altura(mes.total(), maior), formatadorMoeda.formatar(mes.total())))
                .toList();
    }

    private int altura(BigDecimal valor, BigDecimal maior) {
        if (maior.signum() == 0) {
            return 0;
        }
        return valor.multiply(BigDecimal.valueOf(ALTURA_MAXIMA_PX))
                .divide(maior, 0, RoundingMode.HALF_UP)
                .intValue();
    }
}

