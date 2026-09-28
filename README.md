# AI Study & Research Assistant

AI-powered study and research application that uses **LLM, RAG, embeddings, and vector search** to generate questions from uploaded study materials and evaluate user answers.

## 🚀 Overview

The application allows users to upload PDF study material, generate questions based on the document content, answer those questions, and receive AI-powered evaluation with scores, corrections, and feedback.

## 🔄 Workflow

```text
Upload PDF
    ↓
Extract Text
    ↓
Chunk Document
    ↓
Generate Embeddings
    ↓
Store in PostgreSQL + pgvector
    ↓
RAG Retrieval
    ↓
Generate Questions using LLM
    ↓
User Answers
    ↓
Retrieve Relevant Context
    ↓
AI Answer Evaluation
    ↓
Score + Correct Answer + Feedback
```

## 🛠️ Technology Stack

### Backend

* Java 21
* Spring Boot
* Spring AI
* Spring Web / REST API
* Spring Data JPA
* PostgreSQL
* pgvector
* Flyway
* Ollama
* Qwen 2.5 Coder 3B
* OpenAPI / Swagger
* JUnit & Mockito
* Testcontainers
* Docker

### AI / RAG

* Large Language Model (LLM)
* Retrieval-Augmented Generation (RAG)
* Embeddings
* Vector Similarity Search
* Prompt Engineering
* Structured AI Responses

## 📌 Current Scope

* Upload PDF study material
* Process and extract document content
* Split content into chunks
* Generate and store embeddings
* Semantic search using pgvector
* Generate questions using local LLM
* Submit answers
* AI-based answer evaluation
* Accuracy and scoring
* Correct answer and improvement feedback

## 🏗️ Architecture

```text
                 ┌─────────────────────┐
                 │     Spring Boot     │
                 │      Backend        │
                 └──────────┬──────────┘
                            │
              ┌─────────────┼─────────────┐
              │             │             │
              ▼             ▼             ▼
         Document       Question       Answer
          Module         Module         Module
              │             │             │
              └─────────────┼─────────────┘
                            ▼
                         RAG Layer
                            │
                    ┌───────┴────────┐
                    ▼                ▼
               Embeddings       Vector Search
                    │                │
                    └───────┬────────┘
                            ▼
                    PostgreSQL + pgvector
                            │
                            ▼
                         Ollama
                            │
                            ▼
                   Qwen 2.5 Coder 3B
```

## 📂 Project Structure

```text
src/main/java/
└── com.vikas.studyai
    ├── common
    ├── config
    ├── document
    ├── rag
    ├── ai
    ├── question
    ├── answer
    └── evaluation
```

## 🔌 Planned APIs

### Documents

```http
POST   /api/v1/documents
GET    /api/v1/documents
GET    /api/v1/documents/{id}
DELETE /api/v1/documents/{id}
GET    /api/v1/documents/{id}/status
```

### Questions

```http
POST /api/v1/documents/{id}/questions
GET  /api/v1/documents/{id}/questions
GET  /api/v1/questions/{id}
```

### Answers

```http
POST /api/v1/questions/{id}/answers
GET  /api/v1/answers/{id}
```

### Evaluation

```http
GET /api/v1/answers/{id}/evaluation
```

## 🎯 Project Goal

Build a practical AI-powered learning platform while exploring modern **Generative AI, LLM, RAG, embeddings, vector databases, prompt engineering, and Spring AI** using a Java/Spring Boot backend.

## 🚧 Project Status

**In Development**

The project is being developed incrementally, starting with the backend and core RAG pipeline.

## 🔮 Future Enhancements

* React + TypeScript frontend
* User authentication
* Personalized learning
* Difficulty-based questions
* Topic-wise performance analysis
* Knowledge Graph using Neo4j
* Redis caching
* Kafka-based asynchronous processing
* Multi-document RAG
* Multiple LLM providers
* AI-generated study plans
