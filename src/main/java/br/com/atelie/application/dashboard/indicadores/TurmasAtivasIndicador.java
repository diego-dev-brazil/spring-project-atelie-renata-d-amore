package br.com.atelie.application.dashboard.indicadores;

import br.com.atelie.application.dashboard.IndicadorDashboard;
import br.com.atelie.application.dashboard.dto.CartaoIndicador;
import br.com.atelie.application.formatacao.FormatadorQuantidade;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.domain.model.Variacao;
import br.com.atelie.domain.repository.TurmaConsultaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Cartão "Turmas Ativas" conectado diretamente ao {@link TurmaConsultaRepository}.
 */
@Component
public class TurmasAtivasIndicador implements IndicadorDashboard {

    private final TurmaConsultaRepository turmaConsulta;
    private final FormatadorValor formatadorQuantidade;

    public TurmasAtivasIndicador(TurmaConsultaRepository turmaConsulta,
                                 @Qualifier(FormatadorQuantidade.NOME) FormatadorValor formatadorQuantidade) {
        this.turmaConsulta = turmaConsulta;
        this.formatadorQuantidade = formatadorQuantidade;
    }

    @Override
    public int ordem() {
        return 4;
    }

    @Override
    public CartaoIndicador gerarCartao() {
        int totalAtivas = turmaConsulta.contarAtivas();
        return new CartaoIndicador(
                "Turmas Ativas",
                formatadorQuantidade.formatar(BigDecimal.valueOf(totalAtivas)),
                "graduation-cap",
                Variacao.de(0)
        );
    }
}
