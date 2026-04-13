# 📊 Trads Corretora — Back-end

API REST construída com **Spring Boot 4** para análise de dados de CRM de uma corretora. Processa negócios (deals), movimentações, pipelines e etapas, expondo dashboards analíticos com filtros por data, responsável e perfil de acesso.

---

## 🛠 Stack Tecnológica

| Tecnologia | Finalidade |
|---|---|
| **Spring Boot 4.0.5** | Framework principal |
| **Spring Security** | Autenticação e autorização |
| **Spring Data JPA** | Acesso a dados (ORM) |
| **java-jwt (Auth0) 4.4.0** | Tokens JWT |
| **SQLite + Hibernate Community Dialects** | Banco embutido para desenvolvimento |
| **Lombok** | Redução de boilerplate |
| **Java 17** | Linguagem |

---

## 🏗 Arquitetura

Organização inspirada em **Clean Architecture**, separando domínio de infraestrutura:

```
src/main/java/com/example/tradscorretora/
├── config/              # Segurança (JWT, filtros) e carga de dados (ETL)
├── domain/
│   ├── dto/             # Records para exposição de dados
│   ├── entity/          # Entidades JPA (tabelas)
│   │   └── views/       # Entidades mapeadas para Views SQL (@Immutable)
│   └── enums/           # RoleEnum (USER, ADMIN, MANAGER)
├── infrastructure/
│   ├── controllers/     # Endpoints REST
│   ├── repositories/    # Spring Data JPA + queries JPQL customizadas
│   └── services/        # Lógica de negócio e orquestração
└── TradsCorretoraApplication.java
```

A separação `domain` × `infrastructure` garante que a camada de negócio não depende diretamente de frameworks.

---

## 🚀 Versionamento de Banco de Dados — Flyway
Diferente da abordagem padrão de deixar o Hibernate gerar as tabelas (ddl-auto=update), este projeto utiliza o Flyway para garantir a evolução controlada e a reprodutibilidade do banco de dados.

Histórico de Alterações: Localizado em src/main/resources/db/migration, os scripts SQL seguem a ordem cronológica (V1, V2, etc.).

Independência de Framework: As tabelas e, principalmente, as Views Analíticas são criadas via SQL puro, garantindo que a lógica de banco seja preservada independentemente da versão do ORM.

## 📦 Carga de Dados — ETL via JsonData

A classe `JsonData` implementa `CommandLineRunner` — **executa automaticamente ao iniciar a aplicação**, funcionando como um ETL simplificado:

1. **Extract** — Lê 5 arquivos JSON do classpath (`pipelines`, `etapas`, `usuarios`, `negocios`, `movimentacoes`)
2. **Transform** — Converte para entidades JPA, resolvendo relacionamentos via maps de lookup
3. **Load** — Persiste no SQLite

**Decisão:** A importação **só ocorre se o banco estiver vazio** (`count() == 0`). Ao reiniciar a aplicação, os dados não são duplicados.

Além dos dados do CRM, **três usuários de acesso** são criados com senhas em BCrypt:

| Email | Senha | Role |
|---|---|---|
| `admin@demo.crm` | `admin123` | ADMIN |
| `manager@demo.crm` | `manager123` | MANAGER |
| `<email do usuário id=112>` | `user123` | USER |

---

## 🔐 Segurança — JWT e Filtros

### Fluxo de Autenticação

```
Requisição → SecurityFilter (extrai e valida JWT)
                 → Busca usuário no banco
                     → Seta no SecurityContext com authorities
                         → SecurityConfig (regras de acesso por rota)
                             → @PreAuthorize (regras por método/role)
```

### Decisões Técnicas

- **SecurityFilter** estende `OncePerRequestFilter`, registrado **antes** do filtro padrão do Spring, dando prioridade à autenticação via JWT
- **JWT (Auth0 java-jwt):** algoritmo HMAC256, subject = email, claim `role`, expiração de 2h (UTC-3)
- **Sessão STATELESS** — sem cookies, sem sessão no servidor
- **CSRF desabilitado** — justificado pela natureza stateless (autenticação via Bearer token)
- **CORS** configurado para `localhost:4200` (front-end Angular)
- **AuthConfig** implementa `UserDetailsService`, integrando `UserAcess` (que implementa `UserDetails`) ao Spring Security

---

## 👥 Controle de Acesso por Role

O controle acontece em **duas camadas**:

1. **Controller** — `@PreAuthorize` define quais roles acessam cada endpoint
2. **Service** — Filtra os dados retornados conforme a role do usuário autenticado

| Role | Sem filtro userId | Com filtro userId | Pode listar usuários? |
|---|---|---|---|
| **USER** | Vê apenas **seus dados** | Ignorado (sempre filtra por si) | ❌ |
| **MANAGER** | Vê **todos** os dados | Filtra pelo userId informado | ✅ |
| **ADMIN** | Vê **todos** os dados | Filtra pelo userId informado | ✅ |

