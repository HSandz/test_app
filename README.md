# Task Manager - Full Stack Application

A simple yet powerful task management application built with Spring Boot backend and vanilla JavaScript frontend.

## 🚀 Features

- ✅ **CRUD Operations**: Create, Read, Update, Delete tasks
- 🔍 **Search Functionality**: Find tasks by title
- 🎯 **Filter Tasks**: View pending or completed tasks
- ✏️ **Inline Editing**: Edit tasks with a modal interface
- 📱 **Responsive Design**: Works on desktop and mobile
- 🎨 **Modern UI**: Clean and intuitive user interface
- 🔄 **Real-time Updates**: Instant feedback for all operations

## 🏗️ Architecture

```
┌─────────────────────┐    HTTP Requests    ┌─────────────────────┐
│                     │ ◄──────────────────► │                     │
│  Frontend           │                      │  Backend            │
│  (HTML/CSS/JS)      │                      │  (Spring Boot)      │
│  Port: 3000         │                      │  Port: 8080         │
│                     │                      │                     │
├─────────────────────┤                      ├─────────────────────┤
│ • Task Form         │                      │ • REST API          │
│ • Task List         │                      │ • CRUD Operations   │
│ • Search/Filter     │                      │ • CORS Enabled      │
│ • Edit Modal        │                      │ • H2 Database       │
└─────────────────────┘                      └─────────────────────┘
```

## 🛠️ Technology Stack

### Backend
- **Spring Boot 3.5.5** - Java framework
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database
- **Spring Web** - REST API
- **Maven** - Dependency management

### Frontend
- **HTML5** - Markup
- **CSS3** - Styling with modern design
- **Vanilla JavaScript** - Dynamic functionality
- **Fetch API** - HTTP requests

## 📋 Prerequisites

- **Java 24** or higher
- **Maven 3.6+** (included via Maven Wrapper)
- **Python 3.x** (for serving frontend)
- **Modern web browser**

## 🚀 Quick Start

### 1. Clone/Download the Project
```bash
# Navigate to your project directory
cd demo
```

### 2. Start the Backend Server
Open a terminal and run:
```bash
# Windows (PowerShell/CMD)
.\mvnw.cmd spring-boot:run

# Linux/MacOS
./mvnw spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Start the Frontend Server
Open a **new terminal** and run:
```bash
# Navigate to frontend directory
cd frontend

# Start Python HTTP server
python -m http.server 3000
```

The frontend will be available at `http://localhost:3000`

### 4. Access the Application
Open your browser and go to:
```
http://localhost:3000
```

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/tasks
```

### Endpoints

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| `GET` | `/api/tasks` | Get all tasks | - |
| `GET` | `/api/tasks/{id}` | Get task by ID | - |
| `POST` | `/api/tasks` | Create new task | `{"title": "string", "description": "string"}` |
| `PUT` | `/api/tasks/{id}` | Update task | `{"title": "string", "description": "string", "completed": boolean}` |
| `DELETE` | `/api/tasks/{id}` | Delete task | - |
| `GET` | `/api/tasks/status/{completed}` | Get tasks by status | - |
| `GET` | `/api/tasks/search?keyword={keyword}` | Search tasks | - |

### Example Requests

#### Create a Task
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Spring Boot","description":"Complete the tutorial"}'
```

#### Get All Tasks
```bash
curl http://localhost:8080/api/tasks
```

#### Update a Task
```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Spring Boot","description":"Completed the tutorial","completed":true}'
```

#### Search Tasks
```bash
curl "http://localhost:8080/api/tasks/search?keyword=Spring"
```

## 🎯 Usage Guide

### Adding Tasks
1. Fill in the "Title" field (required)
2. Optionally add a description
3. Click "Add Task"

### Managing Tasks
- **Edit**: Click the "Edit" button to modify title, description, or completion status
- **Toggle Status**: Use "Mark Complete" or "Mark Pending" buttons
- **Delete**: Click "Delete" and confirm

### Searching and Filtering
- **Search**: Type in the search box and click "Search" or press Enter
- **Filter**: Use "Show Pending" or "Show Completed" buttons
- **Reset**: Click "Show All" to see all tasks

## 📁 Project Structure

```
demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       ├── DemoApplication.java      # Main application class
│   │   │       ├── entity/
│   │   │       │   └── Task.java             # Task entity
│   │   │       ├── repository/
│   │   │       │   └── TaskRepository.java   # Data repository
│   │   │       ├── controller/
│   │   │       │   └── TaskController.java   # REST controller
│   │   │       └── config/
│   │   │           └── CorsConfig.java       # CORS configuration
│   │   └── resources/
│   │       └── application.properties        # App configuration
│   └── test/
├── frontend/
│   ├── index.html                           # Main HTML file
│   ├── styles.css                           # CSS styles
│   └── script.js                            # JavaScript functionality
├── pom.xml                                  # Maven dependencies
└── README.md                                # This file
```

## 🔧 Configuration

### Backend Configuration (`application.properties`)
```properties
# Application name
spring.application.name=demo

# H2 Database configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Server port
server.port=8080
```

### H2 Database Console
Access the H2 database console at: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

## 🚨 Troubleshooting

### Backend Issues

**Problem**: Java compilation errors
```
Solution: Ensure Java 24+ is installed and JAVA_HOME is set correctly
```

**Problem**: Port 8080 already in use
```bash
# Find and kill the process using port 8080
netstat -ano | findstr :8080
taskkill /PID <process_id> /F
```

### Frontend Issues

**Problem**: CORS errors in browser
```
Solution: Ensure CorsConfig.java is properly configured and backend is running
```

**Problem**: Frontend not connecting to backend
```
Solution: 
1. Check backend is running on http://localhost:8080
2. Test API directly: curl http://localhost:8080/api/tasks
3. Ensure frontend is served via HTTP server (not file://)
```

### Common Issues

**Problem**: "Connection refused" errors
```
Solution: Make sure both servers are running in separate terminals
```

**Problem**: Changes not reflecting
```
Solution: 
1. Hard refresh browser (Ctrl+F5)
2. Restart backend server
3. Clear browser cache
```

## 🎨 Customization

### Adding New Fields
1. Update `Task.java` entity
2. Update database schema in `application.properties`
3. Modify frontend forms and displays

### Changing Styles
Edit `frontend/styles.css` to customize:
- Colors
- Fonts
- Layout
- Responsive breakpoints

### Adding Features
- Extend `TaskController.java` for new endpoints
- Add JavaScript functions in `script.js`
- Update HTML structure in `index.html`

## 📝 License

This project is open source and available under the [MIT License](LICENSE).

## 🤝 Contributing

1. Fork the project
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## 📞 Support

If you have any questions or issues:
1. Check the troubleshooting section
2. Test API endpoints directly
3. Verify both servers are running
4. Check browser console for errors

---

**Happy Task Managing! 🎉**