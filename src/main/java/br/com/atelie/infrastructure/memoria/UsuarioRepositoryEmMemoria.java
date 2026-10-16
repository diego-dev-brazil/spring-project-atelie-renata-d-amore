package br.com.atelie.infrastructure.memoria;

import br.com.atelie.domain.model.Perfil;
import br.com.atelie.domain.model.Usuario;
import br.com.atelie.domain.repository.UsuarioComandoRepository;
import br.com.atelie.domain.repository.UsuarioRepository;
import br.com.atelie.domain.seguranca.CodificadorSenha;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Usuários guardados em memória.
 * Implementa leitura ({@link UsuarioRepository}) e escrita ({@link UsuarioComandoRepository}).
 */
@Repository
public class UsuarioRepositoryEmMemoria implements UsuarioRepository, UsuarioComandoRepository {

    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();

    public UsuarioRepositoryEmMemoria(CodificadorSenha codificador) {
        LocalDate hoje = LocalDate.now();
        salvar(new Usuario("admin", "Renata D'Amore", "Proprietária & Diretora Criativa",
                codificador.codificar("admin123"), Perfil.ADMIN, true, LocalDate.of(1985, 4, 12)));
        salvar(new Usuario("funcionaria", "Camila Souza", "Costureira Sênior",
                codificador.codificar("func123"), Perfil.FUNCIONARIO, true, LocalDate.of(1994, hoje.getMonthValue(), hoje.getDayOfMonth())));
        salvar(new Usuario("beatriz", "Beatriz Martins", "Atendimento & Vendas",
                codificador.codificar("bia123"), Perfil.FUNCIONARIO, true, LocalDate.of(1998, hoje.getMonthValue(), Math.min(28, hoje.getDayOfMonth() + 2))));
        salvar(new Usuario("juliana", "Juliana Rocha", "Auxiliar de Corte (Férias/Inativa)",
                codificador.codificar("ju123"), Perfil.FUNCIONARIO, false, LocalDate.of(1996, 11, 20)));
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
