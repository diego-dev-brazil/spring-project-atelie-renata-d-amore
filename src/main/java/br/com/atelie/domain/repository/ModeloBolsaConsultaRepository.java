package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.ModeloBolsa;

import java.util.List;
import java.util.Optional;

/**
 * Operações de LEITURA de modelos de bolsas (ISP).
 */
public interface ModeloBolsaConsultaRepository {

    List<ModeloBolsa> listarTodos();

    Optional<ModeloBolsa> buscarPorId(Long id);

    int contarModelos();
}

