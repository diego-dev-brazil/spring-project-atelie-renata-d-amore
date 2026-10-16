package br.com.atelie.application.dashboard;

import br.com.atelie.application.dashboard.dto.CartaoIndicador;

/**
 * Um cartão de indicador do painel (Materiais, Vendas do Mês, Lucro...).
 *
 * OCP: o {@link DashboardService} recebe uma LISTA de IndicadorDashboard
 *      injetada pelo Spring. Para adicionar um cartão novo, basta criar uma
 *      classe @Component que implemente esta interface. Nenhuma linha do
 *      serviço ou do HTML precisa mudar.
 * LSP: qualquer implementação pode substituir outra — o serviço só chama
 *      {@link #ordem()} e {@link #gerarCartao()} e espera um cartão válido.
 */
public interface IndicadorDashboard {

    /** Posição do cartão na tela (menor aparece primeiro). */
    int ordem();

    CartaoIndicador gerarCartao();
}

