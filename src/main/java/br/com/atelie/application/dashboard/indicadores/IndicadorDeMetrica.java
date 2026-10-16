package br.com.atelie.application.dashboard.indicadores;

import br.com.atelie.application.dashboard.IndicadorDashboard;
import br.com.atelie.application.dashboard.dto.CartaoIndicador;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.domain.model.Metrica;
import br.com.atelie.domain.model.TipoMetrica;
import br.com.atelie.domain.repository.MetricaRepository;

/**
 * Base reaproveitável para indicadores que só leem uma {@link Metrica}
 * do {@link MetricaRepository} e a formatam.
 *
 * As subclasses só dizem "qual métrica", "qual título", "qual ícone" e
 * "qual formatador". O algoritmo de gerar o cartão fica aqui
 * (padrão Template Method).
 *
 * LSP: toda subclasse continua cumprindo o contrato de IndicadorDashboard
 *      sem surpresas — por isso o DashboardService pode tratá-las igual.
 */
public abstract class IndicadorDeMetrica implements IndicadorDashboard {

    private final MetricaRepository metricaRepository;
    private final FormatadorValor formatador;

    protected IndicadorDeMetrica(MetricaRepository metricaRepository, FormatadorValor formatador) {
        this.metricaRepository = metricaRepository;
        this.formatador = formatador;
    }

    protected abstract TipoMetrica tipo();

    protected abstract String titulo();

    protected abstract String icone();

    @Override
    public final CartaoIndicador gerarCartao() {
        Metrica metrica = metricaRepository.buscar(tipo());
        return new CartaoIndicador(titulo(), formatador.formatar(metrica.valor()), icone(), metrica.variacao());
    }
}

