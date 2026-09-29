# Workspace Guidelines for Túi Khôn Backend

## Agent Directives
- You **MUST ALWAYS** read and follow all Markdown files inside the `.agents/rules/` directory (e.g., `.agents/rules/*.md`). These files contain domain-specific, architectural, and situational rules that take precedence over general instructions. Do this before making any implementation decisions.

## Environment Variables
If a configuration or variable relates to the environment (e.g., database URLs, credentials, external API keys, JWT secrets), it **MUST** be loaded from `.env` and `application-*.properties` files. Under no circumstances should environment-specific configurations be hardcoded in the source code.

## Error Handling & Exception Throwing
When handling business logic errors, **MUST ALWAYS** throw `HttpException` (e.g., `throw new HttpException(HttpStatus.BAD_REQUEST, MessageConstant.SOME_MESSAGE)`) directly from the Service layer or Utility classes.
- **DO NOT** manually construct error response envelopes inside Controllers.
- Let `GlobalExceptionHandler` catch `HttpException` and automatically format it into the standard `ApiResponse` envelope.
- The error message passed to `HttpException` **MUST** be defined in `com.tuikhon.constant.MessageConstant` in Vietnamese.

## Entity Design & Modeling
All JPA entity classes (mapped to database tables) **MUST** extend `com.tuikhon.entity.BaseEntity`. This ensures all tables consistently include auditing fields (`created_at`, `updated_at`, `created_by`, `updated_by`) and soft-delete logic (`is_deleted`).
- User roles **MUST** be embedded directly in `UserEntity` using `@Enumerated(EnumType.STRING) RoleName role = RoleName.USER;`. Do not create separate role entities or join tables for basic user roles.

## Database Queries & Search
- Do **NOT** use Criteria API / Specification classes.
- Implement search and filter operations using Spring Data JPA query methods or explicit `@Query` annotations with JPQL/SQL.
- Always include `isDeleted = false` condition in queries to respect soft-delete logic.

## Code Formatting & Style (IntelliJ IDEA Standard)
- Indentation: **4 spaces** (tab size = 4).
- Brace style: K&R brace style (`{` on the same line).
- Line length: 120 characters standard.
- Null checks: ALWAYS use `java.util.Objects.isNull(val)` or `Objects.nonNull(val)`. NEVER use raw `== null`.
- Empty string checks: ALWAYS use `str.isEmpty()` or `str.isBlank()`.

## Code Generation & Verification
After writing or modifying any code, you **MUST ALWAYS** run the build process (`./mvnw clean compile`) to verify that the source code compiles successfully without any syntax or dependency errors. Fix any errors immediately before completing your turn.
