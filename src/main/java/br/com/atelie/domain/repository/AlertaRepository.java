package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Alerta;

import java.util.List;

/**
 * Contrato para obter os alertas ativos do ateliê.
 */
public interface AlertaRepository {

    List<Alerta> buscarAtivos();
}

