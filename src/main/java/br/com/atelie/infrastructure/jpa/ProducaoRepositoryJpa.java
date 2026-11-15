package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.OrdemProducao;
import br.com.atelie.domain.repository.ProducaoComandoRepository;
import br.com.atelie.domain.repository.ProducaoConsultaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("!memoria")
public class ProducaoRepositoryJpa implements ProducaoConsultaRepository, ProducaoComandoRepository {

    private final OrdemProducaoJpaRepository jpa;

    public ProducaoRepositoryJpa(OrdemProducaoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<OrdemProducao> listarTodas() {
        return jpa.findAll().stream()
                .map(OrdemProducaoEntity::paraDominio)
                .sorted(Comparator.comparing(OrdemProducao::getDataPedido)
                        .thenComparing(OrdemProducao::getId)
                        .reversed())
                .toList();
    }

    @Override
    public Optional<OrdemProducao> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpa.findById(id).map(OrdemProducaoEntity::paraDominio);
    }

    @Override
    public OrdemProducao salvar(OrdemProducao ordem) {
        OrdemProducaoEntity salva = jpa.save(OrdemProducaoEntity.de(ordem));
        ordem.setId(salva.id);
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
