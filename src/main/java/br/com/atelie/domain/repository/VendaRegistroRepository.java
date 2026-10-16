package br.com.atelie.domain.repository;

import br.com.atelie.domain.model.Venda;

/**
 * Operações de ESCRITA de vendas (usada pela tela de Vendas).
 *
 * ISP: quem só lê vendas (o painel) não depende desta interface.
 */
public interface VendaRegistroRepository {

    Venda registrar(Venda venda);

    default boolean excluir(Long id) {
        return false;
    }
}
