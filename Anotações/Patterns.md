## Patterns Design

### Simple Factory

- Uma factory simples quando há várias opções, ao invés de usar um switch ou vários ifs.

### State

- É necessário uma interface que tenha um metodo para cada state, e para cada state será criada uma classe que implementa essa interface e trata cada caso baseado no caso atual.
  - Ex. Um pedido com os status realizado, preparar, iniciar entrega e finalizar entrega, começa com o status realizado e pode apenas ir para o status preparar, ele não deve pular para o iniciar a partir do realizado.

### Facade

- Fornecer uma interface simplificada e unificada para um conjunto de interfaces em um subsistema. Ele oculta a complexidade do sistema subjacente.
  - Um sistema de biblioteca de áudio com várias classes (Decoder, Player, Equalizer) pode ter uma classe AudioFacade que fornece métodos simples como playAudio() ou stopAudio().

### Adapter

- Permitir que duas interfaces incompatíveis trabalhem juntas, convertendo a interface de uma classe em outra que o cliente espera.
  - Um adaptador que converte uma interface de tomada de três pinos para uma de dois pinos.

### Template

- Esse é o padrão de herança usando classe abstrata.

### Decorator
- É quase uma "recursividade" entre classes, onde todas as classes implementam a mesma interface com o metodo decorado, e cada uma delas implementa a sua decoração no metodo e chama o código da classe base.
    - Ex. Um sistema de café onde você tem um café básico e pode adicionar diferentes complementos como leite, açúcar ou chantilly. Cada complemento é um "decorador" que adiciona funcionalidade ao café básico sem alterar sua estrutura original.
