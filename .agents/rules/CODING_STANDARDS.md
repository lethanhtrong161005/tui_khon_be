# 📐 Túi Khôn Coding Standards (MANDATORY)

**Status**: MANDATORY | **Version**: 1.0 | **Base Package**: `com.tuikhon`

---

## 1. Indentation & Formatting (IntelliJ IDEA Standard)

- **Indentation**: **4 spaces** (convert tabs to 4 spaces, NEVER use 2 spaces).
- **Line Length**: **120 characters**.
- **Brace Style**: **K&R Style** (`{` on the same line for classes, methods, and control blocks).
- **Blank Lines**: Maximum 1 consecutive blank line.
- **Imports**: Explicit imports only. NEVER use wildcard imports (`import java.util.*;`).

```java
// ✅ CORRECT: 4 spaces, K&R braces
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponse getUser(UUID userId) {
        if (Objects.isNull(userId)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, MessageConstant.BAD_REQUEST);
        }
        return userRepository.findById(userId)
                .map(userHelper::mapToUserProfileResponse)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, MessageConstant.USER_NOT_FOUND));
    }
}
```

---

## 2. Null & Blank Checks

### ✅ Use `java.util.Objects`
```java
// ✅ CORRECT
if (Objects.isNull(value)) { ... }
if (Objects.nonNull(user)) { ... }
Objects.requireNonNull(userId, "userId must not be null");

// ❌ WRONG
if (value == null) { ... }
if (value != null) { ... }
```

### ✅ String Checks
```java
// ✅ CORRECT
if (name.isBlank()) { ... }  // Preferred for user input (checks whitespace-only)
if (name.isEmpty()) { ... }

// ❌ WRONG
if (name.equals("")) { ... }
if (name == "") { ... }
```

---

## 3. Error Handling & Message Constants

- **Messages**: All Vietnamese user-facing messages must be defined in `com.tuikhon.constant.MessageConstant`:
  ```java
  public static final String LOGIN_SUCCESSFUL = "Đăng nhập thành công";
  public static final String INVALID_CREDENTIALS = "Email hoặc mật khẩu không chính xác";
  ```
- **Throwing Exceptions**: Services and utilities throw `HttpException` directly with HTTP status and message:
  ```java
  throw new HttpException(HttpStatus.UNAUTHORIZED, MessageConstant.INVALID_CREDENTIALS);
  ```
- **Response Format**: Every response must be wrapped in `ApiResponse<T>` with `traceId` and `path`:
  ```java
  // In Controllers, use ResponseUtils:
  return ResponseUtils.successWithData(data, MessageConstant.OPERATION_SUCCESSFUL);
  return ResponseUtils.created(data, MessageConstant.REGISTER_SUCCESSFUL);
  ```

---

## 4. Entity Design

- Every persistent entity **MUST** extend `com.tuikhon.entity.BaseEntity` to inherit auditing fields (`createdAt`, `updatedAt`, `createdBy`, `updatedBy`) and soft-delete (`isDeleted`).
- **User Roles**: Embed role directly in `UserEntity` using `RoleName` enum (`USER`, `ADMIN`):
  ```java
  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, length = 20)
  @Builder.Default
  private RoleName role = RoleName.USER;
  ```
- Never create separate role tables or role assignment entities unless complex permissions are explicitly required.

---

## 5. Data Access (Repositories & Queries)

- **Search & Filtering**: Use Spring Data JPA query methods or explicit `@Query` annotations with JPQL/SQL.
- Do **NOT** use Specification or Criteria API classes.
- Always include `isDeleted = false` condition when querying active records:
  ```java
  @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND u.isDeleted = false")
  Optional<UserEntity> findActiveByEmail(@Param("email") String email);
  ```

---

## 6. Security Standards

- **Passwords**: Always hashed with `BCryptPasswordEncoder`. Never log plain text passwords.
- **Access Tokens**: Short-lived JWTs. Revocation via Redis JTI blacklist (`blacklist:jti:<jti>`).
- **Refresh Tokens**: Long-lived JWTs bound to a `familyId`. Token family rotation on each refresh. Replay attack revokes the whole family (`rt:revoked_family:<familyId>`).
- **Sensitive Data**: Never log passwords, tokens, or personal identifiers in plain text.

---

## 7. Logging Standards

- Use SLF4J: `private static final Logger log = LoggerFactory.getLogger(MyClass.class);` or Lombok `@Slf4j`.
- NEVER use `System.out.println()` or `e.printStackTrace()`.
- Use parameter placeholders: `log.info("User logged in: {}", userId);`.
