package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.ConfiguracaoAtelie;
import br.com.atelie.domain.repository.ConfiguracaoRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!memoria")
public class ConfiguracaoRepositoryJpa implements ConfiguracaoRepository {

    private final ConfiguracaoJpaRepository jpa;

    public ConfiguracaoRepositoryJpa(ConfiguracaoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public ConfiguracaoAtelie obter() {
        return jpa.findById(ConfiguracaoEntity.ID_UNICO)
                .map(ConfiguracaoEntity::paraDominio)
                .orElseGet(() -> new ConfiguracaoAtelie(null, null, null, null, null, null, null, true));
    }

    @Override
    public ConfiguracaoAtelie salvar(ConfiguracaoAtelie configuracao) {
        return jpa.save(ConfiguracaoEntity.de(configuracao)).paraDominio();
    }
}
