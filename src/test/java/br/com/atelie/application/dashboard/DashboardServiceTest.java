package br.com.atelie.application.dashboard;

import br.com.atelie.application.dashboard.dto.BarraGrafico;
import br.com.atelie.application.dashboard.dto.CartaoIndicador;
import br.com.atelie.application.dashboard.dto.PainelDashboard;
import br.com.atelie.application.formatacao.FormatadorMoeda;
import br.com.atelie.domain.model.Variacao;
import br.com.atelie.domain.model.VendaMensal;
import br.com.atelie.domain.repository.VendaConsultaRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DashboardServiceTest {

    private final FormatadorMoeda moeda = new FormatadorMoeda();

    private final VendaConsultaRepository vendasFalsas = new VendaConsultaRepository() {
        @Override
        public List<br.com.atelie.domain.model.Venda> buscarRecentes(int quantidade) {
            return List.of();
        }

        @Override
        public List<VendaMensal> totaisPorMes() {
            return List.of(new VendaMensal("Jan", new BigDecimal("50")), new VendaMensal("Fev", new BigDecimal("100")));
        }
    };

    /** Indicador criado só para o teste: prova que qualquer implementação "encaixa" (OCP + LSP). */
    private static IndicadorDashboard indicadorFixo(int ordem, String titulo) {
        return new IndicadorDashboard() {
            @Override public int ordem() { return ordem; }
            @Override public CartaoIndicador gerarCartao() {
                return new CartaoIndicador(titulo, "1", "box", Variacao.de(0));
            }
        };
    }

    @Test
    void ordenaCartoesPelaOrdemDeclarada() {
        DashboardService service = new DashboardService(
                List.of(indicadorFixo(2, "B"), indicadorFixo(1, "A"), indicadorFixo(3, "C")),
                vendasFalsas, List::of, new CalculadoraGraficoBarras(moeda), moeda);

        PainelDashboard painel = service.montarPainel();

        assertEquals(List.of("A", "B", "C"), painel.cartoes().stream().map(CartaoIndicador::titulo).toList());
    }

    @Test
    void maiorBarraTemAlturaMaxima() {
        List<BarraGrafico> barras = new CalculadoraGraficoBarras(moeda).calcular(vendasFalsas.totaisPorMes());

        assertEquals(100, barras.get(0).alturaPx());
        assertEquals(CalculadoraGraficoBarras.ALTURA_MAXIMA_PX, barras.get(1).alturaPx());
    }

    @Test
    void variacaoFormataSinal() {
        assertEquals("+12%", Variacao.de(12).getTexto());
        assertEquals("-3%", Variacao.de(-3).getTexto());
        assertEquals("0%", Variacao.de(0).getTexto());
    }
}

