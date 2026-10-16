# Ateliê Renata D'Amore — Sistema de Gestão

Sistema de gestão desenvolvido para o **Ateliê Renata D'Amore**, com foco na organização dos processos administrativos e operacionais de um negócio de confecção de bolsas.

O projeto foi desenvolvido no contexto da disciplina de **Engenharia de Software III**, utilizando Java e Spring Boot para estruturar uma aplicação web com arquitetura em camadas, separação de responsabilidades e aplicação prática dos cinco princípios SOLID.

A aplicação reúne funcionalidades de gestão de estoque, modelos de bolsas, produção, vendas, usuários, alunos, turmas e relatórios, integradas a um dashboard administrativo.

Além de atender às necessidades operacionais do negócio, o projeto explora conceitos importantes de engenharia de software, como inversão de dependência, abstração, interfaces segregadas, extensibilidade e testabilidade.

## ✨ Objetivos

O projeto tem como objetivos:

- Desenvolver uma aplicação web para centralizar os processos de gestão do ateliê.
- Aplicar os conceitos de Programação Orientada a Objetos utilizando Java.
- Utilizar Spring Boot para estruturar uma aplicação web.
- Implementar uma arquitetura em camadas, separando domínio, regras de negócio, infraestrutura e interface.
- Aplicar os cinco princípios SOLID para melhorar a organização, a manutenção e a evolução do código.
- Implementar mecanismos de autenticação e controle de acesso por perfil.
- Integrar os módulos operacionais para manter informações consistentes entre estoque, modelos, produção e vendas.
- Desenvolver indicadores gerenciais para acompanhar o desempenho do negócio.
- Explorar interfaces e abstrações para facilitar testes e futuras mudanças tecnológicas.
- Utilizar testes automatizados para verificar o comportamento dos componentes da aplicação.

## 🛠 Tecnologias e Ferramentas

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework backend | Spring Boot 3.5.4 |
| Aplicação web | Spring Web / Spring MVC |
| Templates HTML | Thymeleaf |
| Gerenciamento de dependências | Maven |
| Testes | JUnit 5, Spring Boot Test e MockMvc |
| Arquitetura | Arquitetura em camadas |
| Princípios de design | SOLID |
| Controle de versão | Git e GitHub |

### Principais componentes

**Java 17**

Utilizado na implementação das entidades, serviços, interfaces, regras de negócio e demais componentes da aplicação.

**Spring Boot**

Responsável pela configuração e execução da aplicação, gerenciamento dos componentes e integração entre as camadas.

**Spring MVC**

Permite estruturar os controladores responsáveis por receber requisições HTTP e encaminhar as operações aos serviços correspondentes.

**Thymeleaf**

Motor de templates utilizado para renderizar as páginas HTML da aplicação a partir do backend Java.

**Maven**

Gerencia as dependências e o processo de compilação e execução do projeto.

**JUnit 5 e MockMvc**

Ferramentas utilizadas no ecossistema de testes do projeto para verificar comportamentos da aplicação e interações com os endpoints web.

## 🧩 Funcionalidades

### 📊 Dashboard Administrativo

O dashboard concentra indicadores e informações relevantes para acompanhar a operação do ateliê.

Entre os indicadores previstos na implementação estão:

- Quantidade de materiais em estoque.
- Produtos disponíveis.
- Vendas do mês.
- Turmas ativas.
- Alunos cadastrados.
- Lucro bruto.

Os indicadores são organizados por componentes específicos, permitindo que novos indicadores sejam adicionados sem exigir alterações na lógica central do dashboard.

Além dos indicadores, o sistema reúne notificações relacionadas a situações operacionais que exigem atenção.

### 📦 Gestão de Estoque

O módulo de estoque permite organizar os materiais utilizados nas atividades do ateliê.

Entre as funcionalidades contempladas estão:

- Consulta de materiais cadastrados.
- Acompanhamento das quantidades disponíveis.
- Identificação de materiais abaixo do estoque mínimo.
- Exibição de alertas relacionados à disponibilidade de insumos.
- Integração com o processo produtivo.
- Atualização das quantidades de materiais conforme o consumo na produção.

A integração com o módulo de produção permite que o consumo de materiais seja refletido no estoque, reduzindo a necessidade de atualizações manuais.

### 👜 Gestão de Modelos de Bolsas

