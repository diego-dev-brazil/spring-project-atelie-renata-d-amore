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
 * Cartão "Vendas do Mês".
 *
 * Diferente dos outros, este NÃO herda de {@link IndicadorDeMetrica}: ele
 * calcula o total e a variação a partir do histórico real de vendas.
 * Mesmo assim, cumpre o mesmo contrato {@link IndicadorDashboard} — e o
 * DashboardService não percebe diferença nenhuma. Isso é o LSP na prática.
 */
@Component
public class VendasDoMesIndicador implements IndicadorDashboard {

    private final VendaConsultaRepository vendaConsulta;
    private final FormatadorValor formatadorMoeda;

    public VendasDoMesIndicador(VendaConsultaRepository vendaConsulta,
                                @Qualifier(FormatadorMoeda.NOME) FormatadorValor formatadorMoeda) {
        this.vendaConsulta = vendaConsulta;
        this.formatadorMoeda = formatadorMoeda;
    }

    @Override
    public int ordem() {
        return 3;
    }

    @Override
    public CartaoIndicador gerarCartao() {
        List<VendaMensal> meses = vendaConsulta.totaisPorMes();
        BigDecimal atual = meses.isEmpty() ? BigDecimal.ZERO : meses.get(meses.size() - 1).total();
        BigDecimal anterior = meses.size() < 2 ? BigDecimal.ZERO : meses.get(meses.size() - 2).total();

        return new CartaoIndicador("Vendas do Mês", formatadorMoeda.formatar(atual), "shopping-cart",
                calcularVariacao(atual, anterior));
    }

    private Variacao calcularVariacao(BigDecimal atual, BigDecimal anterior) {
        if (anterior.signum() == 0) {
            return new Variacao(BigDecimal.ZERO);
        }
        BigDecimal percentual = atual.subtract(anterior)
                .multiply(BigDecimal.valueOf(100))
                .divide(anterior, 0, RoundingMode.HALF_UP);
        return new Variacao(percentual);
    }
}

