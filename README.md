# XardHR Backend

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue.svg)
![License](https://img.shields.io/badge/License-MIT-purple.svg)

</div>

XardHR is a comprehensive HR management platform designed to streamline employee management, handle financial operations, and manage user access through a role-based system. The platform serves as a centralized solution for HR operations, employee self-service, and administrative tasks.

## 🚀 Features

### Core Modules

- **Employee Management**
  - Profile management
  - Department assignment and transfer
  - Document management
  - Employee status tracking

- **Time & Attendance**
  - Daily attendance tracking
  - Leave request management
  - Leave balance tracking
  - Attendance reports generation

- **Department Management**
  - Department creation and modification
  - Hierarchy management
  - Employee assignment
  - Department head assignment

- **Financial Management**
  - Bank account information management
  - Expense request submission and approval
  - Transaction tracking
  - Payroll processing (bonus feature)

### Security Features

- Role-based access control (RBAC)
- JWT authentication
- Password validation
- Secure API endpoints
- CORS configuration

## 🛠️ Technology Stack

- **Framework:** Spring Boot 3.4.0
- **Language:** Java 17
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA
- **Security:** Spring Security with JWT
- **Database Migration:** Liquibase
- **Build Tool:** Maven
- **Documentation:** Swagger/OpenAPI
- **Testing:** Spring Boot Test

## 📋 Prerequisites

- JDK 17 or later
- Maven 3.6+
- PostgreSQL 12+
- Your favorite IDE (IntelliJ IDEA recommended)

## 🔧 Setup & Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/bantaaa/xardhr.git
   cd xardhr-backend
   ```

2. **Configure database**
   ```bash
   sudo -u postgres createuser XardHR
   sudo -u postgres createdb XardHR
   sudo -u postgres psql
   alter user XardHR with encrypted password 'XardHR';
   grant all privileges on database XardHR to XardHR;
   ```

3. **Configure application.yaml**
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql:///XardHR
       username: XardHR
       password: XardHR
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will be available at `http://localhost:8081`

## 🔐 Security Configuration

The application uses JWT for authentication. Configure your JWT settings in `application.yaml`:

```yaml
jwt:
  secret: your-256-bit-secret
  expiration: 86400000  # 24 hours in milliseconds
```

## 🎯 API Endpoints

### Authentication
- `POST /auth/login` - Authenticate user
- `POST /auth/register` - Register new user

### Employee Management
- `GET /api/employees` - Get all employees
- `GET /api/employees/{id}` - Get employee by ID
- `POST /api/employees` - Create new employee
- `PUT /api/employees/{id}` - Update employee
- `PUT /api/employees/{id}/deactivate` - Deactivate employee

### Department Management
- `GET /api/departments` - Get all departments
- `POST /api/departments` - Create department
- `PUT /api/departments/{id}` - Update department
- `DELETE /api/departments/{id}` - Delete department

### Leave Management
- `POST /api/leaves/request` - Submit leave request
- `PUT /api/leaves/{id}/status` - Update leave request status
- `GET /api/leaves/employee/{userId}` - Get employee leave requests

## 👥 Role-Based Access

The system supports four user roles:
- `ADMIN` - Full system access
- `HR_MANAGER` - HR operations and approvals
- `DEPT_HEAD` - Department management
- `EMPLOYEE` - Self-service operations

## 🧪 Testing

Run tests using Maven:
```bash
mvn test
```

## 📚 Documentation

API documentation is available at:
- Swagger UI: `http://localhost:8081/swagger-ui/`
- OpenAPI docs: `http://localhost:8081/v3/api-docs`

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## ✨ Acknowledgments

- Spring Boot team for the amazing framework
- The HR community for valuable insights
- All contributors who participate in this project
