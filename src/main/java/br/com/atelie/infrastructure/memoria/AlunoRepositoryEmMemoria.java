package br.com.atelie.infrastructure.memoria;

import br.com.atelie.domain.model.Aluno;
import br.com.atelie.domain.repository.AlunoComandoRepository;
import br.com.atelie.domain.repository.AlunoConsultaRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório em memória de Alunos.
 */
@Repository
public class AlunoRepositoryEmMemoria implements AlunoConsultaRepository, AlunoComandoRepository {

    private final Map<Long, Aluno> alunos = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(0);

    public AlunoRepositoryEmMemoria() {
        salvar(new Aluno(null, "Fernanda Lima", "(11) 98765-1122", "fernanda.lima@email.com", 1L, "Costura Criativa Iniciante - Manhã", 1));
        salvar(new Aluno(null, "Roberta Dias", "(11) 99123-4455", "roberta.dias@email.com", 2L, "Confecção de Bolsas em Couro", 2));
        salvar(new Aluno(null, "Mariana Costa", "(11) 97654-3322", "mariana.costa@email.com", 4L, "Modelagem de Bolsas Estruturadas", 0));
        salvar(new Aluno(null, "Patrícia Albuquerque", "(11) 98222-7788", "patricia.alb@email.com", 2L, "Confecção de Bolsas em Couro", 0));
        salvar(new Aluno(null, "Helena Vasconcelos", "(11) 99444-5566", "helena.v@email.com", 3L, "Patchwork & Quilting Moderno", 0));
        salvar(new Aluno(null, "Larissa Mendes", "(11) 98111-9900", "larissa.mendes@email.com", 6L, "Workshop Necessaires & Carteiras", 0));
    }

    @Override
    public List<Aluno> listarTodos() {
        return alunos.values().stream()
                .sorted(Comparator.comparing(Aluno::getNome))
                .toList();
    }

    @Override
    public Optional<Aluno> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(alunos.get(id));
    }

    @Override
    public int contarTotal() {
        return alunos.size();
    }

    @Override
    public List<Aluno> buscarInadimplentes() {
        return alunos.values().stream()
                .filter(Aluno::isInadimplente)
                .sorted(Comparator.comparingInt(Aluno::getParcelasPendentes).reversed())
                .toList();
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        if (aluno.getId() == null) {
            aluno.setId(sequencia.incrementAndGet());
        }
        alunos.put(aluno.getId(), aluno);
        return aluno;
    }

    @Override
    public boolean excluir(Long id) {
        if (id == null) {
            return false;
        }
        return alunos.remove(id) != null;
    }
}

