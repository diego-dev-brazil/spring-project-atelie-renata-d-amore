package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.Turma;
import br.com.atelie.domain.repository.TurmaComandoRepository;
import br.com.atelie.domain.repository.TurmaConsultaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("!memoria")
public class TurmaRepositoryJpa implements TurmaConsultaRepository, TurmaComandoRepository {

    private final TurmaJpaRepository jpa;

    public TurmaRepositoryJpa(TurmaJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Turma> listarTodas() {
        return jpa.findAll().stream()
                .map(TurmaEntity::paraDominio)
                .sorted(Comparator.comparing(Turma::isAtiva).reversed().thenComparing(Turma::getNome))
                .toList();
    }

    @Override
    public Optional<Turma> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpa.findById(id).map(TurmaEntity::paraDominio);
    }

    @Override
    public int contarAtivas() {
        return (int) jpa.countByAtivaTrue();
    }

    @Override
    public Turma salvar(Turma turma) {
        TurmaEntity salva = jpa.save(TurmaEntity.de(turma));
        turma.setId(salva.id);
        return salva.paraDominio();
    }

    @Override
    public boolean excluir(Long id) {
        if (id == null || !jpa.existsById(id)) {
            return false;
        }
        jpa.deleteById(id);
        return true;
    }
}
