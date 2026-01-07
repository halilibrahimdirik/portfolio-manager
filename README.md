# Portfolio Manager

This project consists of a Spring Boot backend and a React frontend.

## Project Structure

- `portfolio-manager-be`: Backend service (Java/Spring Boot)
- `portfolio-manager-ui`: Frontend application (React)

## Running the Application

### Backend (Spring Boot)

You can run the backend from the root directory using Maven.

**Option 1: From Root Directory**
```bash
mvn -f portfolio-manager-be/portfolio-manager-service/pom.xml spring-boot:run
```

**Option 2: From Service Directory**
```bash
cd portfolio-manager-be/portfolio-manager-service
mvn spring-boot:run
```

*Note: If you have a custom Maven settings file, append `-s /path/to/settings.xml` to the commands above.*

The backend will start on `http://localhost:8080`.

### Frontend (React)

Navigate to the UI directory and start the application:

```bash
cd portfolio-manager-ui
npm install
npm start
```

The frontend will start on `http://localhost:3000`.
