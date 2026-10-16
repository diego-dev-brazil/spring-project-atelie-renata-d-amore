# Guia de Arquitetura e Princípios SOLID — Ateliê Renata D'Amore

Este documento explica como o projeto **Ateliê Renata D'Amore** foi refatorado de uma estrutura monolítica em React/TypeScript para uma arquitetura limpa em camadas utilizando **Java 17 + Spring Boot + Thymeleaf**, aplicando de ponta a ponta os **5 princípios do SOLID**.

---

## 1. Visão Geral da Refatoração (Antes vs. Depois)

### Como era antes (React / Next.js)
* O arquivo `app/dashboard/page.tsx` possuía **430 linhas** que misturavam em um único lugar:
  * Dados estáticos mocados (`mockStats`, `mockSalesData`, `mockAlerts`);
  * Regras de controle de acesso do menu (`adminOnly`);
  * Cálculos matemáticos de proporção de gráficos (`(data.value / maxSalesValue) * 200`);
  * Formatação de moeda (`Intl.NumberFormat`);
  * Marcação visual (HTML/JSX).
* **Problema de Engenharia de Software:** Baixa coesão e alto acoplamento. Qualquer alteração em regra de cálculo, adição de um novo indicador ou mudança na fonte de dados exigia editar o mesmo arquivo da interface gráfica.

### Como ficou no Java (Arquitetura em Camadas + SOLID)
O sistema foi dividido em **4 pacotes principais**, cada um com responsabilidades bem definidas:

```
br.com.atelie
├── domain           -> Entidades, Value Objects e Contratos (Interfaces de Repositório e Segurança)
├── application      -> Casos de Uso (Services), Indicadores Plugáveis e Estratégias de Formatação
├── infrastructure   -> Implementações técnicas (Repositórios em Memória e Criptografia SHA-256)
└── web              -> Controllers HTTP, Sessão, Interceptors de Segurança e Templates Thymeleaf
```

---

## 2. Os 5 Princípios do SOLID na Prática

### **[S] Single Responsibility Principle (Princípio da Responsabilidade Única)**
> *"Uma classe deve ter um, e apenas um, motivo para mudar."*

Cada responsabilidade que antes estava misturada na tela foi extraída para uma classe coesa e dedicada:

| Classe | Responsabilidade Única | Motivo para mudar |
| :--- | :--- | :--- |
| `AutenticacaoService` | Validar se login/senha conferem e se o funcionário está ativo. | Mudança na regra de autenticação. |
| `UsuarioService` | Gerenciar cadastro, bloqueio/liberação de acesso e exclusão de funcionários. | Mudança nas regras de gestão de equipe. |
| `ProducaoService` | Abrir/concluir ordens de produção e descontar materiais da Ficha Técnica no estoque. | Mudança no fluxo produtivo. |
| `CalculadoraGraficoBarras` | Calcular a altura proporcional (em pixels) das barras do gráfico mensal. | Mudança na escala ou cálculo do gráfico. |
| `FormatadorMoeda` | Formatar valores `BigDecimal` para o padrão monetário `R$ 1.234,56`. | Mudança na formatação monetária. |
| `AutenticacaoInterceptor` | Impedir acesso de usuários não logados às rotas internas. | Mudança na política de sessão. |
| `AdminAutorizacaoInterceptor` | Garantir que apenas o perfil `ADMIN` acesse `/usuarios`, `/relatorios` e `/configuracoes`. | Mudança nas permissões de administrador. |

---

### **[O] Open/Closed Principle (Princípio Aberto/Fechado)**
> *"Entidades de software devem estar abertas para extensão, mas fechadas para modificação."*

#### Exemplo 1: Cartões de Indicadores do Dashboard (`IndicadorDashboard`)
No `DashboardService`, os 6 cartões do topo do painel **não estão fixos no código do serviço**:

```java
public interface IndicadorDashboard {
    int ordem();
    CartaoIndicador gerarCartao();
}
```

O `DashboardService` recebe no construtor um `List<IndicadorDashboard>` injetado automaticamente pelo Spring:
* `MateriaisEmEstoqueIndicador` (ordem 1)
* `ProdutosDisponiveisIndicador` (ordem 2)
* `VendasDoMesIndicador` (ordem 3)
* `TurmasAtivasIndicador` (ordem 4)
* `AlunosCadastradosIndicador` (ordem 5)
* `LucroBrutoIndicador` (ordem 6)

