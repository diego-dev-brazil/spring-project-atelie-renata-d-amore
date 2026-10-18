package br.com.atelie.infrastructure.jpa;

import br.com.atelie.domain.model.Aluno;
import br.com.atelie.domain.model.ConfiguracaoAtelie;
import br.com.atelie.domain.model.ItemFichaTecnica;
import br.com.atelie.domain.model.Material;
import br.com.atelie.domain.model.ModeloBolsa;
import br.com.atelie.domain.model.OrdemProducao;
import br.com.atelie.domain.model.OrdemProducao.StatusProducao;
import br.com.atelie.domain.model.Perfil;
import br.com.atelie.domain.model.Turma;
import br.com.atelie.domain.model.Usuario;
import br.com.atelie.domain.model.Venda;
import br.com.atelie.domain.seguranca.CodificadorSenha;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Carrega os mesmos dados de demonstração que os repositórios em memória carregavam,
 * mas SOMENTE nas tabelas que estiverem vazias. Assim, ao reiniciar a aplicação,
 * nada que já esteja salvo no banco é duplicado ou sobrescrito.
 *
 * Os vínculos (ficha técnica, ordens, alunos) são resolvidos pelo NOME, e não por ids fixos,
 * porque o PostgreSQL pode gerar ids diferentes de 1, 2, 3...
 *
 * As senhas abaixo são apenas de demonstração: troque-as antes de qualquer uso real.
 */
@Component
@Profile("!memoria")
public class DadosIniciais implements CommandLineRunner {

    private final MaterialJpaRepository materiais;
    private final ModeloBolsaJpaRepository modelos;
    private final TurmaJpaRepository turmas;
    private final AlunoJpaRepository alunos;
    private final OrdemProducaoJpaRepository ordens;
    private final VendaJpaRepository vendas;
    private final UsuarioJpaRepository usuarios;
    private final ConfiguracaoJpaRepository configuracoes;
    private final CodificadorSenha codificador;

    public DadosIniciais(MaterialJpaRepository materiais,
                         ModeloBolsaJpaRepository modelos,
                         TurmaJpaRepository turmas,
                         AlunoJpaRepository alunos,
                         OrdemProducaoJpaRepository ordens,
                         VendaJpaRepository vendas,
                         UsuarioJpaRepository usuarios,
                         ConfiguracaoJpaRepository configuracoes,
                         CodificadorSenha codificador) {
        this.materiais = materiais;
        this.modelos = modelos;
        this.turmas = turmas;
        this.alunos = alunos;
        this.ordens = ordens;
        this.vendas = vendas;
        this.usuarios = usuarios;
        this.configuracoes = configuracoes;
        this.codificador = codificador;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (materiais.count() == 0) {
            carregarMateriais();
        }
        if (modelos.count() == 0) {
            carregarModelos();
        }
        if (ordens.count() == 0) {
            carregarOrdens();
        }
        if (turmas.count() == 0) {
            carregarTurmas();
        }
        if (alunos.count() == 0) {
            carregarAlunos();
        }
        if (vendas.count() == 0) {
            carregarVendas();
        }
        if (usuarios.count() == 0) {
            carregarUsuarios();
        }
        if (configuracoes.count() == 0) {
            carregarConfiguracao();
        }
    }

    private void carregarMateriais() {
        List<Material> lista = List.of(
                new Material(null, "Tecido Oxford Azul", "Tecidos", 15, 20, "metros", new BigDecimal("25.50")),
                new Material(null, "Linha de Costura Branca", "Linhas", 5, 10, "un", new BigDecimal("4.20")),
                new Material(null, "Linha de Costura Preta", "Linhas", 40, 10, "un", new BigDecimal("4.20")),
                new Material(null, "Zíper Dourado 30cm", "Aviamentos", 0, 15, "un", new BigDecimal("3.50")),
                new Material(null, "Zíper Comum Preto", "Aviamentos", 87, 20, "un", new BigDecimal("2.00")),
                new Material(null, "Tecido Tricoline Estampado", "Tecidos", 250, 50, "metros", new BigDecimal("32.00")),
                new Material(null, "Tecido Linho Cru", "Tecidos", 180, 40, "metros", new BigDecimal("45.00")),
                new Material(null, "Botões Amadeirados", "Aviamentos", 350, 100, "un", new BigDecimal("0.80")),
                new Material(null, "Alças de Couro Sintético", "Acessórios", 120, 30, "un", new BigDecimal("14.90")),
                new Material(null, "Manta Acrílica R2", "Estruturação", 200, 30, "metros", new BigDecimal("18.00"))
        );
        for (Material m : lista) {
            materiais.save(MaterialEntity.de(m));
        }
    }

