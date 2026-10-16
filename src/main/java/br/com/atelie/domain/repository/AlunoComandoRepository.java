package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Aluno;

/**
 * Contrato de ESCRITA de alunos (ISP).
 */
public interface AlunoComandoRepository {

    Aluno salvar(Aluno aluno);

    boolean excluir(Long id);
}

