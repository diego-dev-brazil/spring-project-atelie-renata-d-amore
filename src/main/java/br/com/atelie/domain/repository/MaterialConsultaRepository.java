package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Material;

import java.util.List;
import java.util.Optional;

/**
 * Operações de LEITURA de materiais (ISP).
 * Qualquer serviço que só precise consultar o estoque usa esta interface.
 */
public interface MaterialConsultaRepository {

    List<Material> listarTodos();

    Optional<Material> buscarPorId(Long id);

    int somarQuantidadeTotal();

    List<Material> buscarMateriaisComEstoqueBaixo();
}

