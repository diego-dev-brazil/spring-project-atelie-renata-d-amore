package br.com.atelie.application.modelos;

import br.com.atelie.domain.model.ItemFichaTecnica;
import br.com.atelie.domain.model.Material;
import br.com.atelie.domain.model.ModeloBolsa;
import br.com.atelie.domain.repository.MaterialConsultaRepository;
import br.com.atelie.domain.repository.ModeloBolsaComandoRepository;
import br.com.atelie.domain.repository.ModeloBolsaConsultaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Caso de Uso: Gerenciamento de Modelos de Bolsas e suas Fichas Técnicas.
 *
 * SRP: Centraliza as regras de negócio de modelos e consumo de materiais.
 * DIP: Depende apenas das interfaces de repositório.
 */
@Service
public class ModeloBolsaService {

    private final ModeloBolsaConsultaRepository modeloConsultaRepository;
    private final ModeloBolsaComandoRepository modeloComandoRepository;
    private final MaterialConsultaRepository materialConsultaRepository;

    public ModeloBolsaService(ModeloBolsaConsultaRepository modeloConsultaRepository,
                              ModeloBolsaComandoRepository modeloComandoRepository,
                              MaterialConsultaRepository materialConsultaRepository) {
        this.modeloConsultaRepository = modeloConsultaRepository;
        this.modeloComandoRepository = modeloComandoRepository;
        this.materialConsultaRepository = materialConsultaRepository;
    }

    public List<ModeloBolsa> listarTodos() {
        return modeloConsultaRepository.listarTodos();
    }

    public Optional<ModeloBolsa> buscarPorId(Long id) {
        return modeloConsultaRepository.buscarPorId(id);
    }

    public int totalModelos() {
        return modeloConsultaRepository.contarModelos();
    }

    public ModeloBolsa cadastrarOuAtualizar(Long id, String nome, String descricao, String categoria,
                                           BigDecimal precoVenda, int tempoProducaoMinutos,
                                           List<Long> materialIds, List<Integer> quantidades) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do modelo é obrigatório.");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("A categoria é obrigatória.");
        }

        List<ItemFichaTecnica> fichaTecnica = new ArrayList<>();
        if (materialIds != null && quantidades != null) {
            for (int i = 0; i < materialIds.size(); i++) {
                Long mId = materialIds.get(i);
                if (mId == null) continue;

                int qtd = (i < quantidades.size() && quantidades.get(i) != null) ? quantidades.get(i) : 1;
                Optional<Material> materialOpt = materialConsultaRepository.buscarPorId(mId);
                if (materialOpt.isPresent()) {
                    Material mat = materialOpt.get();
                    fichaTecnica.add(new ItemFichaTecnica(mat.getId(), mat.getNome(), qtd, mat.getUnidadeMedida()));
                }
            }
        }

        ModeloBolsa modelo;
        if (id != null) {
            Optional<ModeloBolsa> existente = modeloConsultaRepository.buscarPorId(id);
            if (existente.isPresent()) {
                modelo = existente.get();
                modelo.setNome(nome);
                modelo.setDescricao(descricao);
                modelo.setCategoria(categoria);
                modelo.setPrecoVenda(precoVenda);
                modelo.setTempoProducaoMinutos(tempoProducaoMinutos);
                modelo.setFichaTecnica(fichaTecnica);
            } else {
                modelo = new ModeloBolsa(id, nome, descricao, categoria, precoVenda, tempoProducaoMinutos, fichaTecnica);
            }
        } else {
            modelo = new ModeloBolsa(null, nome, descricao, categoria, precoVenda, tempoProducaoMinutos, fichaTecnica);
        }

        return modeloComandoRepository.salvar(modelo);
    }

    public boolean excluir(Long id) {
        return modeloComandoRepository.excluir(id);
    }
}