O `DashboardService` usa interfaces funcionais genéricas (`DashboardProvider`, `DashboardDateProvider`) para evitar duplicação da lógica de filtro entre os diferentes endpoints.

---

## 🗄 Views SQL e Queries

### Por que Views?

O Hibernate gera queries automaticamente, mas para análises com **JOINs complexos, window functions e agregações**, a geração automática não atende de forma performática. Por isso, **views SQL foram criadas diretamente no banco SQLite** e mapeadas no JPA como entidades `@Immutable`.

| View | Finalidade |
|---|---|
| `vw_fechamento_ganho` | Negócios fechados com ganho |
| `vw_fechamento_perda` | Negócios fechados com perda |
| `vw_tempo_em_etapa` | Tempo de permanência em cada etapa |
| `vw_transicoes` | Transições entre etapas (de → para) |
| `vw_loss_conversion` | Conversão/perda agrupada por etapa |

### Queries JPQL Customizadas

Nos repositories, queries JPQL foram escritas manualmente para aplicar **filtros dinâmicos opcionais** de data e usuário (`IS NULL OR`), evitando depender dos query methods gerados pelo Hibernate para cenários mais complexos.

---

## 📤 DTOs — Exposição Controlada

Todos os DTOs são **Java Records** (imutáveis e concisos). A decisão de usar DTOs garante:

- **Segurança** — Campos sensíveis (passwords, IDs internos) nunca são expostos
- **Desacoplamento** — Entidade e API evoluem independentemente
- **Performance** — Evita serialização de relações lazy desnecessárias
- **Privacidade** — Os DTOs das views **omitem `userId`**, impedindo o front-end de saber de qual usuário são os dados

---

## 🔍 Filtros do Dashboard

| Filtro | Parâmetro | Obrigatório |
|---|---|---|
| Data Início | `startDate` (ISO date) | Não |
| Data Fim | `endDate` (ISO date) | Não |
| Responsável | `userId` | Não |
| Role do Usuário | Automático via JWT | Automático |

Todos os endpoints retornam `Page<T>` com paginação padrão do Spring Data (tamanho default = 5).

---

## ✅ Validação de Dados

O diretório `sql/validacao.sql` contém **6 queries de consistência** executadas sobre os dados importados:

1. **Órfãos em movimentações** — `id_negocio` sem correspondência em `negocios`
2. **Pipeline/etapa inválidos** — combinação `stage_id + pipeline_id` inexistente
3. **Usuários inexistentes** — `assigned_user_id` ou `created_user_id` sem registro
4. **Snapshot vs última movimentação** — estado atual do negócio bate com a última movimentação
5. **Cronologia** — nenhuma movimentação com data anterior à criação do negócio
6. **Monotonicidade** — sequência temporal das movimentações sem regressão

---

## 🌐 Endpoints da API

### Autenticação (público)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/auth/login` | Login com email/senha → retorna JWT |

### Dashboard (requer JWT)

| Método | Rota | Roles |
|---|---|---|
| `GET` | `/dashboard/vw-gain` | ADMIN, MANAGER, USER |
| `GET` | `/dashboard/vw-loss` | ADMIN, MANAGER, USER |
| `GET` | `/dashboard/vw-time-stage` | ADMIN, MANAGER, USER |
| `GET` | `/dashboard/vw-transitions` | ADMIN, MANAGER, USER |
| `GET` | `/dashboard/vw-loss-conversion` | ADMIN, MANAGER, USER |

### Usuários (requer JWT)

| Método | Rota | Roles |
|---|---|---|
| `GET` | `/users/list` | ADMIN, MANAGER |

---

## 🚀 Como Executar

**Pré-requisitos:** Java 17+ e Maven 3.8+

```bash
git clone <url-do-repositorio>
cd trads-corretora-back
./mvnw spring-boot:run
```

O banco SQLite é criado em `saida/crm_trads.db` e os dados JSON são importados na primeira execução. Aplicação disponível em `http://localhost:8080`.

---

## ⚠️ Limitações Conhecidas

1. **Carga não incremental** — O ETL só importa se o banco estiver vazio. Novos registros nos JSON não serão sincronizados após a primeira execução. Em produção, seria necessário implementar lógica de upsert e diff.

2. **Views externas ao Hibernate** — As views SQL precisam existir previamente no banco (`ddl-auto=none`). O Hibernate não as cria automaticamente.

3. **CORS fixo** — Aceita apenas `localhost:4200`. Em produção, deveria ser configurável via variável de ambiente.

4. **Secret JWT hardcoded** — A chave está no `application.properties`. Em produção, usar variável de ambiente.

5. **Sem registro de usuários** — Apenas login. Não há endpoints de cadastro, recuperação de senha ou OAuth.

6. **SQLite** — Escolhido para simplificar o desenvolvimento. Em produção, seria PostgreSQL ou MySQL.
