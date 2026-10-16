package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.OrdemProducao;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de LEITURA de ordens de produção (ISP).
 */
public interface ProducaoConsultaRepository {

    List<OrdemProducao> listarTodas();

    Optional<OrdemProducao> buscarPorId(Long id);
}

