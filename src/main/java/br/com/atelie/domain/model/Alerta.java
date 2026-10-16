package br.com.atelie.domain.model;

/**
 * Alerta exibido no painel (estoque baixo, pagamento atrasado, produção bloqueada).
 */
public record Alerta(TipoAlerta tipo, String mensagem, Severidade severidade) {

    public boolean isAlta() {
        return severidade == Severidade.ALTA;
    }

    public enum TipoAlerta {
        ESTOQUE, PAGAMENTO, PRODUCAO
    }

    public enum Severidade {
        ALTA, MEDIA
    }
}

