package br.com.atelie.application.usuarios;

import br.com.atelie.domain.model.Aluno;
import br.com.atelie.domain.model.Perfil;
import br.com.atelie.domain.model.Usuario;
import br.com.atelie.infrastructure.memoria.UsuarioRepositoryEmMemoria;
import br.com.atelie.infrastructure.seguranca.Sha256CodificadorSenha;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    private Sha256CodificadorSenha codificador;
    private UsuarioRepositoryEmMemoria repositorio;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        codificador = new Sha256CodificadorSenha();
        repositorio = new UsuarioRepositoryEmMemoria(codificador);
        usuarioService = new UsuarioService(repositorio, repositorio, codificador);
    }

    @Test
    void recusaCadastroDeFuncionarioMenorDe14Anos() {
        LocalDate nascimento12Anos = LocalDate.now().minusYears(12);

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, () ->
                usuarioService.cadastrarNovo("jovem", "Jovem Teste", "Aprendiz", "1234", Perfil.FUNCIONARIO, nascimento12Anos)
        );
        assertTrue(erro.getMessage().contains("14 anos"));
    }

    @Test
    void aceitaCadastroDeFuncionarioCom14AnosOuMais() {
        LocalDate nascimento14Anos = LocalDate.now().minusYears(14);

        Usuario salvo = usuarioService.cadastrarNovo("aprendiz", "Ana Aprendiz", "Aprendiz", "1234", Perfil.FUNCIONARIO, nascimento14Anos);
        assertEquals("aprendiz", salvo.getLogin());
        assertEquals(nascimento14Anos, salvo.getDataNascimento());
    }

    @Test
    void permiteFuncionarioAlterarPropriaSenhaComSenhaAtualCorreta() {
        usuarioService.alterarPropriaSenha("funcionaria", "func123", "novaSenha99", "novaSenha99");

        Usuario atualizado = usuarioService.buscarPorLogin("funcionaria").orElseThrow();
        assertTrue(codificador.confere("novaSenha99", atualizado.getSenhaCodificada()));
    }

    @Test
    void recusaTrocaDeSenhaQuandoSenhaAtualIncorreta() {
        assertThrows(IllegalArgumentException.class, () ->
                usuarioService.alterarPropriaSenha("funcionaria", "senhaErrada", "novaSenha99", "novaSenha99")
        );
    }

    @Test
    void formataMascaraDeTelefoneDe10E11Digitos() {
        assertEquals("(11) 98765-4321", Aluno.formatarTelefone("11987654321"));
        assertEquals("(11) 3456-7890", Aluno.formatarTelefone("1134567890"));
    }
}
