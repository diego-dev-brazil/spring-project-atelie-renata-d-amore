package br.com.atelie.infrastructure.memoria;

import br.com.atelie.domain.model.Alerta;
import br.com.atelie.domain.model.Alerta.Severidade;
import br.com.atelie.domain.model.Alerta.TipoAlerta;
import br.com.atelie.domain.model.Aluno;
import br.com.atelie.domain.model.ItemFichaTecnica;
import br.com.atelie.domain.model.Material;
import br.com.atelie.domain.model.ModeloBolsa;
import br.com.atelie.domain.repository.AlertaRepository;
import br.com.atelie.domain.repository.AlunoConsultaRepository;
import br.com.atelie.domain.repository.MaterialConsultaRepository;
import br.com.atelie.domain.repository.ModeloBolsaConsultaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Gerador dinâmico de alertas do ateliê conectado aos repositórios reais:
 * - Estoque baixo ({@link MaterialConsultaRepository})
 * - Produção bloqueada por falta de insumo ({@link ModeloBolsaConsultaRepository})
 * - Mensalidades atrasadas de alunos ({@link AlunoConsultaRepository})
 */
@Repository
public class AlertaRepositoryEmMemoria implements AlertaRepository {

    private final MaterialConsultaRepository materialConsulta;
    private final ModeloBolsaConsultaRepository modeloConsulta;
    private final AlunoConsultaRepository alunoConsulta;

    public AlertaRepositoryEmMemoria(MaterialConsultaRepository materialConsulta,
                                     ModeloBolsaConsultaRepository modeloConsulta,
                                     AlunoConsultaRepository alunoConsulta) {
        this.materialConsulta = materialConsulta;
        this.modeloConsulta = modeloConsulta;
        this.alunoConsulta = alunoConsulta;
    }

    @Override
    public List<Alerta> buscarAtivos() {
        List<Alerta> ativos = new ArrayList<>();

        // 1. Alertas dinâmicos de estoque baixo
        for (Material m : materialConsulta.buscarMateriaisComEstoqueBaixo()) {
            Severidade severidade = (m.getQuantidade() == 0) ? Severidade.ALTA : Severidade.MEDIA;
            String msg = m.getNome() + " abaixo do estoque mínimo (" + m.getQuantidade() + " " + m.getUnidadeMedida() + " restantes)";
            ativos.add(new Alerta(TipoAlerta.ESTOQUE, msg, severidade));
        }

        // 2. Alertas dinâmicos de produção (modelos impossíveis de produzir por falta de material)
        for (ModeloBolsa modelo : modeloConsulta.listarTodos()) {
            for (ItemFichaTecnica item : modelo.getFichaTecnica()) {
                Optional<Material> matOpt = materialConsulta.buscarPorId(item.getMaterialId());
                if (matOpt.isEmpty() || matOpt.get().getQuantidade() < item.getQuantidadeNecessaria()) {
                    String msg = modelo.getNome() + " impossível de produzir - falta " + item.getMaterialNome();
                    ativos.add(new Alerta(TipoAlerta.PRODUCAO, msg, Severidade.ALTA));
                }
            }
        }

        // 3. Alertas dinâmicos de mensalidades atrasadas de alunos
        for (Aluno aluno : alunoConsulta.buscarInadimplentes()) {
            Severidade sev = aluno.getParcelasPendentes() >= 2 ? Severidade.ALTA : Severidade.MEDIA;
            String msg = "Aluna(o) " + aluno.getNome() + " com " + aluno.getParcelasPendentes() + " parcela(s) pendente(s)";
            ativos.add(new Alerta(TipoAlerta.PAGAMENTO, msg, sev));
        }

        return ativos;
    }
}
