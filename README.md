# ExamSpark 🎓

ExamSpark is a **Spring Boot + MySQL based Quiz Management System** that allows **students** to take quizzes and **admins** to manage quizzes, questions, and users.  

---

## 📌 Features

### 👤 User Panel
- Register and log in securely.
- View available quizzes.
- Take quizzes and submit answers.
- View results after completion.

### 🛠️ Admin Panel
- Manage quizzes (create, update, delete).
- Add, update, and delete questions.
- Manage user accounts.
- View quiz performance and reports.

---

## 🏗️ Tech Stack
- **Backend:** Spring Boot (Java)
- **Database:** MySQL
- **ORM:** Spring Data JPA (Hibernate)
- **Build Tool:** Maven
- **Security:** Spring Security + JWT Authentication
- **Frontend:** Thymeleaf

---

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/your-repo/ExamSpark.git
cd ExamSpark
```

### 2. Create the Database

Login to MySQL and run:
```sql
CREATE DATABASE examspark;
```
### 3. Configure Database Connection

Update the application.properties file:
``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/examspark
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```
### 4. Build & Run the Application

``` bash
mvn clean install
mvn spring-boot:run
```

The app will start at http://localhost:8080


## 📡 API Endpoints

Below is a list of available API endpoints grouped by functionality.  
All secured routes require a valid **JWT token** in the `Authorization` header.



### 🔑 Authentication
| Method | Endpoint          | Description                  |
|--------|-------------------|------------------------------|
| POST   | `/auth/register`  | Register a new user          |
| POST   | `/auth/login`     | Authenticate and get JWT     |


### 👤 User Endpoints
| Method | Endpoint                    | Description                      |
|--------|-----------------------------|----------------------------------|
| GET    | `/user/quizzes`             | Fetch all available quizzes      |
| GET    | `/user/quizzes/{id}`        | Fetch quiz details by ID         |
| POST   | `/user/quizzes/{id}/submit` | Submit answers for a quiz        |
| GET    | `/user/results`             | Retrieve results of completed quizzes |


### 🛠️ Admin Endpoints
| Method | Endpoint                  | Description                    |
|--------|---------------------------|--------------------------------|
| POST   | `/admin/quizzes`          | Create a new quiz              |
| PUT    | `/admin/quizzes/{id}`     | Update quiz details            |
| DELETE | `/admin/quizzes/{id}`     | Delete a quiz                  |
| POST   | `/admin/questions`        | Add a new question to a quiz   |
| PUT    | `/admin/questions/{id}`   | Update an existing question    |
| DELETE | `/admin/questions/{id}`   | Remove a question from a quiz  |
| GET    | `/admin/users`            | List all registered users      |
| DELETE | `/admin/users/{id}`       | Delete a user account          |

---

## 🧪 How It Works

1. **User Authentication**  
   - Users register or log in to receive a **JWT token**.  
   - The token is used in subsequent requests for accessing protected APIs.  

2. **User Flow**  
   - Users can browse quizzes, attempt them, submit answers, and check their results.  

3. **Admin Flow**  
   - Admins manage the quiz lifecycle: creating quizzes, adding/removing questions, and managing users.  

4. **Data Consistency**  
   - The system uses **transactional integrity** to prevent issues such as duplicate submissions or partial quiz updates.  

---

## 📂 Project Structure
``` bash
ExamSpark
│── src/main/java/com/examspark
│   ├── controller      # REST Controllers
│   ├── model           # Entity classes
│   ├── repository      # JPA Repositories
│   ├── service         # Business logic
│   └── security        # JWT + Spring Security
│
│── src/main/resources
│   ├── application.properties
│   └── static / templates (if frontend used)
│
└── pom.xml             # Maven dependencies
```

## 📄 Authors

**Shanmukha Poorna Chand**  
Java Full-Stack Developer | Passionate about Web Development and Clean UI Design  
📧 shanmukhapoornachand14316@gmail.com  
🔗 [LinkedIn Profile](www.linkedin.com/in/shanmukha-poorna-chand-adapaka)


**Satwika**  
Java Full-Stack Developer | Passionate about Web Development and Clean UI Design  
📧 satwikapalavslasa@gmail.com  
🔗 [LinkedIn Profile](www.linkedin.com/in/satwika-palavalasa-8413372a0/)


> *Built with ❤️*