    private void carregarModelos() {
        Map<String, MaterialEntity> porNome = materiaisPorNome();

        salvarModelo(new ModeloBolsa(null, "Bolsa Artesanal Premium",
                "Bolsa de ombro sofisticada com acabamento em couro e forro reforçado.",
                "Premium", new BigDecimal("450.00"), 180, List.of(
                item(porNome, "Tecido Oxford Azul", 2, "metros"),
                item(porNome, "Zíper Dourado 30cm", 1, "un"),
                item(porNome, "Alças de Couro Sintético", 2, "un"),
                item(porNome, "Manta Acrílica R2", 1, "metros"))));

        salvarModelo(new ModeloBolsa(null, "Bolsa Tote Casual",
                "Bolsa ampla e prática para o dia a dia, feita em tecido estampado leve.",
                "Casual", new BigDecimal("280.00"), 120, List.of(
                item(porNome, "Tecido Tricoline Estampado", 2, "metros"),
                item(porNome, "Zíper Comum Preto", 1, "un"),
                item(porNome, "Botões Amadeirados", 4, "un"))));

        salvarModelo(new ModeloBolsa(null, "Bolsa Clutch Festa",
                "Bolsa de mão para eventos e festas elegantes com estrutura firme.",
                "Festa", new BigDecimal("320.00"), 90, List.of(
                item(porNome, "Tecido Linho Cru", 1, "metros"),
                item(porNome, "Zíper Dourado 30cm", 1, "un"),
                item(porNome, "Manta Acrílica R2", 1, "metros"))));

        salvarModelo(new ModeloBolsa(null, "Necessaire Grande",
                "Necessaire multiuso com fechamento seguro em zíper.",
                "Acessórios", new BigDecimal("95.00"), 45, List.of(
                item(porNome, "Tecido Tricoline Estampado", 1, "metros"),
                item(porNome, "Zíper Comum Preto", 1, "un"))));
    }

    private void carregarOrdens() {
        Map<String, ModeloBolsaEntity> porNome = new HashMap<>();
        modelos.findAll().forEach(m -> porNome.put(m.nome, m));

        salvarOrdem(porNome, "Bolsa Tote Casual", 5, "Camila Souza", LocalDate.of(2024, 6, 15), StatusProducao.CONCLUIDA);
        salvarOrdem(porNome, "Necessaire Grande", 10, "Camila Souza", LocalDate.of(2024, 6, 18), StatusProducao.EM_ANDAMENTO);
        salvarOrdem(porNome, "Bolsa Artesanal Premium", 2, "Renata D'Amore", LocalDate.of(2024, 6, 20), StatusProducao.AGUARDANDO_MATERIAIS);
    }

    private void carregarTurmas() {
        List<Turma> lista = List.of(
                new Turma(null, "Costura Criativa Iniciante - Manhã", "Renata D'Amore", "Ter/Qui 09h às 11h", 12, new BigDecimal("280.00"), true),
                new Turma(null, "Confecção de Bolsas em Couro", "Renata D'Amore", "Seg/Qua 14h às 17h", 10, new BigDecimal("420.00"), true),
                new Turma(null, "Patchwork & Quilting Moderno", "Camila Souza", "Sex 09h às 12h", 15, new BigDecimal("310.00"), true),
                new Turma(null, "Modelagem de Bolsas Estruturadas", "Renata D'Amore", "Sáb 09h às 13h", 12, new BigDecimal("450.00"), true),
                new Turma(null, "Acabamentos & Ferragens Finas", "Camila Souza", "Qua 19h às 21h30", 10, new BigDecimal("290.00"), true),
                new Turma(null, "Workshop Necessaires & Carteiras", "Beatriz Martins", "Qui 14h às 16h30", 15, new BigDecimal("220.00"), true)
        );
        for (Turma t : lista) {
            turmas.save(TurmaEntity.de(t));
        }
    }

    private void carregarAlunos() {
        Map<String, TurmaEntity> porNome = new HashMap<>();
        turmas.findAll().forEach(t -> porNome.put(t.nome, t));

        salvarAluno(porNome, "Fernanda Lima", "(11) 98765-1122", "fernanda.lima@email.com", "Costura Criativa Iniciante - Manhã", 1);
        salvarAluno(porNome, "Roberta Dias", "(11) 99123-4455", "roberta.dias@email.com", "Confecção de Bolsas em Couro", 2);
        salvarAluno(porNome, "Mariana Costa", "(11) 97654-3322", "mariana.costa@email.com", "Modelagem de Bolsas Estruturadas", 0);
        salvarAluno(porNome, "Patrícia Albuquerque", "(11) 98222-7788", "patricia.alb@email.com", "Confecção de Bolsas em Couro", 0);
        salvarAluno(porNome, "Helena Vasconcelos", "(11) 99444-5566", "helena.v@email.com", "Patchwork & Quilting Moderno", 0);
        salvarAluno(porNome, "Larissa Mendes", "(11) 98111-9900", "larissa.mendes@email.com", "Workshop Necessaires & Carteiras", 0);
    }

