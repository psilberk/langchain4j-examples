# LangChain4j Oracle ChatMemory Sample

This project demonstrates:

- `MessageWindowChatMemory` with Oracle-backed persistence (`OracleChatMemoryStore` from `langchain4j-oracle`)
- Tool-enabled assistant (`Demotools`)
- Custom metadata message (`CustomMessage`) added to memory

Main sample entrypoint:

- `src/main/sample/java/dev/langchain4j/Main.java`

## What You Need

1. Java 21
2. Maven 3.9+
3. Oracle DB access (connection URL, user, password)
4. Ollama running locally or remotely with model `qwen3:8b`
5. Local LangChain4j checkout available at `/Users/bilallaariny/Documents/pablo/langchain4j`

## Required Environment Variables

Set these before running:

```bash
export OLLAMA_BASE_URL=http://localhost:11434
export url='jdbc:oracle:thin:@YOUR_DB_ALIAS?TNS_ADMIN=/absolute/path/to/Wallet_MemoryStore'
export user='YOUR_DB_USER'
export password='YOUR_DB_PASSWORD'
```

Notes:

- Variable names must be exactly: `OLLAMA_BASE_URL`, `url`, `user`, `password`
- `OracleWalletDataSourceFactory` reads these exact names

## Build Oracle Integration Module (one-time or after changes)

From this project root:

```bash
cd /Users/bilallaariny/Documents/pablo/langchain4j
mvn -pl langchain4j-bom,langchain4j,langchain4j-open-ai,langchain4j-ollama,langchain4j-oracle -am -DskipTests -DskipITs -Denforcer.skip=true install
cd /Users/bilallaariny/Desktop/langchain4j-examples/langchain4j-chatmemory-oracle-example
```

This installs `dev.langchain4j:langchain4j-oracle:1.17.0-beta27-SNAPSHOT` and the required local LangChain4j modules into your local Maven repository.
The `-DskipITs` flag avoids running Oracle integration tests while installing the local artifacts. The `-Denforcer.skip=true` flag is needed for the current local checkout because `ojdbc-provider-jackson-oson:1.0.6` pulls `ojdbc8:23.8.0.25.04` while the Oracle module currently pins `ojdbc8:23.5.0.24.07`.

The sample uses `CHAT_MEMORY_JSON` and configures `OracleChatMemoryStore.ContentColumnType.JSON`, so the chat history is stored in Oracle's native `JSON` column type.

## Build This Project

```bash
mvn -DskipTests compile
```

## Run The Sample

### IntelliJ (recommended)

1. Open `src/main/sample/java/dev/langchain4j/Main.java`
2. Run `Main.main()`
3. Chat in terminal, type `exit` or `quit` to stop
