package br.com.atelie.infrastructure.memoria;

import br.com.atelie.domain.model.ItemFichaTecnica;
import br.com.atelie.domain.model.ModeloBolsa;
import br.com.atelie.domain.repository.ModeloBolsaComandoRepository;
import br.com.atelie.domain.repository.ModeloBolsaConsultaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório em memória para Modelos de Bolsas.
 * Implementa tanto a interface de leitura quanto a de escrita (ISP).
 */
@Repository
public class ModeloBolsaRepositoryEmMemoria implements ModeloBolsaConsultaRepository, ModeloBolsaComandoRepository {

    private final Map<Long, ModeloBolsa> modelos = new ConcurrentHashMap<>();
    private final AtomicLong geradorId = new AtomicLong(0);

    public ModeloBolsaRepositoryEmMemoria() {
        // Inicializa com os modelos clássicos do ateliê já com suas fichas técnicas vinculadas aos materiais reais
        ModeloBolsa premium = new ModeloBolsa(null, "Bolsa Artesanal Premium",
                "Bolsa de ombro sofisticada com acabamento em couro e forro reforçado.",
                "Premium", new BigDecimal("450.00"), 180, List.of(
                new ItemFichaTecnica(1L, "Tecido Oxford Azul", 2, "metros"),
                new ItemFichaTecnica(4L, "Zíper Dourado 30cm", 1, "un"),
                new ItemFichaTecnica(9L, "Alças de Couro Sintético", 2, "un"),
                new ItemFichaTecnica(10L, "Manta Acrílica R2", 1, "metros")
        ));
        salvar(premium);

        ModeloBolsa tote = new ModeloBolsa(null, "Bolsa Tote Casual",
                "Bolsa ampla e prática para o dia a dia, feita em tecido estampado leve.",
                "Casual", new BigDecimal("280.00"), 120, List.of(
                new ItemFichaTecnica(6L, "Tecido Tricoline Estampado", 2, "metros"),
                new ItemFichaTecnica(5L, "Zíper Comum Preto", 1, "un"),
                new ItemFichaTecnica(8L, "Botões Amadeirados", 4, "un")
        ));
        salvar(tote);

        ModeloBolsa clutch = new ModeloBolsa(null, "Bolsa Clutch Festa",
                "Bolsa de mão para eventos e festas elegantes com estrutura firme.",
                "Festa", new BigDecimal("320.00"), 90, List.of(
                new ItemFichaTecnica(7L, "Tecido Linho Cru", 1, "metros"),
                new ItemFichaTecnica(4L, "Zíper Dourado 30cm", 1, "un"),
                new ItemFichaTecnica(10L, "Manta Acrílica R2", 1, "metros")
        ));
        salvar(clutch);

        ModeloBolsa necessaire = new ModeloBolsa(null, "Necessaire Grande",
                "Necessaire multiuso com fechamento seguro em zíper.",
                "Acessórios", new BigDecimal("95.00"), 45, List.of(
                new ItemFichaTecnica(6L, "Tecido Tricoline Estampado", 1, "metros"),
                new ItemFichaTecnica(5L, "Zíper Comum Preto", 1, "un")
        ));
        salvar(necessaire);
    }

    @Override
    public List<ModeloBolsa> listarTodos() {
        return modelos.values().stream()
                .sorted(Comparator.comparing(ModeloBolsa::getNome))
                .toList();
    }

    @Override
    public Optional<ModeloBolsa> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(modelos.get(id));
    }

    @Override
    public int contarModelos() {
        return modelos.size();
    }

    @Override
    public ModeloBolsa salvar(ModeloBolsa modelo) {
        if (modelo.getId() == null) {
            modelo.setId(geradorId.incrementAndGet());
        }
        modelos.put(modelo.getId(), modelo);
        return modelo;
    }

    @Override
    public boolean excluir(Long id) {
        if (id == null) {
            return false;
        }
        return modelos.remove(id) != null;
    }
}

