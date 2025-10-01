# Roteiro de Implementação do BuddyGet

## Visão Geral

Este roteiro fornece uma abordagem estruturada para implementação do projeto BuddyGet. A implementação está organizada em fases baseadas nas dependências entre entidades e na complexidade da lógica de negócios. Cada fase se baseia no trabalho anterior, minimizando a necessidade de refatorações.

## Fases de Implementação

### Fase 1: Autenticação Principal e Gerenciamento de Usuários

#### Entidades
- User
- UserPerfil
- Email (Objeto de Valor)

#### Dependências
- Nenhuma (camada de fundação)

#### Endpoints
- `POST /api/auth/register` - Registro de usuário
- `POST /api/auth/login` - Autenticação de usuário
- `GET /api/auth/me` - Obter detalhes do usuário atual
- `PATCH /api/auth/me` - Atualizar perfil do usuário
- `PATCH /api/auth/password` - Alterar senha
- `GET /api/auth/refresh-token` - Atualizar token de autenticação

#### Notas de Implementação
- Completar a configuração de segurança
- Implementar geração e validação de tokens JWT
- Criar validação de registro de usuário
- Configurar perfis de usuário padrão com permissões

#### Justificativa
A autenticação é a fundação para todas as outras funcionalidades. O gerenciamento de usuários deve ser implementado primeiro, pois todas as outras entidades dependem da entidade User.

---

### Fase 2: Gerenciamento de Categorias

#### Entidades
- Category
- HexadecimalColor (Objeto de Valor)

#### Dependências
- User

#### Endpoints
- `POST /api/categories` - Criar categoria
- `GET /api/categories` - Listar categorias
- `GET /api/categories/{id}` - Obter categoria específica
- `PUT /api/categories/{id}` - Atualizar categoria
- `DELETE /api/categories/{id}` - Excluir categoria

#### Notas de Implementação
- Criar tipos de categoria predefinidos (RECEITA, DESPESA)
- Implementar validação para cores e ícones de categoria
- Garantir que as categorias estejam no escopo do usuário

#### Justificativa
As categorias são fundamentais para o rastreamento financeiro e são necessárias para transações, orçamentos e muitas outras entidades.

---

### Fase 3: Transações Financeiras Básicas

#### Entidades
- GenericTransaction (abstrata)
- Transaction (abstrata)
- IncomeTransaction
- ExpenseTransaction

#### Dependências
- User
- Category

#### Endpoints
- `POST /api/transactions/incomes` - Criar transação de receita
- `GET /api/transactions/incomes` - Listar transações de receita
- `GET /api/transactions/incomes/{id}` - Obter transação de receita específica
- `PUT /api/transactions/incomes/{id}` - Atualizar transação de receita
- `DELETE /api/transactions/incomes/{id}` - Excluir transação de receita
- `POST /api/transactions/expenses` - Criar transação de despesa
- `GET /api/transactions/expenses` - Listar transações de despesa
- `GET /api/transactions/expenses/{id}` - Obter transação de despesa específica
- `PUT /api/transactions/expenses/{id}` - Atualizar transação de despesa
- `DELETE /api/transactions/expenses/{id}` - Excluir transação de despesa

#### Notas de Implementação
- Implementar validação de transação para datas e valores
- Criar filtros para consultas de transação (intervalo de data, categoria, etc.)
- Garantir validação adequada do tipo de categoria

#### Justificativa
As transações financeiras básicas são a funcionalidade central da aplicação. Elas dependem de categorias e usuários, mas são necessárias para recursos mais complexos.

---

### Fase 4: Gerenciamento Financeiro Recorrente

#### Entidades
- Recurring (abstrata)
- IncomeRecurring
- ExpenseRecurring
- DataRange (Objeto de Valor)
- DayOfMonth (Objeto de Valor)

#### Dependências
- User
- Category
- GenericTransaction

#### Endpoints
- `POST /api/recurring/incomes` - Criar receita recorrente
- `GET /api/recurring/incomes` - Listar receitas recorrentes
- `GET /api/recurring/incomes/{id}` - Obter receita recorrente específica
- `PUT /api/recurring/incomes/{id}` - Atualizar receita recorrente
- `DELETE /api/recurring/incomes/{id}` - Excluir receita recorrente
- `POST /api/recurring/expenses` - Criar despesa recorrente
- `GET /api/recurring/expenses` - Listar despesas recorrentes
- `GET /api/recurring/expenses/{id}` - Obter despesa recorrente específica
- `PUT /api/recurring/expenses/{id}` - Atualizar despesa recorrente
- `DELETE /api/recurring/expenses/{id}` - Excluir despesa recorrente

