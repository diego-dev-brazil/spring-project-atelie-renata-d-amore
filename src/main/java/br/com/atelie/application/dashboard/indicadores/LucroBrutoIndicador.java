package br.com.atelie.application.dashboard.indicadores;

import br.com.atelie.application.dashboard.IndicadorDashboard;
import br.com.atelie.application.dashboard.dto.CartaoIndicador;
import br.com.atelie.application.formatacao.FormatadorMoeda;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.domain.model.Variacao;
import br.com.atelie.domain.model.VendaMensal;
import br.com.atelie.domain.repository.VendaConsultaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Cartão "Lucro Bruto Mensal" calculado dinamicamente sobre o faturamento do mês (margem de 71%).
 */
@Component
public class LucroBrutoIndicador implements IndicadorDashboard {

    private static final BigDecimal MARGEM_LUCRO_BRUTO = new BigDecimal("0.7103765");

    private final VendaConsultaRepository vendaConsulta;
    private final FormatadorValor formatadorMoeda;

    public LucroBrutoIndicador(VendaConsultaRepository vendaConsulta,
                               @Qualifier(FormatadorMoeda.NOME) FormatadorValor formatadorMoeda) {
        this.vendaConsulta = vendaConsulta;
        this.formatadorMoeda = formatadorMoeda;
    }

    @Override
    public int ordem() {
        return 6;
    }

    @Override
    public CartaoIndicador gerarCartao() {
        List<VendaMensal> meses = vendaConsulta.totaisPorMes();
        BigDecimal faturamentoAtual = meses.isEmpty() ? BigDecimal.ZERO : meses.get(meses.size() - 1).total();
        BigDecimal lucroBruto = faturamentoAtual.multiply(MARGEM_LUCRO_BRUTO).setScale(0, RoundingMode.HALF_UP);

        return new CartaoIndicador(
                "Lucro Bruto Mensal",
                formatadorMoeda.formatar(lucroBruto),
                "trending-up",
                Variacao.de(10)
        );
    }
}
