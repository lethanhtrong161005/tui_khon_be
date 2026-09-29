# Authentication & Token Management Architecture Specification

## 1. Overview & Security Architecture

Túi Khôn Backend implements a stateless, secure **JWT Authentication & Token Management System** powered by Spring Security, JWT (jjwt 0.12.x), and Redis.

### Key Security Design Patterns

1. **Direct Credential Authentication**:
   - **Login (`POST /api/v1/auth/login`)**: Email and Password verification directly returns an `accessToken` and `refreshToken` pair bound to a unique Token Family.
2. **Instant Access Token Blacklisting**:
   - Access tokens contain a unique JWT ID (`jti`). Upon logout, the `jti` is stored in Redis (`blacklist:jti:<jti>`) with a TTL matching the token's remaining lifespan, ensuring instant stateless revocation.
3. **Token Family Rotation (Mitigating Refresh Token Theft)**:
   - Every Refresh Token belongs to a `familyId`.
   - When a Refresh Token is used (`POST /api/v1/auth/refresh-token`), it is rotated (a new Refresh Token JTI is issued under the same `familyId`).
   - If an old or spent Refresh Token JTI is presented (Replay Attack), the system detects token theft and immediately revokes the **entire Token Family** (`rt:revoked_family:<familyId>`).
4. **Role-Based Access Control (RBAC)**:
   - Enforcing system roles via `RoleName` Enum (`USER`, `ADMIN`) embedded directly in `UserEntity`.

---

## 2. Redis Key Structure & TTL Specifications

| Key Pattern | Data Type | Purpose | TTL |
| :--- | :--- | :--- | :--- |
| `rt:family:<familyId>:<jtiRt>` | String | Identifies active Refresh Token JTI within token family | 7 days (`REFRESH_FAMILY_TTL_DAYS`) |
| `rt:revoked_family:<familyId>` | String | Marks entire Token Family as revoked upon theft/logout | 7 days (`REFRESH_FAMILY_TTL_DAYS`) |
| `blacklist:jti:<jtiAt>` | String | Blacklists Access Token JTI upon logout | Remaining AT lifespan (ms) |

---

## 3. Sequence Diagrams

### 3.1 Login Flow (`POST /api/v1/auth/login`)

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as AuthController
    participant Service as AuthServiceImpl
    participant Repo as UserRepository
    participant JWT as JwtProvider
    participant Redis as Redis Cache

    Client->>Controller: POST /api/v1/auth/login {email, password}
    Controller->>Service: login(LoginRequest)
    Service->>Repo: findByEmailAndIsDeletedFalse(email)
    Repo-->>Service: UserEntity

    alt User Not Found or Password Mismatch
        Service-->>Client: 401 Unauthorized (INVALID_CREDENTIALS)
    end

    alt User Status != ACTIVE
        Service-->>Client: 403 Forbidden (ACCESS_DENIED)
    end

    Service->>Service: Generate familyId = UUID()
    Service->>JWT: generateAccessToken(user, familyId)
    JWT-->>Service: accessToken
    Service->>JWT: generateRefreshToken(user, familyId)
    JWT-->>Service: refreshToken

    Service->>Redis: SET rt:family:<familyId>:<jtiRt> = "ACTIVE" (TTL: 7 days)
    Service-->>Controller: TokenResponse {accessToken, refreshToken}
    Controller-->>Client: 200 OK with ApiResponse<TokenResponse>
```

### 3.2 Token Refresh Flow with Rotation (`POST /api/v1/auth/refresh-token`)

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as AuthController
    participant Service as AuthServiceImpl
    participant JWT as JwtProvider
    participant Redis as Redis Cache
    participant Repo as UserRepository

    Client->>Controller: POST /api/v1/auth/refresh-token {refreshToken}
    Controller->>Service: refreshToken(RefreshTokenRequest)
    Service->>JWT: validateToken(refreshToken)

    alt Token Signature / Expiry Invalid
        Service-->>Client: 401 Unauthorized (TOKEN_INVALID)
    end

    Service->>JWT: extract familyId, jtiRt, userId
    Service->>Redis: EXISTS rt:revoked_family:<familyId>

    alt Family Revoked
        Service-->>Client: 401 Unauthorized (TOKEN_REVOKED)
    end

    Service->>Redis: EXISTS rt:family:<familyId>:<jtiRt>

    alt Spent JTI (Token Theft Detected / Replay Attack)
        Service->>Redis: SET rt:revoked_family:<familyId> = "REVOKED" (TTL: 7 days)
        Service-->>Client: 401 Unauthorized (TOKEN_REVOKED)
    end

    Service->>Redis: DEL rt:family:<familyId>:<jtiRt>
    Service->>Repo: findByUserIdAndIsDeletedFalse(userId)
    Repo-->>Service: UserEntity

    Service->>JWT: generateAccessToken(user, familyId)
    JWT-->>Service: newAccessToken
    Service->>JWT: generateRefreshToken(user, familyId)
    JWT-->>Service: newRefreshToken

    Service->>Redis: SET rt:family:<familyId>:<newJtiRt> = "ACTIVE" (TTL: 7 days)
    Service-->>Controller: TokenResponse {newAccessToken, newRefreshToken}
    Controller-->>Client: 200 OK with ApiResponse<TokenResponse>
```

### 3.3 Logout Flow (`POST /api/v1/auth/logout`)

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as AuthController
    participant Service as AuthServiceImpl
    participant JWT as JwtProvider
    participant Redis as Redis Cache

    Client->>Controller: POST /api/v1/auth/logout {refreshToken} + Authorization: Bearer <accessToken>
    Controller->>Service: logout(authHeader, LogoutRequest)

    opt Valid Access Token present
        Service->>JWT: getJtiFromToken(accessToken)
        Service->>JWT: getRemainingExpirationMs(accessToken)
        Service->>Redis: SET blacklist:jti:<jtiAt> = "REVOKED" (TTL: remainingMs)
    end

    opt Valid Refresh Token present
        Service->>JWT: extract familyId, jtiRt
        Service->>Redis: SET rt:revoked_family:<familyId> = "REVOKED" (TTL: 7 days)
        Service->>Redis: DEL rt:family:<familyId>:<jtiRt>
    end

    Service-->>Controller: void
    Controller-->>Client: 200 OK (LOGOUT_SUCCESSFUL)
```
