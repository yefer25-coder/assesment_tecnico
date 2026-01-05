# Sistema de Gestión de Proyectos y Tareas

Este proyecto es una implementación de referencia de un backend RESTful utilizando **Java 17**, **Spring Boot 3** y **Arquitectura Hexagonal (Ports & Adapters)**.

## 📋 Descripción

El sistema permite la gestión de usuarios, proyectos y tareas, asegurando que:
- Un proyecto pertenece a un usuario.
- Una tarea pertenece a un proyecto.
- Solo el propietario puede modificar sus recursos.
- Se aplican reglas de negocio como validación de activación de proyectos y auditoría de acciones.

## 🚀 Tecnologías

- **Lenguaje**: Java 17
- **Framework**: Spring Boot 3.x
- **Arquitectura**: Hexagonal (Clean Architecture)
- **Base de Datos**: PostgreSQL 15
- **Migraciones**: Flyway
- **Seguridad**: Spring Security + JWT (Stateless)
- **Documentación**: OpenAPI / Swagger
- **Contenedorización**: Docker & Docker Compose

## 🛠️ Requisitos Previos

- **Docker** y **Docker Compose** (Recomendado)
- Opcional para desarrollo local:
    - Java 17 JDK
    - Maven 3.8+
    - PostgreSQL local

## 🏃‍♂️ Ejecución

### Opción 1: Docker Compose (Recomendado)

Esta es la forma más sencilla de levantar la aplicación y la base de datos.

1. **Construir y levantar los contenedores**:
   ```bash
   docker compose up --build
   ```
2. **Acceder a la API**:
   - La aplicación estará disponible en: `http://localhost:8080`
   - Documentación Swagger: `http://localhost:8080/swagger-ui.html`

### Opción 2: Ejecución Local

1. Asegúrate de tener una base de datos PostgreSQL corriendo en `localhost:5432`.
2. Configura las credenciales en `src/main/resources/application.properties` si son diferentes a `postgres/postgres`.
3. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```

## 🔑 Credenciales de Prueba

El sistema incluye datos de prueba precargados (vía Flyway `V2__seed_data.sql`).

| Rol | Usuario | Contraseña |
|---|---|---|
| **Usuario** | `testuser` | `password123` |

También puedes registrar nuevos usuarios mediante el endpoint `POST /api/auth/register`.

## 🏗️ Decisiones Técnicas

### 1. Arquitectura Hexagonal
Se ha seguido estrictamente la arquitectura de Puertos y Adaptadores para desacoplar el núcleo del negocio de la infraestructura.
- **Domain**: Contiene los modelos (`User`, `Project`, `Task`) y las interfaces de los puertos (`in` y `out`). No tiene dependencias de Spring ni JPA.
- **Application**: Contiene los Servicios (`UseCases`) que implementan la lógica de negocio.
- **Infrastructure**: Contiene los adaptadores para Base de Datos (JPA), Seguridad (JWT), API (Controllers) y Configuración.

### 2. Seguridad (JWT)
- Se implementó autenticación mediante **JSON Web Tokens (JWT)**.
- **Sin Roles en Base de Datos**: Siguiendo estrictamente el modelo de datos del enunciado, no se almacena un campo `role` en la base de datos. La autorización se basa en la autenticación y la propiedad del recurso (Owner Check).
- Spring Security se configura con una política `STATELESS`.

### 3. Persistencia
- **Flyway**: Se utiliza para el versionado y migración de la base de datos, asegurando que el esquema esté siempre sincronizado.
- **JPA**: Se usan Entidades JPA (`UserEntity`, etc.) separadas de los Modelos de Dominio para mantener la pureza del dominio. Se usan Mappers para convertir entre ellos.

### 4. Testing
- Se incluyen pruebas unitarias con **JUnit 5** y **Mockito** enfocadas en los Casos de Uso de la capa de Aplicación, validando las reglas de negocio sin levantar el contexto de Spring.

## 📚 API Endpoints Principales

- **Auth**:
    - `POST /api/auth/register`: Registrar usuario.
    - `POST /api/auth/login`: Iniciar sesión (Obtener Token).
- **Proyectos**:
    - `POST /api/projects`: Crear proyecto.
    - `GET /api/projects`: Listar proyectos del usuario.
    - `PATCH /api/projects/{id}/activate`: Activar proyecto.
- **Tareas**:
    - `POST /api/projects/{projectId}/tasks`: Crear tarea.
    - `PATCH /api/tasks/{id}/complete`: Completar tarea.

---
Desarrollado como parte del Assessment Técnico.
