package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Turma;

/**
 * Contrato de ESCRITA de turmas (ISP).
 */
public interface TurmaComandoRepository {

    Turma salvar(Turma turma);

    boolean excluir(Long id);
}