#### Notas de Implementação
- Implementar lógica de geração de transação recorrente
- Criar job em segundo plano para gerar transações a partir de entradas recorrentes
- Adicionar validação para intervalos de data e dias recorrentes

#### Justificativa
As transações recorrentes se baseiam no modelo de transação básica e fornecem funcionalidades mais complexas.

---

### Fase 5: Gerenciamento de Cartão de Crédito

#### Entidades
- CreditCard
- CreditCardBill

#### Dependências
- User
- ExpenseTransaction
- IncomeTransaction

#### Endpoints
- `POST /api/credit-cards` - Criar cartão de crédito - OK
- `GET /api/credit-cards` - Listar cartões de crédito - OK
- `PUT /api/credit-cards/{id}` - Atualizar cartão de crédito
- `DELETE /api/credit-cards/{id}` - Excluir cartão de crédito
- `GET /api/credit-cards/{id}/bills` - Listar faturas de cartão de crédito
- `GET /api/credit-cards/{id}/bills/{billId}` - Obter fatura detalhada específica

#### Notas de Implementação
- Implementar cálculos de ciclo de cobrança
- Criar rastreamento de despesas de cartão de crédito
- Construir gerenciamento de limite de crédito

#### Justificativa
O gerenciamento de cartões de crédito depende do sistema de transações, mas adiciona funcionalidade especializada para rastrear despesas específicas do cartão.

---

### Fase 6: Gerenciamento de Orçamento e Metas

#### Entidades
- Budget
- Goal
- MonthlySummary

#### Dependências
- User
- Category
- Transactions (Despesa e Receita)

#### Endpoints
- `POST /api/budgets` - Criar orçamento
- `GET /api/budgets` - Listar orçamentos
- `GET /api/budgets/{id}` - Obter orçamento específico
- `PUT /api/budgets/{id}` - Atualizar orçamento
- `DELETE /api/budgets/{id}` - Excluir orçamento
- `POST /api/goals` - Criar meta
- `GET /api/goals` - Listar metas
- `GET /api/goals/{id}` - Obter meta específica
- `PUT /api/goals/{id}` - Atualizar meta
- `DELETE /api/goals/{id}` - Excluir meta
- `GET /api/monthly-summary` - Obter resumos mensais
- `GET /api/monthly-summary/{year}/{month}` - Obter resumo de mês específico

#### Notas de Implementação
- Implementar rastreamento de orçamento em relação às despesas reais
- Criar cálculos de progresso do orçamento
- Construir rastreamento de progresso de meta
- Gerar relatórios de resumo mensal

#### Justificativa
Orçamentos e metas se baseiam em transações e categorias para fornecer recursos de planejamento financeiro.

---
### (DESCONTINUADO)
### Fase 7: Planos de Parcelamento e Gerenciamento Complexo de Despesas

#### Entidades
- InstallmentPlan
- Recursos restantes de ExpenseTransaction

#### Dependências
- User
- ExpenseTransaction
- ExpenseRecurring

#### Endpoints
- `POST /api/installment-plans` - Criar plano de parcelamento
- `GET /api/installment-plans` - Listar planos de parcelamento
- `GET /api/installment-plans/{id}` - Obter plano de parcelamento específico
- `PUT /api/installment-plans/{id}` - Atualizar plano de parcelamento
- `DELETE /api/installment-plans/{id}` - Excluir plano de parcelamento

#### Notas de Implementação
- Implementar lógica de geração de parcelas
- Construir acompanhamento de parcelas
- Criar rastreamento de status de pagamento

#### Justificativa
Os planos de parcelamento representam um cenário financeiro complexo que se baseia no sistema de transações.

---

### Fase 8: Gerenciamento de Lista de Desejos

#### Entidades
- WishListItem

#### Dependências
- User
- Category
- ExpenseTransaction

