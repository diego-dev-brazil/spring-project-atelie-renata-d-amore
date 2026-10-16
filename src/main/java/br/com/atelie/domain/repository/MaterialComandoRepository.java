package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Material;

/**
 * Operações de ESCRITA/MUTABILIDADE de materiais (ISP).
 * Adicionar, editar e remover materiais.
 */
public interface MaterialComandoRepository {

    Material salvar(Material material);

    boolean excluir(Long id);
}

