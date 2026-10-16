package br.com.atelie.application.dashboard.indicadores;

import br.com.atelie.application.dashboard.IndicadorDashboard;
import br.com.atelie.application.dashboard.dto.CartaoIndicador;
import br.com.atelie.application.formatacao.FormatadorQuantidade;
import br.com.atelie.application.formatacao.FormatadorValor;
import br.com.atelie.domain.model.Variacao;
import br.com.atelie.domain.repository.AlunoConsultaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Cartão "Alunos Cadastrados" conectado diretamente ao {@link AlunoConsultaRepository}.
 */
@Component
public class AlunosCadastradosIndicador implements IndicadorDashboard {

    private final AlunoConsultaRepository alunoConsulta;
    private final FormatadorValor formatadorQuantidade;

    public AlunosCadastradosIndicador(AlunoConsultaRepository alunoConsulta,
                                      @Qualifier(FormatadorQuantidade.NOME) FormatadorValor formatadorQuantidade) {
        this.alunoConsulta = alunoConsulta;
        this.formatadorQuantidade = formatadorQuantidade;
    }

    @Override
    public int ordem() {
        return 5;
    }

    @Override
    public CartaoIndicador gerarCartao() {
        int totalAlunos = alunoConsulta.contarTotal();
        return new CartaoIndicador(
                "Alunos Cadastrados",
                formatadorQuantidade.formatar(BigDecimal.valueOf(totalAlunos)),
                "users",
                Variacao.de(8)
        );
    }
}
