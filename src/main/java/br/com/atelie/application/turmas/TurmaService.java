package br.com.atelie.application.turmas;

import br.com.atelie.domain.model.Turma;
import br.com.atelie.domain.repository.TurmaComandoRepository;
import br.com.atelie.domain.repository.TurmaConsultaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Caso de Uso: Gestão de Turmas do Ateliê.
 */
@Service
public class TurmaService {

    private final TurmaConsultaRepository consultaRepository;
    private final TurmaComandoRepository comandoRepository;

    public TurmaService(TurmaConsultaRepository consultaRepository,
                        TurmaComandoRepository comandoRepository) {
        this.consultaRepository = consultaRepository;
        this.comandoRepository = comandoRepository;
    }

    public List<Turma> listarTodas() {
        return consultaRepository.listarTodas();
    }

    public Optional<Turma> buscarPorId(Long id) {
        return consultaRepository.buscarPorId(id);
    }

    public int totalTurmasAtivas() {
        return consultaRepository.contarAtivas();
    }

    public Turma salvar(Long id, String nome, String instrutora, String horario, int vagasTotais, BigDecimal valorMensalidade, boolean ativa) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da turma é obrigatório.");
        }
        Turma turma = new Turma(id, nome, instrutora, horario, vagasTotais, valorMensalidade, ativa);
        return comandoRepository.salvar(turma);
    }

    public boolean excluir(Long id) {
        return comandoRepository.excluir(id);
    }
}

