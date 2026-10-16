package br.com.atelie.application.dashboard.indicadores;

import br.com.atelie.application.dashboard.IndicadorDashboard;
import br.com.atelie.application.dashboard.dto.CartaoIndicador;
import br.com.atelie.application.formatacao.FormatadorQuantidade;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.domain.model.Variacao;
import br.com.atelie.domain.repository.ModeloBolsaConsultaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Cartão "Modelos / Produtos Disponíveis" no Dashboard.
 *
 * Agora este indicador é dinâmico e conectado ao repositório real de Modelos de Bolsas!
 * Demonstração pura de OCP e LSP: substitui a leitura de métrica estática por contagem real.
 */
@Component
public class ProdutosDisponiveisIndicador implements IndicadorDashboard {

    private final ModeloBolsaConsultaRepository modeloConsultaRepository;
    private final FormatadorValor formatadorQuantidade;

    public ProdutosDisponiveisIndicador(ModeloBolsaConsultaRepository modeloConsultaRepository,
                                        @Qualifier(FormatadorQuantidade.NOME) FormatadorValor formatadorQuantidade) {
        this.modeloConsultaRepository = modeloConsultaRepository;
        this.formatadorQuantidade = formatadorQuantidade;
    }

    @Override
    public int ordem() {
        return 2;
    }

    @Override
    public CartaoIndicador gerarCartao() {
        int total = modeloConsultaRepository.contarModelos();
        return new CartaoIndicador(
                "Modelos de Bolsas",
                formatadorQuantidade.formatar(BigDecimal.valueOf(total)),
                "shopping-bag",
                Variacao.de(5)
        );
    }
}
