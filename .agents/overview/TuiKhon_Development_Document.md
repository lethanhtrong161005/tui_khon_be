  
**TÚI KHÔN**

Ứng dụng Quản lý Tài chính Cá nhân tích hợp AI

**PROJECT DEVELOPMENT DOCUMENT**

*Tài liệu phát triển dự án — Backend · Frontend · AI · Deployment*

| Phiên bản | v2.0 — đồng bộ SRS Final v1.0 |
| :---- | :---- |
| **Ngày** | 01/06/2026 |
| **Mục đích** | Kim chỉ nam kỹ thuật cho toàn team |
| **Phạm vi** | BE · FE · AI/NLP · DevOps · Quy trình |
| **Timeline** | 5 tuần · 26/05 → 01/07/2026 |
| **Tài liệu liên quan** | SRS Final v1.0 · UI Prompts · AI Coding Rules |

# **MỤC LỤC** {#mục-lục}

[MỤC LỤC	1](#mục-lục)

[1\. TỔNG QUAN KỸ THUẬT	3](#1.-tổng-quan-kỹ-thuật)

[1.1 Kiến trúc tổng thể	3](#1.1-kiến-trúc-tổng-thể)

[1.2 Phân chia repository	3](#1.2-phân-chia-repository)

[1.3 Phân công theo vai trò	3](#1.3-phân-công-theo-vai-trò)

[1.4 Tech Stack tóm tắt	3](#1.4-tech-stack-tóm-tắt)

[2\. BACKEND (BE)	4](#2.-backend-\(be\))

[2.1 Cấu trúc layered	4](#2.1-cấu-trúc-layered)

[2.2 Định dạng Response & Error chuẩn	4](#2.2-định-dạng-response-&-error-chuẩn)

[Success response	4](#success-response)

[Error response	4](#error-response)

[HTTP status codes dùng trong dự án	4](#http-status-codes-dùng-trong-dự-án)

[2.3 Danh sách API Endpoints	4](#2.3-danh-sách-api-endpoints)

[Nhóm Auth	4](#nhóm-auth)

[Nhóm Transactions & Categories	5](#nhóm-transactions-&-categories)

[Nhóm Receipt	5](#nhóm-receipt)

[Nhóm AI (Gemini)	5](#nhóm-ai-\(gemini\))

[Nhóm Planning	5](#nhóm-planning)

[Nhóm Notifications	5](#nhóm-notifications)

[2.4 Database Schema (Supabase / PostgreSQL)	6](#2.4-database-schema-\(supabase-/-postgresql\))

[2.5 Bảo mật Backend	6](#2.5-bảo-mật-backend)

[3\. XỬ LÝ NGÔN NGỮ TỰ NHIÊN & TỐI ƯU GEMINI	6](#3.-xử-lý-ngôn-ngữ-tự-nhiên-&-tối-ưu-gemini)

[3.1 Pipeline tổng thể	6](#3.1-pipeline-tổng-thể)

[3.2 Bảng quy đổi đơn vị tiền tệ tiếng Việt	7](#3.2-bảng-quy-đổi-đơn-vị-tiền-tệ-tiếng-việt)

[3.3 Xử lý cách nói đặc biệt	7](#3.3-xử-lý-cách-nói-đặc-biệt)

[3.4 Pseudocode hàm normalize	7](#3.4-pseudocode-hàm-normalize)

[3.5 Code mẫu (TypeScript) — normalize tiền tệ cơ bản	7](#3.5-code-mẫu-\(typescript\)-—-normalize-tiền-tệ-cơ-bản)

[3.6 System Prompt cho Gemini (mẫu)	8](#3.6-system-prompt-cho-gemini-\(mẫu\))

[3.7 Few-shot examples (đưa vào prompt)	8](#3.7-few-shot-examples-\(đưa-vào-prompt\))

[3.8 Validate output & Fallback	8](#3.8-validate-output-&-fallback)

[3.9 Tối ưu token & chi phí	9](#3.9-tối-ưu-token-&-chi-phí)

[4\. FRONTEND (FE)	9](#4.-frontend-\(fe\))

[4.1 Cấu trúc thư mục	9](#4.1-cấu-trúc-thư-mục)

[4.2 State Management (Zustand) — chi tiết	9](#4.2-state-management-\(zustand\)-—-chi-tiết)

[4.3 Navigation	9](#4.3-navigation)

[4.4 Map màn hình ↔ chức năng ↔ API	9](#4.4-map-màn-hình-↔-chức-năng-↔-api)

[4.5 Acceptance Criteria (tiêu chí hoàn thành) — ví dụ	10](#4.5-acceptance-criteria-\(tiêu-chí-hoàn-thành\)-—-ví-dụ)

[Add Transaction	10](#add-transaction)

[AI Chat	10](#ai-chat)

[4.6 Voice Input	10](#4.6-voice-input)

[5\. DATA FLOW & SEQUENCE	10](#5.-data-flow-&-sequence)

[5.1 Đăng ký \+ Xác thực OTP	11](#5.1-đăng-ký-+-xác-thực-otp)

[5.2 Đăng nhập	11](#5.2-đăng-nhập)

[5.2b Quên mật khẩu	11](#5.2b-quên-mật-khẩu)

[5.3 Upload & xem ảnh hóa đơn	11](#5.3-upload-&-xem-ảnh-hóa-đơn)

[5.4 AI Chat ghi giao dịch	11](#5.4-ai-chat-ghi-giao-dịch)

[5.5 Offline Sync	11](#5.5-offline-sync)

[5.6 Edge cases & xử lý lỗi	11](#5.6-edge-cases-&-xử-lý-lỗi)

[6\. DEPLOYMENT & DEVOPS	12](#6.-deployment-&-devops)

[6.1 Môi trường	12](#6.1-môi-trường)

[6.2 Branch Strategy	12](#6.2-branch-strategy)

[6.3 CI/CD (GitHub Actions)	12](#6.3-ci/cd-\(github-actions\))

[6.4 Build & Release (EAS)	12](#6.4-build-&-release-\(eas\))

[6.5 Biến môi trường	12](#6.5-biến-môi-trường)

[6.6 Dev Onboarding — setup máy chạy dự án	13](#6.6-dev-onboarding-—-setup-máy-chạy-dự-án)

[7\. TESTING & QUALITY	13](#7.-testing-&-quality)

[7.1 Definition of Done	13](#7.1-definition-of-done)

[7.2 Performance budget	13](#7.2-performance-budget)

[7.3 Security checklist	13](#7.3-security-checklist)

*(Trong Word: chuột phải vào mục lục → Update Field để hiện số trang.)*

# **1\. TỔNG QUAN KỸ THUẬT** {#1.-tổng-quan-kỹ-thuật}

## **1.1 Kiến trúc tổng thể** {#1.1-kiến-trúc-tổng-thể}

Hệ thống gồm 3 tầng tách biệt, giao tiếp qua REST API. App không bao giờ truy cập trực tiếp Gemini hoặc Storage — mọi thao tác nhạy cảm đi qua API Server.

┌──────────────────────┐    HTTPS / JWT    ┌──────────────────────┐

│   Flutter App        │ ────────────────► │    API Server        │

│ (Android·iOS·Web)    │ ◄──────────────── │ (Java Spring Boot)   │

└──────────────────────┘  JSON / SignedURL  └──────────────────────┘

                                                  │ JDBC / SDK

                                                  ▼

                          ┌──────────────────────────────────┐

                          │  PostgreSQL  \+   Gemini AI        │

                          │  (Supabase Storage · Auth · RLS)  │

                          └──────────────────────────────────┘

## **1.2 Phân chia repository** {#1.2-phân-chia-repository}

| Repo | Nội dung | Owner chính |
| ----- | ----- | ----- |
| tuikhon-flutter | App Flutter (Android · iOS · Web) | Toàn (FE) |
| tuikhon-api | API Server Java Spring Boot | Trí (BE) |
| tuikhon-infra | Config, CI/CD, tài liệu hạ tầng | Bách (BE) |

## **1.3 Phân công theo vai trò** {#1.3-phân-công-theo-vai-trò}

| Thành viên | Trách nhiệm chính |
| ----- | ----- |
| Trần Hoàng Phúc (PM) | Quản lý dự án, Jira, viết SRS, code FE UI, review PR |
| Nguyễn Đỗ Minh Quân | UX/UI Designer \+ code FE UI |
| Nguyễn Tuấn Toàn (FE) | Frontend lead, navigation, màn hình, viết SRS |
| Lê Nguyễn Quang Trí (BE) | API Server, AI Chat endpoint, auth, rate limit |
| Lê Bá Bách (BE) | Supabase, Storage, AI Vision, offline sync |

## **1.4 Tech Stack tóm tắt** {#1.4-tech-stack-tóm-tắt}

| Tầng | Công nghệ | Phiên bản / Ghi chú |
| ----- | ----- | ----- |
| Mobile & Web | Flutter | Dart, SDK ≥ 3.x |
| Mobile & Web | Riverpod (state), GoRouter (navigation) | flutter\_riverpod v2 |
| Mobile & Web | fl\_chart, connectivity\_plus, Hive | charts, offline, local cache |
| Mobile & Web | flutter\_secure\_storage, speech\_to\_text | JWT store, voice input |
| API | Java Spring Boot | v3.x · Java 21 LTS |
| API | Spring Security, Spring Validation, Bucket4j | JWT, validate, rate-limit |
| API | Spring Data JPA, Flyway | ORM, DB migration |
| API | Gemini Java SDK, AWS S3 SDK / Supabase Storage | AI, file storage |
| Database | PostgreSQL | v16 · Supabase hosted |
| Cloud | Supabase (Auth · Storage · RLS) | RLS bật |
| AI | Google Gemini | key server-side |
| Theming | Light \+ Dark mode (ThemeData) | Toggle ở Profile |
| DevOps | GitHub Actions, Codemagic, Jira, Sentry | CI/CD, build, monitor |

# **2\. BACKEND (BE)** {#2.-backend-(be)}

## **2.1 Cấu trúc layered** {#2.1-cấu-trúc-layered}

API Server tổ chức theo kiến trúc Spring Boot layered chuẩn: Controller → Service → Repository → Database/Gemini.

> * **Controller** (@RestController) — nhận HTTP request, validate input (@Valid), trả ResponseEntity chuẩn hóa.

> * **Service** (@Service) — business logic, gọi Repository và Gemini SDK.

> * **Repository** (@Repository / JpaRepository) — tương tác PostgreSQL qua Spring Data JPA.

> * **Security Filter** (Spring Security) — xác thực JWT, phân quyền User/Admin, rate limiting (Bucket4j).

> * **Exception Handler** (@ControllerAdvice) — xử lý lỗi tập trung, trả error response chuẩn.

Cấu trúc package:

```
src/main/java/com/tuikhon/
  ├─ controller/     # REST Controllers
  ├─ service/        # Business logic
  ├─ repository/     # JPA Repositories
  ├─ entity/         # JPA Entities (User, Transaction...)
  ├─ dto/            # Request/Response DTOs
  ├─ security/       # JWT filter, SecurityConfig
  ├─ config/         # App configs, Gemini config
  └─ exception/      # Custom exceptions, GlobalExceptionHandler
```

## **2.2 Định dạng Response & Error chuẩn** {#2.2-định-dạng-response-&-error-chuẩn}

Mọi endpoint trả về một cấu trúc nhất quán để FE xử lý dễ dàng.

### **Success response** {#success-response}

{ "success": true, "data": { ... }, "meta": { ... } }

### **Error response** {#error-response}

{ "success": false, "error": {

    "code": "RATE\_LIMIT\_EXCEEDED",

    "message": "Bạn đã vượt giới hạn quét hóa đơn hôm nay",

    "details": { "limit": 20, "retryAfter": 3600 } } }

### **HTTP status codes dùng trong dự án** {#http-status-codes-dùng-trong-dự-án}

| Status | Ý nghĩa |
| ----- | ----- |
| 200 / 201 | Thành công / Tạo mới thành công |
| 400 | Input sai (Zod validation thất bại) |
| 401 | Chưa xác thực / JWT hết hạn |
| 403 | Không có quyền (RLS / sai chủ sở hữu) |
| 404 | Không tìm thấy tài nguyên |
| 429 | Vượt rate limit (AI calls, request/phút) |
| 500 | Lỗi server |

## **2.3 Danh sách API Endpoints** {#2.3-danh-sách-api-endpoints}

### **Nhóm Auth** {#nhóm-auth}

| Endpoint | Method | Mô tả logic |
| ----- | ----- | ----- |
| /api/auth/register | POST | Tạo tài khoản email/mật khẩu → Supabase gửi OTP |
| /api/auth/verify-otp | POST | Xác thực mã OTP 6 số → cấp JWT app |
| /api/auth/resend-otp | POST | Gửi lại mã OTP (sau đếm ngược 60s) |
| /api/auth/login | POST | Đăng nhập email \+ mật khẩu → JWT |
| /api/auth/google | POST | Google OAuth (bỏ qua OTP) → JWT |
| /api/auth/forgot-password | POST | Gửi mã đặt lại mật khẩu qua email |
| /api/auth/reset-password | POST | Đặt lại mật khẩu với mã \+ mật khẩu mới |
| /api/auth/refresh | POST | Làm mới JWT khi hết hạn |
| /api/auth/logout | POST | Thu hồi phiên hiện tại |

### **Nhóm Transactions & Categories** {#nhóm-transactions-&-categories}

| Endpoint | Method | Mô tả logic |
| ----- | ----- | ----- |
| /api/transactions | GET | Danh sách, lọc theo ngày/danh mục/loại, phân trang |
| /api/transactions | POST | Tạo mới (hỗ trợ batch cho offline sync) |
| /api/transactions/:id | PATCH | Cập nhật giao dịch |
| /api/transactions/:id | DELETE | Xóa giao dịch |
| /api/categories | GET / POST | Lấy & tạo danh mục (thu/chi, icon, màu) |
| /api/categories/:id | PATCH / DELETE | Sửa / xóa danh mục |

### **Nhóm Receipt** {#nhóm-receipt}

| Endpoint | Method | Mô tả logic |
| ----- | ----- | ----- |
| /api/receipt/upload | POST | Ảnh → Sharp resize ≤1920px → Storage → Signed URL TTL 15' |
| /api/receipt/url | GET | Sinh lại Signed URL khi URL cũ hết hạn |

### **Nhóm AI (Gemini)** {#nhóm-ai-(gemini)}

| Endpoint | Method | Mô tả logic |
| ----- | ----- | ----- |
| /api/ai/chat | POST | Chat tiếng Việt, function calling: addTransaction/getBalance |
| /api/ai/scan-receipt | POST | Gemini Vision đọc ảnh → {amount, category, date, merchant} |

### **Nhóm Planning** {#nhóm-planning}

| Endpoint | Method | Mô tả logic |
| ----- | ----- | ----- |
| /api/budgets | CRUD | Ngân sách theo danh mục \+ cảnh báo 80%/100% |
| /api/goals | CRUD | Mục tiêu tiết kiệm, cập nhật số tiền đã nạp |
| /api/debts | CRUD | Sổ nợ, theo dõi thanh toán từng phần |

### **Nhóm Notifications** {#nhóm-notifications}

| Endpoint | Method | Mô tả logic |
| ----- | ----- | ----- |
| /api/notifications | GET | Danh sách thông báo, nhóm theo thời gian, cờ đã đọc |
| /api/notifications/:id/read | PATCH | Đánh dấu 1 thông báo đã đọc |
| /api/notifications/read-all | PATCH | Đánh dấu tất cả đã đọc |

## **2.4 Database Schema (Supabase / PostgreSQL)** {#2.4-database-schema-(supabase-/-postgresql)}

Mỗi user sở hữu dữ liệu riêng, bảo vệ bằng Row Level Security (RLS).

| Bảng | Các trường chính |
| ----- | ----- |
| users | id, email, display\_name, avatar\_url, created\_at |
| categories | id, user\_id, name, type (income/expense), icon, color, created\_at |
| transactions | id, user\_id, type, amount, category\_id, date, note, payment\_method, bank\_name, receipt\_path, has\_receipt, created\_at |
| budgets | id, user\_id, category\_id, limit\_amount, month (YYYY-MM) |
| goals | id, user\_id, name, target\_amount, current\_amount, deadline |
| debts | id, user\_id, name, type (borrow/lend), amount, paid\_amount, due\_date |
| notifications | id, user\_id, type (budget/reminder/debt/goal), message, is\_read, created\_at |

  *⚠  Mọi bảng có user\_id \+ RLS policy: user chỉ đọc/ghi dữ liệu của chính mình. Storage chặn truy cập trực tiếp, chỉ qua Signed URL.*

## **2.5 Bảo mật Backend** {#2.5-bảo-mật-backend}

> * GEMINI\_API\_KEY và Supabase Service Role Key chỉ nằm trong biến môi trường server.

> * Rate limiting: 100 req/phút/user; 20 scan hóa đơn/ngày; 50 AI chat/ngày.

> * Validate & sanitize toàn bộ input bằng Zod trước khi xử lý.

> * Signed URL TTL 15 phút; Storage bucket chặn public access.

> * Audit log mọi thao tác ghi (create/update/delete).

# **3\. XỬ LÝ NGÔN NGỮ TỰ NHIÊN & TỐI ƯU GEMINI** {#3.-xử-lý-ngôn-ngữ-tự-nhiên-&-tối-ưu-gemini}

Đây là phần lõi tạo nên trải nghiệm 'ghi chép bằng lời nói' của TúiKhôn. Mục tiêu: hiểu đúng câu tiếng Việt đời thường của người dùng, chuẩn hóa số tiền, xác định loại giao dịch và danh mục — đồng thời giảm token và tăng độ chính xác khi gọi Gemini.

## **3.1 Pipeline tổng thể** {#3.1-pipeline-tổng-thể}

Input thô (text/voice)

   │  ví dụ: "Tôi trúng số 300 củ"

   ▼

\[1\] PREPROCESS (client \+ server)

   \- chuẩn hóa khoảng trắng, lowercase phụ trợ

   \- phát hiện & quy đổi đơn vị tiền tệ (củ → triệu)

   \- đánh dấu các token số \+ đơn vị

   ▼

\[2\] GỌI GEMINI (function calling)

   \- system prompt VN \+ few-shot examples

   \- schema addTransaction(amount, type, category, note, date)

   ▼

\[3\] POSTPROCESS / VALIDATE (server)

   \- kiểm tra Gemini trả về hợp lệ (Zod)

   \- đối chiếu lại số tiền đã normalize

   \- nếu thiếu/sai → fallback hỏi lại user

   ▼

\[4\] ACTION CARD → user xác nhận → lưu giao dịch

## **3.2 Bảng quy đổi đơn vị tiền tệ tiếng Việt** {#3.2-bảng-quy-đổi-đơn-vị-tiền-tệ-tiếng-việt}

Hàm normalize quét các từ khóa sau (không phân biệt hoa thường, có dấu/không dấu):

| Từ khóa | Hệ số | Ví dụ |
| ----- | ----- | ----- |
| k, nghìn, ngàn, ngìn | × 1.000 | "65k" → 65.000 |
| tr, triệu, trieu, củ, củ | × 1.000.000 | "300 củ" → 300.000.000 |
| tỷ, tỉ, ty, ti, tỏi | × 1.000.000.000 | "1 tỷ" → 1.000.000.000 |
| lít, ký, cái... (đơn vị SL) | giữ nguyên số lượng | "2 lít" → số lượng \= 2 |
| (số trần, ngữ cảnh tiền) | × 1.000 (heuristic) | "phở 65" → 65.000 |

  *⚠  Quy tắc 'số trần ×1000' chỉ áp dụng khi ngữ cảnh chắc chắn là tiền và số \< 1000\. Khi mơ hồ → để Gemini quyết định, rồi xác nhận với user.*

## **3.3 Xử lý cách nói đặc biệt** {#3.3-xử-lý-cách-nói-đặc-biệt}

| Cách nói | Diễn giải |
| ----- | ----- |
| "hai trăm rưỡi" (k) | 250.000 — 'rưỡi' \= \+nửa đơn vị |
| "2 lít rưỡi" | 2,5 đơn vị số lượng (không phải tiền) |
| "1 tỏi" | 1.000.000.000 (tiếng lóng của 'tỷ') |
| "3 xị" | 300.000 ('xị' \= trăm nghìn, tùy vùng) |
| "năm chục k" | 50.000 |

  *⚠  Tiếng lóng vùng miền ('xị', 'tỏi', 'chục') nên xử lý ở few-shot examples của Gemini hơn là hardcode, để dễ mở rộng.*

## **3.4 Pseudocode hàm normalize** {#3.4-pseudocode-hàm-normalize}

function normalizeAmount(text):

    text \= lowercase(strip(text))

    \# tìm cụm: \<số\> \<đơn vị\>

    for each match (number, unit) in text:

        base \= parseVietnameseNumber(number)  \# '300', 'hai trăm'

        if unit in \[k, nghìn, ngàn\]:      value \= base \* 1\_000

        elif unit in \[tr, triệu, củ\]:     value \= base \* 1\_000\_000

        elif unit in \[tỷ, tỉ, tỏi\]:       value \= base \* 1\_000\_000\_000

        else:                             value \= base  \# cần Gemini xác định

        collect(value)

    return largestMoneyCandidate(collected)  \# chọn ứng viên tiền hợp lý

## **3.5 Code mẫu (TypeScript) — normalize tiền tệ cơ bản** {#3.5-code-mẫu-(typescript)-—-normalize-tiền-tệ-cơ-bản}

const UNIT: Record\<string, number\> \= {

  k: 1e3, nghin: 1e3, ngan: 1e3,

  tr: 1e6, trieu: 1e6, cu: 1e6,

  ty: 1e9, ti: 1e9, toi: 1e9,

};

// bỏ dấu tiếng Việt để khớp key ở trên

const deaccent \= (s: string) \=\>

  s.normalize('NFD').replace(/\[\\u0300-\\u036f\]/g, '');

export function normalizeAmount(input: string): number | null {

  const text \= deaccent(input.toLowerCase());

  const re \= /(\\d+(?:\[.,\]\\d+)?)\\s\*(k|nghin|ngan|tr|trieu|cu|ty|ti|toi)/g;

  let best: number | null \= null, m;

  while ((m \= re.exec(text)) \!== null) {

    const base \= parseFloat(m\[1\].replace(',', '.'));

    const val \= base \* (UNIT\[m\[2\]\] ?? 1);

    if (best \=== null || val \> best) best \= val; // chọn ứng viên lớn nhất

  }

  return best; // null → để Gemini xử lý

}

Ví dụ kiểm thử:

| Input | normalizeAmount | Gemini xác định thêm |
| ----- | ----- | ----- |
| "Tôi trúng số 300 củ" | 300.000.000 | type=income, cat=Khác |
| "Ăn phở 65k" | 65.000 | type=expense, cat=Ăn uống |
| "Lương về 1 tỏi rưỡi" | 1.000.000.000\* | \*‘rưỡi’ xử lý ở Gemini → 1.5 tỷ |

  *⚠  normalizeAmount chỉ bắt số \+ đơn vị cơ bản. Các trường hợp 'rưỡi', tiếng lóng, ngữ cảnh loại giao dịch → giao cho Gemini qua few-shot để hệ thống linh hoạt và dễ mở rộng.*

## **3.6 System Prompt cho Gemini (mẫu)** {#3.6-system-prompt-cho-gemini-(mẫu)}

Bạn là trợ lý tài chính của app TúiKhôn.

Nhiệm vụ: từ câu tiếng Việt của người dùng, trích xuất một giao dịch.

Luôn trả về bằng cách gọi function addTransaction với:

  \- amount (số nguyên VND, đã quy đổi đơn vị)

  \- type ('income' | 'expense')

  \- category (chọn từ danh sách danh mục của user)

  \- note (mô tả ngắn)

  \- date (mặc định hôm nay nếu không nói)

Quy đổi: k=nghìn, tr/củ=triệu, tỷ/tỏi=tỷ, 'rưỡi'=+0.5 đơn vị.

Nếu thiếu thông tin quan trọng (vd số tiền), hỏi lại ngắn gọn.

## **3.7 Few-shot examples (đưa vào prompt)** {#3.7-few-shot-examples-(đưa-vào-prompt)}

| Câu người dùng | amount | type / category |
| ----- | ----- | ----- |
| "Tôi trúng số 300 củ" | 300000000 | income / Khác |
| "Ăn phở 65k" | 65000 | expense / Ăn uống |
| "Đổ xăng 80 nghìn" | 80000 | expense / Di chuyển |
| "Lương tháng này 15 triệu" | 15000000 | income / Lương |
| "Cho Lan mượn 2 tr" | 2000000 | expense / Cho vay |

## **3.8 Validate output & Fallback** {#3.8-validate-output-&-fallback}

> * Dùng Zod kiểm tra Gemini trả đúng schema (amount là số \> 0, type hợp lệ, category tồn tại).

> * Đối chiếu amount của Gemini với normalizeAmount; lệch lớn → ưu tiên hỏi lại user.

> * Nếu Gemini không trả function call (chỉ trả text) → hiển thị text \+ gợi ý user nhập rõ hơn.

> * Mọi giao dịch từ AI đều qua action card xác nhận trước khi lưu — không tự động ghi.

## **3.9 Tối ưu token & chi phí** {#3.9-tối-ưu-token-&-chi-phí}

> * Preprocess phía server để rút gọn input trước khi gửi Gemini.

> * Few-shot ngắn gọn, chỉ giữ ví dụ tiêu biểu; không nhồi toàn bộ danh mục nếu danh sách dài.

> * Dùng model Flash (rẻ, nhanh) cho chat; chỉ dùng model mạnh hơn khi thật cần.

> * Cache danh mục user trong context ngắn hạn để không gửi lại mỗi request.

> * Rate limit per user vừa kiểm soát chi phí vừa chống lạm dụng.

# **4\. FRONTEND (FE)** {#4.-frontend-(fe)}

## **4.1 Cấu trúc thư mục** {#4.1-cấu-trúc-thư-mục}

```
lib/
  ├─ main.dart              # Entry point
  ├─ app.dart               # MaterialApp + GoRouter + ProviderScope
  ├─ core/
  │   ├─ router/            # GoRouter định nghĩa routes
  │   ├─ theme/             # ThemeData light/dark, design tokens
  │   ├─ network/           # Dio client, interceptors (JWT, refresh)
  │   └─ utils/             # format tiền VND, date, normalizeAmount
  ├─ features/              # Mỗi feature 1 thư mục (screen + provider + repo)
  │   ├─ auth/
  │   ├─ transaction/
  │   ├─ ai_chat/
  │   ├─ receipt/
  │   ├─ planning/
  │   ├─ statistics/
  │   ├─ notification/
  │   └─ profile/
  ├─ shared/
  │   ├─ widgets/           # Button, Card, Input, Modal, EmptyState...
  │   └─ models/            # Dart models dùng chung
  └─ offline/               # connectivity_plus, Hive queue, sync logic
```

## **4.2 State Management (Riverpod) — chi tiết** {#4.2-state-management-(riverpod)-—-chi-tiết}

Dùng **flutter\_riverpod v2** với kiến trúc Provider → Notifier → Repository.

| Provider | Giữ gì | Notifier Action chính |
| ----- | ----- | ----- |
| authProvider | user, token, isAuthed | login, logout, refresh |
| transactionProvider | list, filter, loading | fetch, add, update, remove |
| categoryProvider | income\[\], expense\[\] | fetch, add, edit, delete |
| planningProvider | budgets, goals, debts | CRUD từng loại |
| offlineProvider | isOnline, queue\[\], syncing | enqueue, flush, setOnline |
| notificationProvider | list\[\], unreadCount | fetch, markRead, markAllRead |

## **4.3 Navigation** {#4.3-navigation}

Dùng **GoRouter** (declarative routing), hỗ trợ cả mobile và Flutter Web (deep link, URL-based).

Thanh điều hướng là 1 capsule lớn chứa 5 tab, FAB (+) nổi bật ở giữa, cân đối 2 tab mỗi bên: Home · Plan · FAB · Stats · Profile.

> * Home → `/home` — Dashboard stack

> * Plan → `/plan` — Planning stack (Ngân sách / Mục tiêu / Sổ nợ)

> * FAB (+) → BottomSheet overlay: Thêm thủ công · AI Chat · Quét hóa đơn

> * Stats → `/stats` — Statistics stack

> * Profile → `/profile` — Profile stack (chứa nút 'Khác' cho trang phụ)

> * AI Chat → `/ai-chat` — full screen overlay, KHÔNG hiển thị bottom nav.

> * Web: GoRouter tự sinh URL → hỗ trợ browser back/forward và bookmark.

## **4.4 Map màn hình ↔ chức năng ↔ API** {#4.4-map-màn-hình-↔-chức-năng-↔-api}

| Màn hình | Component chính | API / Logic |
| ----- | ----- | ----- |
| Onboarding | FeatureSlides, GoogleBtn | → Login / Register |
| Login | EmailInput, PwdInput, GoogleBtn | /api/auth/login, /api/auth/google |
| Register | Form, OtpNote | /api/auth/register |
| OTP Verification | OtpInput(6), ResendTimer | /api/auth/verify-otp, /resend-otp |
| Forgot Password | EmailInput, NewPwdForm | /api/auth/forgot-password, /reset-password |
| Dashboard | BalanceCard, TxList, FAB | /api/transactions GET |
| Add Transaction | BottomSheet, AmountInput, MicBtn | /api/transactions POST |
| AI Chat | ChatBubble, ActionCard, MicBtn | /api/ai/chat |
| Receipt Camera | Camera, ImagePicker | /api/receipt/upload, /api/ai/scan-receipt |
| Statistics | DonutChart, BarChart, InsightCard | /api/transactions aggregate |
| Planning | Tabs Budget/Goals/Debts | /api/budgets, /api/goals, /api/debts |
| Profile | SettingsList, MoreMenu | user info, sub-pages |
| Notifications | NotifList, Grouped, Empty | /api/notifications, /read, /read-all |
| Add Budget | BottomSheet, CategoryPicker, AmountInput | /api/budgets POST |
| Add Goal | BottomSheet, IconPicker, AmountInput | /api/goals POST |
| Add Debt | BottomSheet, Toggle, AmountInput | /api/debts POST |

## **4.5 Acceptance Criteria (tiêu chí hoàn thành) — ví dụ** {#4.5-acceptance-criteria-(tiêu-chí-hoàn-thành)-—-ví-dụ}

### **Add Transaction** {#add-transaction}

> * Nhập số tiền, chọn danh mục, lưu thành công → xuất hiện ở Dashboard ngay.

> * Khi offline → lưu vào queue, hiển thị trạng thái pending.

> * Validate: không cho lưu khi amount \= 0 hoặc chưa chọn danh mục.

### **AI Chat** {#ai-chat}

> * Nhập 'ăn phở 65k' → hiện action card đúng số tiền \+ danh mục.

> * User bấm Lưu → ghi giao dịch; bấm Sửa → mở form chỉnh.

> * Khi vượt 50 chat/ngày → báo lỗi 429 thân thiện.

## **4.6 Voice Input** {#4.6-voice-input}

Dùng package **speech\_to\_text** (Flutter) gọi Speech-to-Text của OS (Android/iOS/Web, miễn phí, hỗ trợ tiếng Việt `vi_VN`). Text thu được đi vào pipeline AI Chat như khi gõ tay. Nút mic ở Add Transaction và AI Chat.

```dart
final stt = SpeechToText();
await stt.initialize(onStatus: ..., onError: ...);
await stt.listen(localeId: 'vi_VN', onResult: (result) {
  // gửi result.recognizedWords vào AI Chat pipeline
});
```

# **5\. DATA FLOW & SEQUENCE** {#5.-data-flow-&-sequence}

## **5.1 Đăng ký \+ Xác thực OTP** {#5.1-đăng-ký-+-xác-thực-otp}

User nhập email/mật khẩu → POST /api/auth/register

Server tạo user (Supabase) → Supabase gửi OTP 6 số qua email

User nhập mã ở màn OTP → POST /api/auth/verify-otp

Server xác thực mã → tạo JWT app → trả về → vào app

(Gửi lại mã: POST /api/auth/resend-otp sau đếm ngược 60s)

## **5.2 Đăng nhập** {#5.2-đăng-nhập}

Email/mật khẩu: App → POST /api/auth/login → JWT

Google: App → Google OAuth → token → POST /api/auth/google → JWT (bỏ qua OTP)

App lưu JWT vào SecureStore, đính vào header mọi request

## **5.2b Quên mật khẩu** {#5.2b-quên-mật-khẩu}

POST /api/auth/forgot-password (email) → Supabase gửi mã

User nhập mã \+ mật khẩu mới → POST /api/auth/reset-password

## **5.3 Upload & xem ảnh hóa đơn** {#5.3-upload-&-xem-ảnh-hóa-đơn}

App chụp ảnh → POST /api/receipt/upload (multipart \+ JWT)

Server: Multer nhận → Sharp resize ≤1920px

Server → Supabase Storage → tạo Signed URL (TTL 15')

Server → trả {receiptPath, signedUrl} → App hiển thị

Xem lại: URL hết hạn → GET /api/receipt/url → URL mới

## **5.4 AI Chat ghi giao dịch** {#5.4-ai-chat-ghi-giao-dịch}

User: 'Tôi trúng số 300 củ'

App → POST /api/ai/chat (message \+ lịch sử)

Server: preprocess (300 củ → 300.000.000)

Server → Gemini (function calling addTransaction)

Server: validate output (Zod) → trả action card

App hiển thị card → user 'Lưu' → POST /api/transactions

## **5.5 Offline Sync** {#5.5-offline-sync}

Mất mạng: NetInfo.isConnected \= false

User ghi giao dịch → lưu MMKV queue (status pending)

Hiển thị indicator 'Offline'

Có mạng lại: NetInfo event → online

offlineStore.flush(): đọc queue → POST /api/transactions/batch

Thành công → xóa queue → 'Đã đồng bộ ✓'

Thất bại → giữ queue → retry lần sau

## **5.6 Edge cases & xử lý lỗi** {#5.6-edge-cases-&-xử-lý-lỗi}

| Tình huống | Cách xử lý |
| ----- | ----- |
| JWT hết hạn (401) | Tự gọi /api/auth/refresh, retry request |
| Mất mạng giữa chừng | Chuyển sang offline queue, không mất dữ liệu |
| Vượt rate limit AI (429) | Báo lỗi thân thiện \+ thời gian thử lại |
| Gemini hiểu sai số tiền | Action card cho user sửa trước khi lưu |
| Ảnh hóa đơn mờ / sai | AI trả độ tin thấp → gợi ý chụp lại |
| Signed URL hết hạn | Tự xin URL mới khi mở ảnh |
| OTP sai / hết hạn | Báo lỗi, cho nhập lại; gửi lại mã sau 60s |
| Email đã đăng ký | Báo lỗi rõ ràng, gợi ý đăng nhập |
| Xung đột sync (sửa 2 nơi) | Last-write-wins; ghi log để rà soát |

# **6\. DEPLOYMENT & DEVOPS** {#6.-deployment-&-devops}

## **6.1 Môi trường** {#6.1-môi-trường}

| Môi trường | Mục đích |
| ----- | ----- |
| Development | Code & test cục bộ; Supabase project dev |
| Staging | Bản thử gần production để QA |
| Production | Bản phát hành thật; Supabase project prod |

## **6.2 Branch Strategy** {#6.2-branch-strategy}

> * main — production, được bảo vệ (protected).

> * develop — nhánh tích hợp chính.

> * feature/TKN-{ID}-{slug} — nhánh tính năng.

> * fix/TKN-{ID}-{slug} — nhánh sửa lỗi.

> * release/v{version} — nhánh chuẩn bị phát hành.

Commit format: TKN-{ID}: {type}: {mô tả}  (vd: TKN-019: feat: add AI chat endpoint)

## **6.3 CI/CD (GitHub Actions + Codemagic)** {#6.3-ci/cd-(github-actions)}

| Workflow | Tool | Trigger \& các bước |
| ----- | ----- | ----- |
| ci-flutter | GitHub Actions | PR vào develop: flutter pub get → flutter analyze → flutter test |
| ci-api | GitHub Actions | PR vào develop: mvn verify → checkstyle → unit test |
| deploy-api | GitHub Actions | Push vào main: mvn package → Docker build → deploy (Railway/Render) |
| build-flutter | Codemagic | Push vào main: flutter build apk + ipa + web → publish store |

## **6.4 Build \& Release (Codemagic)** {#6.4-build-\&-release-(codemagic)}

> * **Android**: `flutter build apk --release` → `.apk` / `flutter build appbundle` → `.aab` (Google Play).

> * **iOS**: `flutter build ipa` → `.ipa` (App Store) — cần Apple Developer Account.

> * **Web**: `flutter build web --release` → deploy static files lên Vercel / Firebase Hosting.

> * **API**: `mvn package -DskipTests` → Docker image → deploy lên Railway hoặc Render.

> * Biến môi trường tách theo dev/staging/prod trong Codemagic và `.env` của Spring Boot.

## **6.5 Biến môi trường** {#6.5-biến-môi-trường}

| Biến (vị trí) | Mô tả |
| ----- | ----- |
| GEMINI\_API\_KEY (server) | Key gọi Gemini — KHÔNG đưa lên app |
| SUPABASE\_SERVICE\_KEY (server) | Service role key thao tác Storage/DB |
| SUPABASE\_URL / ANON\_KEY | Cấu hình kết nối Supabase |
| SPRING\_DATASOURCE\_URL | JDBC URL kết nối PostgreSQL |
| SPRING\_DATASOURCE\_USERNAME / PASSWORD | Tài khoản DB |
| JWT\_SECRET (server) | Ký \& xác thực JWT (Spring Security) |
| SENTRY\_DSN | Giám sát lỗi production |
| FLUTTER\_BASE\_URL | URL API server (đưa vào dart-define khi build) |

## **6.6 Dev Onboarding — setup máy chạy dự án** {#6.6-dev-onboarding-—-setup-máy-chạy-dự-án}

**Backend (Spring Boot):**

> 1. Cài Java 21 LTS, Maven 3.9+, Git, IntelliJ IDEA (khuyến nghị).

> 2. Clone repo tuikhon-api.

> 3. Copy `application.example.properties` → `application.properties`, điền các key (xin từ PM).

> 4. Chạy: `mvn spring-boot:run` — server khởi động tại `http://localhost:8080`.

> 5. Flyway tự động chạy migration DB khi khởi động.

**Frontend (Flutter):**

> 6. Cài Flutter SDK ≥ 3.x, Android Studio / Xcode (tùy nền tảng target), Git.

> 7. Clone repo tuikhon-flutter.

> 8. Chạy `flutter pub get` để cài dependencies.

> 9. Chạy app: `flutter run` (chọn device/emulator) hoặc `flutter run -d chrome` cho web.

> 10. Đăng nhập thử bằng Google để xác minh luồng auth hoạt động.

# **7\. TESTING & QUALITY** {#7.-testing-&-quality}

| Loại test | Tool | Phạm vi |
| ----- | ----- | ----- |
| Unit BE | JUnit 5 + Mockito | Service, utils (normalizeAmount, format tiền) |
| Unit FE | flutter\_test | Widget tests, Provider logic |
| Integration BE | Spring Boot Test + Testcontainers | API endpoints, DB với PostgreSQL thật |
| Integration FE | flutter\_test | Luồng màn hình: auth, CRUD, AI chat, upload |
| E2E | Patrol (Flutter) | Kịch bản đầu-cuối trên Android Emulator + iOS Simulator |
| Manual QA | — | Offline mode, voice, quét hóa đơn trên thiết bị thật |

## **7.1 Definition of Done** {#7.1-definition-of-done}

> * Code pass lint \+ test, được review qua PR.

> * Hoạt động đúng trên cả iOS và Android.

> * Không lỗi nghiêm trọng trên Sentry.

> * Cập nhật trạng thái task trên Jira.

## **7.2 Performance budget** {#7.2-performance-budget}

> * Cold start \< 3 giây.

> * UI response \< 300ms.

> * Upload ảnh \< 5 giây qua 4G.

## **7.3 Security checklist** {#7.3-security-checklist}

> * Không có key/secret nào nằm trong code client.

> * Mọi input validate bằng Spring Validation (@Valid + ConstraintValidator) ở server.

> * RLS bật cho tất cả bảng Supabase.

> * Signed URL có TTL; Storage chặn public.

> * Rate limit hoạt động cho AI endpoints.

# **8\. TÍNH NĂNG MỞ RỘNG (NEW CORE FEATURES)** {#8.-tính-năng-mở-rộng}

Ba tính năng dưới đây được bổ sung nhằm nâng cấp trải nghiệm từ "ghi chép" lên "quản lý tài chính toàn diện", tận dụng tối đa tech stack hiện tại.

| Tính năng | Độ khó | Giá trị người dùng | Tận dụng tech có sẵn |
| ----- | ----- | ----- | ----- |
| AI Financial Advisor | ⭐⭐ Trung bình | 🔥🔥🔥 Cao | Gemini + DB |
| Split Bill | ⭐⭐ Trung bình | 🔥🔥 Khá cao | Debts module |
| Smart Recurring | ⭐ Thấp | 🔥🔥🔥 Cao | Notifications |

---

## **8.1 AI Financial Advisor — Tư vấn tài chính thông minh** {#8.1-ai-financial-advisor}

### **Mô tả**

Gemini phân tích toàn bộ lịch sử giao dịch của user → đưa ra lời khuyên tài chính cá nhân hóa, nhận xét xu hướng chi tiêu và dự đoán tháng tới. Nâng cấp tab Statistics từ "xem số liệu" thành "hiểu tài chính".

### **API Endpoints**

| Endpoint | Method | Mô tả logic |
| ----- | ----- | ----- |
| /api/ai/analyze | POST | Gửi lịch sử 30/90 ngày → Gemini phân tích → trả insight JSON |
| /api/ai/forecast | GET | Dự đoán chi tiêu tháng tới theo danh mục dựa trên pattern |

### **Cấu trúc response /api/ai/analyze**

```json
{
  "success": true,
  "data": {
    "summary": "Tháng này bạn chi tiêu tăng 18% so với tháng trước.",
    "insights": [
      { "category": "Ăn uống", "change": +40, "advice": "Nên giảm khoảng 500k" },
      { "category": "Di chuyển", "change": -10, "advice": "Tốt, duy trì thói quen này" }
    ],
    "forecast": { "nextMonth": 8200000, "riskCategories": ["Ăn uống", "Mua sắm"] }
  }
}
```

### **Database — không cần bảng mới**

Tận dụng bảng `transactions` và `categories` hiện có. Kết quả phân tích cache ngắn hạn (TTL 1 giờ) phía server để tiết kiệm token Gemini.

### **Frontend**

| Màn hình | Component mới | API |
| ----- | ----- | ----- |
| Statistics (nâng cấp) | InsightCard, ForecastChart, AdvisorChat | /api/ai/analyze, /api/ai/forecast |

### **Data Flow**

```
User mở tab Statistics → chọn "Phân tích AI"
  → GET /api/ai/analyze?range=30d
  → Server tổng hợp transactions → gọi Gemini
  → Gemini trả insight JSON
  → App hiển thị InsightCard + ForecastChart
  → User có thể hỏi tiếp trong AdvisorChat
```

### **System Prompt mẫu cho Advisor**

```
Bạn là cố vấn tài chính cá nhân của app TúiKhôn.
Phân tích dữ liệu chi tiêu dưới đây và trả về JSON với:
- summary: nhận xét tổng quan (1-2 câu)
- insights[]: mảng {category, change (%), advice}
- forecast: {nextMonth (VND), riskCategories[]}
Ngôn ngữ: tiếng Việt, thân thiện, ngắn gọn.
```

---

## **8.2 Split Bill — Chia tiền nhóm & Nhắc nợ thông minh** {#8.2-split-bill}

### **Mô tả**

Cho phép user tạo phiên chia tiền (đi ăn uống, du lịch nhóm, mua sắm chung). Nhập tổng hóa đơn hoặc quét ảnh hóa đơn (tích hợp Receipt Scan), chọn thành viên và tỷ lệ chia (đều, theo số tiền hoặc phần trăm) → kết quả tự động tạo bản ghi trong **Sổ nợ** (bảng `debts` hiện có).

Đặc biệt, tính năng tích hợp **Nhắc nợ thông minh qua SMS & Zalo** và **Quy trình cập nhật trạng thái nợ linh hoạt**:
* **Nhắc nợ 1-chạm (SMS & Zalo)**: Tự động tạo tin nhắn nhắc nợ thân thiện, điền sẵn thông tin STK cá nhân của chủ bill và kèm link mã VietQR động để bạn bè chuyển khoản siêu tốc.
* **Cập nhật trạng thái thanh toán**: Khi nhận được tiền mặt hoặc tiền về tài khoản ngân hàng, chủ bill bấm xác nhận "Đã nhận tiền" → app tự động cập nhật trạng thái phiên chia, đồng bộ sổ nợ và tùy chọn cộng tiền vào số dư ví.

### **API Endpoints**

| Endpoint | Method | Mô tả logic |
| ----- | ----- | ----- |
| /api/split-bill | POST | Tạo phiên chia tiền mới (tự động sinh records trong debts) |
| /api/split-bill | GET | Danh sách các phiên chia tiền của user |
| /api/split-bill/:id | GET | Chi tiết 1 phiên (kèm danh sách thành viên và trạng thái thanh toán) |
| /api/split-bill/:id/members/:memberId/pay | PATCH | Cập nhật trạng thái thành viên đã thanh toán (đồng bộ debts + tùy chọn tạo transaction thu nợ) |
| /api/split-bill/:id/members/:memberId/remind-template | GET | Sinh mẫu tin nhắn nhắc nợ (SMS/Zalo) kèm link ảnh VietQR động |
| /api/split-bill/:id | DELETE | Xóa phiên chia tiền (cascade xóa thành viên, cập nhật debts) |

### **Database Schema**

```sql
-- Bảng: split_sessions (phiên chia tiền)
CREATE TABLE split_sessions (
  id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id      UUID REFERENCES users(id),
  title        TEXT NOT NULL,                        -- vd: "Ăn sinh nhật Lan"
  total_amount BIGINT NOT NULL,
  receipt_path TEXT,                                 -- link ảnh hóa đơn (nếu quét)
  due_date     DATE,                                 -- hạn thanh toán mong muốn (tùy chọn)
  created_at   TIMESTAMPTZ DEFAULT now()
);

-- Bảng: split_members (thành viên tham gia chia tiền)
CREATE TABLE split_members (
  id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  session_id       UUID REFERENCES split_sessions(id) ON DELETE CASCADE,
  name             TEXT NOT NULL,                    -- tên người nợ
  phone_number     TEXT,                             -- SĐT (tùy chọn, để mở SMS/Zalo 1-chạm)
  amount_owed      BIGINT NOT NULL,                  -- số tiền phải trả
  is_paid          BOOLEAN DEFAULT false,            -- trạng thái thanh toán
  paid_at          TIMESTAMPTZ,                      -- thời điểm xác nhận đã trả
  debt_id          UUID REFERENCES debts(id)         -- liên kết sổ nợ tự động
);
```

> ⚠ **Bảo mật RLS**: Áp dụng RLS theo `user_id` của chủ phiên. Khi tạo `split_members` với `is_paid = false`, server tự động tạo 1 bản ghi tương ứng trong bảng `debts` (type = `lend`, status = `active`).

### **Cơ chế Nhắc nợ thông minh (SMS & Zalo Auto-fill)**

Khi chủ bill bấm nút **"Nhắc nợ"** bên cạnh thành viên chưa thanh toán, app mở BottomSheet cung cấp 3 lựa chọn hành động:

#### 1. Nhắc qua Zalo (1-chạm mở app Zalo)
* App dùng Flutter package `share_plus` hoặc `url_launcher` mở ứng dụng Zalo với tin nhắn điền sẵn:
```text
👋 Chào [Tên_bạn], 
Bill "[Tên_bữa_ăn]" phần của bạn là [Số_tiền]đ nhé!
💳 Bạn chuyển giúp mình qua:
• Ngân hàng: [Tên_ngân_hàng]
• STK: [Số_tài_khoản]
• Chủ TK: [Tên_chủ_tài_khoản]
📲 Hoặc quét mã VietQR tại đây để chuyển nhanh:
https://img.vietqr.io/image/[BANK_BIN]-[ACCOUNT_NO]-compact2.png?amount=[AMOUNT]&addInfo=TK[SESSION_ID]%20[NAME]
Cảm ơn bạn nhiều! ✨
```

#### 2. Nhắc qua SMS (1-chạm mở app Tin nhắn mặc định)
* App gọi `url_launcher` với URI `sms:[phone_number]?body=[encoded_text]`:
```text
[TuiKhon] Chao [Tên_bạn], bua [Tên_bữa_ăn] phan ban la [Số_tiền]d nha. STK: [Số_tài_khoản] ([Tên_ngân_hàng]) hoac quet VietQR: [Link_VietQR]. Thanks ban!
```

#### 3. Sao chép tin nhắn & Hiển thị mã VietQR tại chỗ
* Hỗ trợ sao chép nội dung vào Clipboard hoặc hiển thị mã QR phóng to toàn màn hình nếu bạn bè đang ngồi đối diện muốn quét thanh toán trực tiếp.

### **Cơ chế Cập nhật trạng thái thanh toán (Mark as Paid)**

1. **Khi bạn bè chuyển khoản / đưa tiền mặt**:
   * Chủ bill mở app → vào màn hình **Split Detail**.
   * Bấm nút gạt **"Đã nhận tiền" (PaymentToggle)** bên cạnh tên thành viên đó.
2. **Popup xác nhận thông minh**:
   * Hiện dialog: *"Xác nhận [Tên_bạn] đã thanh toán [Số_tiền]đ?"*
   * Tùy chọn checkbox: ☑️ *Ghi nhận vào thu nhập (Cộng [Số_tiền]đ vào số dư ví hiện tại)*.
3. **Xử lý Backend (`PATCH /api/split-bill/:id/members/:memberId/pay`)**:
   * `split_members`: Set `is_paid = true`, `paid_at = now()`.
   * `debts`: Cập nhật `paid_amount = amount_owed`, đổi trạng thái sang `SETTLED`.
   * `transactions` (nếu user tích chọn ghi nhận thu nhập): Tự động tạo 1 giao dịch loại `INCOME`, danh mục `Thu nợ`, ghi chú *"Thu tiền chia bill: [Tên_bữa_ăn] từ [Tên_thành_viên]"*.

### **Frontend (Flutter)**

| Màn hình | Component mới | Thư viện / API |
| ----- | ----- | ----- |
| Split Bill (tab Plan) | MemberList, AmountSplitter, ReceiptBtn | /api/split-bill, /api/receipt/upload |
| Split Detail | MemberDebtTile, PaymentToggle, RemindModal, VietQrDialog | url\_launcher, share\_plus, /api/split-bill/:id/members/:memberId/pay |

### **Data Flow toàn bộ chu trình Split Bill**

```
[1. Tạo phiên]
User vào tab Plan → Bấm "Chia tiền nhóm"
  → Nhập tiêu đề, chọn thành viên (nhập tên, SĐT tùy chọn) hoặc quét hóa đơn
  → Chọn cách chia (đều / tỷ lệ)
  → POST /api/split-bill
  → Backend lưu split_sessions, split_members và tự động tạo records trong bảng debts

[2. Nhắc nợ]
Tại màn hình Split Detail:
  → Bấm "Nhắc nợ" cạnh tên thành viên chưa trả
  → App sinh tin nhắn mẫu kèm link VietQR động
  → Chọn gửi qua Zalo hoặc SMS → App tự động mở Zalo/SMS với nội dung điền sẵn

[3. Thanh toán & Cập nhật]
Bạn bè chuyển khoản / đưa tiền mặt
  → Chủ bill bấm toggle "Đã nhận tiền"
  → Popup hỏi: "Có ghi vào thu nhập ví không?" (Mặc định: Có)
  → PATCH /api/split-bill/:id/members/:memberId/pay
  → Backend cập nhật split_members.is_paid = true + debts (SETTLED) + transactions (+số dư ví)
  → UI hiển thị badge "Đã thanh toán" màu xanh lá
```

---

## **8.3 Smart Recurring — Giao dịch & Nhắc nhở định kỳ** {#8.3-smart-recurring}

### **Mô tả**

User đặt giao dịch lặp lại (tiền nhà, điện nước, Netflix...). App tự nhắc trước hạn thanh toán qua hệ thống Notifications có sẵn. Gemini phát hiện pattern lặp lại từ lịch sử và gợi ý tạo recurring tự động.

### **API Endpoints**

| Endpoint | Method | Mô tả logic |
| ----- | ----- | ----- |
| /api/recurring | GET | Danh sách giao dịch định kỳ |
| /api/recurring | POST | Tạo mới giao dịch định kỳ |
| /api/recurring/:id | PATCH | Cập nhật (thay đổi ngày, số tiền) |
| /api/recurring/:id | DELETE | Xóa giao dịch định kỳ |
| /api/recurring/suggestions | GET | Gemini gợi ý recurring từ lịch sử giao dịch |

### **Database Schema**

```sql
-- Bảng mới: recurring_transactions
CREATE TABLE recurring_transactions (
  id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id         UUID REFERENCES users(id),
  title           TEXT NOT NULL,                   -- vd: "Tiền thuê nhà"
  amount          BIGINT NOT NULL,
  category_id     UUID REFERENCES categories(id),
  frequency       TEXT NOT NULL,                   -- 'daily' | 'weekly' | 'monthly' | 'yearly'
  next_due_date   DATE NOT NULL,                   -- ngày đến hạn tiếp theo
  remind_before   INT DEFAULT 3,                   -- nhắc trước N ngày
  is_active       BOOLEAN DEFAULT true,
  created_at      TIMESTAMPTZ DEFAULT now()
);
```

### **Cron Job (Background)**

```
Chạy mỗi ngày lúc 08:00
  → Quét recurring_transactions WHERE next_due_date = today + remind_before
  → Tạo notification cho từng user (bảng notifications hiện có)
  → Sau khi đến hạn: tự tạo transaction + cập nhật next_due_date
```

### **Frontend**

| Màn hình | Component mới | API |
| ----- | ----- | ----- |
| Recurring (tab Plan) | RecurringCard, FrequencyPicker, DatePicker | /api/recurring |
| Suggestions | SuggestionBanner, OneClickAdd | /api/recurring/suggestions |

### **Data Flow**

```
[Tạo thủ công]
User vào tab Plan → "Định kỳ" → Thêm mới
  → Nhập tiêu đề, số tiền, tần suất, ngày đến hạn
  → POST /api/recurring
  → Server lưu vào recurring_transactions

[Gợi ý tự động]
User mở tab Plan → App gọi GET /api/recurring/suggestions
  → Server gửi 90 ngày giao dịch lên Gemini
  → Gemini phát hiện pattern ("Mỗi tháng có khoản ~1.2tr danh mục Nhà ở")
  → App hiển thị SuggestionBanner → User bấm "Thêm" → POST /api/recurring

[Nhắc nhở]
Cron 08:00 hàng ngày
  → Phát hiện sắp đến hạn → INSERT vào notifications
  → App nhận push notification: "Tiền thuê nhà đến hạn sau 3 ngày (1.200.000đ)"
  → User xác nhận đã trả → tự động ghi transaction + cập nhật next_due_date
```

---

## **8.4 Tóm tắt thay đổi kỹ thuật cần bổ sung**

| Hạng mục | AI Financial Advisor | Split Bill | Smart Recurring |
| ----- | ----- | ----- | ----- |
| Bảng DB mới | Không | split\_sessions, split\_members | recurring\_transactions |
| Endpoint mới | /ai/analyze, /ai/forecast | /split-bill (CRUD) | /recurring (CRUD), /recurring/suggestions |
| Màn hình mới | Nâng cấp Statistics | Tab Plan → Split | Tab Plan → Recurring |
| Gemini call | ✅ analyze + forecast | Không (tùy chọn scan) | ✅ suggestions |
| Cron Job | Không | Không | ✅ nhắc nhở hàng ngày |
| Notifications | Không | Không | ✅ dùng bảng hiện có |

# **9\. ACTORS & WEB DASHBOARD** {#9.-actors-web-dashboard}

## **9.1 Phân loại Actor** {#9.1-phân-loại-actor}

Hệ thống TúiKhôn có **2 actor** chính với quyền hạn và giao diện riêng biệt:

| Actor | Giao diện | Mục đích |
| ----- | ----- | ----- |
| **User** | Mobile App & Web (Flutter) | Quản lý tài chính cá nhân |
| **Admin** | Web Dashboard (Flutter Web) | Vận hành & giám sát hệ thống |

### **Quyền hạn chi tiết**

| Hành động | User | Admin |
| ----- | ----- | ----- |
| Ghi / xem giao dịch của mình | ✅ | ❌ (không xem dữ liệu cá nhân) |
| Dùng AI Chat, Receipt Scan | ✅ | ❌ |
| Xem thống kê & báo cáo cá nhân | ✅ | ❌ |
| Export dữ liệu của mình | ✅ (web) | ❌ |
| Quản lý tài khoản user | ❌ | ✅ |
| Cấu hình rate limit hệ thống | ❌ | ✅ |
| Xem thống kê toàn hệ thống | ❌ | ✅ |
| Gửi thông báo broadcast | ❌ | ✅ |
| Xem audit log & error log | ❌ | ✅ |

> ⚠ Admin **không bao giờ** truy cập dữ liệu tài chính cá nhân của User (RLS vẫn được áp dụng). Admin chỉ thao tác metadata tài khoản và cấu hình hệ thống.

---

## **9.2 Database — Bổ sung cho Admin** {#9.2-database-admin}

```sql
-- Bảng mới: admins
CREATE TABLE admins (
  id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  email        TEXT UNIQUE NOT NULL,
  password_hash TEXT NOT NULL,
  display_name TEXT,
  role         TEXT DEFAULT 'admin',   -- 'admin' | 'super_admin'
  created_at   TIMESTAMPTZ DEFAULT now()
);

-- Bảng mới: system_configs (cấu hình động, không cần deploy lại)
CREATE TABLE system_configs (
  key          TEXT PRIMARY KEY,       -- vd: 'ai_chat_limit_per_day'
  value        TEXT NOT NULL,
  description  TEXT,
  updated_by   UUID REFERENCES admins(id),
  updated_at   TIMESTAMPTZ DEFAULT now()
);

-- Bảng mới: broadcast_notifications
CREATE TABLE broadcast_notifications (
  id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  title        TEXT NOT NULL,
  message      TEXT NOT NULL,
  target       TEXT DEFAULT 'all',     -- 'all' | filter theo điều kiện
  sent_by      UUID REFERENCES admins(id),
  sent_at      TIMESTAMPTZ DEFAULT now()
);
```

---

## **9.3 Admin Web Dashboard** {#9.3-admin-web-dashboard}

### **Mô tả**

Giao diện web dành riêng cho team vận hành. Truy cập qua trình duyệt, đăng nhập bằng tài khoản Admin riêng biệt (không dùng chung với user thường).

### **Tech Stack**

| Tầng | Công nghệ |
| ----- | ----- |
| Framework | Flutter Web (cùng codebase với mobile, route `/admin/*`) |
| UI | Flutter Material 3 + custom AdminTheme |
| Charts | fl\_chart / syncfusion\_flutter\_charts |
| Auth | Admin JWT riêng (không dùng Supabase Auth) |
| API | Gọi /api/admin/\* trên cùng Spring Boot server |

### **API Endpoints — Nhóm Admin**

| Endpoint | Method | Mô tả logic |
| ----- | ----- | ----- |
| /api/admin/auth/login | POST | Đăng nhập admin → JWT admin |
| /api/admin/users | GET | Danh sách user, lọc, tìm kiếm, phân trang |
| /api/admin/users/:id | GET | Chi tiết 1 user (metadata, không có tx) |
| /api/admin/users/:id/status | PATCH | Khoá / mở khoá tài khoản |
| /api/admin/users/:id/reset-password | POST | Gửi email reset mật khẩu cho user |
| /api/admin/stats/overview | GET | Tổng user, DAU, MAU, tổng giao dịch hệ thống |
| /api/admin/stats/ai-usage | GET | Số lượng AI call theo ngày/user, chi phí token |
| /api/admin/configs | GET / PATCH | Đọc & cập nhật system\_configs |
| /api/admin/notifications/broadcast | POST | Gửi thông báo đến tất cả user |
| /api/admin/logs | GET | Audit log, error log, lọc theo thời gian |

> ⚠ Toàn bộ endpoint /api/admin/\* yêu cầu **Admin JWT** riêng, middleware kiểm tra role = 'admin'. User thường không thể truy cập nhóm này dù có JWT.

### **Màn hình Admin Dashboard**

| Màn hình | Chức năng chính |
| ----- | ----- |
| **Login** | Đăng nhập tài khoản admin |
| **Overview** | Tổng user, DAU/MAU, tổng giao dịch, chi phí Gemini token hôm nay |
| **User Management** | Bảng danh sách user, tìm kiếm, lọc; khoá/mở khoá; gửi reset password |
| **AI Usage Monitor** | Biểu đồ số lượng AI chat & scan theo ngày; phát hiện user lạm dụng |
| **System Config** | Form chỉnh rate limit (chat/day, scan/day, req/min) không cần deploy |
| **Broadcast** | Soạn & gửi thông báo đến toàn bộ user |
| **Audit Log** | Bảng log mọi thao tác ghi trong hệ thống, lọc theo user/thời gian |

### **Data Flow — Khoá tài khoản User**

```
Admin tìm user trên User Management
  → Bấm "Khoá tài khoản"
  → PATCH /api/admin/users/:id/status { status: 'banned' }
  → Server cập nhật Supabase Auth (disable user)
  → Ghi audit log
  → User login → 403 Forbidden → hiển thị thông báo tài khoản bị khoá
```

### **Data Flow — Điều chỉnh Rate Limit**

```
Admin vào System Config
  → Sửa giá trị 'ai_chat_limit_per_day' từ 50 → 30
  → PATCH /api/admin/configs { key: 'ai_chat_limit_per_day', value: '30' }
  → Server cập nhật bảng system_configs
  → Middleware rate-limit đọc config động → áp dụng ngay, không cần restart
```

---

## **9.4 User Web Dashboard** {#9.4-user-web-dashboard}

### **Mô tả**

Giao diện web dành cho **User** muốn xem báo cáo tài chính trên màn hình lớn, xuất dữ liệu, quản lý danh mục. Đăng nhập bằng cùng tài khoản với mobile app (Google OAuth hoặc email/password).

### **Tech Stack**

Dùng chung Flutter project (tuikhon-flutter) hỗ trợ đa nền tảng (Web + Mobile), tách route bằng GoRouter: `/app/*` cho User Web, `/admin/*` cho Admin Web.

### **API Endpoints — Nhóm User Web** (tái sử dụng API hiện có)

| Endpoint | Tái sử dụng từ | Ghi chú |
| ----- | ----- | ----- |
| /api/auth/login, /api/auth/google | Auth hiện có | Đăng nhập web |
| /api/transactions | Transactions hiện có | Xem lịch sử |
| /api/transactions/export | **Mới** | Xuất CSV/Excel |
| /api/categories | Categories hiện có | Quản lý danh mục |
| /api/budgets, /api/goals, /api/debts | Planning hiện có | Xem tổng quan |
| /api/ai/analyze, /api/ai/forecast | AI Advisor hiện có | Báo cáo thông minh |

### **Màn hình User Web Dashboard**

| Màn hình | Chức năng chính |
| ----- | ----- |
| **Login** | Đăng nhập email/password hoặc Google OAuth |
| **Dashboard** | Tổng quan số dư, thu/chi tháng này, biểu đồ line chart theo năm |
| **Transactions** | Bảng giao dịch đầy đủ, lọc nâng cao, tìm kiếm |
| **Export** | Chọn khoảng thời gian → xuất CSV / Excel / PDF |
| **Statistics** | Biểu đồ nâng cao: heatmap chi tiêu, so sánh tháng, xu hướng |
| **AI Insights** | Báo cáo AI Financial Advisor chi tiết trên màn hình lớn |
| **Categories** | Tạo/sửa/xóa danh mục dễ hơn trên bàn phím |
| **Planning** | Tổng quan ngân sách + mục tiêu + sổ nợ theo năm |
| **Profile** | Đổi tên, avatar, mật khẩu |

### **Data Flow — Export dữ liệu**

```
User chọn khoảng thời gian (vd: 01/2026 → 09/2026)
  → Chọn định dạng: CSV / Excel / PDF
  → GET /api/transactions/export?from=2026-01&to=2026-09&format=xlsx
  → Server tổng hợp transactions → sinh file
  → Trả về file download → Browser lưu về máy
```

---

## **9.5 Tóm tắt phân chia hệ thống theo Actor**

```
┌─────────────────────────────────────────────────────────────────┐
│                        TúiKhôn System                          │
├────────────────────┬──────────────────┬─────────────────────────┤
│  Flutter Mobile    │  Flutter Web     │  Flutter Web            │
│  (Android & iOS)   │  User Dashboard  │  Admin Dashboard        │
│                    │  (route /app/*)  │  (route /admin/*)       │
├────────────────────┼──────────────────┼─────────────────────────┤
│ Actor: USER        │ Actor: USER      │ Actor: ADMIN            │
│ - Ghi giao dịch    │ - Xem báo cáo   │ - Quản lý user          │
│ - AI Chat/Scan     │ - Export dữ liệu│ - Giám sát AI usage     │
│ - Voice Input      │ - Quản lý cat.  │ - System config         │
│ - Offline Sync     │ - AI Insights   │ - Broadcast notif       │
│ - Notifications    │ - Planning      │ - Audit log             │
└────────────────────┴──────────────────┴─────────────────────────┘
                              │
                 ┌────────────┴────────────┐
                 │   Java Spring Boot      │
                 │  /api/*  /api/admin/*   │
                 └────────────┬────────────┘
                              │
              ┌───────────────┴───────────────┐
              │  PostgreSQL (Supabase hosted)  │
              │  Supabase Storage · Auth · RLS │
              │  Google Gemini AI              │
              └───────────────────────────────┘
```