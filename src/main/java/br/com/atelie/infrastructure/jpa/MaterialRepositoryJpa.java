package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.Material;
import br.com.atelie.domain.repository.MaterialComandoRepository;
import br.com.atelie.domain.repository.MaterialConsultaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Implementação PostgreSQL (JPA) dos contratos de materiais.
 * Ativa em qualquer perfil, exceto "memoria".
 */
@Repository
@Profile("!memoria")
public class MaterialRepositoryJpa implements MaterialConsultaRepository, MaterialComandoRepository {

    private final MaterialJpaRepository jpa;

    public MaterialRepositoryJpa(MaterialJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<Material> listarTodos() {
        return jpa.findAll().stream()
                .map(MaterialEntity::paraDominio)
                .sorted(Comparator.comparing(Material::getNome))
                .toList();
    }

    @Override
    public Optional<Material> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpa.findById(id).map(MaterialEntity::paraDominio);
    }

    @Override
    public int somarQuantidadeTotal() {
        return (int) jpa.somarQuantidade();
    }

    @Override
    public List<Material> buscarMateriaisComEstoqueBaixo() {
        return jpa.buscarComEstoqueBaixo().stream()
                .map(MaterialEntity::paraDominio)
                .toList();
    }

    @Override
    public Material salvar(Material material) {
        MaterialEntity salvo = jpa.save(MaterialEntity.de(material));
        material.setId(salvo.id);
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
