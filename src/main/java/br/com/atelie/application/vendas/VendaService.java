package br.com.atelie.application.vendas;

import br.com.atelie.domain.model.Venda;
import br.com.atelie.domain.model.VendaMensal;
import br.com.atelie.domain.repository.VendaConsultaRepository;
import br.com.atelie.domain.repository.VendaRegistroRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Caso de Uso: Gestão de Vendas do Ateliê.
 *
 * SRP: Regras de negócio para registrar, consultar e excluir vendas.
 * DIP: Depende de {@link VendaConsultaRepository} e {@link VendaRegistroRepository}.
 */
@Service
public class VendaService {

    private final VendaConsultaRepository consultaRepository;
    private final VendaRegistroRepository registroRepository;

    public VendaService(VendaConsultaRepository consultaRepository,
                        VendaRegistroRepository registroRepository) {
        this.consultaRepository = consultaRepository;
        this.registroRepository = registroRepository;
    }

    public List<Venda> listarTodas() {
        return consultaRepository.listarTodas();
    }

    public BigDecimal totalVendasMesAtual() {
        List<VendaMensal> meses = consultaRepository.totaisPorMes();
        return meses.isEmpty() ? BigDecimal.ZERO : meses.get(meses.size() - 1).total();
    }

    public Venda registrarVenda(String produto, String cliente, BigDecimal valor, LocalDate data) {
        if (produto == null || produto.isBlank()) {
            throw new IllegalArgumentException("O produto vendido é obrigatório.");
        }
        if (cliente == null || cliente.isBlank()) {
            throw new IllegalArgumentException("O nome do cliente é obrigatório.");
        }
        if (valor == null || valor.signum() <= 0) {
            throw new IllegalArgumentException("O valor da venda deve ser maior que zero.");
        }
        LocalDate dataVenda = data != null ? data : LocalDate.now();
        return registroRepository.registrar(new Venda(null, produto.trim(), cliente.trim(), valor, dataVenda));
    }

    public boolean excluir(Long id) {
        return registroRepository.excluir(id);
    }
}

