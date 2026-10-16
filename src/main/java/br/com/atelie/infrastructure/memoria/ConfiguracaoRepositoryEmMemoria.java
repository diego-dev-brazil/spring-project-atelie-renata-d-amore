package br.com.atelie.infrastructure.memoria;

import br.com.atelie.domain.model.ConfiguracaoAtelie;
import br.com.atelie.domain.repository.ConfiguracaoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class ConfiguracaoRepositoryEmMemoria implements ConfiguracaoRepository {

    private final AtomicReference<ConfiguracaoAtelie> atual = new AtomicReference<>(
            new ConfiguracaoAtelie(
                    "Ateliê Renata D'Amore",
                    "Renata D'Amore",
                    "34.567.890/0001-12",
                    "(11) 3456-7890",
                    "contato@atelierenatadamore.com.br",
                    "Rua das Hortênsias, 420 - Jardim Paulista, São Paulo/SP",
                    new BigDecimal("50000.00"),
                    true
            )
    );

    @Override
    public ConfiguracaoAtelie obter() {
        return atual.get();
    }

    @Override
    public ConfiguracaoAtelie salvar(ConfiguracaoAtelie configuracao) {
        atual.set(configuracao);
        return configuracao;
    }
}

