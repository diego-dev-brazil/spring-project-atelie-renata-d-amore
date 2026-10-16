package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.ConfiguracaoAtelie;

/**
 * Contrato de persistência das configurações do ateliê (DIP).
 */
public interface ConfiguracaoRepository {

    ConfiguracaoAtelie obter();

    ConfiguracaoAtelie salvar(ConfiguracaoAtelie configuracao);
}