#### Endpoints
- `POST /api/wish-list` - Criar item de lista de desejos
- `GET /api/wish-list` - Listar itens de lista de desejos
- `GET /api/wish-list/{id}` - Obter item de lista de desejos específico
- `PUT /api/wish-list/{id}` - Atualizar item de lista de desejos
- `DELETE /api/wish-list/{id}` - Excluir item de lista de desejos
- `POST /api/wish-list/{id}/purchase` - Converter item de lista de desejos em despesa

#### Notas de Implementação
- Implementar ordenação baseada em prioridade
- Criar conversão de item de lista de desejos para transação de despesa
- Construir interface para estimar cronogramas de compra com base no orçamento

#### Justificativa
A funcionalidade de lista de desejos depende de categorias de despesa e transações, mas representa um recurso de planejamento especializado.

---

### Fase 9: Sistema de Notificações

#### Entidades
- Notification
- Metadata (Objeto de Valor)

#### Dependências
- User
- (Potencialmente todas as outras entidades para geração de notificações)

#### Endpoints
- `GET /api/notifications` - Listar notificações
- `PATCH /api/notifications/{id}/read` - Marcar notificação como lida
- `DELETE /api/notifications/{id}` - Excluir notificação
- `PATCH /api/notifications/read-all` - Marcar todas as notificações como lidas

#### Notas de Implementação
- Implementar geração de notificação para vários eventos
- Criar sistema de entrega de notificação
- Construir rastreamento de status lido/não lido

#### Justificativa
As notificações dependem de vários eventos da aplicação e devem ser implementadas após as funcionalidades principais estarem em vigor.

---

### Fase 10: Sistema de Dashboard e Relatórios

#### Entidades
- (Usa entidades existentes para agregação de dados)

#### Dependências
- Todas as entidades anteriores

#### Endpoints
- `GET /api/dashboard` - Obter resumo do dashboard
- `GET /api/reports/expenses` - Gerar relatórios de despesas
- `GET /api/reports/incomes` - Gerar relatórios de receitas
- `GET /api/reports/budget` - Gerar relatórios de orçamento
- `GET /api/reports/goals` - Gerar relatórios de progresso de metas

#### Notas de Implementação
- Criar serviços de agregação de dados
- Implementar lógica de geração de relatórios
- Construir estruturas de dados prontas para visualização
- Adicionar opções de filtro para todos os relatórios

#### Justificativa
Dashboards e relatórios dependem de todas as outras funcionalidades estarem em vigor para gerar insights significativos.

---

## Diretrizes de Implementação

### Estratégia de Teste
- Criar testes unitários para toda validação de entidade de domínio
- Implementar testes de integração para controladores e serviços
- Usar repositórios mock para teste de serviço

### Considerações de Banco de Dados
- Garantir indexação adequada em campos frequentemente consultados
- Considerar a adição de gatilhos de exclusão lógica para manter relacionamentos de dados
- Implementar migrações de banco de dados para controle de versão de alterações de esquema

### Preocupações de Segurança
- Garantir que todos os endpoints tenham verificações de autorização adequadas
- Validar que todas as entidades mantenham isolamento adequado no nível do usuário
- Implementar validação de entrada no nível do controlador

### Desafios Potenciais
1. **Complexidade da Transação**: Gerenciar os diferentes tipos de transação e seus relacionamentos pode se tornar complexo
2. **Lógica Recorrente**: Geração adequada de transações recorrentes sem duplicação
3. **Desempenho**: Recursos de dashboard e relatórios podem exigir otimização para grandes conjuntos de dados
4. **Experiência do Usuário**: Equilibrar o rastreamento financeiro abrangente com a usabilidade

## Conclusão

Este roteiro fornece uma abordagem estruturada para implementar o BuddyGet. Ao seguir estas fases, o desenvolvimento pode prosseguir de maneira lógica que minimiza a refatoração e garante o gerenciamento adequado de dependências.

A ordem de implementação é projetada para construir uma base sólida antes de adicionar recursos mais complexos. Entidades principais como User e Category vêm primeiro, seguidas pelo rastreamento financeiro básico e, em seguida, recursos mais especializados como orçamento, metas e relatórios.

Revisões regulares da arquitetura e refatoração quando necessário ajudarão a manter a qualidade do código durante todo o processo de implementação.
