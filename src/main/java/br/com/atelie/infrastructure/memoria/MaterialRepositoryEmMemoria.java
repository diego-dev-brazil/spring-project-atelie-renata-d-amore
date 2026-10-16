package br.com.atelie.infrastructure.memoria;

import br.com.atelie.domain.model.Material;
import br.com.atelie.domain.repository.MaterialComandoRepository;
import br.com.atelie.domain.repository.MaterialConsultaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositório em memória para Materiais.
 * Implementa tanto a interface de consulta quanto a de comando (ISP),
 * mas os clientes consomem apenas as interfaces específicas que precisam.
 */
@Repository
public class MaterialRepositoryEmMemoria implements MaterialConsultaRepository, MaterialComandoRepository {

    private final Map<Long, Material> materiais = new ConcurrentHashMap<>();
    private final AtomicLong geradorId = new AtomicLong(0);

    public MaterialRepositoryEmMemoria() {
        // Inicializa com dados reais que somam 1.247 unidades (o valor inicial do dashboard)
        salvar(new Material(null, "Tecido Oxford Azul", "Tecidos", 15, 20, "metros", new BigDecimal("25.50")));
        salvar(new Material(null, "Linha de Costura Branca", "Linhas", 5, 10, "un", new BigDecimal("4.20")));
        salvar(new Material(null, "Linha de Costura Preta", "Linhas", 40, 10, "un", new BigDecimal("4.20")));
        salvar(new Material(null, "Zíper Dourado 30cm", "Aviamentos", 0, 15, "un", new BigDecimal("3.50")));
        salvar(new Material(null, "Zíper Comum Preto", "Aviamentos", 87, 20, "un", new BigDecimal("2.00")));
        salvar(new Material(null, "Tecido Tricoline Estampado", "Tecidos", 250, 50, "metros", new BigDecimal("32.00")));
        salvar(new Material(null, "Tecido Linho Cru", "Tecidos", 180, 40, "metros", new BigDecimal("45.00")));
        salvar(new Material(null, "Botões Amadeirados", "Aviamentos", 350, 100, "un", new BigDecimal("0.80")));
        salvar(new Material(null, "Alças de Couro Sintético", "Acessórios", 120, 30, "un", new BigDecimal("14.90")));
        salvar(new Material(null, "Manta Acrílica R2", "Estruturação", 200, 30, "metros", new BigDecimal("18.00")));
    }

    @Override
    public List<Material> listarTodos() {
        return materiais.values().stream()
                .sorted(Comparator.comparing(Material::getNome))
                .toList();
    }

    @Override
    public Optional<Material> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(materiais.get(id));
    }

    @Override
    public int somarQuantidadeTotal() {
        return materiais.values().stream()
                .mapToInt(Material::getQuantidade)
                .sum();
    }

    @Override
    public List<Material> buscarMateriaisComEstoqueBaixo() {
        return materiais.values().stream()
                .filter(Material::isEstoqueBaixo)
                .toList();
    }

    @Override
    public Material salvar(Material material) {
        if (material.getId() == null) {
            material.setId(geradorId.incrementAndGet());
        }
        materiais.put(material.getId(), material);
        return material;
    }

    @Override
    public boolean excluir(Long id) {
        if (id == null) {
            return false;
        }
        return materiais.remove(id) != null;
    }
}

