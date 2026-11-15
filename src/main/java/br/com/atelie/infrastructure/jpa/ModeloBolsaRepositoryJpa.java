package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.ModeloBolsa;
import br.com.atelie.domain.repository.ModeloBolsaComandoRepository;
import br.com.atelie.domain.repository.ModeloBolsaConsultaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("!memoria")
public class ModeloBolsaRepositoryJpa implements ModeloBolsaConsultaRepository, ModeloBolsaComandoRepository {

    private final ModeloBolsaJpaRepository jpa;

    public ModeloBolsaRepositoryJpa(ModeloBolsaJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<ModeloBolsa> listarTodos() {
        return jpa.findAll().stream()
                .map(ModeloBolsaEntity::paraDominio)
                .sorted(Comparator.comparing(ModeloBolsa::getNome))
                .toList();
    }

    @Override
    public Optional<ModeloBolsa> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpa.findById(id).map(ModeloBolsaEntity::paraDominio);
    }

    @Override
    public int contarModelos() {
        return (int) jpa.count();
    }

    @Override
    public ModeloBolsa salvar(ModeloBolsa modelo) {
        ModeloBolsaEntity salvo = jpa.save(ModeloBolsaEntity.de(modelo));
        modelo.setId(salvo.id);
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
