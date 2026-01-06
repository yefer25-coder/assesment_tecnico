# Project and Task Management System

Full-stack project and task management system implemented with **Hexagonal Architecture**, **Spring Boot 3**, **PostgreSQL**, and frontend in **HTML/JavaScript**.

## 📋 Description

System that allows:
- User registration and authentication (JWT)
- Project creation and management
- Task creation and management per project
- Project activation (requires at least one task)
- Task completion
- Action auditing and notifications

**Business Rules:**
- A project belongs to a user
- A task belongs to a project
- Only the owner can modify their resources
- A project cannot be activated without tasks

## 🚀 Technologies

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot 3.3.5
- **Architecture**: Hexagonal (Ports & Adapters)
- **Database**: PostgreSQL 15
- **Migrations**: Flyway
- **Security**: Spring Security + JWT (Stateless)
- **Documentation**: OpenAPI / Swagger UI
- **Testing**: JUnit 5 + Mockito

### Frontend
- **HTML5** + **CSS3** + **Vanilla JavaScript**
- **Bootstrap 5** (UI Framework)
- **Fetch API** (HTTP Client)
- **LocalStorage** (JWT Token Management)

### DevOps
- **Docker** + **Docker Compose**
- **Nginx** (Frontend Server)

## 🛠️ Prerequisites

- **Docker** and **Docker Compose** installed
- Optional for local development:
    - Java 17 JDK
    - Maven 3.8+
    - Python 3 (for local HTTP server for frontend)

## 🏃‍♂️ Steps to Run the Application

### Option 1: Docker Compose (Production - Recommended)

Launch the entire stack (Database + Backend + Frontend):

```bash
# 1. Clone the repository
git clone <repository-url>
cd projects

# 2. Start all services
docker compose up --build

# 3. Access the application
# Frontend: http://localhost
# Backend API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### Option 2: Local Development

For development with hot-reload:

```bash
# Terminal 1: Database
docker compose up db_new

# Terminal 2: Backend
mvn spring-boot:run

# Terminal 3: Frontend
cd frontend
python3 -m http.server 8000

# Access:
# Frontend: http://localhost:8000
# Backend: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
```

## 🔑 Test Credentials

The system includes preloaded test data (Flyway migration `V2__seed_data.sql`):

| Username | Password | Description |
|---------|------------|-------------|
| `testuser` | `password123` | Test user with sample project and task |

You can also register new users from the frontend or via:
```bash
POST /api/auth/register
{
  "username": "newuser",
  "email": "user@example.com",
  "password": "password123"
}
```

## 🏗️ Technical Decisions

### 1. Hexagonal Architecture (Clean Architecture)

**Project structure:**
```
src/main/java/com/app/projects/
├── domain/              # Business core (no external dependencies)
│   ├── model/          # Domain entities (User, Project, Task)
│   └── port/
│       ├── in/         # Input ports (Use Cases)
│       └── out/        # Output ports (Repositories, Services)
├── application/         # Use cases (Business logic)
│   └── service/        # Use Case implementations
└── infrastructure/      # Adapters (Frameworks and tools)
    ├── adapter/        # Output adapters
    │   ├── persistence/    # JPA Repositories
    │   ├── security/       # JWT, Password Encoder
    │   ├── audit/          # Audit logs
    │   └── notification/   # Notification system
    ├── rest/           # Input adapters (Controllers, DTOs)
    ├── security/       # Spring Security configuration
    └── config/         # General configuration
```

**Benefits:**
- ✅ Pure domain without framework dependencies
- ✅ Easy testing (interface mocks)
- ✅ Swappable adapters (e.g., switch from PostgreSQL to MongoDB without touching domain)
- ✅ Complies with SOLID principles

### 2. JWT Stateless Security

- **Authentication**: JWT (JSON Web Tokens) with 24-hour expiration
- **Authorization**: Ownership-based (only the owner modifies their resources)
- **No roles in DB**: Following the statement's data model
- **CORS**: Configured to allow frontend on ports 80/8000

### 3. Persistence and Migrations

- **Flyway**: Automatic schema versioning
- **JPA Entities separated from Domain**: Mappers for conversion
- **Relationships**: `@ManyToOne` between Task-Project and Project-User
- **Soft Delete**: `deleted` field for auditing

### 4. Auditing and Notifications

Implemented via **Ports & Adapters**:
- **AuditLogPort**: Records actions (ACTIVATE_PROJECT, COMPLETE_TASK)
- **NotificationPort**: Notifies events
- **Current implementation**: Console logs (SLF4J)
- **Easily replaceable** by: Database, Email, Slack, etc.

### 5. Simple and Functional Frontend

- **No JS frameworks**: Vanilla JavaScript for simplicity
- **Bootstrap 5**: Modern and responsive UI
- **Unique design**: Emerald green color (differentiator)
- **Improved UX**: 
  - Confirmation modal instead of native `confirm()`
  - Toasts for feedback
  - Subtle animations
  - Bootstrap Icons

### 6. Testing

- **Unit Tests**: JUnit 5 + Mockito for use cases
- **Coverage**: Critical business rules (activation, task completion)
- **No Spring Context**: Fast and focused tests

## 📚 API Endpoints

### Authentication
- `POST /api/auth/register` - Register user
- `POST /api/auth/login` - Login (get JWT)

### Projects
- `GET /api/projects` - List authenticated user's projects
- `POST /api/projects` - Create project (initial state: DRAFT)
- `PATCH /api/projects/{id}/activate` - Activate project (requires tasks)

### Tasks
- `GET /api/projects/{projectId}/tasks` - List project tasks
- `POST /api/projects/{projectId}/tasks` - Create task
- `PATCH /api/tasks/{id}/complete` - Complete task

**Full documentation**: `http://localhost:8080/swagger-ui.html`

## 🧪 Functional Tests

1. **Register**: Create new user
2. **Login**: Authenticate with `testuser` / `password123`
3. **Create Project**: Name "My Project"
4. **Try to Activate without Tasks**: Should fail with 400 error
5. **Create Task**: "Test task"
6. **Activate Project**: Should now work (state → ACTIVE)
7. **Complete Task**: Mark as completed

## 📦 Repository Structure

```
.
├── src/                    # Backend source code
├── frontend/               # Frontend web application
│   ├── css/               # Custom styles
│   ├── js/                # JavaScript logic
│   ├── index.html         # Login/Register
│   ├── dashboard.html     # Main dashboard
│   └── Dockerfile         # Nginx to serve frontend
├── docker-compose.yml     # Service orchestration
├── Dockerfile             # Backend (multi-stage build)
├── pom.xml               # Maven dependencies
└── README.md             # This file
```

## 🐳 Docker Services

The `docker-compose.yml` defines 3 services:

1. **db_new**: PostgreSQL 15 (port 5433)
2. **app**: Spring Boot Backend (port 8080)
3. **frontend**: Nginx serving HTML/CSS/JS (port 80)

## 📝 Logs and Monitoring

Audit and notification logs can be viewed in the backend console:

```bash
# View backend logs
docker compose logs -f app

# Example output:
# AUDIT: Action=ACTIVATE_PROJECT EntityId=abc-123
# NOTIFICATION: Proyecto activado: abc-123
```

---

**Developed as part of Technical Assessment**  
Hexagonal Architecture | Spring Boot 3 | PostgreSQL | Docker