O módulo de modelos organiza os produtos fabricados pelo ateliê.

Cada modelo pode possuir uma **ficha técnica**, que descreve os materiais necessários para sua fabricação.

Essa estrutura permite relacionar os produtos aos insumos utilizados na produção.

A ficha técnica é importante para:

- Identificar os materiais necessários para fabricar cada modelo.
- Calcular o consumo de insumos durante a produção.
- Integrar o cadastro dos modelos ao controle de estoque.
- Facilitar o acompanhamento dos recursos utilizados na fabricação.

A separação entre modelos e materiais também contribui para a organização das regras de negócio.

### 🏭 Gestão da Produção

O módulo de produção organiza as ordens de fabricação e integra o processo produtivo ao controle de estoque.

Entre as funcionalidades estão:

- Abertura de ordens de produção.
- Acompanhamento das ordens registradas.
- Conclusão de ordens de produção.
- Consulta à ficha técnica do modelo produzido.
- Desconto automático dos materiais utilizados na fabricação.
- Atualização dos indicadores e alertas relacionados ao estoque.

#### Fluxo de produção

```text
Modelo de Bolsa
      |
      v
Ficha Técnica
      |
      v
Abertura da Ordem de Produção
      |
      v
Execução da Produção
      |
      v
Conclusão da Ordem
      |
      v
Consumo dos Materiais
      |
      v
Atualização do Estoque
      |
      v
Atualização dos Indicadores
```

Esse fluxo demonstra como diferentes módulos podem trabalhar de forma integrada para representar um processo real do negócio.

### 💰 Gestão de Vendas

O sistema contempla um módulo de vendas integrado aos indicadores gerenciais.

As informações de vendas alimentam o acompanhamento do desempenho comercial, incluindo o indicador de vendas do mês e o cálculo do lucro bruto apresentado no dashboard.

A separação dos componentes responsáveis pelo registro e pela consulta das vendas facilita a organização das operações e a evolução das funcionalidades.

### 🎓 Gestão de Alunos e Turmas

O projeto também contempla funcionalidades relacionadas à organização de alunos e turmas.

Os dados desses módulos são utilizados para alimentar indicadores como:

- Total de alunos cadastrados.
- Quantidade de turmas ativas.

A existência de componentes específicos para esses domínios permite manter as regras de negócio separadas das demais funcionalidades administrativas.

### 👥 Gestão de Usuários

O módulo de usuários permite administrar os acessos ao sistema.

Entre as funcionalidades contempladas estão:

- Cadastro de funcionários.
- Consulta de usuários.
- Bloqueio e liberação de acesso.
- Exclusão de funcionários.
- Verificação da situação de ativação do usuário.
- Controle de funcionalidades disponíveis conforme o perfil.

O sistema diferencia os usuários administrativos dos demais perfis, aplicando regras específicas de autorização.

### 🔐 Autenticação e Controle de Acesso

A aplicação possui mecanismos para autenticar usuários e restringir o acesso às áreas internas.

A implementação utiliza serviços de autenticação e interceptadores responsáveis por verificar as condições de acesso às rotas protegidas.

Entre os componentes envolvidos estão:

- `AutenticacaoService`: centraliza as regras de autenticação.
- `AutenticacaoInterceptor`: verifica se o usuário está autenticado.
- `AdminAutorizacaoInterceptor`: restringe determinadas áreas aos administradores.
- `Usuario`: representa informações e o estado de ativação do usuário.
- `ItemMenu`: permite definir quais perfis podem acessar cada item de navegação.

O menu também considera as permissões associadas aos perfis, evitando que todos os usuários visualizem necessariamente as mesmas opções.

> **Observação de segurança:** o projeto possui caráter educacional. A implementação de autenticação e codificação de senhas deve ser revisada antes de qualquer utilização em produção, especialmente quanto ao armazenamento de credenciais, à proteção de sessões e à adoção de algoritmos apropriados para senhas.

### 🔔 Sistema de Notificações

O sistema utiliza uma estrutura extensível para reunir notificações relacionadas a situações relevantes do negócio.

Entre os provedores de notificação implementados estão:

