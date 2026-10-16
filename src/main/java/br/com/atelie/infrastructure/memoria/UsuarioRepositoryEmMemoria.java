package br.com.atelie.infrastructure.memoria;

import br.com.atelie.domain.model.Perfil;
import br.com.atelie.domain.model.Usuario;
import br.com.atelie.domain.repository.UsuarioComandoRepository;
import br.com.atelie.domain.repository.UsuarioRepository;
import br.com.atelie.domain.seguranca.CodificadorSenha;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Usuários guardados em memória.
 * Implementa leitura ({@link UsuarioRepository}) e escrita ({@link UsuarioComandoRepository}).
 *
 * Usuários iniciais:
 *   admin       / admin123  -> Administradora
 *   funcionaria / func123   -> Funcionária (Costureira Sênior)
 *   beatriz     / bia123    -> Funcionária (Auxiliar de Produção)
 */
@Repository
public class UsuarioRepositoryEmMemoria implements UsuarioRepository, UsuarioComandoRepository {

    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();

    public UsuarioRepositoryEmMemoria(CodificadorSenha codificador) {
        salvar(new Usuario("admin", "Renata D'Amore", "Proprietária & Diretora Criativa", codificador.codificar("admin123"), Perfil.ADMIN, true));
        salvar(new Usuario("funcionaria", "Camila Souza", "Costureira Sênior", codificador.codificar("func123"), Perfil.FUNCIONARIO, true));
        salvar(new Usuario("beatriz", "Beatriz Martins", "Atendimento & Vendas", codificador.codificar("bia123"), Perfil.FUNCIONARIO, true));
        salvar(new Usuario("juliana", "Juliana Rocha", "Auxiliar de Corte (Férias/Inativa)", codificador.codificar("ju123"), Perfil.FUNCIONARIO, false));
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        usuarios.put(usuario.getLogin().toLowerCase(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        if (login == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(usuarios.get(login.trim().toLowerCase()));
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarios.values().stream()
                .sorted(Comparator.comparing(Usuario::isAdmin).reversed()
                        .thenComparing(Usuario::getNome))
                .toList();
    }

    @Override
    public boolean excluirPorLogin(String login) {
        if (login == null) {
            return false;
        }
        return usuarios.remove(login.trim().toLowerCase()) != null;
    }
}
