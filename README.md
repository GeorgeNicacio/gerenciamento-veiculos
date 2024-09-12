# Sistema de Gerenciamento de Estacionamento

Este projeto é uma aplicação de gerenciamento de estacionamento que permite registrar entradas e saídas de veículos, associá-los a empresas e gerar relatórios de movimentação. O backend foi implementado usando **Spring Boot**, com APIs REST e suporte a **GraphQL** para consultas flexíveis e dinâmicas.

## Funcionalidades

- Cadastro de empresas e veículos.
- Controle de entradas e saídas de veículos.
- Geração de relatórios de movimentação por data e hora.
- Flexibilidade para consultas usando GraphQL.

## Tecnologias Utilizadas

- **Java** (Spring Boot)
- **GraphQL**
- **REST APIs**
- **Banco de Dados Relacional (H2)**
- **JUnit** (Testes automatizados)
- **Git** e **GitHub** (Controle de versão)

## Instruções de Instalação

### Pré-requisitos

- **Java 17** ou superior
- **Maven**
- **H2** ou outro banco de dados relacional
- Existem 2 Branchs uma se chama Rest com API Rest e outra GraphQL com o suporte ao mesmo.
  
### Passos para Rodar o Projeto

1. Clone o repositório:
   ```bash
   git clone https://github.com/GeorgeNicacio/gerenciamento-veiculos.git

2. Navegue até o diretório do projeto:
   ```bash
   cd gerenciamento-veiculos
3. Compile e execute o projeto:
   ```bash
   ./mvnw spring-boot:run
4. Acesse a aplicação e testar pelo swagger ou outras aplicações como postman: em http://localhost:8080/swagger-ui/index.html
5. Acesse a Branch do GrapQL para testes como o mesmo: https://github.com/GeorgeNicacio/gerenciamento-veiculos/tree/GraphQL

# Questões e Respostas

## 1. GraphQL
**Pergunta 1:** Explique o que é o GraphQL e como ele se diferencia de uma API REST tradicional.

**Resposta:** GraphQL é uma linguagem de consulta para APIs que permite ao cliente solicitar exatamente os dados de que precisa. A principal diferença em relação à API REST é que, enquanto em REST você faz múltiplas requisições a diferentes endpoints para obter dados relacionados, com o GraphQL você pode consultar várias fontes de dados em uma única requisição. Isso torna as consultas mais flexíveis e eficientes, pois evita o over-fetching (receber mais dados do que o necessário) e o under-fetching (receber menos dados do que o necessário).

**Pergunta 2:** Descreva como você implementaria o uso do GraphQL como BFF (Backend for Frontend) neste projeto de gerenciamento de estacionamento. Forneça exemplos práticos.

**Resposta:** Para implementar GraphQL como BFF (Backend for Frontend) no projeto de gerenciamento de estacionamento, eu criaria um endpoint GraphQL que servisse como ponto único de entrada para todas as consultas e mutações relacionadas a empresas e veículos. O cliente (frontend) poderia solicitar, por exemplo, a lista de veículos estacionados e, ao mesmo tempo, obter os detalhes das empresas associadas a esses veículos em uma única consulta.

Exemplo de consulta GraphQL:

 ```bash
