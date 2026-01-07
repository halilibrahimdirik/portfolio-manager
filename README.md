# Portfolio Manager

This project consists of a Spring Boot backend and a React frontend.

## Project Structure

- `portfolio-manager-be`: Backend service (Java/Spring Boot)
- `portfolio-manager-ui`: Frontend application (React)

## Uygulamayı Çalıştırma

### Backend (Spring Boot)

Backend’i kök dizinden Maven ile başlatabilirsiniz.

**Seçenek 1: Kök Dizinden**
```bash
mvn -f portfolio-manager-be/portfolio-manager-service/pom.xml spring-boot:run
```

**Seçenek 2: Servis Dizininden**
```bash
cd portfolio-manager-be/portfolio-manager-service
mvn spring-boot:run
```

Not: Özel bir Maven ayar dosyanız varsa komutlara `-s /path/to/settings.xml` ekleyebilirsiniz.

Backend `http://localhost:8080` adresinde çalışır.

### Frontend (React)

UI dizinine geçip uygulamayı başlatın:

```bash
cd portfolio-manager-ui
npm install
npm start
```

Frontend `http://localhost:3000` adresinde çalışır.

### Fund Price Updates
To manually update fund prices in the database, use the following command:
```bash
curl -X POST http://localhost:8080/api/fund-prices/crawl
```

### TEFAS(Midas) CSV Import
CSV dosyasından fonları toplu yüklemek için UI üzerindeki “Import CSV” butonunu kullanabilir veya aşağıdaki endpoint’i çağırabilirsiniz:

Endpoint:
```
POST /api/assets/import
Content-Type: multipart/form-data
Form fields:
  - file: CSV dosyası
  - source: MIDAS veya YAPIKREDI
```

Örnek CSV içerik:
```csv
Fund Code,Quantity,Purchase Price,Purchase Date
TTE,100,5.50,2023-01-01
MAC,50,120.25,2023-05-15
IPB,1000,0.85,2023-11-20
```

Örnek curl:
```bash
curl -X POST "http://localhost:8080/api/assets/import" \
  -F "file=@/path/to/midas_fonlar.csv" \
  -F "source=MIDAS"
```
