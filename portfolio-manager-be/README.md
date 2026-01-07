# Portfolio Manager

## Running the Application

### Backend (Spring Boot)

Navigate to the service directory (`portfolio-manager-service`) and run:

```bash
cd portfolio-manager-service
mvn spring-boot:run
```

### Frontend (React/NPM)

Navigate to the UI directory (`../portfolio-manager-ui`) and run:

```bash
cd ../portfolio-manager-ui
npm install
npm start
```

## Fund Price Updates

To manually update fund prices in the database, use the following command:

```bash
curl -X POST http://localhost:8080/api/fund-prices/crawl
```
