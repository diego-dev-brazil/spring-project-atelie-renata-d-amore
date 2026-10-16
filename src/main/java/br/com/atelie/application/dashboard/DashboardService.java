package br.com.atelie.application.dashboard;

import br.com.atelie.application.dashboard.dto.PainelDashboard;
import br.com.atelie.application.dashboard.dto.VendaResumo;
import br.com.atelie.application.formatacao.FormatadorMoeda;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.domain.model.Venda;
import br.com.atelie.domain.repository.AlertaRepository;
import br.com.atelie.domain.repository.VendaConsultaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

/**
 * Caso de uso: "montar o painel inicial".
 *
 * SRP: só ORQUESTRA — pede os dados para quem sabe buscá-los e devolve tudo
 *      pronto num {@link PainelDashboard}. Não sabe HTML nem HTTP.
 * OCP: recebe {@code List<IndicadorDashboard>}; novos cartões entram sem
 *      alterar esta classe.
 * ISP: depende de {@link VendaConsultaRepository} (só leitura), e não de um
 *      repositório "gordo" com métodos de gravar que ela nunca usaria.
 * DIP: todas as dependências são interfaces, recebidas pelo construtor.
 */
@Service
public class DashboardService {

    private static final int QUANTIDADE_VENDAS_RECENTES = 5;
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final List<IndicadorDashboard> indicadores;
    private final VendaConsultaRepository vendaConsulta;
    private final AlertaRepository alertaRepository;
    private final CalculadoraGraficoBarras calculadoraGrafico;
    private final FormatadorValor formatadorMoeda;

    public DashboardService(List<IndicadorDashboard> indicadores,
                            VendaConsultaRepository vendaConsulta,
                            AlertaRepository alertaRepository,
                            CalculadoraGraficoBarras calculadoraGrafico,
                            @Qualifier(FormatadorMoeda.NOME) FormatadorValor formatadorMoeda) {
        this.indicadores = indicadores;
        this.vendaConsulta = vendaConsulta;
        this.alertaRepository = alertaRepository;
        this.calculadoraGrafico = calculadoraGrafico;
        this.formatadorMoeda = formatadorMoeda;
    }

    public PainelDashboard montarPainel() {
        return new PainelDashboard(
                indicadores.stream()
                        .sorted(Comparator.comparingInt(IndicadorDashboard::ordem))
                        .map(IndicadorDashboard::gerarCartao)
                        .toList(),
                calculadoraGrafico.calcular(vendaConsulta.totaisPorMes()),
                alertaRepository.buscarAtivos(),
                vendaConsulta.buscarRecentes(QUANTIDADE_VENDAS_RECENTES).stream()
                        .map(this::paraResumo)
                        .toList()
        );
    }

    private VendaResumo paraResumo(Venda venda) {
        return new VendaResumo(
                venda.produto(),
                venda.cliente(),
                formatadorMoeda.formatar(venda.valor()),
                venda.data().format(FORMATO_DATA)
        );
    }
}