**Por que isso respeita o OCP?**
Se amanhã a proprietária pedir um **7º cartão** no Dashboard (por exemplo, *"Ordens em Andamento"*), basta criar uma nova classe `OrdensEmAndamentoIndicador implements IndicadorDashboard` anotada com `@Component`. **Zero linhas** do `DashboardService`, do `DashboardController` ou do `dashboard.html` precisam ser modificadas!

#### Exemplo 2: Central de Notificações Plugável (`ProvedorNotificacao`)
No pacote `application.notificacoes`, cada tipo de aviso importante do sistema implementa a interface `ProvedorNotificacao`:
* `FaltaMaterialNotificacaoProvedor` (avisos de estoque baixo e modelos travados na produção)
* `PagamentoAtrasadoNotificacaoProvedor` (avisos de alunas com parcelas em atraso)
* `AniversarioFuncionarioNotificacaoProvedor` (avisos de aniversariantes do dia e do mês na equipe)

O `NotificacaoService` recebe `List<ProvedorNotificacao>` por injeção de dependência e agrega todas as mensagens automaticamente tanto no sino do cabeçalho quanto na aba `/notificacoes`. Para criar um novo tipo de notificação no futuro, basta criar uma nova classe `@Component implements ProvedorNotificacao` sem alterar o `NotificacaoService`.

#### Exemplo 3: Filtro de Permissões no Menu (`ItemMenu`)
Em vez de um `if (userRole == "admin")` espalhado pelo código, cada `ItemMenu` possui um `Set<Perfil> perfisPermitidos`:

```java
public boolean permitidoPara(Perfil perfil) {
    return perfisPermitidos.contains(perfil);
}
```
Se um novo perfil for criado no futuro (ex: `PROFESSORA`), a lógica de filtragem do `MenuService` permanece fechada para modificação.

---

### **[L] Liskov Substitution Principle (Princípio da Substituição de Liskov)**
> *"Subtipos devem poder substituir seus tipos base sem quebrar o funcionamento do programa."*

* No pacote `application.dashboard.indicadores`, temos a classe abstrata `IndicadorDeMetrica` (que implementa `IndicadorDashboard` usando o padrão *Template Method*) e diversas implementações diretas de `IndicadorDashboard` que buscam dados em tempo real dos repositórios (`MateriaisEmEstoqueIndicador`, `ProdutosDisponiveisIndicador`, `VendasDoMesIndicador`, `TurmasAtivasIndicador`, `AlunosCadastradosIndicador`, `LucroBrutoIndicador`).
* Inicialmente, vários indicadores herdavam de `IndicadorDeMetrica`. Quando evoluímos o sistema para conectar os módulos reais (`Estoque`, `Modelos`, `Turmas`, `Alunos`, `Vendas`), substituímos a implementação interna de cada indicador para consultar os respectivos repositórios.
* O consumidor (`DashboardService`) continuou chamando exatamente `indicador.gerarCartao()` sem precisar saber como cada subtipo calcula seu valor e sem sofrer efeitos colaterais.

Outro exemplo claro está em `FormatadorValor`:
* `FormatadorMoeda` (`R$ 1.247,00`) e `FormatadorQuantidade` (`1.247`) implementam `FormatadorValor` e podem ser substituídos livremente onde um `FormatadorValor` é esperado.

---

### **[I] Interface Segregation Principle (Princípio da Segregação de Interfaces)**
> *"Nenhum cliente deve ser forçado a depender de métodos que não utiliza."*

Em vez de criarmos uma única interface gigante para cada módulo (com métodos de leitura e escrita misturados), **segregamos os repositórios em interfaces de Consulta (Leitura) e Comando (Escrita)**:

| Módulo | Interface de Leitura (Consulta) | Interface de Escrita (Comando) |
| :--- | :--- | :--- |
| **Estoque** | `MaterialConsultaRepository` | `MaterialComandoRepository` |
| **Modelos de Bolsas** | `ModeloBolsaConsultaRepository` | `ModeloBolsaComandoRepository` |
| **Produção** | `ProducaoConsultaRepository` | `ProducaoComandoRepository` |
| **Vendas** | `VendaConsultaRepository` | `VendaRegistroRepository` |
| **Turmas** | `TurmaConsultaRepository` | `TurmaComandoRepository` |
| **Alunos** | `AlunoConsultaRepository` | `AlunoComandoRepository` |
| **Usuários** | `UsuarioRepository` | `UsuarioComandoRepository` |

