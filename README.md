# Spring AI POS-GPT
A demonstration project showcasing how to integrate AI capabilities into Spring Boot applications using the Spring AI framework.

## Overview
This project implements a smart retail assistant for a Point of Sale (POS) system using Spring AI. It demonstrates how to use AI to enhance customer interactions through:
- Contextual product inquiries
- Order tracking
- Personalized product recommendations
- General customer support

## Technologies
- Spring Boot 3
- Spring AI
- Spring Data JPA
- H2 Database
- Anthropic Claude (with flexibility to switch to other providers)

## Getting Started
### Prerequisites

- Java 17 or higher
- Maven
- Anthropic API key (or another supported provider)

## Configuration

### 1. Clone the repository:

```
git clone https://github.com/lakitha96/posgpt.git
cd posgpt
```

### 2. Add your API key to application.properties:

```
spring.ai.anthropic.api-key=YOUR_API_KEY
```

### 3. Run the project
```
mvn spring-boot:run
```

## AI Model Options
Spring AI supports multiple AI providers:

Anthropic Claude: Requires API key from console.anthropic.com
OpenAI (ChatGPT): Requires API key from platform.openai.com
Deepseek: Offers local deployment option via Ollama for cost control and privacy

## API Endpoints

- POST /api/chat/{customerId} - Send a customer message and get AI response
- GET /api/chat/{customerId}/history - Get chat history for a customer
- GET /api/chat/{customerId}/recent?hours=24 - Get recent chat messages

### Example Usage
```
# Send a message from customer with ID 1
curl -X POST \
  http://localhost:8081/api/chat/1 \
  -H 'Content-Type: application/json' \
  -d '{
    "message": "Can you recommend some accessories for my Smartphone X Pro?"
  }'
```

## Key Features

- Context-Aware AI: Uses customer data, purchase history, and product details to create rich prompts
- Provider Agnostic: Built using Spring AI abstractions for flexibility between AI providers
- Database Integration: Demonstrates how to enrich AI interactions with application data

## Switching AI Providers
The project is configured to use Anthropic Claude by default, but can be easily switched to other providers:

```
# For OpenAI/ChatGPT
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.model.chat=openai
spring.ai.openai.chat.options.model=gpt-4o
spring.ai.openai.chat.options.temperature=0.7

# For local Deepseek via Ollama
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.model.chat=ollama
spring.ai.ollama.chat.options.model=deepseek
```

[This project is featured in the article Building AI-Powered Applications with Spring AI on Medium.](https://lakithaprabudh.medium.com/bringing-ai-to-java-why-spring-ai-is-a-game-changer-7b9c9e763d18)