package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.Venda;
import br.com.atelie.domain.model.VendaMensal;
import br.com.atelie.domain.repository.VendaConsultaRepository;
import br.com.atelie.domain.repository.VendaRegistroRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
@Profile("!memoria")
public class VendaRepositoryJpa implements VendaConsultaRepository, VendaRegistroRepository {

    /** Mesmo valor-base de demonstração usado na versão em memória. */
    private static final BigDecimal BASE_JUNHO_ANTERIOR = new BigDecimal("44355");

    private static final Comparator<Venda> MAIS_RECENTES_PRIMEIRO =
            Comparator.comparing(Venda::data).thenComparing(Venda::id).reversed();

    private final VendaJpaRepository jpa;

    public VendaRepositoryJpa(VendaJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Venda registrar(Venda venda) {
        VendaEntity salva = jpa.save(VendaEntity.de(new Venda(null, venda.produto(), venda.cliente(),
                venda.valor(), venda.data())));
        return salva.paraDominio();
    }

    @Override
    public boolean excluir(Long id) {
        if (id == null || !jpa.existsById(id)) {
            return false;
        }
        jpa.deleteById(id);
        return true;
    }

    @Override
    public List<Venda> buscarRecentes(int quantidade) {
        return jpa.findAll().stream()
                .map(VendaEntity::paraDominio)
                .sorted(MAIS_RECENTES_PRIMEIRO)
                .limit(quantidade)
                .toList();
    }

    @Override
    public List<Venda> listarTodas() {
        return jpa.findAll().stream()
                .map(VendaEntity::paraDominio)
                .sorted(MAIS_RECENTES_PRIMEIRO)
                .toList();
    }

    /**
     * ATENÇÃO: mantém o comportamento de demonstração da versão em memória
     * (Jan–Mai fixos e Jun = base + soma de todas as vendas). Para um relatório real,
     * trocar por uma consulta agrupada por mês (GROUP BY) sobre a coluna data_venda.
     */
    @Override
    public List<VendaMensal> totaisPorMes() {
        BigDecimal somaRegistradas = jpa.findAll().stream()
                .map(v -> v.valor)
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
