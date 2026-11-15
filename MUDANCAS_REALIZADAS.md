# Resumo das Mudanças Implementadas — Ateliê Renata D'Amore

Documento técnico resumindo as 3 melhorias aplicadas ao sistema para alinhamento da equipe.

---

## 1. Máscara de Número de Telefone `(XX) XXXXX-XXXX` / `(XX) XXXX-XXXX`

### Onde foi aplicado:
- **Front-end (Máscara em tempo real durante a digitação):**
  - `src/main/resources/static/js/app.js`: Adicionada a função `aplicarMascaraTelefone(valor)`, que intercepta automaticamente todos os campos `input[name="telefone"]` e `[data-mascara-telefone]`, formatando números fixos de 10 dígitos como `(11) 3456-7890` e celulares de 11 dígitos como `(11) 98765-4321`.
  - `src/main/resources/templates/alunos.html`: Aplicado `type="tel"`, `data-mascara-telefone` e `maxlength="15"` nos formulários de matrícula e edição de alunos.
  - `src/main/resources/templates/configuracoes.html`: Aplicado `type="tel"`, `data-mascara-telefone` e `maxlength="15"` no telefone institucional do ateliê.
- **Back-end / Domínio (Padronização ao salvar):**
  - `src/main/java/br/com/atelie/domain/model/Aluno.java` e `ConfiguracaoAtelie.java`: Método utilitário `Aluno.formatarTelefone(String)` garante que o telefone seja salvo sempre formatado mesmo que venha apenas com números.

---

## 2. Consistência de Idade Mínima para Funcionários (Mínimo de 14 Anos)

### Regra de Negócio:
Não é permitido cadastrar nem atualizar um funcionário com menos de **14 anos de idade** (ou com data de nascimento futura).

### Onde foi aplicado:
- **Domínio (`src/main/java/br/com/atelie/domain/model/Usuario.java`):**
  - Constante `IDADE_MINIMA_TRABALHO = 14`.
  - Método `Usuario.validarIdadeMinima(LocalDate dataNascimento)` que calcula `Period.between(dataNascimento, LocalDate.now()).getYears()` e lança `IllegalArgumentException` caso a idade seja inferior a 14 anos ou a data seja no futuro.
- **Serviço de Aplicação (`src/main/java/br/com/atelie/application/usuarios/UsuarioService.java`):**
  - Validação obrigatória tanto em `cadastrarNovo(...)` quanto em `atualizar(...)`.
- **Interface Web (`UsuarioController.java` + `templates/usuarios.html`):**
  - O controller envia `dataMaximaNascimento` (`LocalDate.now().minusYears(14)`) para a tela.
  - Os campos `<input type="date" name="dataNascimento">` no modal de novo funcionário e na edição possuem `th:max="${dataMaximaNascimento}"` e `required`, bloqueando no próprio calendário datas de pessoas com menos de 14 anos.

---

## 3. Troca da Própria Senha pelo Funcionário (`/minha-senha`)

### Contexto:
Como a tela `/usuarios` é exclusiva para o perfil `ADMIN`, os usuários com perfil `FUNCIONARIO` precisavam de um fluxo seguro para alterar a própria senha sem depender da administradora.

### Onde foi aplicado:
- **Serviço (`src/main/java/br/com/atelie/application/usuarios/UsuarioService.java`):**
  - Novo método `alterarPropriaSenha(loginLogado, senhaAtualPura, novaSenhaPura, confirmacaoSenhaPura)`:
    1. Verifica se a **senha atual** informada confere com o hash salvo (`codificadorSenha.verificar(...)`);
    2. Valida tamanho mínimo (4 caracteres) e se a **nova senha** coincide com a **confirmação**;
    3. Impede que a nova senha seja idêntica à senha anterior;
    4. Codifica com SHA-256 e persiste no repositório (Memória ou PostgreSQL/JPA).
- **Controller e Tela (`MinhaSenhaController.java` + `templates/minha-senha.html`):**
  - Nova rota `GET /minha-senha` e `POST /minha-senha` acessível a qualquer funcionário autenticado.
- **Menu Lateral, Cabeçalho e Segurança (`MenuService.java`, `WebConfig.java`, `fragments/layout.html`):**
  - Item **"Alterar Senha"** (`/minha-senha`) liberado para todos os perfis (`ItemMenu.paraTodos(...)`) no menu lateral e ícone de chave (`key-round`) no cabeçalho superior ao lado do botão de sair.
  - Rota `/minha-senha` incluída no `AutenticacaoInterceptor` em `WebConfig.java` (fora do bloqueio exclusivo de Admin).

---

## 4. Testes Automatizados Adicionados
- `src/test/java/br/com/atelie/application/usuarios/UsuarioServiceTest.java`:
  - `recusaCadastroDeFuncionarioMenorDe14Anos`
  - `aceitaCadastroDeFuncionarioCom14AnosOuMais`
  - `permiteFuncionarioAlterarPropriaSenhaComSenhaAtualCorreta`
  - `recusaTrocaDeSenhaQuandoSenhaAtualIncorreta`
  - `formataMascaraDeTelefoneDe10E11Digitos`
