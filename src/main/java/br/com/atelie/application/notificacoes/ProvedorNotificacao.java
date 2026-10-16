package br.com.atelie.application.notificacoes;

import br.com.atelie.domain.model.Notificacao;

import java.util.List;

/**
 * Contrato para qualquer fonte geradora de notificações do sistema.
 *
 * Princípios SOLID aplicados:
 * - OCP (Open/Closed Principle): para adicionar uma nova categoria de notificação
 *   no futuro (ex: meta de vendas atingida), basta criar uma nova classe que
 *   implemente esta interface, sem alterar o {@link NotificacaoService}.
 * - LSP (Liskov Substitution Principle): todos os provedores cumprem o mesmo
 *   contrato e podem ser iterados polimorficamente.
 * - ISP (Interface Segregation Principle): interface enxuta focada apenas em
 *   fornecer notificações.
 * - DIP (Dependency Inversion Principle): o serviço de alto nível depende desta
 *   abstração e não das regras concretas de cada módulo.
 */
public interface ProvedorNotificacao {

    /**
     * Ordem de prioridade de exibição (menor valor aparece primeiro).
     */
    int ordem();

    /**
     * Gera a lista atualizada de notificações desta categoria.
     */
    List<Notificacao> gerarNotificacoes();
}