- `FaltaMaterialNotificacaoProvedor`: avisos sobre estoque baixo e modelos com problemas de disponibilidade de materiais.
- `PagamentoAtrasadoNotificacaoProvedor`: avisos sobre parcelas de alunas em atraso.
- `AniversarioFuncionarioNotificacaoProvedor`: notificações sobre aniversariantes da equipe.

Os provedores implementam o contrato `ProvedorNotificacao` e são agregados pelo `NotificacaoService`.

Essa abordagem permite adicionar novos tipos de notificação sem precisar modificar a implementação central do serviço.

### 📑 Relatórios

O sistema possui uma área de relatórios destinada à consulta de informações administrativas.

A arquitetura utiliza interfaces específicas de consulta para permitir que os componentes de leitura acessem os dados necessários sem depender de operações de escrita.

Essa separação reduz o acoplamento entre os módulos e facilita a manutenção das funcionalidades de consulta.

### ⚙️ Configurações Administrativas

O sistema também contempla uma área de configurações administrativas, com acesso restrito conforme as regras de autorização implementadas.

As permissões são aplicadas tanto à navegação quanto às rotas protegidas da aplicação.

## 🏗 Arquitetura do Projeto

Um dos principais objetivos técnicos do projeto foi evoluir de uma estrutura concentrada na interface para uma arquitetura em camadas, organizada por responsabilidades.

O backend foi estruturado nos seguintes pacotes principais:

| Camada | Responsabilidade |
|---|---|
| `domain` | Entidades, objetos de valor e contratos que representam o domínio do negócio. |
| `application` | Casos de uso, serviços, indicadores e regras de negócio. |
| `infrastructure` | Implementações técnicas, persistência em memória e codificação de senhas. |
| `web` | Controladores HTTP, gerenciamento de sessão, interceptadores e templates Thymeleaf. |

### Visão geral da arquitetura

```text
                    USUÁRIO
                       |
                       v
                Interface Web
                  Thymeleaf
                       |
                       v
                 Camada Web
              Controllers / MVC
                       |
                       v
               Camada Application
             Services / Casos de Uso
                       |
                       v
                 Camada Domain
          Entidades / Interfaces / Contratos
                       ^
                       |
                       v
              Camada Infrastructure
          Repositórios / Implementações
```

A arquitetura favorece a separação entre regras de negócio e detalhes técnicos.

Por exemplo, um serviço de aplicação pode depender de uma interface de repositório definida no domínio, sem precisar conhecer a implementação responsável por armazenar os dados.

Isso permite substituir implementações de infraestrutura sem exigir alterações generalizadas nas regras de negócio.

## 🧠 Princípios SOLID Aplicados

O projeto aplica os cinco princípios SOLID para promover um código mais coeso, extensível, testável e menos acoplado a implementações específicas.

### 1. SRP — Single Responsibility Principle

**Princípio da Responsabilidade Única**

Uma classe deve possuir uma responsabilidade bem definida e um motivo principal para mudar.

No projeto, as responsabilidades são distribuídas entre classes especializadas.

| Classe | Responsabilidade |
|---|---|
| `AutenticacaoService` | Validar credenciais e verificar a situação do usuário. |
| `UsuarioService` | Gerenciar operações relacionadas aos usuários. |
| `ProducaoService` | Gerenciar ordens de produção e consumo de materiais. |
| `CalculadoraGraficoBarras` | Calcular proporções para representação gráfica. |
| `FormatadorMoeda` | Formatar valores monetários. |
| `AutenticacaoInterceptor` | Verificar a autenticação nas rotas protegidas. |
| `AdminAutorizacaoInterceptor` | Aplicar restrições de acesso administrativo. |

A separação dessas responsabilidades evita concentrar regras de negócio, formatação, autenticação e apresentação em uma única classe.

### 2. OCP — Open/Closed Principle

**Princípio Aberto/Fechado**

Os componentes devem permitir extensões sem exigir modificações constantes em seu código existente.

O projeto aplica esse princípio principalmente nos indicadores do dashboard e no sistema de notificações.

#### Indicadores extensíveis

A interface `IndicadorDashboard` define um contrato para os indicadores:

```java
public interface IndicadorDashboard {
    int ordem();

    CartaoIndicador gerarCartao();
}
```

Cada indicador possui uma implementação específica, responsável por obter os dados necessários e gerar seu cartão.

Entre as implementações estão:

