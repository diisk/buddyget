1. Criar regra para o goal criar uma transaction do tipo expense com uma categoria propria, para receber dinheiro do saldo atual, gerando um histórico.

2. Refatorar as validações das entidades para serem classes ou funções com parametros para validar antes de setar o valor.

3. Mudar a forma como as recorrencias são feitas de maneira que:
-não exista transações pendentes para as recorrencias (OK)
-as pendencias das recorrencias vão ser exibidas baseada em lógica sem dados salvos e devem ignorar recorrencias finalizadas (OK)
-ao pagar uma transacao de recorrencia ela será criada já paga (OK)
-necessário um serviço para buscar todas as transacoes pagas de uma ou mais recorrencias (OK)
-necessário um caso de uso que usa a recorrencia para contar quantas transacoes aquela recorrencia deve ter até a data final ou até a data atual caso não tenha data final (OK)
-após cada atualização das transações relacionadas a recorrencia, ou a própria recorrencia bater as informações de quantidade de transações pagas e transações totais daquela ocorrencia, se todas estiverem pagas alterar a flag da recorrencia para finalizada caso ela tenha data final(OK)
-Testar se a mudança de flag está funcionando(OK)
- Implementar endpoint delete da recorrência de despesas
- Replicar alterações para incomes


