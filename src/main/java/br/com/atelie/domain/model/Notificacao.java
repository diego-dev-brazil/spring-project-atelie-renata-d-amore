package br.com.atelie.domain.model;

/**
 * Representa uma notificação importante gerada dinamicamente pelo sistema:
 * - Falta de material em estoque / modelos travados na produção
 * - Pagamento atrasado de alunas(os)
 * - Aniversário de funcionários do ateliê
 */
public class Notificacao {

    public enum Categoria {
        FALTA_MATERIAL(
                "Falta de Material",
                "package-x",
                "bg-red-50 text-red-700 border-red-200",
                "bg-red-100 text-red-700"
        ),
        PAGAMENTO_ATRASADO(
                "Pagamento Atrasado",
                "credit-card",
                "bg-amber-50 text-amber-800 border-amber-200",
                "bg-amber-100 text-amber-800"
        ),
        ANIVERSARIO_FUNCIONARIO(
                "Aniversário da Equipe",
                "cake",
                "bg-teal-50 text-[#2d6a6a] border-teal-200",
                "bg-teal-100 text-[#2d6a6a]"
        );

        private final String descricao;
        private final String icone;
        private final String classesCartao;
        private final String classesBadge;

        Categoria(String descricao, String icone, String classesCartao, String classesBadge) {
            this.descricao = descricao;
            this.icone = icone;
            this.classesCartao = classesCartao;
            this.classesBadge = classesBadge;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getIcone() {
            return icone;
        }

        public String getClassesCartao() {
            return classesCartao;
        }

        public String getClassesBadge() {
            return classesBadge;
        }
    }

    private final Categoria categoria;
    private final String titulo;
    private final String mensagem;
    private final String detalhe;
    private final String linkDestino;
    private final String textoAcao;
    private final boolean urgente;

    public Notificacao(Categoria categoria,
                       String titulo,
                       String mensagem,
                       String detalhe,
                       String linkDestino,
                       String textoAcao,
                       boolean urgente) {
        this.categoria = categoria;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.detalhe = detalhe;
        this.linkDestino = linkDestino;
        this.textoAcao = textoAcao;
        this.urgente = urgente;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getDetalhe() {
        return detalhe;
    }

    public String getLinkDestino() {
        return linkDestino;
    }

    public String getTextoAcao() {
        return textoAcao;
    }

    public boolean isUrgente() {
        return urgente;
    }
}

