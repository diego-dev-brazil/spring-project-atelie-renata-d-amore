package br.com.atelie.infrastructure.memoria;

import br.com.atelie.domain.model.OrdemProducao;
import br.com.atelie.domain.model.OrdemProducao.StatusProducao;
import br.com.atelie.domain.repository.ProducaoComandoRepository;
import br.com.atelie.domain.repository.ProducaoConsultaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório em memória para Ordens de Produção.
 */
@Repository
public class ProducaoRepositoryEmMemoria implements ProducaoConsultaRepository, ProducaoComandoRepository {

    private final Map<Long, OrdemProducao> ordens = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(0);

    public ProducaoRepositoryEmMemoria() {
        salvar(new OrdemProducao(null, 2L, "Bolsa Tote Casual", 5, "Camila Souza", LocalDate.of(2024, 6, 15), StatusProducao.CONCLUIDA));
        salvar(new OrdemProducao(null, 4L, "Necessaire Grande", 10, "Camila Souza", LocalDate.of(2024, 6, 18), StatusProducao.EM_ANDAMENTO));
        salvar(new OrdemProducao(null, 1L, "Bolsa Artesanal Premium", 2, "Renata D'Amore", LocalDate.of(2024, 6, 20), StatusProducao.AGUARDANDO_MATERIAIS));
    }

    @Override
    public List<OrdemProducao> listarTodas() {
        return ordens.values().stream()
                .sorted(Comparator.comparing(OrdemProducao::getDataPedido).thenComparing(OrdemProducao::getId).reversed())
                .toList();
    }

    @Override
    public Optional<OrdemProducao> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(ordens.get(id));
    }

    @Override
    public OrdemProducao salvar(OrdemProducao ordem) {
        if (ordem.getId() == null) {
            ordem.setId(sequencia.incrementAndGet());
        }
        ordens.put(ordem.getId(), ordem);
        return ordem;
    }

    @Override
    public boolean excluir(Long id) {
        if (id == null) {
            return false;
        }
        return ordens.remove(id) != null;
    }
}