- `MateriaisEmEstoqueIndicador`
- `ProdutosDisponiveisIndicador`
- `VendasDoMesIndicador`
- `TurmasAtivasIndicador`
- `AlunosCadastradosIndicador`
- `LucroBrutoIndicador`

O `DashboardService` recebe uma lista de implementações dessa interface por injeção de dependência.

Dessa forma, um novo indicador pode ser criado como um componente independente, respeitando o contrato definido, sem exigir que a lógica central do dashboard conheça antecipadamente todas as implementações.

#### Notificações extensíveis

A interface `ProvedorNotificacao` permite implementar diferentes tipos de notificações.

O `NotificacaoService` reúne os provedores registrados no Spring e agrega as mensagens produzidas por eles.

Isso permite acrescentar novos tipos de aviso sem concentrar todas as condições em um único serviço.

### 3. LSP — Liskov Substitution Principle

**Princípio da Substituição de Liskov**

Implementações de uma abstração devem poder substituir umas às outras sem quebrar o comportamento esperado pelos componentes que dependem dela.

No projeto, os indicadores implementam `IndicadorDashboard` e podem ser utilizados pelo `DashboardService` por meio do contrato comum.

O serviço não precisa conhecer os detalhes internos de cada indicador: ele solicita a geração do cartão por meio de `gerarCartao()`.

O mesmo conceito é aplicado aos formatadores.

As implementações `FormatadorMoeda` e `FormatadorQuantidade` seguem o contrato `FormatadorValor`, permitindo que diferentes estratégias de formatação sejam utilizadas conforme a necessidade.

### 4. ISP — Interface Segregation Principle

**Princípio da Segregação de Interfaces**

Os componentes não devem depender de métodos que não utilizam.

Para aplicar esse princípio, o projeto separa contratos de consulta e de escrita em diferentes interfaces de repositório.

Exemplos:

| Módulo | Interface de consulta | Interface de escrita |
|---|---|---|
| Estoque | `MaterialConsultaRepository` | `MaterialComandoRepository` |
| Modelos de bolsas | `ModeloBolsaConsultaRepository` | `ModeloBolsaComandoRepository` |
| Produção | `ProducaoConsultaRepository` | `ProducaoComandoRepository` |
| Vendas | `VendaConsultaRepository` | `VendaRegistroRepository` |
| Turmas | `TurmaConsultaRepository` | `TurmaComandoRepository` |
| Alunos | `AlunoConsultaRepository` | `AlunoComandoRepository` |
| Usuários | `UsuarioRepository` | `UsuarioComandoRepository` |

Essa organização permite que componentes responsáveis apenas pela leitura dependam de interfaces de consulta, sem receber acesso desnecessário a operações de alteração ou exclusão.

### 5. DIP — Dependency Inversion Principle

**Princípio da Inversão de Dependência**

Módulos de alto nível devem depender de abstrações, e não diretamente de implementações de baixo nível.

No projeto, os serviços da camada `application` dependem de contratos definidos na camada `domain`.

Por exemplo:

```java
@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final CodificadorSenha codificadorSenha;

    // Dependências fornecidas pelo Spring.
}
```

O exemplo ilustra a dependência em abstrações, permitindo que a implementação concreta dos repositórios e do mecanismo de codificação seja definida separadamente.

Essa abordagem facilita:

- A substituição de implementações de infraestrutura.
- A criação de testes unitários com dependências controladas.
- A redução do acoplamento entre regras de negócio e tecnologias.
- A evolução da persistência e de outros componentes técnicos.

## 🔄 Inversão de Dependência e Injeção de Dependências

O projeto utiliza o Spring para gerenciar componentes e fornecer suas dependências.

Essa abordagem reduz a necessidade de instanciar manualmente todas as classes utilizadas pelos serviços.

A injeção de dependências é aplicada, por exemplo, ao `DashboardService`, que recebe uma lista de implementações de `IndicadorDashboard`.

O resultado é uma estrutura em que os componentes dependem de contratos bem definidos e podem ser desenvolvidos ou testados de maneira mais independente.

É importante distinguir os conceitos:

- **Inversão de Dependência (DIP):** princípio de design que orienta as dependências entre módulos.
- **Injeção de Dependências (DI):** técnica utilizada para fornecer a um componente as dependências de que ele necessita.
- **Inversão de Controle (IoC):** conceito mais amplo no qual o controle da criação e da gestão de componentes é delegado a um framework ou contêiner.

