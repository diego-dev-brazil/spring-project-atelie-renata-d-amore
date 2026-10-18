package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.Aluno;
import br.com.atelie.domain.repository.AlunoComandoRepository;
import br.com.atelie.domain.repository.AlunoConsultaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("!memoria")
public class AlunoRepositoryJpa implements AlunoConsultaRepository, AlunoComandoRepository {

    private final AlunoJpaRepository jpa;

    public AlunoRepositoryJpa(AlunoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Aluno> listarTodos() {
        return jpa.findAll().stream()
                .map(AlunoEntity::paraDominio)
                .sorted(Comparator.comparing(Aluno::getNome))
                .toList();
    }

    @Override
    public Optional<Aluno> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpa.findById(id).map(AlunoEntity::paraDominio);
    }

    @Override
    public int contarTotal() {
        return (int) jpa.count();
    }

    @Override
    public List<Aluno> buscarInadimplentes() {
        return jpa.findByParcelasPendentesGreaterThanOrderByParcelasPendentesDesc(0).stream()
                .map(AlunoEntity::paraDominio)
                .toList();
    }

    @Override
    public Aluno salvar(Aluno aluno) {
        AlunoEntity salvo = jpa.save(AlunoEntity.de(aluno));
        aluno.setId(salvo.id);
        return salvo.paraDominio();
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