mutation {
  cadastrarEmpresa(
    input: {
      nome: "Empresa Teste"
      cnpj: "12345678000199"
      endereco: "Rua Exemplo"
      telefone: "(11) 98765-4321"
      vagasMotos: 5
      vagasCarros: 10
      user: { username: "12345678000199", password: "senhaSegura" }
    }
  ) {
    id
    nome
    cnpj
    endereco
    telefone
  }
  cadastrarVeiculo(
    input: {
      marca: "Honda"
      modelo: "Civic"
      cor: "Preto"
      placa: "ABC-1234"
      tipo: CARRO
      empresaIds: [1]
    }
  ) {
    marca
    modelo
    cor
    placa
    tipo
  }
  registrarEntrada(empresaId: "1", veiculoId: "1") {
    id
  }
  registrarSaida(empresaId: "1", veiculoId: "1") {
    id
  }
}
```
**Pergunta 3:** Quais são os benefícios de utilizar GraphQL em relação à flexibilidade das consultas? Cite possíveis desafios ao utilizá-lo.

**Resposta:** Os principais benefícios de usar GraphQL são:

Flexibilidade: Permite ao cliente solicitar apenas os dados que realmente precisa.
Eficiência: Reduz o número de chamadas à API, combinando múltiplas requisições em uma única chamada.
Autodocumentação: As consultas e mutações são tipadas, facilitando a documentação automática.
Os principais desafios incluem:

Complexidade: Requer mais esforço de configuração e aprendizado, tanto para desenvolvedores backend quanto frontend.
N+1 Problem: Pode ocorrer quando as consultas fazem múltiplas chamadas ao banco de dados, o que pode impactar o desempenho.

## 2. Banco de Dados
**Pergunta 1:** Explique os principais conceitos de um banco de dados relacional, como tabelas, chaves primárias e estrangeiras.

**Resposta:** Em bancos de dados relacionais, os dados são organizados em tabelas, que são estruturas bidimensionais compostas de linhas (registros) e colunas (atributos). Cada tabela deve ter uma chave primária (primary key), que é um identificador único para os registros. As chaves estrangeiras (foreign keys) são utilizadas para estabelecer relacionamentos entre diferentes tabelas, vinculando os dados de uma tabela à outra.

**Pergunta 2:** No contexto de uma aplicação de gerenciamento de estacionamento, como você organizaria a modelagem de dados para suportar as funcionalidades de controle de entrada e saída de veículos?

**Resposta:** Eu criaria tabelas para Empresas, Veiculos e Movimentacoes, onde cada entrada de movimentação teria uma chave estrangeira para Veiculos e Empresas. A tabela de movimentações armazenaria as entradas e saídas de veículos, incluindo a hora da movimentação, tipo (entrada/saída), e a empresa associada.

**Pergunta 3:** Quais seriam as vantagens e desvantagens de utilizar um banco de dados NoSQL neste projeto?

Resposta: Vantagens:

Flexibilidade de Esquema: NoSQL permite armazenar dados de forma não estruturada, o que pode ser útil em sistemas com mudanças frequentes.
Escalabilidade: NoSQL pode lidar melhor com grandes volumes de dados distribuídos horizontalmente.
Desvantagens:

Consistência: Bancos de dados NoSQL muitas vezes não garantem consistência imediata, o que pode ser um problema em sistemas de controle rigoroso como o de estacionamento.
Consultas Complexas: Operações que exigem relacionamentos complexos entre dados podem ser mais difíceis de implementar em NoSQL.

##3. Agilidade
**Pergunta 1:** Explique o conceito de metodologias ágeis e como elas impactam o desenvolvimento de software.

**Resposta:** Metodologias ágeis são abordagens de desenvolvimento de software que enfatizam entregas contínuas e iterativas, colaboração entre equipes e adaptação a mudanças. Impactam o desenvolvimento ao promover ciclos de desenvolvimento curtos, focando em feedback rápido e melhoria contínua.

**Pergunta 2:** No desenvolvimento deste projeto, como você aplicaria princípios ágeis para garantir entregas contínuas e com qualidade?

**Resposta:** Aplicaria os princípios ágeis dividindo o desenvolvimento em pequenas sprints, com entregas incrementais de novas funcionalidades. Utilizaria ferramentas de CI/CD para garantir a integração contínua, e testes automatizados para garantir a qualidade do código em cada iteração.

**Pergunta 3:** Qual a importância da comunicação entre as equipes em um ambiente ágil? Dê exemplos de boas práticas.

**Resposta:** A comunicação clara e frequente entre as equipes é essencial para identificar bloqueios e alinhar expectativas. Boas práticas incluem reuniões diárias de stand-up, uso de ferramentas como Slack ou Microsoft Teams para comunicação rápida, e plataformas como Jira para acompanhamento das tarefas.

##4. DevOps
**Pergunta 1:** O que é DevOps e qual a sua importância para o ciclo de vida de uma aplicação?

**Resposta:** DevOps é uma abordagem que une desenvolvimento (Dev) e operações (Ops), com o objetivo de automatizar e integrar os processos de desenvolvimento, entrega e manutenção de software. É importante porque acelera o ciclo de vida das aplicações, reduzindo o tempo de entrega e melhorando a confiabilidade e a escalabilidade.

**Pergunta 2:** Descreva como você integraria práticas de DevOps no desenvolvimento desta aplicação de estacionamento. Inclua exemplos de CI/CD.

**Resposta:** Integraria práticas de DevOps utilizando pipelines de CI/CD, onde todo código commitado seria automaticamente testado, construído e implantado. Utilizaria ferramentas como Jenkins, GitHub Actions ou CircleCI para automatizar esse processo, garantindo entregas contínuas e rápidas.

**Pergunta 3:** Cite as ferramentas que você usaria para automatizar o processo de deploy e monitoramento da aplicação.

**Resposta:** Para deploy, usaria ferramentas como Docker e Kubernetes para containerização e orquestração, e para monitoramento, usaria Prometheus e Grafana, além de logs centralizados com ELK Stack (Elasticsearch, Logstash, Kibana).
