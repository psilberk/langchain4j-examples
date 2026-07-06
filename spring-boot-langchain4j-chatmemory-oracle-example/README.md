# Spring Boot 4 LangChain4j Oracle Chat Memory

Spring Boot 4 example that uses the LangChain4j Community Oracle Spring Boot 4 starter for Oracle-backed chat memory persistence.

This app demonstrates:

- `@AiService`-driven assistant creation
- Ollama chat model wiring with `OllamaChatModel`
- `MessageWindowChatMemory` backed by the Oracle `ChatMemoryStore`
- Oracle `ChatMemoryStore` auto-configuration from `langchain4j-community-oracle-spring-boot4-starter`

## Requirements

- Java 21+
- Maven 3.9+
- Oracle Database access (JDBC URL, username, password)
- Ollama server (local or remote)
- Local LangChain4j Community source checkout at:
  `/Users/bilallaariny/Downloads/langchain4j-community`

## Installation

Install the local Oracle Spring Boot 4 starter used by this example:

```bash
cd /Users/bilallaariny/Downloads/langchain4j-community
mvn -pl spring-boot4-starters/langchain4j-community-oracle-spring-boot4-starter -am -DskipTests -Dmaven.javadoc.skip=true -Denforcer.skip=true install
```

The local starter source currently needs `-Denforcer.skip=true` because its dependency management pins a few transitive versions below the LangChain4j 1.17 snapshot dependencies used by the Oracle module.

Build this app:

```bash
mvn -DskipTests compile
```

## Quick Start

Set environment variables:

```bash
export OLLAMA_BASE_URL=http://localhost:11434
export OLLAMA_MODEL_NAME=qwen3:8b

export ORACLE_JDBC_URL='jdbc:oracle:thin:@YOUR_DB_ALIAS?TNS_ADMIN=/absolute/path/to/Wallet_MemoryStore'
export ORACLE_JDBC_USER='YOUR_DB_USER'
export ORACLE_JDBC_PASSWORD='YOUR_DB_PASSWORD'

# Optional
export ORACLE_CHAT_MEMORY_TABLE=LANGCHAIN4J_CHAT_MEMORY
```

Run:

```bash
mvn spring-boot:run
```

CLI usage:
- Type your message and press Enter
- Type `exit` or `quit` to stop

## Auto-Configured Components

### Assistant

The `@AiService` interface is auto-implemented and injected:
- `src/main/java/dev/langchain4j/Assistant.java`

### Chat Model

Ollama chat model is created from:
- `langchain4j.ollama.chat-model.*`

### Chat Memory

Message window memory is created from:
- `langchain4j.chat-memory.max-messages`

### Oracle Chat Memory Store

Oracle-backed persistence is configured from:
- `langchain4j.community.oracle.chat-memory.*`

The starter also auto-configures an Oracle embedding store by default. This example disables it with
`langchain4j.community.oracle.embeddingstore.enabled=false` because it only demonstrates chat memory.

## Configuration Properties

The app configuration lives in `src/main/resources/application.yml`.

### Spring DataSource

| Property | Default in this app | Description |
| --- | --- | --- |
| `spring.datasource.url` | `${ORACLE_JDBC_URL:${url}}` | Oracle JDBC URL. |
| `spring.datasource.username` | `${ORACLE_JDBC_USER:${user}}` | Oracle DB username. |
| `spring.datasource.password` | `${ORACLE_JDBC_PASSWORD:${password}}` | Oracle DB password. |
| `spring.datasource.driver-class-name` | `oracle.jdbc.OracleDriver` | Oracle JDBC driver class. |

Legacy fallbacks supported by this app:
- `url` (fallback for `ORACLE_JDBC_URL`)
- `user` (fallback for `ORACLE_JDBC_USER`)
- `password` (fallback for `ORACLE_JDBC_PASSWORD`)

### Ollama Model

Prefix: `langchain4j.ollama.chat-model`

| Property | Default in this app | Description |
| --- | --- | --- |
| `base-url` | `${OLLAMA_BASE_URL:http://localhost:11434}` | Ollama endpoint. |
| `model-name` | `${OLLAMA_MODEL_NAME:qwen3:8b}` | Model used for chat completion. |

### Chat Memory Window

Prefix: `langchain4j.chat-memory`

| Property | Default in this app | Description |
| --- | --- | --- |
| `max-messages` | `20` | Max messages kept in window before eviction. |

### Oracle Chat Memory Store

Prefix: `langchain4j.community.oracle.chat-memory`

| Property | Default in this app | Description |
| --- | --- | --- |
| `enabled` | `true` | Enables Oracle chat memory auto-configuration. |
| `table-name` | `${ORACLE_CHAT_MEMORY_TABLE:LANGCHAIN4J_CHAT_MEMORY}` | Oracle table used for persisted memory entries. |
| `create-table` | `true` | Creates the table when the store is built. |
| `memory-id-column-name` | `MEMORY_ID` | Column used for chat memory IDs. |
| `content-column-name` | `CONTENT` | Column used for serialized chat messages. |
| `content-column-type` | `CLOB` | Column type for serialized chat messages. |

## Full Example `application.yml`

```yaml
spring:
  datasource:
    url: ${ORACLE_JDBC_URL:${url}}
    username: ${ORACLE_JDBC_USER:${user}}
    password: ${ORACLE_JDBC_PASSWORD:${password}}
    driver-class-name: oracle.jdbc.OracleDriver

langchain4j:
  ollama:
    chat-model:
      base-url: ${OLLAMA_BASE_URL:http://localhost:11434}
      model-name: ${OLLAMA_MODEL_NAME:qwen3:8b}
  chat-memory:
    max-messages: 20
  community:
    oracle:
      embeddingstore:
        enabled: false
      chat-memory:
        enabled: true
        table-name: ${ORACLE_CHAT_MEMORY_TABLE:LANGCHAIN4J_CHAT_MEMORY}
        create-table: true
        memory-id-column-name: MEMORY_ID
        content-column-name: CONTENT
        content-column-type: CLOB
```

## Disabling Oracle Chat Memory

Disable Oracle chat memory store auto-configuration:

```yaml
langchain4j:
  community:
    oracle:
      chat-memory:
        enabled: false
```

## Project Layout

```text
src/main/java/dev/langchain4j/
  Assistant.java
  ChatMemoryOracleApplication.java
  Demotools.java
  LangChain4jConfiguration.java

src/main/resources/
  application.yml
```
