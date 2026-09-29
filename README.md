# 💰 Túi Khôn Backend (`tui_khon_be`)

Production-ready, monolithic enterprise backend for the **Túi Khôn** (Personal Financial Management) platform, built with **Spring Boot 3.5.x**, **Java 21**, **PostgreSQL 16**, **Redis 7**, and **Flyway**.

---

## 🏗️ Architecture & Package Structure

The codebase strictly adheres to the standard Spring Boot project layout under package `com.tuikhon`:

```
src/main/java/com/tuikhon
│
├── TuiKhonBeApplication.java     # Global UTC timezone enforcement & Dotenv loader
│
├── config                        # AuditorAwareImpl, JpaConfig, OpenApiConfig, RedisConfig, RestTemplateConfig, SecurityConfig, WebConfig
├── constant                      # AppConstant, MessageConstant
├── controller                    # AuthController, HealthController
├── dto
│   ├── request
│   │   ├── auth                  # LoginRequest, RegisterRequest, RefreshTokenRequest, LogoutRequest, ChangePasswordRequest, UpdateProfileRequest
│   │   └── common                # BaseSortCondition, PageRequest, PayloadSearchRequest
│   └── response
│       ├── auth                  # TokenResponse, UserProfileResponse
│       └── common                # ApiResponse, HealthCheckResponse, PageResponse
├── entity                        # BaseEntity, UserEntity (Embedded RoleName enum)
├── enums                         # RoleName, SortOrder, UserStatus
├── exception                     # HttpException, GlobalExceptionHandler
├── helper                        # UserHelper (Entity <-> DTO Mapping)
├── repository                    # UserRepository
├── security                      # JwtAuthenticationFilter, JwtProvider, SecurityConfig, TraceIdFilter
├── service                       # AuthService, RedisService
│   └── impl                      # AuthServiceImpl, RedisServiceImpl
├── util                          # CommonUtil, DotenvUtils, ResponseUtils
└── validation                    # EnumValue, EnumValueValidator, RequireField, RequireFieldValidator
```

---

## 🔐 Authentication & Security Architecture

1. **Direct JWT Authentication**:
   - **Login (`POST /api/v1/auth/login`)**: Validates `email` and `password`. If successful, directly returns `accessToken` and `refreshToken` in response payload.
   - **Register (`POST /api/v1/auth/register`)**: Creates an account with `email`, `password`, `displayName`, and `phoneNumber`.
2. **Instant Access Token Blacklisting**:
   - Access Tokens contain a unique JWT ID (`jti`). Logout instantly blacklists `jti` in Redis (`blacklist:jti:<jti>`) with TTL matching the remaining token lifespan.
3. **Token Family Rotation (Mitigating Refresh Token Theft)**:
   - Every Refresh Token belongs to a `family_id`.
   - When a Refresh Token is refreshed (`POST /api/v1/auth/refresh-token`), it is rotated with a new Refresh Token JTI under the same `family_id`.
   - Reusing an old/rotated Refresh Token triggers immediate revocation of the **entire Token Family** (`rt:revoked_family:<familyId>`).
4. **Role-Based Access Control (RBAC)**:
   - System roles are represented by `RoleName` enum (`USER`, `ADMIN`) embedded directly in `UserEntity`.
5. **Standardized Response Envelope**:
   - Every response (success or error) contains `status`, `message`, `traceId`, `path`, `data`, and `timestamp`:
   ```json
   {
     "status": 200,
     "message": "Đăng nhập thành công",
     "traceId": "c8f94e96-6e47-4f68-9844-8da8fb6bf9c4",
     "path": "/api/v1/auth/login",
     "data": {
       "accessToken": "eyJhbGciOi...",
       "refreshToken": "eyJhbGciOi..."
     },
     "timestamp": "2026-09-29T12:00:00Z"
   }
   ```

---

## 💬 Message & Error Management

