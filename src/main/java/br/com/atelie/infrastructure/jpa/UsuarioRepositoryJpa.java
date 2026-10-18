package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.Usuario;
import br.com.atelie.domain.repository.UsuarioComandoRepository;
import br.com.atelie.domain.repository.UsuarioRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("!memoria")
public class UsuarioRepositoryJpa implements UsuarioRepository, UsuarioComandoRepository {

    private final UsuarioJpaRepository jpa;

    public UsuarioRepositoryJpa(UsuarioJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        if (login == null) {
            return Optional.empty();
        }
        return jpa.findById(login.trim().toLowerCase()).map(UsuarioEntity::paraDominio);
    }

    @Override
    public List<Usuario> listarTodos() {
        return jpa.findAll().stream()
                .map(UsuarioEntity::paraDominio)
                .sorted(Comparator.comparing(Usuario::isAdmin).reversed()
                        .thenComparing(Usuario::getNome))
                .toList();
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        return jpa.save(UsuarioEntity.de(usuario)).paraDominio();
    }

    @Override
    public boolean excluirPorLogin(String login) {
        if (login == null) {
            return false;
        }
        String chave = login.trim().toLowerCase();
        if (!jpa.existsById(chave)) {
            return false;
        }
        jpa.deleteById(chave);
        return true;
    }
}
