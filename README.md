# 🤖 IAgentebot

IAgentebot es un bot de Telegram construido con Spring Boot que actúa como frontend de un sistema de IA personal.  
Se comunica con un backend (`iagente`) que gestiona memoria de usuario y respuestas basadas en LLM (Ollama).

---

## 🚀 Arquitectura del sistema

El sistema está compuesto por varios servicios Docker:

- 🧠 **iagente** → Backend principal (Spring Boot + IA + memoria)
- 🤖 **iagentebot** → Bot de Telegram (frontend)
- 🗄️ **PostgreSQL** → Base de datos para memoria e historial
- 🧠 **Ollama** → Modelo LLM local (ej: qwen2.5)

---

## 📦 Flujo de funcionamiento

1. El usuario escribe al bot de Telegram
2. El bot envía el mensaje al backend `iagente`
3. El backend:
   - Guarda el chat
   - Extrae memoria del usuario
   - Consulta el LLM (Ollama)
4. Devuelve la respuesta al bot
5. El bot responde en Telegram

---

## 🧰 Tecnologías utilizadas

- Java 17+
- Spring Boot
- Spring AI
- Telegram Bots API
- PostgreSQL
- Docker & Docker Compose
- Ollama (LLM local)

---

## ⚙️ Configuración

Variables principales del bot:

```yaml
iagente:
  api:
    url: http://iagente:8080/agent/chat

telegram:
  bot:
    token: TU_TOKEN_AQUI