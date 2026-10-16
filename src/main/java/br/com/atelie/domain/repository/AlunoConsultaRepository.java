package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Aluno;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de LEITURA de alunos (ISP).
 */
public interface AlunoConsultaRepository {

    List<Aluno> listarTodos();

    Optional<Aluno> buscarPorId(Long id);

    int contarTotal();

    List<Aluno> buscarInadimplentes();
}

