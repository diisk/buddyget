## Principios

### S - Single Responsibility

### O - Open Closed

- Normalmente aplicado em regras que contém tipo.
- Muito comum usar o Strategy pattern para resolver

### L - LISKOV Substitution

- Pré-condição: A subclasse não pode exigir mais do que a classe base exigia.
  - Ex. A classe base diz que o saldo deve ser maior que 0, mas a subclasse fala que o saldo deve ser maior que 10.
- Pós-condição: A subclasse não pode reduzir as garantias fornecidas pela classe base após a execução do método.
  - Ex. todas as classes devem usar a mesma lógica após a execução.
  - Normalmente resolvido com o principio I criando interfaces.
- Invariância: A subclasse não pode alterar condições internas que a classe base mantinha constantes.
  - Ex. A classe base diz que o saldo deve ser maior que 0, mas a subclasse possui um credito especial adicionado ao saldo fazendo que o salvo fique negativo.
