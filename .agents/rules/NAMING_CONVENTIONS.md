# 🏷️ Naming Conventions (MANDATORY)

**Project**: Túi Khôn Backend | **Base Package**: `com.tuikhon`

---

## 1. General Identifiers

| Element | Convention | Example |
| :--- | :--- | :--- |
| **Class / Interface** | `PascalCase` | `AuthService`, `UserRepository` |
| **Method / Variable** | `camelCase` | `findByEmail()`, `userId` |
| **Constant** | `UPPER_SNAKE_CASE` | `DEFAULT_TIMEZONE`, `LOGIN_SUCCESSFUL` |
| **Package** | lowercase, dot-separated | `com.tuikhon.service` |
| **REST Endpoint** | kebab-case, plural nouns | `/api/v1/users`, `/api/v1/wallets` |
| **DB Table** | `snake_case`, plural | `users`, `wallets`, `transactions` |
| **DB Column** | `snake_case` | `created_at`, `is_deleted`, `user_id` |
| **DTO Suffix** | `Request` / `Response` | `LoginRequest`, `TokenResponse`, `UserProfileResponse` |
| **Entity** | PascalCase, suffix `Entity` | `UserEntity`, `BaseEntity` |
| **Exception** | suffix `Exception` | `HttpException` |
| **Boolean Field/Method** | prefix `is` / `has` / `can` | `isDeleted`, `hasPermission()` |

---

## 2. Documentation Annotations

- **Controllers**:
  - MUST annotate class with `@Tag(name = "...", description = "...")`.
  - MUST annotate endpoints with `@Operation(summary = "...")`.
- **Request / Response DTOs**:
  - MUST annotate class with `@Schema(description = "...")`.
  - MUST annotate fields with `@Schema(description = "...", example = "...")`.

---

## 3. Package Suffix Rules

| Package | Required Suffix | ✅ Example | ❌ Wrong |
| :--- | :--- | :--- | :--- |
| `constant` | `Constant` | `AppConstant`, `MessageConstant` | `Constants`, `Messages` |
| `config` | `Config` | `JpaConfig`, `SecurityConfig` | `JpaConfiguration` |
| `security` | `Filter` / `Provider` | `JwtAuthenticationFilter`, `JwtProvider` | `Security` |
| `util` | `Utils` | `ResponseUtils`, `DotenvUtils` | `ResponseHelper` |
| `helper` | `Helper` | `UserHelper` | `UserUtil` |
| `validation` | `Validator` | `RequireFieldValidator`, `EnumValueValidator` | `RequireCheck` |
| `enums` | *(none — noun)* | `RoleName`, `UserStatus` | `RoleEnum` |

---

## 4. Message Constant Standards

All user-facing messages are centralized in `com.tuikhon.constant.MessageConstant`:
- Grouped by feature: Success, Auth errors, User errors, Common errors.
- Name format: `UPPER_SNAKE_CASE`.
- Values in Vietnamese.

```java
public final class MessageConstant {
    private MessageConstant() {}

    public static final String OPERATION_SUCCESSFUL = "Thao tác thành công";
    public static final String LOGIN_SUCCESSFUL = "Đăng nhập thành công";
    public static final String INVALID_CREDENTIALS = "Email hoặc mật khẩu không chính xác";
}
```

---

## 5. General Language Rules

- **English only** in code, comments, identifiers, variable names, and commit messages.
- **Vietnamese** is strictly reserved for the string values inside `MessageConstant`.
- No abbreviations except widely accepted terms: `id`, `url`, `dto`, `jwt`, `api`.
