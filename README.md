# FinSight — Financial Document Intelligence Platform

A RAG-powered document intelligence platform built in Java that enables
financial analysts to query large volumes of financial documents through
a natural language interface — with exact source citations.

## Tech Stack
- **Java 21** + **Spring Boot 4.0.6**
- **LangChain4j** — RAG pipeline orchestration
- **MySQL 8** — relational data
- **Qdrant** — vector database for semantic search
- **MinIO** — document file storage
- **Apache PDFBox** — PDF text extraction
- **Docker Compose** — local development stack

## Getting Started

### Prerequisites
- Java 21
- Maven 3.9+
- Docker Desktop

### Run the infrastructure
```bash
docker compose up -d
```

### Set your OpenAI API key
```bash
# Windows
set OPENAI_API_KEY=your_key_here

# Mac/Linux
export OPENAI_API_KEY=your_key_here
```

### Run the application
```bash
mvn spring-boot:run
```

## Services
| Service | URL |
|---|---|
| API | http://localhost:8080 |
| Qdrant Dashboard | http://localhost:6333/dashboard |
| MinIO Console | http://localhost:9001 |

## Disclaimer
FinSight is an academic portfolio project. It does not provide financial
advice or investment recommendations.