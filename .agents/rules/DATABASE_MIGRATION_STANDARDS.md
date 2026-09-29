# 🗄️ Database Migration Standards (Flyway & PostgreSQL)

This document defines mandatory database migration rules and SQL coding conventions for the **Túi Khôn** backend (`com.tuikhon`).

---

## 1. File Location & Naming Conventions

### 📁 Migration Directory
All migration scripts MUST be placed in:
`src/main/resources/db/migration/`

### 🏷️ Flyway File Naming Rules
Flyway requires a strict naming pattern using **double underscores (`__`)**:

```
V<VERSION>__<description>.sql
```

#### Examples:
- `V1__create_users_table.sql` (Initial users schema setup)
- `V2__create_wallets_table.sql` (Wallets / accounts table)
- `V3__create_transactions_table.sql` (Transactions table)
- `V3.1__add_index_transactions_date.sql` (Minor index addition)

> ⚠️ **CRITICAL RULE**: ALWAYS use a **double underscore (`__`)** between the version number and the description. Single underscores (`_`) will cause Flyway parsing errors!

---

## 2. Table & Column Naming Rules

| Object Type | Casing | Plurality | Example |
| :--- | :--- | :--- | :--- |
| **Table Name** | `snake_case` | **Plural** | `users`, `wallets`, `transactions`, `budgets` |
| **Column Name** | `snake_case` | Singular | `created_at`, `user_id`, `role`, `status`, `is_deleted` |
| **Primary Key Constraint** | `pk_<table_name>` | Plural | `pk_users`, `pk_wallets` |
| **Foreign Key Constraint** | `fk_<table_name>_<target_table>` | Plural | `fk_wallets_users`, `fk_transactions_wallets` |
| **Index Name** | `idx_<table_name>_<column_name>` | Plural | `idx_users_email`, `idx_users_role` |

---

## 3. Mandatory Audit Columns Standard

Every new entity table MUST include the following base columns matching `com.tuikhon.entity.BaseEntity`:

```sql
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    
    -- Domain specific columns
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    
    -- Mandatory Audit & Soft Delete Columns
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT uq_users_email UNIQUE (email)
);
```

---

## 4. SQL Writing Rules

1. **SQL Keywords**: Write ALL SQL keywords in **UPPERCASE** (`CREATE TABLE`, `ALTER TABLE`, `PRIMARY KEY`, `NOT NULL`, `DEFAULT`, `CONSTRAINT`, `FOREIGN KEY`, `INDEX`).
2. **PostgreSQL Data Types**:
   - Primary Keys: `UUID`
   - Timestamps: Always use `TIMESTAMPTZ` (UTC enforcement)
   - Text: `VARCHAR(n)` or `TEXT`
   - Numeric: `BIGINT`, `INTEGER`, `NUMERIC(15, 2)` (for currency / money amounts)
   - Booleans: `BOOLEAN` with `DEFAULT FALSE` or `DEFAULT TRUE`
3. **Idempotency & Safety**:
   - Explicitly name constraints (`CONSTRAINT fk_...`, `CONSTRAINT uq_...`).
   - Create indexes safely using `CREATE INDEX IF NOT EXISTS`.
   - Never drop columns or tables in production migration scripts without backward-compatible deprecation phases.
