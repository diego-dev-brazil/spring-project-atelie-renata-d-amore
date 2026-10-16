package br.com.atelie.infrastructure.memoria;

import br.com.atelie.domain.model.Metrica;
import br.com.atelie.domain.model.TipoMetrica;
import br.com.atelie.domain.model.Variacao;
import br.com.atelie.domain.repository.MetricaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/**
 * Métricas fixas de demonstração (no React: mockStats).
 */
@Repository
public class MetricaRepositoryEmMemoria implements MetricaRepository {

    private final Map<TipoMetrica, Metrica> metricas = new EnumMap<>(TipoMetrica.class);

    public MetricaRepositoryEmMemoria() {
        metricas.put(TipoMetrica.MATERIAIS_EM_ESTOQUE, new Metrica(new BigDecimal("1247"), Variacao.de(12)));
        metricas.put(TipoMetrica.PRODUTOS_DISPONIVEIS, new Metrica(new BigDecimal("89"), Variacao.de(5)));
        metricas.put(TipoMetrica.TURMAS_ATIVAS, new Metrica(new BigDecimal("8"), Variacao.de(0)));
        metricas.put(TipoMetrica.ALUNOS_CADASTRADOS, new Metrica(new BigDecimal("124"), Variacao.de(8)));
        metricas.put(TipoMetrica.LUCRO_BRUTO_MENSAL, new Metrica(new BigDecimal("32450"), Variacao.de(-3)));
    }

    @Override
    public Metrica buscar(TipoMetrica tipo) {
        Metrica metrica = metricas.get(tipo);
        if (metrica == null) {
            throw new IllegalArgumentException("Métrica não cadastrada: " + tipo);
        }
        return metrica;
    }
}

