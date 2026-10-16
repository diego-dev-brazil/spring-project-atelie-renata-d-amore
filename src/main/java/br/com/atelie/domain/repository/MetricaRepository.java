package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Metrica;
import br.com.atelie.domain.model.TipoMetrica;

/**
 * Contrato para buscar as métricas gerais do ateliê
 * (materiais, produtos, turmas, alunos, lucro).
 */
public interface MetricaRepository {

    Metrica buscar(TipoMetrica tipo);
}

