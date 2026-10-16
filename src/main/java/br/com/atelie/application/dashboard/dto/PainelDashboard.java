package br.com.atelie.application.dashboard.dto;

import br.com.atelie.domain.model.Alerta;

import java.util.List;

/**
 * Tudo que a tela do painel precisa, montado em um único objeto.
 */
public record PainelDashboard(
        List<CartaoIndicador> cartoes,
        List<BarraGrafico> grafico,
        List<Alerta> alertas,
        List<VendaResumo> vendasRecentes
) {
}