**Onde isso brilha na prática?**
* O `DashboardService`, o `RelatorioService` e o `AlertaRepositoryEmMemoria` **só precisam ler dados**. Graças ao ISP, eles dependem exclusivamente das interfaces `*ConsultaRepository`. Eles sequer enxergam métodos como `salvar()` ou `excluir()`, evitando acoplamento desnecessário e impedindo que um relatório altere dados por acidente.
* Já os serviços de gestão (`EstoqueService`, `ModeloBolsaService`, `UsuarioService`, etc.) injetam ambas as interfaces para realizar o CRUD completo.

---

### **[D] Dependency Inversion Principle (Princípio da Inversão de Dependência)**
> *"Módulos de alto nível não devem depender de módulos de baixo nível. Ambos devem depender de abstrações."*

* **Nenhuma classe da camada `application` (alto nível) importa qualquer classe do pacote `infrastructure` (baixo nível).**
* Confira nos `import` de `AutenticacaoService`, `EstoqueService`, `ProducaoService`, `ModeloBolsaService`, `VendaService`, `TurmaService`, `AlunoService`, `UsuarioService` e `RelatorioService`: todos eles dependem **apenas das interfaces** definidas em `domain.repository` e `domain.seguranca`.

```java
// Alto nível (Application) depende apenas de Abstrações (Domain)
@Service
public class AutenticacaoService {
    private final UsuarioRepository usuarioRepository;     // Interface!
    private final CodificadorSenha codificadorSenha;       // Interface!
    ...
}
```

**Benefícios diretos para mostrar na apresentação:**
1. **Troca fácil de tecnologia:** Hoje os dados ficam em memória (`*RepositoryEmMemoria`) e a senha usa `Sha256CodificadorSenha`. Para migrar para banco de dados PostgreSQL/MySQL (com JPA) e criptografia `BCrypt`, basta criar novas classes em `infrastructure/` implementando as mesmas interfaces. **Nenhuma regra de negócio na camada `application` precisará ser alterada.**
2. **Testabilidade imediata:** Em `AutenticacaoServiceTest` e `DashboardServiceTest`, os testes unitários rodam em milissegundos passando implementações falsas (*fakes* / lambdas) pelo construtor, sem precisar subir banco de dados nem servidor web.

---

## 3. Roteiro Rápido de Demonstração para a Banca / Professora

1. **Login e Controle de Acesso (`/usuarios`):**
   * Entre como `admin` / `admin123`.
   * Mostre que o menu exibe 10 itens (incluindo *Relatórios*, *Usuários* e *Configurações*).
   * Vá em **Usuários**, clique em **Bloquear** na funcionária `Camila Souza` (`funcionaria`) e tente fazer login com ela em outra aba — o sistema barra a entrada na hora (`AutenticacaoService` + `Usuario::isAtivo`).
2. **Estoque -> Modelos -> Produção -> Dashboard (Fluxo Integrado):**
   * Vá em **Estoque e Materiais** (`/estoque`) e mostre os insumos e alertas de estoque mínimo.
   * Vá em **Modelos de Bolsas** (`/modelos`, logo acima de Produção) e mostre que cada modelo possui sua **Ficha Técnica** indicando quais materiais do estoque são gastos para fabricá-lo.
   * Vá em **Produção** (`/producao`), conclua uma ordem de produção e mostre que o sistema desconta automaticamente os materiais da Ficha Técnica lá no Estoque e atualiza o cartão **"Materiais em Estoque"** e os **Alertas** no Dashboard.
3. **Código e SOLID:**
   * Abra `DashboardService.java` e `IndicadorDashboard.java` para explicar **OCP** e **LSP**.
   * Abra `MaterialConsultaRepository.java` e `MaterialComandoRepository.java` (ou `RelatorioService.java`) para explicar **ISP**.
   * Abra `AutenticacaoService.java` e `AutenticacaoServiceTest.java` para explicar **SRP** e **DIP**.