## 🗃 Persistência e Organização dos Dados

A implementação documentada utiliza repositórios em memória para armazenar os dados da aplicação.

Os contratos de repositório ficam separados das respectivas implementações, permitindo que a camada de aplicação trabalhe com abstrações.

Essa decisão favorece o estudo de arquitetura e facilita a criação de implementações alternativas.

### Características da abordagem atual

- Os serviços de aplicação não precisam conhecer os detalhes internos da persistência.
- Os contratos de consulta podem ser separados dos contratos de escrita.
- As implementações em memória podem ser substituídas por outras tecnologias.
- Os testes podem utilizar implementações falsas ou controladas das dependências.

### Evolução futura

Uma evolução possível seria implementar persistência em um banco de dados relacional, como PostgreSQL ou MySQL, por meio de Spring Data JPA.

Essa mudança exigiria a implementação dos contratos de repositório para a nova tecnologia e a revisão das configurações e dos testes necessários.

**Importante:** a existência de interfaces de repositório não significa que o sistema já utilize um banco de dados relacional. A implementação documentada utiliza persistência em memória.

## 🧪 Testes e Testabilidade

A arquitetura foi organizada para permitir que componentes sejam testados de maneira isolada.

O projeto inclui a dependência `spring-boot-starter-test`, que disponibiliza ferramentas do ecossistema de testes do Spring, incluindo JUnit 5 e MockMvc.

A separação entre contratos e implementações permite fornecer dependências controladas aos serviços durante os testes.

Entre os componentes mencionados na documentação de arquitetura estão:

- `AutenticacaoServiceTest`
- `DashboardServiceTest`

Os testes podem verificar comportamentos como:

- Validação das regras de autenticação.
- Comportamento dos serviços diante de diferentes situações.
- Geração dos indicadores do dashboard.
- Interação entre os componentes da aplicação.

A existência da dependência de testes no Maven, por si só, não garante cobertura completa de todas as funcionalidades. A abrangência deve ser avaliada a partir dos testes efetivamente implementados.

## 🔐 Segurança e Perfis de Acesso

O sistema utiliza serviços de autenticação e interceptadores para controlar o acesso às áreas protegidas.

A arquitetura separa a autenticação, a autorização administrativa e a definição das permissões dos itens de menu.

Essa divisão facilita a manutenção das regras de acesso e evita espalhar verificações de perfil por toda a aplicação.

Os principais componentes relacionados à segurança são:

| Componente | Responsabilidade |
|---|---|
| `AutenticacaoService` | Verificar credenciais e situação do usuário. |
| `AutenticacaoInterceptor` | Proteger rotas que exigem autenticação. |
| `AdminAutorizacaoInterceptor` | Restringir rotas administrativas. |
| `CodificadorSenha` | Definir o contrato para codificação de senhas. |
| `Sha256CodificadorSenha` | Implementar a codificação de senhas utilizando SHA-256. |
| `ItemMenu` | Definir quais perfis podem acessar cada item de navegação. |

### Considerações de segurança

A implementação é destinada ao contexto educacional e deve ser revisada antes de uma implantação real.

Em especial:

- SHA-256 simples não é recomendado para armazenamento de senhas.
- Uma aplicação de produção deve utilizar um algoritmo específico para senhas, como BCrypt, com configuração adequada.
- As sessões precisam de proteção contra acessos indevidos.
- As permissões devem ser verificadas no servidor, independentemente dos itens exibidos no menu.
- Credenciais de demonstração não devem ser utilizadas em ambientes reais.
- As configurações de segurança devem ser revisadas antes da exposição da aplicação à internet.

## 🚀 Como Executar o Projeto

### Pré-requisitos

Antes de iniciar, verifique se possui:

- Java Development Kit (JDK) 17.
- Apache Maven.
- Git.
- Uma IDE Java, como IntelliJ IDEA, Eclipse ou Visual Studio Code, opcionalmente.

### 1. Clonar o repositório

```bash
git clone https://github.com/diego-dev-brazil/Atelie_D_Amore.git
```

### 2. Entrar no diretório do projeto

```bash
cd Atelie_D_Amore
```

### 3. Verificar as versões instaladas

Verifique a versão do Java:

