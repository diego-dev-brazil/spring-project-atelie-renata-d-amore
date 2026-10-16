package br.com.atelie.application.configuracoes;

import br.com.atelie.domain.model.ConfiguracaoAtelie;
import br.com.atelie.domain.repository.ConfiguracaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Caso de Uso: Gerenciamento das Configurações do Ateliê.
 */
@Service
public class ConfiguracaoService {

    private final ConfiguracaoRepository repository;

    public ConfiguracaoService(ConfiguracaoRepository repository) {
        this.repository = repository;
    }

    public ConfiguracaoAtelie obter() {
        return repository.obter();
    }

    public ConfiguracaoAtelie atualizar(String nomeAtelie, String proprietaria, String cnpj,
                                        String telefone, String emailContato, String endereco,
                                        BigDecimal metaVendasMensal, boolean alertasEstoqueAtivos) {
        ConfiguracaoAtelie conf = new ConfiguracaoAtelie(
                nomeAtelie, proprietaria, cnpj, telefone, emailContato, endereco, metaVendasMensal, alertasEstoqueAtivos
        );
        return repository.salvar(conf);
    }
}

