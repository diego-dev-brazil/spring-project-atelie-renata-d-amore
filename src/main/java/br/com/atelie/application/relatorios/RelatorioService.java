package br.com.atelie.application.relatorios;

import br.com.atelie.domain.model.Material;
import br.com.atelie.domain.model.OrdemProducao;
import br.com.atelie.domain.model.Turma;
import br.com.atelie.domain.model.VendaMensal;
import br.com.atelie.domain.repository.AlunoConsultaRepository;
import br.com.atelie.domain.repository.MaterialConsultaRepository;
import br.com.atelie.domain.repository.ModeloBolsaConsultaRepository;
import br.com.atelie.domain.repository.ProducaoConsultaRepository;
import br.com.atelie.domain.repository.TurmaConsultaRepository;
import br.com.atelie.domain.repository.VendaConsultaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Caso de Uso: Consolidação de Relatórios Gerenciais para a Administradora.
 *
 * SRP: Consolida indicadores financeiros, de estoque, produção e cursos.
 * ISP + DIP: Consome apenas as interfaces de LEITURA (*ConsultaRepository) de cada módulo.
 */
@Service
public class RelatorioService {

    private final MaterialConsultaRepository materialConsulta;
    private final ModeloBolsaConsultaRepository modeloConsulta;
    private final ProducaoConsultaRepository producaoConsulta;
    private final VendaConsultaRepository vendaConsulta;
    private final TurmaConsultaRepository turmaConsulta;
    private final AlunoConsultaRepository alunoConsulta;

    public RelatorioService(MaterialConsultaRepository materialConsulta,
                            ModeloBolsaConsultaRepository modeloConsulta,
                            ProducaoConsultaRepository producaoConsulta,
                            VendaConsultaRepository vendaConsulta,
                            TurmaConsultaRepository turmaConsulta,
                            AlunoConsultaRepository alunoConsulta) {
        this.materialConsulta = materialConsulta;
        this.modeloConsulta = modeloConsulta;
        this.producaoConsulta = producaoConsulta;
        this.vendaConsulta = vendaConsulta;
        this.turmaConsulta = turmaConsulta;
        this.alunoConsulta = alunoConsulta;
    }

    public BigDecimal calcularValorPatrimonialEstoque() {
        return materialConsulta.listarTodos().stream()
                .map(m -> m.getPrecoUnitario().multiply(BigDecimal.valueOf(m.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularFaturamentoSemestral() {
        return vendaConsulta.totaisPorMes().stream()
                .map(VendaMensal::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularReceitaPotencialTurmas() {
        return turmaConsulta.listarTodas().stream()
                .filter(Turma::isAtiva)
                .map(t -> t.getValorMensalidade().multiply(BigDecimal.valueOf(t.getVagasTotais())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int contarPecasProduzidas() {
        return producaoConsulta.listarTodas().stream()
                .filter(OrdemProducao::isConcluida)
                .mapToInt(OrdemProducao::getQuantidadePecas)
                .sum();
    }

    public List<VendaMensal> historicoVendasMensais() {
        return vendaConsulta.totaisPorMes();
    }

    public int totalInadimplentes() {
        return alunoConsulta.buscarInadimplentes().size();
    }

    public int totalMateriaisCriticos() {
        return materialConsulta.buscarMateriaisComEstoqueBaixo().size();
    }

    public int totalModelosCatalogo() {
        return modeloConsulta.contarModelos();
    }
}

