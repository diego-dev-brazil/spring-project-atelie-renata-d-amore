package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Usuario;

/**
 * Contrato de ESCRITA/COMANDO de usuários (ISP).
 * Utilizado apenas pelo módulo administrativo de gestão de funcionários.
 */
public interface UsuarioComandoRepository {

    Usuario salvar(Usuario usuario);

    boolean excluirPorLogin(String login);
}

