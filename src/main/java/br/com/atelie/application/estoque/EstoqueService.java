package br.com.atelie.application.estoque;

import br.com.atelie.domain.model.Material;
import br.com.atelie.domain.repository.MaterialComandoRepository;
import br.com.atelie.domain.repository.MaterialConsultaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Caso de Uso: Gerenciamento do Estoque e Materiais.
 *
 * SRP: Responsável unicamente pela regra de negócio e fluxo de materiais.
 * DIP: Depende das interfaces de repositório (MaterialConsultaRepository e MaterialComandoRepository),
 *      e não da implementação em memória.
 */
@Service
public class EstoqueService {

    private final MaterialConsultaRepository consultaRepository;
    private final MaterialComandoRepository comandoRepository;

    public EstoqueService(MaterialConsultaRepository consultaRepository,
                          MaterialComandoRepository comandoRepository) {
        this.consultaRepository = consultaRepository;
        this.comandoRepository = comandoRepository;
    }

    public List<Material> listarTodos() {
        return consultaRepository.listarTodos();
    }

    public Optional<Material> buscarPorId(Long id) {
        return consultaRepository.buscarPorId(id);
    }

    public int totalItensEmEstoque() {
        return consultaRepository.somarQuantidadeTotal();
    }

    public Material cadastrarOuAtualizar(Long id, String nome, String categoria, int quantidade, int quantidadeMinima, String unidadeMedida, BigDecimal precoUnitario) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do material não pode ser vazio.");
        }
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("A categoria é obrigatória.");
        }

        Material material = new Material(id, nome, categoria, quantidade, quantidadeMinima, unidadeMedida, precoUnitario);
        return comandoRepository.salvar(material);
    }

    public boolean excluir(Long id) {
        return comandoRepository.excluir(id);
    }
}

