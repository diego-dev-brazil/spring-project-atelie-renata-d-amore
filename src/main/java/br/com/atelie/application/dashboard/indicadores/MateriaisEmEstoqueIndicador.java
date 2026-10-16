package br.com.atelie.application.dashboard.indicadores;

import br.com.atelie.application.dashboard.IndicadorDashboard;
import br.com.atelie.application.dashboard.dto.CartaoIndicador;
import br.com.atelie.application.formatacao.FormatadorQuantidade;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.domain.model.Variacao;
import br.com.atelie.domain.repository.MaterialConsultaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Cartão "Materiais em Estoque" no Dashboard.
 *
 * Agora este indicador é DINÂMICO e conectado ao repositório real de materiais!
 * Ele consulta o total de itens via MaterialConsultaRepository.
 *
 * Cumpre o contrato {@link IndicadorDashboard} (OCP/LSP), demonstrando que o
 * DashboardService não precisa ser alterado para que a fonte de dados se torne real.
 */
@Component
public class MateriaisEmEstoqueIndicador implements IndicadorDashboard {

    private final MaterialConsultaRepository materialConsultaRepository;
    private final FormatadorValor formatadorQuantidade;

    public MateriaisEmEstoqueIndicador(MaterialConsultaRepository materialConsultaRepository,
                                       @Qualifier(FormatadorQuantidade.NOME) FormatadorValor formatadorQuantidade) {
        this.materialConsultaRepository = materialConsultaRepository;
        this.formatadorQuantidade = formatadorQuantidade;
    }

    @Override
    public int ordem() {
        return 1;
    }

    @Override
    public CartaoIndicador gerarCartao() {
        int total = materialConsultaRepository.somarQuantidadeTotal();
        return new CartaoIndicador(
                "Materiais em Estoque",
                formatadorQuantidade.formatar(BigDecimal.valueOf(total)),
                "package",
                Variacao.de(12) // Variação em relação ao mês anterior mantida para exibição
        );
    }
}
