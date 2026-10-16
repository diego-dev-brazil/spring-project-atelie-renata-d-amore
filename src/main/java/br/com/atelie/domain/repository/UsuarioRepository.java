package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de LEITURA de usuários (ISP + DIP).
 */
public interface UsuarioRepository {

    Optional<Usuario> buscarPorLogin(String login);

    default List<Usuario> listarTodos() {
        return List.of();
    }
}
