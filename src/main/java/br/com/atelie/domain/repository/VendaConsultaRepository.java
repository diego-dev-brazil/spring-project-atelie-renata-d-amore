package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Venda;
import br.com.atelie.domain.model.VendaMensal;

import java.util.List;

/**
 * Operações de LEITURA de vendas.
 *
 * ISP: separamos leitura ({@code VendaConsultaRepository}) de escrita
 * ({@link VendaRegistroRepository}). O painel só precisa ler, então ele
 * depende apenas desta interface pequena e não "enxerga" métodos de gravar.
 */
public interface VendaConsultaRepository {

    List<Venda> buscarRecentes(int quantidade);

    List<VendaMensal> totaisPorMes();

    default List<Venda> listarTodas() {
        return buscarRecentes(Integer.MAX_VALUE);
    }
}
