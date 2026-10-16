package br.com.atelie.application.notificacoes;

import br.com.atelie.application.formatacao.FormatadorMoeda;
import br.com.atelie.domain.model.Aluno;
import br.com.atelie.domain.model.Notificacao;
import br.com.atelie.domain.model.Notificacao.Categoria;
import br.com.atelie.domain.model.Turma;
import br.com.atelie.domain.repository.AlunoConsultaRepository;
import br.com.atelie.domain.repository.TurmaConsultaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Gera notificações sobre alunas(os) com parcelas de mensalidade atrasadas (SRP + OCP + DIP).
 */
@Component
public class PagamentoAtrasadoNotificacaoProvedor implements ProvedorNotificacao {

    private final AlunoConsultaRepository alunoConsulta;
    private final TurmaConsultaRepository turmaConsulta;
    private final FormatadorMoeda formatadorMoeda;

    public PagamentoAtrasadoNotificacaoProvedor(AlunoConsultaRepository alunoConsulta,
                                                TurmaConsultaRepository turmaConsulta,
                                                FormatadorMoeda formatadorMoeda) {
        this.alunoConsulta = alunoConsulta;
        this.turmaConsulta = turmaConsulta;
        this.formatadorMoeda = formatadorMoeda;
    }

    @Override
    public int ordem() {
        return 2;
    }

    @Override
    public List<Notificacao> gerarNotificacoes() {
        List<Notificacao> lista = new ArrayList<>();

        for (Aluno aluno : alunoConsulta.buscarInadimplentes()) {
            BigDecimal mensalidade = turmaConsulta.buscarPorId(aluno.getTurmaId())
                    .map(Turma::getValorMensalidade)
                    .orElse(new BigDecimal("280.00"));
            BigDecimal totalPendente = mensalidade.multiply(BigDecimal.valueOf(aluno.getParcelasPendentes()));
            boolean critico = aluno.getParcelasPendentes() >= 2;

            String titulo = "Pagamento Atrasado: " + aluno.getNome();
            String mensagem = "Possui " + aluno.getParcelasPendentes() + " parcela(s) em atraso na turma \""
                    + aluno.getTurmaNome() + "\", totalizando "
                    + formatadorMoeda.formatar(totalPendente) + ".";
            String detalhe = "Contato: " + aluno.getTelefone() + " • " + aluno.getEmail();

            lista.add(new Notificacao(
                    Categoria.PAGAMENTO_ATRASADO,
                    titulo,
                    mensagem,
                    detalhe,
                    "/alunos",
                    "Regularizar Mensalidade",
                    critico
            ));
        }

        return lista;
    }
}

