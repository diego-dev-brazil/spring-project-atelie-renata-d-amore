package br.com.atelie.application.notificacoes;

import br.com.atelie.domain.model.Notificacao;
import br.com.atelie.domain.model.Notificacao.Categoria;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Serviço agregador das notificações importantes do sistema.
 *
 * Princípios SOLID aplicados:
 * - SRP: apenas coordena e filtra as notificações vindas dos provedores.
 * - OCP: recebe {@code List<ProvedorNotificacao>} injetada pelo Spring; novos
 *        tipos de avisos entram sem alterar uma linha desta classe.
 * - DIP: depende exclusivamente da abstração {@link ProvedorNotificacao}.
 */
@Service
public class NotificacaoService {

    private final List<ProvedorNotificacao> provedores;

    public NotificacaoService(List<ProvedorNotificacao> provedores) {
        this.provedores = provedores.stream()
                .sorted(Comparator.comparingInt(ProvedorNotificacao::ordem))
                .toList();
    }

    public List<Notificacao> listarTodas() {
        return provedores.stream()
                .flatMap(p -> p.gerarNotificacoes().stream())
                .toList();
    }

    public List<Notificacao> listarPorCategoria(String categoriaFiltro) {
        List<Notificacao> todas = listarTodas();
        if (categoriaFiltro == null || categoriaFiltro.isBlank() || "TODAS".equalsIgnoreCase(categoriaFiltro)) {
            return todas;
        }
        return todas.stream()
                .filter(n -> n.getCategoria().name().equalsIgnoreCase(categoriaFiltro))
                .toList();
    }

    public long contarPorCategoria(Categoria categoria) {
        return listarTodas().stream()
                .filter(n -> n.getCategoria() == categoria)
                .count();
    }

    public long contarUrgentes() {
        return listarTodas().stream()
                .filter(Notificacao::isUrgente)
                .count();
    }
}

