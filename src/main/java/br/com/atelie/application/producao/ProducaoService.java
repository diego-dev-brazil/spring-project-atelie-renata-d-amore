package br.com.atelie.application.producao;

import br.com.atelie.domain.model.ItemFichaTecnica;
import br.com.atelie.domain.model.Material;
import br.com.atelie.domain.model.ModeloBolsa;
import br.com.atelie.domain.model.OrdemProducao;
import br.com.atelie.domain.model.OrdemProducao.StatusProducao;
import br.com.atelie.domain.repository.MaterialComandoRepository;
import br.com.atelie.domain.repository.MaterialConsultaRepository;
import br.com.atelie.domain.repository.ModeloBolsaConsultaRepository;
import br.com.atelie.domain.repository.ProducaoComandoRepository;
import br.com.atelie.domain.repository.ProducaoConsultaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Caso de Uso: Gestão de Ordens de Produção e Baixa Automática de Insumos do Estoque.
 *
 * SRP: Orquestra a criação e conclusão de ordens de produção validando a ficha técnica.
 * DIP: Depende exclusivamente das abstrações de repositórios.
 */
@Service
public class ProducaoService {

    private final ProducaoConsultaRepository producaoConsulta;
    private final ProducaoComandoRepository producaoComando;
    private final ModeloBolsaConsultaRepository modeloConsulta;
    private final MaterialConsultaRepository materialConsulta;
    private final MaterialComandoRepository materialComando;

    public ProducaoService(ProducaoConsultaRepository producaoConsulta,
                           ProducaoComandoRepository producaoComando,
                           ModeloBolsaConsultaRepository modeloConsulta,
                           MaterialConsultaRepository materialConsulta,
                           MaterialComandoRepository materialComando) {
        this.producaoConsulta = producaoConsulta;
        this.producaoComando = producaoComando;
        this.modeloConsulta = modeloConsulta;
        this.materialConsulta = materialConsulta;
        this.materialComando = materialComando;
    }

    public List<OrdemProducao> listarTodas() {
        return producaoConsulta.listarTodas();
    }

    public OrdemProducao abrirOrdem(Long modeloId, int quantidadePecas, String responsavel) {
        ModeloBolsa modelo = modeloConsulta.buscarPorId(modeloId)
                .orElseThrow(() -> new IllegalArgumentException("Modelo de bolsa não encontrado."));

        List<String> faltantes = verificarMateriaisFaltantes(modelo, quantidadePecas);
        StatusProducao statusInicial = faltantes.isEmpty() ? StatusProducao.EM_ANDAMENTO : StatusProducao.AGUARDANDO_MATERIAIS;

        OrdemProducao nova = new OrdemProducao(
                null,
                modelo.getId(),
                modelo.getNome(),
                quantidadePecas,
                responsavel,
                LocalDate.now(),
                statusInicial
        );
        return producaoComando.salvar(nova);
    }

    /**
     * Conclui uma ordem de produção e desconta automaticamente os materiais da Ficha Técnica no estoque.
     */
    public void concluirOrdem(Long ordemId) {
        OrdemProducao ordem = producaoConsulta.buscarPorId(ordemId)
                .orElseThrow(() -> new IllegalArgumentException("Ordem de produção não encontrada."));

        if (ordem.isConcluida()) {
            return;
        }

        Optional<ModeloBolsa> modeloOpt = modeloConsulta.buscarPorId(ordem.getModeloId());
        if (modeloOpt.isPresent()) {
            ModeloBolsa modelo = modeloOpt.get();
            List<String> faltantes = verificarMateriaisFaltantes(modelo, ordem.getQuantidadePecas());
            if (!faltantes.isEmpty()) {
                ordem.setStatus(StatusProducao.AGUARDANDO_MATERIAIS);
                producaoComando.salvar(ordem);
                throw new IllegalStateException("Estoque insuficiente para concluir produção. Faltam: " + String.join(", ", faltantes));
            }

            // Desconta cada material da ficha técnica no estoque real
            for (ItemFichaTecnica item : modelo.getFichaTecnica()) {
                materialConsulta.buscarPorId(item.getMaterialId()).ifPresent(mat -> {
                    int consumoTotal = item.getQuantidadeNecessaria() * ordem.getQuantidadePecas();
                    mat.setQuantidade(mat.getQuantidade() - consumoTotal);
                    materialComando.salvar(mat);
                });
            }
        }

        ordem.setStatus(StatusProducao.CONCLUIDA);
        producaoComando.salvar(ordem);
    }

    public void excluir(Long id) {
        producaoComando.excluir(id);
    }

    public List<String> verificarMateriaisFaltantes(ModeloBolsa modelo, int quantidadePecas) {
        List<String> faltantes = new ArrayList<>();
        for (ItemFichaTecnica item : modelo.getFichaTecnica()) {
            int necessario = item.getQuantidadeNecessaria() * quantidadePecas;
            Optional<Material> matOpt = materialConsulta.buscarPorId(item.getMaterialId());
            if (matOpt.isEmpty() || matOpt.get().getQuantidade() < necessario) {
                faltantes.add(item.getMaterialNome() + " (necessário: " + necessario + ")");
            }
        }
        return faltantes;
    }
}

