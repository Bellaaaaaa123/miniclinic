# MiniClinic 社區診所掛號系統

一個以 Spring Boot 實作的社區診所掛號系統，支援醫師登入、病患掛號、
掛號狀態管理等功能。

## 線上 Demo

https://miniclinic-bellaaaaaa123.onrender.com

## 目錄

- [技術棧](#技術棧)
- [功能清單](#功能清單)
- [Prerequisites](#prerequisites)
- [本機執行](#本機執行)
- [配置說明](#配置說明)
- [API 文檔](#api-文檔)
- [測試](#測試)
- [部署](#部署)
- [專案結構](#專案結構)
- [常見問題](#常見問題)
- [作者](#作者)

## 技術棧

- **Java 17**
- **Spring Boot 3.5.14**
- Spring Data JPA
- Thymeleaf
- SQLite（開發環境）/ PostgreSQL（部署環境）
- BCrypt（密碼雜湊）
- Maven（依賴管理）

## 功能清單

- ✅ 醫師登入 / 登出
- ✅ 醫師個人 Dashboard
- ✅ 病患資料管理（CRUD）
- ✅ 線上掛號功能
- ✅ 掛號狀態變更（booked / completed / cancelled）
- ✅ 掛號統計報表
- ✅ RESTful API（支援第三方整合）

## Prerequisites

### 系統要求

- **Java 17** 或以上版本
- **Maven 3.6** 或以上版本（或使用專案內的 Maven Wrapper）
- **Git**

### 驗證環境

```bash
# 檢查 Java 版本
java -version

# 檢查 Maven 版本
mvn -version
```

## 本機執行

### 1. 克隆專案

```bash
git clone https://github.com/bellaaaaaa123/miniclinic.git
cd miniclinic
```

### 2. 啟動應用

使用 Maven Wrapper（推薦，無須預先安裝 Maven）：

```bash
./mvnw spring-boot:run
```

或使用已安裝的 Maven：

```bash
mvn spring-boot:run
```

### 3. 訪問應用

開啟瀏覽器訪問：http://localhost:8080

### 4. 預設登入帳密

醫師帳號格式為 `D001` 至 `D005`

| 帳號 | 密碼    |
|------|--------|
| D001 | pass1234 |
| D002 | pass1234 |
| D003 | pass1234 |
| D004 | pass1234 |
| D005 | pass1234 |

### 5. 資料初始化

第一次啟動時，`src/main/resources/data.sql` 會自動插入測試資料：
- 5 位虛構醫師
- 3 位虛構病患（TEST00001, TEST00002, TEST00003）
- 3 筆示範掛號

資料將存放於：
- **開發環境**：SQLite 資料庫（自動建立於專案根目錄，名稱為 `miniclinic.db`）
- **部署環境**：PostgreSQL 資料庫

## 配置說明

### 環境配置

專案支援多環境配置：

#### Dev 環境（開發，預設）
```bash
./mvnw spring-boot:run
```

配置文件：`src/main/resources/application-dev.properties`
- 使用 SQLite 資料庫
- SQL 日誌：OFF

#### Prod 環境（生產/部署）
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

配置文件：`src/main/resources/application-prod.properties`
- 使用 PostgreSQL 資料庫
- 需設定環境變數 `SPRING_DATASOURCE_URL`、`SPRING_DATASOURCE_USERNAME`、`SPRING_DATASOURCE_PASSWORD`

### 常見配置

在 `src/main/resources/application.properties` 中修改：

| 配置項 | 預設值 | 說明 |
|--------|--------|------|
| `server.port` | 8080 | 伺服器埠號 |
| `spring.jpa.show-sql` | false | 是否列印 SQL 語句 |
| `spring.profiles.active` | dev | 啟用的 Profile |

## API 文檔

### 醫師 API

#### 取得所有醫師
```
GET /api/doctors
```
Response (JSON):
```json
[
  {"id": 1, "doctorId": "D001", "name": "王醫師"},
  {"id": 2, "doctorId": "D002", "name": "李醫師"}
]
```

#### 取得醫師詳情
```
GET /api/doctors/{id}
```

### 掛號 API

#### 取得所有掛號
```
GET /api/appointments
```

#### 新增掛號
```
POST /api/appointments
Content-Type: application/json

{
  "patientId": "TEST00001",
  "doctorId": "D001",
  "appointmentDate": "2026-06-15",
  "status": "booked"
}
```

#### 更新掛號狀態
```
PUT /api/appointments/{id}/status
Content-Type: application/json

{
  "status": "completed"
}
```

#### 取消掛號
```
DELETE /api/appointments/{id}
```

**詳細 API 使用範例見 `api-tests.http`**

## 測試

### 運行所有測試

```bash
./mvnw test
```

### 運行特定測試類

```bash
./mvnw test -Dtest=AppointmentApiControllerTest
```

### 生成測試報告

```bash
./mvnw surefire-report:report
```

測試報告位置：`target/site/surefire-report.html`

## 部署

### 部署至 Render（當前狀態）

1. **推送至 GitHub**
   ```bash
   git push origin main
   ```

2. **在 Render 建立 Web Service**
   - 連接 GitHub Repository
   - Build Command: `mvn clean package`
   - Start Command: `java -jar target/miniclinic-0.0.1-SNAPSHOT.jar`

3. **設定環境變數**
   - `SPRING_PROFILES_ACTIVE=prod`
   - `SPRING_DATASOURCE_URL=<PostgreSQL URL>`
   - `SPRING_DATASOURCE_USERNAME=<Database Username>`
   - `SPRING_DATASOURCE_PASSWORD=<Database Password>`
   - `PORT=8080`

4. **首次部署可能需時 2-3 分鐘**

### Docker 部署

專案已包含 Dockerfile：

```bash
docker build -t miniclinic .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod miniclinic
```

## 專案結構

```
miniclinic/
├── src/
│   ├── main/
│   │   ├── java/tw/edu/fju/miniclinic/
│   │   │   ├── MiniclinicApplication.java    # Spring Boot 主應用
│   │   │   ├── controller/                   # MVC 和 REST 控制器
│   │   │   │   ├── AppointmentController.java       # 掛號 MVC
│   │   │   │   ├── AppointmentApiController.java    # 掛號 REST API
│   │   │   │   ├── DoctorController.java            # 醫師 MVC
│   │   │   │   ├── DoctorApiController.java         # 醫師 REST API
│   │   │   │   ├── DashboardController.java         # 儀表板
│   │   │   │   ├── LoginController.java             # 登入登出
│   │   │   │   ├── PatientController.java           # 病患管理
│   │   │   │   ├── HomeController.java              # 首頁
│   │   │   │   ├── HealthController.java            # 健康檢查
│   │   │   │   └── StatsController.java             # 統計報表
│   │   │   ├── model/                       # JPA Entity 與 Repository
│   │   │   │   └── (Patient.java, Doctor.java, Appointment.java 等)
│   │   │   ├── interceptor/                 # 登入驗證攔截器
│   │   │   └── config/                      # Spring 配置
│   │   │       └── WebConfig.java
│   │   └── resources/
│   │       ├── templates/                   # Thymeleaf HTML 模板
│   │       ├── static/                      # CSS、JavaScript、圖片
│   │       ├── application.properties       # 主配置文件
│   │       ├── application-dev.properties   # 開發環境配置
│   │       ├── application-prod.properties  # 生產環境配置
│   │       ├── data.sql                     # 初始化 SQL 數據
│   │       └── data-prod.sql                # 生產環境初始化 SQL
│   └── test/
│       └── java/tw/edu/fju/miniclinic/
│           ├── AppointmentApiControllerTest.java
│           └── MiniclinicApplicationTests.java
├── target/                                  # Maven 編譯輸出
├── Dockerfile                               # Docker 構建文件
├── pom.xml                                  # Maven 配置和依賴
├── mvnw、mvnw.cmd                           # Maven Wrapper
├── api-tests.http                           # REST API 測試文件
├── README.md                                # 本文件
└── HELP.md                                  # Spring Boot 生成的幫助文檔
```

## 常見問題

### Q: 啟動時出現 "port 8080 already in use" 錯誤

**A:** 改用其他埠號啟動
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Q: 資料庫連接失敗

**A:** 確認環境變數已正確設定（生產環境）或資料庫檔案有讀寫權限（開發環境）

### Q: 無法以預設帳密登入

**A:** 
1. 確認已執行過初始化（重啟應用或手動運行 `data.sql`）
2. 嘗試清除瀏覽器 Cookie 並重新登入
3. 檢查 `data.sql` 中是否存在測試帳號

### Q: 如何修改預設密碼？

**A:** 在 `data.sql` 中修改，密碼經 BCrypt 加密存儲。可使用線上 BCrypt 產生器產生新密碼：
```sql
INSERT INTO doctors (doctor_id, name, password) 
VALUES ('D001', '王醫師', '$2a$10$...');  -- BCrypt 加密密碼
```

### Q: 部署至 Render 後首次訪問很慢

**A:** 首次啟動會進行資料庫遷移和初始化，可能需要 1-2 分鐘，屬於正常情況。

### Q: 如何檢視 SQL 執行語句？

**A:** 修改 `application.properties`
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## 作者

2026 年 Java 程式設計課程作業

## 聲明

所有病患資料均為虛構，僅供教學使用。本專案不可用於真實醫療環境。