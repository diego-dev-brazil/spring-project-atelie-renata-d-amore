package br.com.atelie.application.autenticacao;

import br.com.atelie.domain.model.Usuario;
import br.com.atelie.domain.repository.UsuarioRepository;
import br.com.atelie.domain.seguranca.CodificadorSenha;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Caso de uso: "verificar se login e senha estão corretos e se o usuário está com acesso ativo".
 *
 * SRP: esta classe tem UMA responsabilidade — autenticar. Ela não sabe
 *      nada de HTTP, sessão ou HTML (isso é papel do Controller).
 * DIP: depende de {@link UsuarioRepository} e {@link CodificadorSenha},
 *      que são interfaces. O Spring injeta as implementações pelo construtor.
 */
@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final CodificadorSenha codificadorSenha;

    public AutenticacaoService(UsuarioRepository usuarioRepository, CodificadorSenha codificadorSenha) {
        this.usuarioRepository = usuarioRepository;
        this.codificadorSenha = codificadorSenha;
    }

    public Optional<UsuarioLogado> autenticar(String login, String senha) {
        if (login == null || login.isBlank() || senha == null || senha.isEmpty()) {
            return Optional.empty();
        }
        return usuarioRepository.buscarPorLogin(login)
                .filter(Usuario::isAtivo)
                .filter(usuario -> codificadorSenha.confere(senha, usuario.getSenhaCodificada()))
                .map(usuario -> new UsuarioLogado(usuario.getLogin(), usuario.getNome(), usuario.getPerfil()));
    }
}