```bash
java -version
```

Verifique se o compilador está disponível:

```bash
javac -version
```

Verifique a instalação do Maven:

```bash
mvn -version
```

O projeto utiliza Java 17 e Maven para gerenciar a compilação e a execução.

### 4. Compilar o projeto

Na raiz do repositório, execute:

```bash
mvn clean compile
```

### 5. Executar os testes

```bash
mvn test
```

### 6. Iniciar a aplicação

```bash
mvn spring-boot:run
```

A aplicação será iniciada utilizando a configuração de execução definida no projeto.

A porta HTTP e as rotas disponíveis dependem das configurações e dos controladores implementados.

> **Observação:** os comandos acima pressupõem que o Maven esteja instalado e disponível no PATH do sistema. O repositório deve conter o `pom.xml` na raiz, mas não foi identificado um Maven Wrapper entre os arquivos visíveis.

## 🧭 Navegação da Aplicação

A documentação do projeto identifica as seguintes rotas de referência:

| Rota | Funcionalidade |
|---|---|
| `/estoque` | Gestão de materiais e acompanhamento do estoque. |
| `/modelos` | Gestão dos modelos de bolsas. |
| `/producao` | Gestão das ordens de produção. |
| `/notificacoes` | Consulta das notificações do sistema. |
| `/usuarios` | Gestão de usuários, com acesso administrativo. |
| `/relatorios` | Área de relatórios, com acesso administrativo. |
| `/configuracoes` | Configurações administrativas. |

O acesso às áreas internas depende das regras de autenticação e autorização configuradas na aplicação.

## 📚 Documentação de Arquitetura

O repositório inclui o arquivo [`SOLID_EXPLICACAO.md`](SOLID_EXPLICACAO.md), que apresenta uma explicação detalhada das decisões de arquitetura e da aplicação dos princípios SOLID.

O documento aborda:

- A evolução da estrutura da aplicação.
- A separação de responsabilidades.
- A extensibilidade dos indicadores do dashboard.
- O sistema de notificações baseado em interfaces.
- A segregação dos contratos de consulta e escrita.
- A inversão de dependência entre as camadas.
- Exemplos de componentes e suas responsabilidades.
- Um roteiro para demonstrar os conceitos aplicados no projeto.

Esse material complementa o código-fonte e ajuda a compreender os motivos por trás das decisões de design.

## 🔮 Possíveis Evoluções

A arquitetura atual permite explorar melhorias futuras, mantendo o foco na separação de responsabilidades e na qualidade do software.

Algumas possibilidades são:

- Implementar persistência em banco de dados relacional com Spring Data JPA.
- Adicionar migrações de banco de dados.
- Reforçar a segurança com Spring Security.
- Adotar BCrypt ou outro algoritmo apropriado para armazenamento de senhas.
- Ampliar a cobertura de testes unitários e de integração.
- Implementar validação de dados de entrada.
- Padronizar o tratamento de exceções.
- Melhorar a documentação técnica e funcional.
- Implementar mecanismos de auditoria para operações administrativas.
- Aprimorar o controle de consistência entre estoque e produção.
- Evoluir os relatórios gerenciais.
- Adicionar recursos de exportação de dados, conforme as necessidades do negócio.

Essas possibilidades representam caminhos de evolução, não funcionalidades que necessariamente já estejam implementadas.

## 🎓 Contexto Acadêmico

Projeto desenvolvido no contexto da disciplina de **Engenharia de Software III**, com foco na aplicação prática de conceitos de arquitetura de software, Programação Orientada a Objetos e princípios SOLID.

O desenvolvimento permite explorar como decisões de arquitetura influenciam a manutenção, a testabilidade e a evolução de um sistema voltado a necessidades reais de negócio.

## 🧑‍💻 Autor

**Grupo Formado por: 
Diego Augusto
Leonardo Nunes
Giovanni Nogalis
Luigi Gabriel
Eduardo Torres
Rafael Prado
Pedro Cecheto
Matheus Kato**

- [GitHub](https://github.com/diego-dev-brazil)
- [Repositório Ateliê Renata D'Amore](https://github.com/diego-dev-brazil/Atelie_D_Amore)

---

Desenvolvido para fins acadêmicos, com foco em Java, Spring Boot, arquitetura em camadas, princípios SOLID e engenharia de software.
