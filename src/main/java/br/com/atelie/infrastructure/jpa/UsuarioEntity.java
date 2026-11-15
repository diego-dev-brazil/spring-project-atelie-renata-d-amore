package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.Perfil;
import br.com.atelie.domain.model.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Tabela "usuario". A chave primária é o próprio login (o domínio já trata o login como
 * identificador e nunca o altera).
 */
@Entity
@Table(name = "usuario")
public class UsuarioEntity {

    @Id
    @Column(length = 60)
    String login;

    @Column(nullable = false, length = 150)
    String nome;

    @Column(nullable = false, length = 150)
    String cargo;

    @Column(name = "senha_codificada", nullable = false, length = 255)
    String senhaCodificada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    Perfil perfil;

    @Column(nullable = false)
    boolean ativo;

    @Column(name = "data_nascimento")
    LocalDate dataNascimento;

    protected UsuarioEntity() {
        // exigido pelo JPA
    }

    static UsuarioEntity de(Usuario u) {
        UsuarioEntity e = new UsuarioEntity();
        e.login = u.getLogin();
        e.nome = u.getNome();
        e.cargo = u.getCargo();
        e.senhaCodificada = u.getSenhaCodificada();
        e.perfil = u.getPerfil();
        e.ativo = u.isAtivo();
        e.dataNascimento = u.getDataNascimento();
        return e;
    }

    Usuario paraDominio() {
        return new Usuario(login, nome, cargo, senhaCodificada, perfil, ativo, dataNascimento);
    }
}