    private void carregarVendas() {
        List<Venda> lista = List.of(
                new Venda(null, "Bolsa Tote Casual", "Lucia Ferreira", new BigDecimal("280"), LocalDate.of(2024, 6, 18)),
                new Venda(null, "Necessaire Grande", "Paula Costa", new BigDecimal("95"), LocalDate.of(2024, 6, 18)),
                new Venda(null, "Bolsa Clutch Festa", "Carla Santos", new BigDecimal("320"), LocalDate.of(2024, 6, 19)),
                new Venda(null, "Kit Costura Iniciante", "Ana Oliveira", new BigDecimal("180"), LocalDate.of(2024, 6, 19)),
                new Venda(null, "Bolsa Artesanal Premium", "Maria Silva", new BigDecimal("450"), LocalDate.of(2024, 6, 20))
        );
        for (Venda v : lista) {
            vendas.save(VendaEntity.de(v));
        }
    }

    private void carregarUsuarios() {
        LocalDate hoje = LocalDate.now();
        List<Usuario> lista = List.of(
                new Usuario("admin", "Renata D'Amore", "Proprietária & Diretora Criativa",
                        codificador.codificar("admin123"), Perfil.ADMIN, true, LocalDate.of(1985, 4, 12)),
                new Usuario("funcionaria", "Camila Souza", "Costureira Sênior",
                        codificador.codificar("func123"), Perfil.FUNCIONARIO, true,
                        LocalDate.of(1994, hoje.getMonthValue(), hoje.getDayOfMonth())),
                new Usuario("beatriz", "Beatriz Martins", "Atendimento & Vendas",
                        codificador.codificar("bia123"), Perfil.FUNCIONARIO, true,
                        LocalDate.of(1998, hoje.getMonthValue(), Math.min(28, hoje.getDayOfMonth() + 2))),
                new Usuario("juliana", "Juliana Rocha", "Auxiliar de Corte (Férias/Inativa)",
                        codificador.codificar("ju123"), Perfil.FUNCIONARIO, false, LocalDate.of(1996, 11, 20))
        );
        for (Usuario u : lista) {
            usuarios.save(UsuarioEntity.de(u));
        }
    }

    private void carregarConfiguracao() {
        configuracoes.save(ConfiguracaoEntity.de(new ConfiguracaoAtelie(
                "Ateliê Renata D'Amore",
                "Renata D'Amore",
                "34.567.890/0001-12",
                "(11) 3456-7890",
                "contato@atelierenatadamore.com.br",
                "Rua das Hortênsias, 420 - Jardim Paulista, São Paulo/SP",
                new BigDecimal("50000.00"),
                true)));
    }

    // ---------- auxiliares ----------

    private Map<String, MaterialEntity> materiaisPorNome() {
        Map<String, MaterialEntity> mapa = new HashMap<>();
        materiais.findAll().forEach(m -> mapa.put(m.nome, m));
        return mapa;
    }

    private ItemFichaTecnica item(Map<String, MaterialEntity> porNome, String nomeMaterial, int qtd, String unidade) {
        MaterialEntity material = porNome.get(nomeMaterial);
        if (material == null) {
            throw new IllegalStateException("Material de demonstração não encontrado: " + nomeMaterial);
        }
        return new ItemFichaTecnica(material.id, material.nome, qtd, unidade);
    }

    private void salvarModelo(ModeloBolsa modelo) {
        modelos.save(ModeloBolsaEntity.de(modelo));
    }

    private void salvarOrdem(Map<String, ModeloBolsaEntity> porNome, String nomeModelo, int pecas,
                             String responsavel, LocalDate data, StatusProducao status) {
        ModeloBolsaEntity modelo = porNome.get(nomeModelo);
        if (modelo == null) {
            return;
        }
        ordens.save(OrdemProducaoEntity.de(
                new OrdemProducao(null, modelo.id, modelo.nome, pecas, responsavel, data, status)));
    }

    private void salvarAluno(Map<String, TurmaEntity> porNome, String nome, String telefone, String email,
                             String nomeTurma, int parcelasPendentes) {
        TurmaEntity turma = porNome.get(nomeTurma);
        Long turmaId = turma != null ? turma.id : null;
        alunos.save(AlunoEntity.de(new Aluno(null, nome, telefone, email, turmaId, nomeTurma, parcelasPendentes)));
    }
}
