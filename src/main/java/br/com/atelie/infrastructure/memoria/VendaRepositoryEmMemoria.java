package br.com.atelie.infrastructure.memoria;

import br.com.atelie.domain.model.Venda;
import br.com.atelie.domain.model.VendaMensal;
import br.com.atelie.domain.repository.VendaConsultaRepository;
import br.com.atelie.domain.repository.VendaRegistroRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Vendas guardadas em memória.
 *
 * Uma única classe implementa as DUAS interfaces (consulta e registro),
 * mas cada cliente enxerga só a parte que precisa (ISP).
 * Quando novas vendas são adicionadas ou excluídas, o total do mês atual (Jun)
 * reflete dinamicamente as alterações no painel principal e gráficos!
 */
@Repository
public class VendaRepositoryEmMemoria implements VendaConsultaRepository, VendaRegistroRepository {

    private final List<Venda> vendas = new CopyOnWriteArrayList<>();
    private final AtomicLong sequencia = new AtomicLong();

    private static final BigDecimal BASE_JUNHO_ANTERIOR = new BigDecimal("44355");

    public VendaRepositoryEmMemoria() {
        registrar(new Venda(null, "Bolsa Tote Casual", "Lucia Ferreira", new BigDecimal("280"), LocalDate.of(2024, 6, 18)));
        registrar(new Venda(null, "Necessaire Grande", "Paula Costa", new BigDecimal("95"), LocalDate.of(2024, 6, 18)));
        registrar(new Venda(null, "Bolsa Clutch Festa", "Carla Santos", new BigDecimal("320"), LocalDate.of(2024, 6, 19)));
        registrar(new Venda(null, "Kit Costura Iniciante", "Ana Oliveira", new BigDecimal("180"), LocalDate.of(2024, 6, 19)));
        registrar(new Venda(null, "Bolsa Artesanal Premium", "Maria Silva", new BigDecimal("450"), LocalDate.of(2024, 6, 20)));
    }

    @Override
    public Venda registrar(Venda venda) {
        Venda salva = new Venda(
                sequencia.incrementAndGet(),
                venda.produto(),
                venda.cliente(),
                venda.valor(),
                venda.data() != null ? venda.data() : LocalDate.now()
        );
        vendas.add(salva);
        return salva;
    }

    @Override
    public boolean excluir(Long id) {
        if (id == null) {
            return false;
        }
        return vendas.removeIf(v -> v.id().equals(id));
    }

    @Override
    public List<Venda> buscarRecentes(int quantidade) {
        return vendas.stream()
                .sorted(Comparator.comparing(Venda::data).thenComparing(Venda::id).reversed())
                .limit(quantidade)
                .toList();
    }

    @Override
    public List<Venda> listarTodas() {
        return vendas.stream()
                .sorted(Comparator.comparing(Venda::data).thenComparing(Venda::id).reversed())
                .toList();
    }

    @Override
    public List<VendaMensal> totaisPorMes() {
        BigDecimal somaRegistradas = vendas.stream()
                .map(Venda::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalMesAtual = BASE_JUNHO_ANTERIOR.add(somaRegistradas);

        List<VendaMensal> meses = new ArrayList<>();
        meses.add(new VendaMensal("Jan", new BigDecimal("28000")));
        meses.add(new VendaMensal("Fev", new BigDecimal("32000")));
        meses.add(new VendaMensal("Mar", new BigDecimal("29000")));
        meses.add(new VendaMensal("Abr", new BigDecimal("38000")));
        meses.add(new VendaMensal("Mai", new BigDecimal("42000")));
        meses.add(new VendaMensal("Jun", totalMesAtual));
        return meses;
    }
}
