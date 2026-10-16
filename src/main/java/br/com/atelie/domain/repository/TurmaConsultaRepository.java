package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Turma;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de LEITURA de turmas (ISP).
 */
public interface TurmaConsultaRepository {

    List<Turma> listarTodas();

    Optional<Turma> buscarPorId(Long id);

    int contarAtivas();
}

