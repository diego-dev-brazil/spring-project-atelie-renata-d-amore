package br.com.atelie.domain.model;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

/**
 * Usuário do sistema (administradora ou funcionária).
 *
 * Guarda a senha já codificada (hash), nunca a senha em texto puro.
 * Possui flag {@code ativo} para controle de acesso e {@code dataNascimento}
 * para avisos de aniversário da equipe e validação de idade mínima (14 anos).
 */
public class Usuario {

    public static final int IDADE_MINIMA_TRABALHO = 14;

    private final String login;
    private String nome;
    private String cargo;
    private String senhaCodificada;
    private Perfil perfil;
    private boolean ativo;
    private LocalDate dataNascimento;

    public Usuario(String login, String nome, String senhaCodificada, Perfil perfil) {
        this(login, nome, perfil == Perfil.ADMIN ? "Gestora / Proprietária" : "Costureira / Atendente",
                senhaCodificada, perfil, true, LocalDate.of(1990, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()));
    }

    public Usuario(String login, String nome, String cargo, String senhaCodificada, Perfil perfil, boolean ativo) {
        this(login, nome, cargo, senhaCodificada, perfil, ativo, LocalDate.of(1992, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()));
    }

    public Usuario(String login, String nome, String cargo, String senhaCodificada, Perfil perfil, boolean ativo, LocalDate dataNascimento) {
        this.login = Objects.requireNonNull(login, "login é obrigatório").trim().toLowerCase();
        this.nome = Objects.requireNonNull(nome, "nome é obrigatório").trim();
        this.cargo = (cargo == null || cargo.isBlank()) ? "Funcionária" : cargo.trim();
        this.senhaCodificada = Objects.requireNonNull(senhaCodificada, "senha é obrigatória");
        this.perfil = Objects.requireNonNull(perfil, "perfil é obrigatório");
        this.ativo = ativo;
        LocalDate nascimentoEfetivo = dataNascimento != null ? dataNascimento : LocalDate.of(1995, 5, 15);
        validarIdadeMinima(nascimentoEfetivo);
        this.dataNascimento = nascimentoEfetivo;
    }

    public static void validarIdadeMinima(LocalDate dataNascimento) {
        if (dataNascimento == null) {
            throw new IllegalArgumentException("A data de nascimento do funcionário é obrigatória.");
        }
        LocalDate hoje = LocalDate.now();
        if (dataNascimento.isAfter(hoje)) {
            throw new IllegalArgumentException("A data de nascimento não pode ser uma data futura.");
        }
        int idade = Period.between(dataNascimento, hoje).getYears();
        if (idade < IDADE_MINIMA_TRABALHO) {
            throw new IllegalArgumentException(
                    "Não é permitido cadastrar funcionário menor de " + IDADE_MINIMA_TRABALHO
                    + " anos trabalhando (idade informada: " + idade + " ano(s)).");
        }
    }

    public String getLogin() {
        return login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = Objects.requireNonNull(nome, "nome é obrigatório").trim();
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = (cargo == null || cargo.isBlank()) ? "Funcionária" : cargo.trim();
    }

    public String getSenhaCodificada() {
        return senhaCodificada;
    }

    public void setSenhaCodificada(String senhaCodificada) {
        this.senhaCodificada = Objects.requireNonNull(senhaCodificada, "senha é obrigatória");
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = Objects.requireNonNull(perfil, "perfil é obrigatório");
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento != null) {
            validarIdadeMinima(dataNascimento);
            this.dataNascimento = dataNascimento;
        }
    }

    public boolean isAdmin() {
        return perfil == Perfil.ADMIN;
    }

    public boolean aniversarioHoje(LocalDate referencia) {
        return dataNascimento != null
                && referencia != null
                && dataNascimento.getMonthValue() == referencia.getMonthValue()
                && dataNascimento.getDayOfMonth() == referencia.getDayOfMonth();
    }

    public boolean aniversarioNesteMes(LocalDate referencia) {
        return dataNascimento != null
                && referencia != null
                && dataNascimento.getMonthValue() == referencia.getMonthValue();
    }
}
