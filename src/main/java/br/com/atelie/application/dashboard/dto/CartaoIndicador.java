package br.com.atelie.application.dashboard.dto;

import br.com.atelie.domain.model.Variacao;

/**
 * Dados prontos para desenhar um cartão do painel.
 * (DTO = objeto só para transportar dados até a tela.)
 */
public record CartaoIndicador(String titulo, String valor, String icone, Variacao variacao) {
}

