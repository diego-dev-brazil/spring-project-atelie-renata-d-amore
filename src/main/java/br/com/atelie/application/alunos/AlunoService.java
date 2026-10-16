package br.com.atelie.application.alunos;

import br.com.atelie.domain.model.Aluno;
import br.com.atelie.domain.model.Turma;
import br.com.atelie.domain.repository.AlunoComandoRepository;
import br.com.atelie.domain.repository.AlunoConsultaRepository;
import br.com.atelie.domain.repository.TurmaConsultaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Caso de Uso: Gestão de Alunos Matriculados.
 */
@Service
public class AlunoService {

    private final AlunoConsultaRepository alunoConsulta;
    private final AlunoComandoRepository alunoComando;
    private final TurmaConsultaRepository turmaConsulta;

    public AlunoService(AlunoConsultaRepository alunoConsulta,
                        AlunoComandoRepository alunoComando,
                        TurmaConsultaRepository turmaConsulta) {
        this.alunoConsulta = alunoConsulta;
        this.alunoComando = alunoComando;
        this.turmaConsulta = turmaConsulta;
    }

    public List<Aluno> listarTodos() {
        return alunoConsulta.listarTodos();
    }

    public Optional<Aluno> buscarPorId(Long id) {
        return alunoConsulta.buscarPorId(id);
    }

    public int totalAlunos() {
        return alunoConsulta.contarTotal();
    }

    public Aluno salvar(Long id, String nome, String telefone, String email, Long turmaId, int parcelasPendentes) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do aluno(a) é obrigatório.");
        }
        String nomeTurma = turmaConsulta.buscarPorId(turmaId)
                .map(Turma::getNome)
                .orElse("Sem turma");

        Aluno aluno = new Aluno(id, nome, telefone, email, turmaId, nomeTurma, parcelasPendentes);
        return alunoComando.salvar(aluno);
    }

    public boolean excluir(Long id) {
        return alunoComando.excluir(id);
    }
}