- **Centralized Vietnamese Messages**: All user-facing messages are declared in [MessageConstant.java](file:///Users/admin/Documents/PRM392/tui_khon_be/src/main/java/com/tuikhon/constant/MessageConstant.java):
  ```java
  public static final String LOGIN_SUCCESSFUL = "Đăng nhập thành công";
  public static final String INVALID_CREDENTIALS = "Email hoặc mật khẩu không chính xác";
  ```
- **HttpException**: Service and Utility layers throw `HttpException` directly with message strings:
  ```java
  throw new HttpException(HttpStatus.UNAUTHORIZED, MessageConstant.INVALID_CREDENTIALS);
  ```
- **GlobalExceptionHandler**: Catches `HttpException`, validation annotations (`@RequireField`, `@EnumValue`), and unhandled exceptions into standardized `ApiResponse` envelopes.

---

## 📑 Logging Architecture & Categorized Log Folders

Logs are managed via [logback-spring.xml](file:///Users/admin/Documents/PRM392/tui_khon_be/src/main/resources/logback-spring.xml) and categorized into dedicated subfolders under `${LOG_PATH:-./logs}`:

| Folder & File | Purpose & Level | Rolling Policy |
| :--- | :--- | :--- |
| `logs/app/app.log` | General application logs (`com.tuikhon`, INFO/DEBUG) | 20MB/file, 30 days retention, Gzip compressed |
| `logs/error/error.log` | System errors only (`ThresholdFilter` = **ERROR**) | 20MB/file, 60 days retention, Gzip compressed |
| `logs/security/security.log` | Auth & Security audit logs (`com.tuikhon.security`, Spring Security) | 20MB/file, 30 days retention, Gzip compressed |
| `logs/sql/sql.log` | Database queries & prepared statements (`org.hibernate.SQL`, parameters) | 30MB/file, 15 days retention, Gzip compressed |

- **Log Pattern**: Includes timestamp, thread, log level, traceId (`[traceId=%X{traceId:-SYSTEM}]`), logger name, and message.
- **Async Logging**: High-throughput asynchronous appenders (`AsyncAppender`) for production performance.

---

## 🌐 Environment & Profiles

- `src/main/resources/application.properties` — Base configuration (`${APP_PROFILE:local}`, `${SERVER_PORT:8080}`)
- `src/main/resources/application-local.properties` — Local profile (`local`) for IntelliJ IDEA
- `src/main/resources/application-dev.properties` — Development container profile (`dev`)
- `src/main/resources/application-prod.properties` — Production profile (`prod`)

---

## 🚀 Running the Application

### 1️⃣ Local Development (IntelliJ IDEA + Docker Infrastructure)

Start only PostgreSQL and Redis containers, then run Spring Boot inside IntelliJ IDEA:

```bash
# 1. Start local PostgreSQL & Redis infrastructure
docker compose -f docker-compose.local.yml up -d

# 2. Run / Debug TuiKhonBeApplication in IntelliJ IDEA (Profile: local, loaded from .env.local)

# 3. Stop infrastructure when done
docker compose -f docker-compose.local.yml down
```

### 2️⃣ Development Container Mode

Run the full container stack:

```bash
docker compose -f docker-compose.dev.yml up --build
```

---

## 🛡️ Coding Standards (IntelliJ Standard)

1. **Indentation**: 4 spaces standard (tabs converted to 4 spaces).
2. **Brace Style**: K&R style for methods and classes (`{` on same line).
3. **Line Length**: 120 characters standard.
4. **Null Checks**: ALWAYS use `java.util.Objects.isNull(val)` or `Objects.nonNull(val)`. NEVER use raw `== null`.
5. **Blank String Checks**: ALWAYS use `str.isEmpty()` or `str.isBlank()`.
6. **No Wildcard Imports**: Import explicit classes only.

---

## 🔗 System Endpoints

- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI Docs**: `http://localhost:8080/v3/api-docs`
- **Health Check**: `http://localhost:8080/api/v1/health`
