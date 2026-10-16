package br.com.atelie.infrastructure.memoria;

import br.com.atelie.domain.model.Turma;
import br.com.atelie.domain.repository.TurmaComandoRepository;
import br.com.atelie.domain.repository.TurmaConsultaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório em memória de Turmas.
 */
@Repository
public class TurmaRepositoryEmMemoria implements TurmaConsultaRepository, TurmaComandoRepository {

    private final Map<Long, Turma> turmas = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(0);

    public TurmaRepositoryEmMemoria() {
        salvar(new Turma(null, "Costura Criativa Iniciante - Manhã", "Renata D'Amore", "Ter/Qui 09h às 11h", 12, new BigDecimal("280.00"), true));
        salvar(new Turma(null, "Confecção de Bolsas em Couro", "Renata D'Amore", "Seg/Qua 14h às 17h", 10, new BigDecimal("420.00"), true));
        salvar(new Turma(null, "Patchwork & Quilting Moderno", "Camila Souza", "Sex 09h às 12h", 15, new BigDecimal("310.00"), true));
        salvar(new Turma(null, "Modelagem de Bolsas Estruturadas", "Renata D'Amore", "Sáb 09h às 13h", 12, new BigDecimal("450.00"), true));
        salvar(new Turma(null, "Acabamentos & Ferragens Finas", "Camila Souza", "Qua 19h às 21h30", 10, new BigDecimal("290.00"), true));
        salvar(new Turma(null, "Workshop Necessaires & Carteiras", "Beatriz Martins", "Qui 14h às 16h30", 15, new BigDecimal("220.00"), true));
    }

    @Override
    public List<Turma> listarTodas() {
        return turmas.values().stream()
                .sorted(Comparator.comparing(Turma::isAtiva).reversed().thenComparing(Turma::getNome))
                .toList();
    }

    @Override
    public Optional<Turma> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(turmas.get(id));
    }

    @Override
    public int contarAtivas() {
        return (int) turmas.values().stream().filter(Turma::isAtiva).count();
    }

    @Override
    public Turma salvar(Turma turma) {
        if (turma.getId() == null) {
            turma.setId(sequencia.incrementAndGet());
        }
        turmas.put(turma.getId(), turma);
        return turma;
    }

    @Override
    public boolean excluir(Long id) {
        if (id == null) {
            return false;
        }
        return turmas.remove(id) != null;
    }
}

