# EduSys — Backend

A Spring Boot REST API built with Java 21 for the **EduSys** educational management platform at iCET, Panadura.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.7 |
| Database | MySQL |
| ORM | Spring Data JPA / Hibernate |
| Build Tool | Maven |
| Config | `.env` via `springboot4-dotenv` |
| Dev Tools | Spring Boot DevTools |

---

## Project Structure

```
edusys-backend/
├── src/
│   └── main/
│       ├── java/com/edusys/
│       │   └── Main.java               # Application entry point
│       └── resources/
│           └── application.yml         # Spring configuration
├── .env.example                        # Environment variable template
├── .env                                # Local env vars (git-ignored)
└── pom.xml
```

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- MySQL 8.0+

### 1. Clone the repository

```bash
git clone https://github.com/your-org/edusys-backend.git
cd edusys-backend
```

### 2. Set up the database

```sql
CREATE DATABASE edusys;
```

### 3. Configure environment variables

Copy `.env.example` to `.env` and fill in your values:

```bash
cp .env.example .env
```

```env
DB_URL=jdbc:mysql://localhost:3306/edusys
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
```

### 4. Run the application

```bash
mvn spring-boot:run
```

The server starts on `http://localhost:8080` by default.

---

## Feature Modules

The system is organized into **5 feature sets** covering **14 modules**:

### Feature Set 1 — Institute Administration
- **User & Role Management** — CRUD for Admin, Teacher, Reviewer, Student, and Parent accounts with role-based access control (RBAC)
- **Course Structure & Calendar Management** — Define courses, batches, and academic schedules
- **Admissions Module** — Track inquiries, intake students, and assign initial Career Scale Level

### Feature Set 2 — Student Lifecycle & Finance
- **Fee Management** — Record payments, auto-generate digital receipts, and send automated overdue reminders
- **Student Lifecycle Tracking** — Profile management from admission through graduation

### Feature Set 3 — Academic Process Management
- **Question Bank & Online Exams** — Central question bank with timed, one-attempt online tests
- **Course Material & Assignment Grading** — Teachers upload materials and grade student submissions
- **Auto-Result Publishing** — Automatic result publication with instant feedback after grading

### Feature Set 4 — Career Scale Process Management ★
- **Career Scale Task Creator** — Admin/Teacher defines tasks with point values and grading rubrics
- **Reviewer Evaluation Workflow** — Reviewers assess submissions and assign Career Scale Levels (L1–L7)
- **Career Points Engine** — Automatically calculates total career points and enforces L7 override business rules

### Feature Set 5 — Stakeholder Portals & Communication
- **QR Attendance System** — Time-sensitive QR codes for attendance marking with expiry enforcement
- **Dual-Progress Dashboards** — Students and parents view combined Academic + Career Scale progress in real time
- **Reporting Engine** — Attendance reports, progress reports, financial balance sheets, and merit lists

---

## User Roles

| Role | Access |
|---|---|
| **Admin** | Full system access — user management, courses, finance, reports |
| **Teacher** | Course materials, assignments, grading, exam scheduling, QR attendance |
| **Reviewer** | Career Scale submissions review and level assignment |
| **Student** | Own academic + career scale dashboard, exam attempts, attendance |
| **Parent** | Read-only view of their child's dual-progress dashboard |

---

## Troubleshooting

### Application fails to start — database connection error

Ensure MySQL is running and the credentials in `.env` are correct. Verify the database exists:

```sql
SHOW DATABASES LIKE 'edusys';
```

If missing, create it:

```sql
CREATE DATABASE edusys;
```

### `.env` values not being picked up

Make sure the `.env` file is in the project root (same directory as `pom.xml`), not inside `src/`. The `springboot4-dotenv` library loads it from the working directory at startup.

### Port 8080 already in use

Another process is occupying the port. Either stop that process or override the port in `.env`:

```env
SERVER_PORT=8081
```

And reference it in `application.yml`:

```yaml
server:
  port: ${SERVER_PORT:8080}
```

### Maven build fails — Java version mismatch

This project requires **Java 21**. Check your version:

```bash
java -version
```

If you have multiple JDKs installed, set `JAVA_HOME` to point to a Java 21 installation before running Maven.

### JPA/Hibernate schema errors on first run

If you see `Table 'edusys.xxx' doesn't exist`, ensure `spring.jpa.hibernate.ddl-auto` is set to `update` or `create` in `application.yml` for your development environment.

---

## Contributing

### Branching Strategy

```
main          — stable, production-ready code
dev           — integration branch for completed features
feature/<name> — individual feature branches (branch off dev)
fix/<name>     — bug fix branches
```

Always branch from `dev`, never directly from `main`.

### Workflow

1. Fork the repository and create your branch from `dev`:
   ```bash
   git checkout dev
   git pull origin dev
   git checkout -b feature/your-feature-name
   ```

2. Make your changes and write meaningful commit messages:
   ```bash
   git commit -m "feat: add career points calculation engine"
   ```

3. Push your branch and open a Pull Request targeting `dev`:
   ```bash
   git push origin feature/your-feature-name
   ```

4. Ensure your PR describes **what** changed and **why**. Reference any related issue numbers.

### Commit Message Convention

Use [Conventional Commits](https://www.conventionalcommits.org/):

| Prefix | Use for |
|---|---|
| `feat:` | New feature |
| `fix:` | Bug fix |
| `refactor:` | Code restructure without behaviour change |
| `docs:` | Documentation only |
| `test:` | Adding or updating tests |
| `chore:` | Build config, dependencies |

### Code Standards

- Follow standard Java naming conventions (camelCase for methods/variables, PascalCase for classes)
- Keep controllers thin — business logic belongs in the service layer
- All REST endpoints must be tested via Postman before submitting a PR
- Do not commit `.env` — it is git-ignored for a reason

---

## API Testing

Use [Postman](https://www.postman.com/) for API testing. Import and run collections against `http://localhost:8080`.

---

## Related Repositories

| Repository | Description |
|---|---|
| `edusys-backend` | This repo — Spring Boot REST API |
| `edusys-frontend` | React.js SPA (dashboards, portals) |

---

