package br.com.atelie.application.autenticacao;

import br.com.atelie.domain.model.Perfil;
import br.com.atelie.domain.model.Usuario;
import br.com.atelie.domain.repository.UsuarioRepository;
import br.com.atelie.domain.seguranca.CodificadorSenha;
import br.com.atelie.infrastructure.seguranca.Sha256CodificadorSenha;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste SEM Spring e SEM banco: passamos um repositório "falso" (lambda).
 * Isso só é possível porque o serviço depende de uma INTERFACE (DIP).
 */
class AutenticacaoServiceTest {

    private final CodificadorSenha codificador = new Sha256CodificadorSenha();

    private final UsuarioRepository repositorioFalso = login ->
            "maria".equals(login)
                    ? Optional.of(new Usuario("maria", "Maria", codificador.codificar("segredo"), Perfil.FUNCIONARIO))
                    : Optional.empty();

    private final AutenticacaoService service = new AutenticacaoService(repositorioFalso, codificador);

    @Test
    void autenticaComSenhaCorreta() {
        Optional<UsuarioLogado> resultado = service.autenticar("maria", "segredo");

        assertTrue(resultado.isPresent());
        assertEquals("Maria", resultado.get().nome());
        assertEquals(Perfil.FUNCIONARIO, resultado.get().perfil());
    }

    @Test
    void recusaSenhaErrada() {
        assertTrue(service.autenticar("maria", "errada").isEmpty());
    }

    @Test
    void recusaUsuarioInexistente() {
        assertTrue(service.autenticar("joao", "segredo").isEmpty());
    }

    @Test
    void recusaCamposVazios() {
        assertTrue(service.autenticar("", "").isEmpty());
        assertTrue(service.autenticar(null, null).isEmpty());
    }
}

