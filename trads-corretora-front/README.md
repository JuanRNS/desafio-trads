# Trads Corretora — Front-end

Dashboard analítico para corretora de seguros, construído com **Angular 21** e **Tailwind CSS 4**. A aplicação consome uma API REST (Spring Boot) e exibe gráficos interativos de desempenho comercial — ganhos, perdas, transições de pipeline e tempo médio por etapa de funil.

---

## Sumário

- [Tecnologias](#tecnologias)
- [Arquitetura do Projeto](#arquitetura-do-projeto)
- [Decisões Técnicas](#decisões-técnicas)
- [Como Rodar](#como-rodar)

---

## Tecnologias

| Tecnologia   | Versão | Finalidade                          |
| ------------ | ------ | ----------------------------------- |
| Angular      | 21     | Framework SPA                       |
| Tailwind CSS | 4      | Estilização utilitária              |
| Chart.js     | 4.5    | Motor de renderização de gráficos   |
| ng2-charts   | 10     | Wrapper Angular para Chart.js       |
| RxJS         | 7.8    | Programação reativa (HTTP, streams) |
| TypeScript   | 5.9    | Tipagem estática                    |
| Vitest       | 4      | Framework de testes unitários       |

---

## Arquitetura do Projeto

```
src/app/
├── core/                       # Código reutilizável e independente de feature
│   ├── abstract/               # Classe abstrata para requisições HTTP
│   ├── components/             # Componentes compartilhados (gráficos, header)
│   ├── enums/                  # Enums (métodos HTTP)
│   ├── guards/                 # Guard funcional de autenticação
│   ├── interfaces/             # Contratos de tipos (DTOs, request options)
│   └── utils/                  # Funções utilitárias (tradução de estágios)
├── env/                        # Configurações de ambiente (dev/prod)
├── features/
│   ├── services/               # Services (auth, dashboard)
│   └── views/                  # Telas (dashboard, login)
├── app.config.ts               # Providers globais
├── app.routes.ts               # Rotas com guard
└── app.ts                      # Componente raiz
```

---

## Decisões Técnicas

### Signals e Computed

O projeto utiliza **Signals** como mecanismo primário de estado reativo. A escolha em vez do padrão `BehaviorSubject` + `async pipe` se dá por:

- **Reatividade granular** — o Angular rastreia quais signals cada template consome e re-renderiza apenas o necessário, sem depender do `zone.js` verificando a árvore inteira.
- **Leitura síncrona** — acessar estado com `this.gains()` é mais simples do que gerenciar `subscribe()` ou `async pipe`.
- **Derivação declarativa com `computed()`** — valores derivados recomputam automaticamente quando qualquer signal-dependente muda.

No dashboard, os dados brutos vindos da API são armazenados em signals, e os dados formatados para cada gráfico são derivados via `computed()`. Assim, quando um filtro muda e os dados são rebuscados, toda a cadeia de transformação até o gráfico se atualiza automaticamente.

---

### Componente Genérico de Gráficos

O `DashboardGraphicsComponent` foi projetado para ser **reutilizável** — o mesmo componente renderiza todos os gráficos do dashboard (barras, linhas, pizza, doughnut, number cards). Ele recebe os dados e o tipo de gráfico via `input()` e usa `computed()` internamente para derivar a configuração do Chart.js.

Isso evita duplicação de configuração em cada tela e centraliza mudanças visuais em um único lugar. A biblioteca utilizada é o **ng2-charts**, registrada globalmente no `app.config.ts`.

---

### HTTP Abstract

A classe abstrata `HttpAbstract` centraliza a lógica de comunicação HTTP. Todo service que precisa fazer requisições estende essa classe e ganha automaticamente:

- **Injeção do token JWT** no header `Authorization` de toda requisição.
- **Base URL configurável** por service (via `environment.apiUrl`).
- **Métodos simplificados** (`get`, `post`, `put`, `delete`) — os services chamam apenas o path e o body, sem se preocupar com headers ou configuração.

---

### Guards de Rota

O `authGuard` é um guard funcional (`CanActivateFn`) que protege a rota do dashboard. Verifica se o token JWT existe e se não está expirado — se não autenticado, redireciona para `/login`.

---

### Utils — Mapeamento de Estágios

A função `verifyTransitions()` traduz os códigos de estágio do backend (ex: `P0_NEW`, `P10_DISCOVERY`) para labels legíveis em português (ex: `[P0] Novo`, `[P10] Descoberta`). É uma função pura, sem dependência de Angular, usada por qualquer componente que precise exibir nomes de estágios.

O projeto suporta três pipelines:

| Pipeline | Etapas                                                                   |
| -------- | ------------------------------------------------------------------------ |
| **P0**   | Novo → Qualificado → Proposta → Negociação → Ganho/Perda                |
| **P10**  | Novo → Descoberta → Reunião Técnica → Proposta → Jurídico → Ganho/Perda |
| **P20**  | Contato → Oferta → Ganho/Perda                                          |

---

### Controle de Role no AuthService

O `AuthService` decodifica o token JWT e expõe a **role** e o **email** do usuário como signals (`currentUserRole`, `currentUserEmail`). No template do dashboard, seções exclusivas para administradores (como o filtro de corretores) são protegidas com `@if (role === 'ADMIN')`, garantindo que corretores comuns vejam apenas seus próprios dados.

---

### Interfaces Tipadas

Todas as interfaces estão centralizadas em `core/interfaces/`, com contratos bem definidos entre front e back. Destaque para `IPageResponse<T>`, que é genérica e reflete o padrão `Page<>` do Spring Data, e os DTOs de cada view do dashboard (`IVwGain`, `IVwLoss`, `IVwTimeStage`, `IVwTransitions`, `IVwLossConversion`).

---

### Estratégia de Filtros

O dashboard combina filtros no **back-end** e no **front-end**:

- **Back-end** — filtros de **data** (início/fim) e **responsável** (corretor) são enviados como query params para a API, que retorna apenas os dados já filtrados. Isso evita tráfego desnecessário.
- **Front-end** — filtros de **pipeline** (P0/P10/P20) e **agrupamento temporal** (dia/semana/mês) são aplicados localmente via `computed()`, pois são apenas transformações visuais sobre os dados já recebidos.

---

## Como Rodar

### Pré-requisitos

- **Node.js** 18+
- **npm** 10+
- **Angular CLI** 21+ (`npm install -g @angular/cli`)
- Backend da API rodando em `http://localhost:8080`

### Instalação e Execução

```bash
npm install
ng serve
```

Acesse `http://localhost:4200/`.

### Build de Produção

```bash
ng build
```

### Testes

```bash
ng test
```
