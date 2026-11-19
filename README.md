# Rota Segura API

API REST de alerta de enchentes desenvolvida para a Global Solution 2026/2 (FIAP), disciplina Domain Driven Design - Java.

- Deploy: https://rota-segura-api.onrender.com/swagger-ui/index.html
- Repositório: https://github.com/maatvasques/rota-segura-api

## Integrantes

Turma 2ESPW

| Nome | RM |
|------|----|
| Bernardo Yuji Rodriguez Hanashiro | RM565266 |
| Matheus Vasques | RM563309 |

## Sobre o projeto

No Jardim Pantanal, na zona leste de São Paulo, os córregos que deságuam no Tietê transbordam com frequência em dias de chuva forte. Muitos moradores só percebem o risco quando a água já está dentro de casa e não sabem para qual abrigo ir.

A Rota Segura recebe as leituras de sensores instalados nos córregos (nível da água em cm e chuva em mm). Quando o nível passa de 80% do limite do sensor, a API gera um alerta de ATENCAO; quando atinge o limite, gera um alerta de PERIGO. Junto com o alerta, a resposta traz os abrigos ativos e com vagas no mesmo bairro. A Defesa Civil também pode criar alertas manualmente e encerrá-los quando o risco passar.

ODS relacionados: 11 (Cidades e comunidades sustentáveis) e 13 (Ação contra a mudança global do clima).

## Tecnologias

- Java 21
- Spring Boot 3.5 (Web, Data JPA, Validation)
- Oracle Database (servidor da FIAP)
- H2 (banco em memória, perfil `prod`)
- Springdoc OpenAPI (Swagger)
- Maven
- Docker
- Render

## Estrutura

```
src/main/java/br/com/fiap/rotasegura
├── controller   requisições HTTP
├── bo           regras de negócio
├── dao          acesso ao banco (Spring Data JPA)
├── model        entidades
├── dto          objetos de entrada e saída
└── exception    exceções e tratamento global de erros
```

O arquivo `script.sql` contém o DDL das tabelas no Oracle (`TB_RS_ABRIGO`, `TB_RS_SENSOR`, `TB_RS_LEITURA` e `TB_RS_ALERTA`).

## Como executar

Requisitos: JDK 21 e Maven (ou o IntelliJ, que já traz o Maven).

### Com Oracle

A conexão é configurada por variáveis de ambiente:

| Variável | Valor |
|----------|-------|
| `DB_USER` | RM do aluno |
| `DB_PASSWORD` | senha do Oracle |
| `DB_URL` | opcional, padrão `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` |

Também é possível copiar o `.env.example` para `.env` e preencher os dados. O `.env` não é versionado.

```bash
mvn spring-boot:run
```

### Com H2 (sem Oracle)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Depois de iniciar, acesse http://localhost:8080/swagger-ui.html. Na primeira execução são cadastrados 4 abrigos, 3 sensores e 5 leituras de exemplo.

### Com Docker

```bash
docker build -t rota-segura-api .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod rota-segura-api
```

### Deploy no Render

Web Service do tipo Docker, branch `master`, plano Free. No deploy publicado a variável `SPRING_PROFILES_ACTIVE` está definida como `prod`, então a API usa o H2 e os dados voltam aos de exemplo quando o serviço reinicia. Para usar o Oracle, basta remover essa variável e definir `DB_USER` e `DB_PASSWORD`.

No plano gratuito o serviço hiberna após 15 minutos sem acesso, e a primeira requisição pode levar cerca de 1 minuto.

## Endpoints

### Abrigos

| Método | Rota | Descrição | Status |
|--------|------|-----------|--------|
| GET | `/abrigos` | Lista os abrigos | 200 |
| GET | `/abrigos/{id}` | Busca um abrigo | 200 |
| GET | `/abrigos/bairro/{bairro}` | Abrigos ativos com vaga no bairro | 200 |
| POST | `/abrigos` | Cadastra um abrigo | 201 |
| PUT | `/abrigos/{id}` | Atualiza um abrigo | 200 |
| DELETE | `/abrigos/{id}` | Remove um abrigo | 204 |

### Sensores

| Método | Rota | Descrição | Status |
|--------|------|-----------|--------|
| GET | `/sensores` | Lista os sensores | 200 |
| GET | `/sensores/{id}` | Busca um sensor | 200 |
| GET | `/sensores/{id}/leituras` | Histórico de leituras | 200 |
| POST | `/sensores` | Cadastra um sensor | 201 |
| PUT | `/sensores/{id}` | Atualiza um sensor | 200 |
| DELETE | `/sensores/{id}` | Remove um sensor sem leituras | 204 |

### Leituras

| Método | Rota | Descrição | Status |
|--------|------|-----------|--------|
| POST | `/leituras` | Registra uma leitura e gera alerta quando necessário | 201 |

### Alertas

| Método | Rota | Descrição | Status |
|--------|------|-----------|--------|
| GET | `/alertas` | Lista os alertas | 200 |
| GET | `/alertas/ativos` | Lista os alertas ativos | 200 |
| GET | `/alertas/{id}` | Busca um alerta | 200 |
| POST | `/alertas` | Cria um alerta manual | 201 |
| PUT | `/alertas/{id}/encerrar` | Encerra um alerta | 200 |
| DELETE | `/alertas/{id}` | Remove um alerta | 204 |

### Erros

Os erros seguem sempre o mesmo formato:

```json
{
  "status": 404,
  "mensagem": "Abrigo não encontrado com id 9999",
  "timestamp": "2026-11-18T22:45:56.710"
}
```

| Situação | Status |
|----------|--------|
| Registro ou rota inexistente | 404 |
| Dados inválidos ou JSON mal formatado | 400 |
| Parâmetro com tipo errado | 400 |
| Regra de negócio violada | 400 |
| Método não suportado | 405 |
| Restrição do banco violada | 409 |
| Erro inesperado | 500 |

## Exemplo

O sensor `SNS-001` tem limite de 180 cm. Uma leitura de 185 cm gera um alerta de PERIGO:

```http
POST /leituras
Content-Type: application/json

{
  "codigoSensor": "SNS-001",
  "nivelCm": 185.0,
  "chuvaMm": 42.5
}
```

Resposta (201):

```json
{
  "leitura": {
    "id": 6,
    "codigoSensor": "SNS-001",
    "nivelCm": 185.0,
    "chuvaMm": 42.5,
    "dataHora": "2026-11-18T22:45:55.912"
  },
  "alerta": {
    "id": 1,
    "nivel": "PERIGO",
    "mensagem": "PERIGO: o córrego em Córrego Três Pontes - ponte da Rua das Flores atingiu 185.0 cm (limite 180.0 cm). Procure um abrigo no bairro Jardim Pantanal.",
    "bairro": "Jardim Pantanal",
    "dataHora": "2026-11-18T22:45:55.914",
    "ativo": true,
    "codigoSensor": "SNS-001",
    "leituraId": 6
  },
  "abrigosDisponiveis": [
    {
      "id": 1,
      "nome": "EMEF Jardim Pantanal",
      "endereco": "Rua das Acácias, 120",
      "bairro": "Jardim Pantanal",
      "capacidade": 150,
      "vagasDisponiveis": 120,
      "latitude": -23.4952,
      "longitude": -46.4318,
      "ativo": true
    }
  ]
}
```

Leituras entre 144 cm e 180 cm geram ATENCAO. Abaixo de 144 cm não há alerta e a resposta vem com `alerta` nulo e a lista de abrigos vazia.

O arquivo `requests.http` tem todas as requisições prontas para testar no IntelliJ.
