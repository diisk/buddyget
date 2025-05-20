## Principios

### Regra Nº 3 - Encapsule os tipos primitivos

- Não usar tipos primitivos quando houver regras de negócio, ex: CPF,Email...
  - Usar as annotations Embeddable e Embbeded para mapear os objetos de valor no banco.

### Regra Nº 5 - Use apenas um ponto por linha

- Não fazer chamada de objetos dentro de objetos
- Criar um metodo de retorno que retorna o dado do objeto filho.

### Regra Nº 9 - Evite métodos getters e setters

- Evitar getters e setters para não criar entidades anemicas
- Para deixar as regras de négocio dentro das entidades
