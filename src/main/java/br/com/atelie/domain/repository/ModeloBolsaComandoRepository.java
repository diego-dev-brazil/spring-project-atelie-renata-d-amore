package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.ModeloBolsa;

/**
 * Operações de ESCRITA/MUTABILIDADE de modelos de bolsas (ISP).
 */
public interface ModeloBolsaComandoRepository {

    ModeloBolsa salvar(ModeloBolsa modelo);

    boolean excluir(Long id);
}

