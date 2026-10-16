package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.OrdemProducao;

/**
 * Contrato de ESCRITA de ordens de produção (ISP).
 */
public interface ProducaoComandoRepository {

    OrdemProducao salvar(OrdemProducao ordem);

    boolean excluir(Long id);
}

