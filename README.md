# N1-ConstrucaoDeSoftware
# 🛒 Sistema de E-commerce - API REST (N1)

Este projeto é uma API REST para gerenciamento de um e-commerce simplificado, desenvolvida como requisito para a avaliação **N1 da disciplina de Construção de Software**. A aplicação foca na persistência de dados relacional e na implementação de regras de negócio essenciais no lado do servidor.

---

## 🚀 Tecnologias e Configuração

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.5.x
* **ORM:** Spring Data JPA / Hibernate
* **Banco de Dados:** H2 Database (Em memória)
* **Gerenciador de Dependências:** Maven

### Como rodar a aplicação:
1.  Certifique-se de ter o **JDK 21** configurado na sua IDE.
2.  Execute o comando `mvn spring-boot:run` ou rode a classe `SistemaecomerceApplication`.
3.  Acesse o Console do H2 em: `http://localhost:8080/h2-console`
    * **JDBC URL:** `jdbc:h2:file:./data/db-api`
    * **User:** `sa` | **Password:** (vazio)

---

## 🏗️ Arquitetura e Regras de Negócio

O projeto segue o padrão de **3 camadas**: `Controller` -> `Service` -> `Repository`.

### Regras Implementadas:
1.  **Regra de E-mail Único**: Validação no `ClienteService` para impedir cadastros duplicados.
2.  **Baixa de Estoque**: O estoque do produto é subtraído automaticamente ao criar um pedido.
3.  **Validação de Disponibilidade**: O pedido é bloqueado caso a quantidade solicitada supere o estoque.
4.  **Preço de Fechamento**: O `ItemPedido` armazena o preço do produto no momento da compra, evitando que alterações futuras no preço do produto alterem pedidos antigos.
5.  **Ciclo de Status**: Fluxo lógico controlado (`CRIADO`, `PAGO`, `ENVIADO`, `CANCELADO`).

---

## 📡 Guia de Endpoints (Bateria de Testes)

Para testar a API, siga a ordem sugerida abaixo para garantir que as chaves estrangeiras (IDs) existam.

### 1. Clientes (`/clientes`)
* **POST**: Cadastrar um novo cliente.
    ```json
    { "nome": "Luiz Santos", "email": "luiz@teste.com" }
    ```
* **GET**: Listar todos os clientes: `http://localhost:8080/clientes`
* **PUT**: Atualizar dados do cliente: `http://localhost:8080/clientes/1`

### 2. Endereços (`/enderecos`)
* **POST**: Cadastrar endereço (deve ser feito antes do pedido).
    ```json
    { "rua": "Avenida Central, 123", "cidade": "Goiânia", "cep": "74000-000" }
    ```
* **GET**: `http://localhost:8080/enderecos`

### 3. Produtos (`/produtos`)
* **POST**: Adicionar produto ao estoque.
    ```json
    { "nome": "Teclado Mecânico", "preco": 250.0, "estoque": 10 }
    ```
* **GET**: Ver catálogo e verificar estoque: `http://localhost:8080/produtos`

### 4. Pedidos (`/pedidos`)
* **POST**: Criar Pedido. (O sistema buscará o preço unitário do produto automaticamente).
    ```json
    {
      "cliente": { "id": 1 },
      "endereco": { "id": 1 },
      "itens": [
        { "produto": { "id": 1 }, "quantidade": 2 }
      ]
    }
    ```
* **GET**: Listar pedidos (com JSON limpo via `@JsonIgnoreProperties`): `http://localhost:8080/pedidos`
* **PATCH**: Alterar Status (Ex: de CRIADO para PAGO).
    * URL: `http://localhost:8080/pedidos/1/status?novoStatus=PAGO`
* **DELETE**: Cancelar pedido.
    * URL: `http://localhost:8080/pedidos/1`
    * *Nota: Só será permitido se o status for `CRIADO`.*

---

## 🛠️ Notas de Implementação

Durante o desenvolvimento, foram aplicadas soluções técnicas para atender aos requisitos do documento e garantir a estabilidade da API:

1.  **Isolamento de Camadas**: O `Controller` nunca acessa o `Repository`. Toda a inteligência (cálculos, validações de estoque e e-mail) reside na camada `Service`.
2.  **Consistência de Preço**: O `ItemPedido` não confia no preço enviado pelo cliente. O `PedidoService` busca o preço atual no banco de dados e o fixa no item, garantindo a integridade financeira do pedido.
3.  **Tratamento de Recursão Circular**: Para evitar o erro de `StackOverflow` e JSONs infinitos (comum em relações `@ManyToOne`), utilizamos:
    * `@JsonIgnoreProperties`: Para cortar ramos desnecessários da árvore JSON em consultas `GET`.
    * `@JsonBackReference` / `@JsonManagedReference`: Para gerenciar o vínculo bidirecional entre Pedido e Itens sem loops.
4.  **Persistência em Cascata**: Foi configurado `CascadeType.ALL` na relação Pedido -> Itens. Isso permite que ao salvar um Pedido, todos os seus itens sejam persistidos em uma única operação atômica.
5.  **Gestão Transacional**: O uso de `@Transactional` no `PedidoService` garante que, se houver erro ao baixar o estoque de um dos produtos, toda a operação de criação do pedido seja revertida (rollback), evitando dados inconsistentes.