package br.com.atelie.application.usuarios;

import br.com.atelie.domain.model.Perfil;
import br.com.atelie.domain.model.Usuario;
import br.com.atelie.domain.repository.UsuarioComandoRepository;
import br.com.atelie.domain.repository.UsuarioRepository;
import br.com.atelie.domain.seguranca.CodificadorSenha;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Caso de Uso: Gestão de Funcionários e Controle de Acesso (Usuários).
 */
@Service
public class UsuarioService {

    private final UsuarioRepository consultaRepository;
    private final UsuarioComandoRepository comandoRepository;
    private final CodificadorSenha codificadorSenha;

    public UsuarioService(UsuarioRepository consultaRepository,
                          UsuarioComandoRepository comandoRepository,
                          CodificadorSenha codificadorSenha) {
        this.consultaRepository = consultaRepository;
        this.comandoRepository = comandoRepository;
        this.codificadorSenha = codificadorSenha;
    }

    public List<Usuario> listarTodos() {
        return consultaRepository.listarTodos();
    }

    public Optional<Usuario> buscarPorLogin(String login) {
        return consultaRepository.buscarPorLogin(login);
    }

    public Usuario cadastrarNovo(String login, String nome, String cargo, String senhaPura, Perfil perfil, LocalDate dataNascimento) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("O login de acesso é obrigatório.");
        }
        String loginNormalizado = login.trim().toLowerCase();
        if (consultaRepository.buscarPorLogin(loginNormalizado).isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com o login '" + loginNormalizado + "'.");
        }
        if (senhaPura == null || senhaPura.isBlank()) {
            throw new IllegalArgumentException("A senha inicial é obrigatória.");
        }
        if (dataNascimento == null) {
            throw new IllegalArgumentException("A data de nascimento do funcionário é obrigatória (mínimo de 14 anos).");
        }
        Usuario.validarIdadeMinima(dataNascimento);
        Usuario novo = new Usuario(loginNormalizado, nome, cargo, codificadorSenha.codificar(senhaPura), perfil, true, dataNascimento);
        return comandoRepository.salvar(novo);
    }

    public Usuario atualizar(String login, String nome, String cargo, String novaSenhaPura, Perfil perfil, boolean ativo, LocalDate dataNascimento, String loginLogado) {
        Usuario existente = consultaRepository.buscarPorLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (existente.getLogin().equalsIgnoreCase(loginLogado) && (!ativo || perfil != Perfil.ADMIN)) {
            throw new IllegalArgumentException("Você não pode desativar ou remover seu próprio perfil de administradora.");
        }

        if (dataNascimento != null) {
            Usuario.validarIdadeMinima(dataNascimento);
            existente.setDataNascimento(dataNascimento);
        }
        existente.setNome(nome);
        existente.setCargo(cargo);
        existente.setPerfil(perfil);
        existente.setAtivo(ativo);
        if (novaSenhaPura != null && !novaSenhaPura.isBlank()) {
            existente.setSenhaCodificada(codificadorSenha.codificar(novaSenhaPura));
        }
        return comandoRepository.salvar(existente);
    }

    /**
     * Permite que qualquer funcionário logado troque a sua própria senha,
     * validando a senha atual antes de gravar a nova.
     */
    public void alterarPropriaSenha(String loginLogado,
                                    String senhaAtualPura,
                                    String novaSenhaPura,
                                    String confirmacaoSenhaPura) {
        if (loginLogado == null || loginLogado.isBlank()) {
            throw new IllegalArgumentException("Sessão inválida. Faça login novamente.");
        }
        Usuario usuario = consultaRepository.buscarPorLogin(loginLogado)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (senhaAtualPura == null || senhaAtualPura.isBlank()) {
            throw new IllegalArgumentException("Informe a sua senha atual.");
        }
        if (!codificadorSenha.confere(senhaAtualPura, usuario.getSenhaCodificada())) {
            throw new IllegalArgumentException("A senha atual informada está incorreta.");
        }
        if (novaSenhaPura == null || novaSenhaPura.trim().length() < 4) {
            throw new IllegalArgumentException("A nova senha deve ter pelo menos 4 caracteres.");
        }
        if (!novaSenhaPura.equals(confirmacaoSenhaPura)) {
            throw new IllegalArgumentException("A confirmação da nova senha não confere.");
        }
        if (codificadorSenha.confere(novaSenhaPura, usuario.getSenhaCodificada())) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da senha atual.");
        }

        usuario.setSenhaCodificada(codificadorSenha.codificar(novaSenhaPura));
        comandoRepository.salvar(usuario);
    }

    public void alternarAcesso(String login, String loginLogado) {
        Usuario usuario = consultaRepository.buscarPorLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        if (usuario.getLogin().equalsIgnoreCase(loginLogado)) {
            throw new IllegalArgumentException("Você não pode bloquear o seu próprio acesso.");
        }
        usuario.setAtivo(!usuario.isAtivo());
        comandoRepository.salvar(usuario);
    }

    public void excluir(String login, String loginLogado) {
        if (login != null && login.trim().equalsIgnoreCase(loginLogado)) {
            throw new IllegalArgumentException("Você não pode excluir o usuário com o qual está logada.");
        }
        if (!comandoRepository.excluirPorLogin(login)) {
            throw new IllegalArgumentException("Usuário não encontrado para exclusão.");
        }
    }
}
