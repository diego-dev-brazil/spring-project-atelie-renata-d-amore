package br.com.atelie.application.notificacoes;

import br.com.atelie.domain.model.ItemFichaTecnica;
import br.com.atelie.domain.model.Material;
import br.com.atelie.domain.model.ModeloBolsa;
import br.com.atelie.domain.model.Notificacao;
import br.com.atelie.domain.model.Notificacao.Categoria;
import br.com.atelie.domain.repository.MaterialConsultaRepository;
import br.com.atelie.domain.repository.ModeloBolsaConsultaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Gera notificações sobre falta de materiais em estoque e modelos de bolsas
 * cuja produção está bloqueada por falta de insumo (SRP + OCP + DIP).
 */
@Component
public class FaltaMaterialNotificacaoProvedor implements ProvedorNotificacao {

    private final MaterialConsultaRepository materialConsulta;
    private final ModeloBolsaConsultaRepository modeloConsulta;

    public FaltaMaterialNotificacaoProvedor(MaterialConsultaRepository materialConsulta,
                                            ModeloBolsaConsultaRepository modeloConsulta) {
        this.materialConsulta = materialConsulta;
        this.modeloConsulta = modeloConsulta;
    }

    @Override
    public int ordem() {
        return 1;
    }

    @Override
    public List<Notificacao> gerarNotificacoes() {
        List<Notificacao> lista = new ArrayList<>();

        // 1. Materiais abaixo do estoque mínimo ou esgotados
        for (Material m : materialConsulta.buscarMateriaisComEstoqueBaixo()) {
            boolean esgotado = m.getQuantidade() == 0;
            String titulo = esgotado
                    ? "Material Esgotado: " + m.getNome()
                    : "Estoque Baixo: " + m.getNome();
            String mensagem = esgotado
                    ? "O insumo " + m.getNome() + " (" + m.getCategoria() + ") zerou no estoque."
                    : "Restam apenas " + m.getQuantidade() + " " + m.getUnidadeMedida()
                      + " em estoque (mínimo recomendado: " + m.getQuantidadeMinima() + " " + m.getUnidadeMedida() + ").";
            String detalhe = "Categoria: " + m.getCategoria() + " • Mínimo: " + m.getQuantidadeMinima() + " " + m.getUnidadeMedida();

            lista.add(new Notificacao(
                    Categoria.FALTA_MATERIAL,
                    titulo,
                    mensagem,
                    detalhe,
                    "/estoque",
                    "Repor no Estoque",
                    esgotado || m.getQuantidade() <= Math.max(1, m.getQuantidadeMinima() / 2)
            ));
        }

        // 2. Modelos de bolsas travados por falta de material na ficha técnica
        for (ModeloBolsa modelo : modeloConsulta.listarTodos()) {
            for (ItemFichaTecnica item : modelo.getFichaTecnica()) {
                Optional<Material> matOpt = materialConsulta.buscarPorId(item.getMaterialId());
                int disponivel = matOpt.map(Material::getQuantidade).orElse(0);
                if (disponivel < item.getQuantidadeNecessaria()) {
                    String titulo = "Produção Bloqueada: " + modelo.getNome();
                    String mensagem = "Falta " + item.getMaterialNome() + " para produzir 1 un. de "
                            + modelo.getNome() + " (necessário: " + item.getQuantidadeNecessaria()
                            + " " + item.getUnidadeMedida() + ", disponível: " + disponivel + ").";
                    lista.add(new Notificacao(
                            Categoria.FALTA_MATERIAL,
                            titulo,
                            mensagem,
                            "Ficha Técnica • " + modelo.getCategoria(),
                            "/modelos",
                            "Ver Ficha Técnica",
                            true
                    ));
                }
            }
        }

        return lista;
    }
}
