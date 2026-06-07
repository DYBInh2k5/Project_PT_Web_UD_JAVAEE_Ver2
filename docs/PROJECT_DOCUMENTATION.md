# TÀI LIỆU ĐỒ ÁN WEB BÁN SÁCH (BookStore)

## MỤC LỤC
1. [Tổng quan](#1-tổng-quan)
2. [Công nghệ sử dụng](#2-công-nghệ-sử-dụng)
3. [Cấu trúc Project (MVC)](#3-cấu-trúc-project-mvc)
4. [Database](#4-database)
5. [Các chức năng chi tiết](#5-các-chức-năng-chi-tiết)
6. [Luồng hoạt động](#6-luồng-hoạt-động)
7. [Spring Security & Phân quyền](#7-spring-security--phân-quyền)
8. [Giải thích code từng tầng](#8-giải-thích-code-từng-tầng)
9. [Các câu hỏi vấn đáp thường gặp](#9-các-câu-hỏi-vấn-đáp-thường-gặp)

---

## 1. TỔNG QUAN

**BookStore** là ứng dụng web bán sách trực tuyến xây dựng bằng Spring Boot MVC.

### Yêu cầu chức năng

| Nhóm | Chức năng |
|------|-----------|
| **Khách hàng** | Đăng ký, đăng nhập, đăng xuất. Xem sản phẩm (phân trang). Tìm kiếm theo tên, theo loại. Sắp xếp theo giá (tăng/giảm). Quản lý giỏ hàng (thêm, sửa số lượng, xóa, thanh toán - yêu cầu login). |
| **Admin** | Đăng nhập, đăng xuất. Quản lý sản phẩm (xem, thêm, sửa, xóa). Quản lý đơn hàng (xem, cập nhật trạng thái: Mới/Đã vận chuyển/Đã thanh toán). Xem doanh thu (theo ngày, tháng, năm + biểu đồ cột 7 ngày). |

### Dữ liệu mẫu
- 1 admin + 2 khách hàng
- 8 danh mục sách
- 35 sản phẩm (mỗi sản phẩm có 1 hình)
- 6 đơn hàng mẫu

---

## HƯỚNG DẪN CHẠY PROJECT

### Yêu cầu
- **Java 17+** (kiểm tra: `java -version`)
- **Maven** (kiểm tra: `mvn -version`)
- **SQL Server** (đã cài đặt và có database `BookStore` hoặc để Hibernate tự tạo)
- **Ảnh sách**: 35 file ảnh `book1.jpg` ~ `book35.jpg` trong `src/main/resources/static/images/`

### Cách 1: Chạy với Hibernate tự tạo bảng (dễ nhất)
```
1. Kiểm tra SQL Server đang chạy (dịch vụ: SQL Server (MSSQLSERVER))
2. Sửa application.properties nếu cần (username/password SQL Server)
3. Mở cmd tại thư mục BookStore, chạy:
   mvn spring-boot:run
4. Mở trình duyệt: http://localhost:8080
```

> Hibernate tự tạo 7 bảng (ddl-auto=update).
> `data.sql` tự động chèn danh mục, 35 sản phẩm, 6 đơn hàng.
> `DataInitializer` tự động chèn 3 tài khoản.

### Cách 2: Chạy database.sql trước (chủ động)
```
1. Mở SQL Server Management Studio (SSMS)
2. File → Open → Chọn database.sql → Execute (F5)
3. Chạy Spring Boot:
   mvn spring-boot:run
4. Mở trình duyệt: http://localhost:8080
```

> `database.sql` tạo database + 7 bảng + chèn toàn bộ dữ liệu mẫu.
> Spring Boot chạy với dữ liệu đã có, không cần seed lại.
> Nếu bị lỗi duplicate key thì comment dòng `spring.sql.init.mode=always` trong application.properties.

### Lưu ý
- Nếu chưa có ảnh sách: vào thư mục `static/images/` và thêm 35 file book1.jpg ~ book35.jpg
- Tài khoản mặc định: `admin` / `123456` (ADMIN), `nguyenvana` / `123456` (CUSTOMER)
- Port mặc định: 8080 (có thể sửa ở `application.properties`)
- Khi chạy lần đầu, Hibernate tạo bảng + data.sql seed dữ liệu → lần sau sẽ không seed lại (do có `IF NOT EXISTS`)

---

## 2. CÔNG NGHỆ SỬ DỤNG

| Công nghệ | Mục đích |
|-----------|----------|
| **Spring Boot 3.2** | Framework chính |
| **Spring MVC** | Mô hình Model-View-Controller |
| **Spring Data JPA (Hibernate)** | ORM - thao tác database |
| **Spring Security** | Xác thực + phân quyền |
| **Thymeleaf** | Template engine (View) |
| **SQL Server** | Cơ sở dữ liệu |
| **BCrypt** | Mã hóa mật khẩu |
| **Bootstrap 5** | Giao diện frontend |
| **Chart.js** | Vẽ biểu đồ doanh thu |
| **Maven** | Build tool |

---

## 3. CẤU TRÚC PROJECT (MVC)

```
BookStore/
├── pom.xml
├── database.sql                    # Script tạo database hoàn chỉnh
├── YEU_CAU.md                      # File yêu cầu
├── docs/
│   ├── PROJECT_DOCUMENTATION.md    # Tài liệu này
│   └── CODE_SAMPLES.md             # Code mẫu tham khảo
├── src/
│   ├── main/
│   │   ├── java/com/bookstore/
│   │   │   ├── BookStoreApplication.java     # Main class
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java       # Cấu hình bảo mật
│   │   │   │   ├── WebMvcConfig.java         # Cấu hình static resources
│   │   │   │   └── DataInitializer.java      # Khởi tạo dữ liệu users
│   │   │   ├── entity/                       # 7 entity (Model)
│   │   │   ├── repository/                   # 7 repository (DAO)
│   │   │   ├── service/                      # 6 service (Business)
│   │   │   ├── dto/                          # 2 DTO
│   │   │   └── controller/                   # Controller
│   │   │       ├── AuthController.java
│   │   │       ├── HomeController.java
│   │   │       ├── CartController.java
│   │   │       ├── OrderController.java
│   │   │       └── admin/
│   │   │           ├── AdminController.java
│   │   │           ├── ProductController.java
│   │   │           └── OrderManageController.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── data.sql                      # SQL seed data (categories, products, orders)
│   │       ├── static/
│   │       │   ├── css/style.css
│   │       │   ├── js/main.js
│   │       │   └── images/                   # Ảnh sách (book1.jpg ~ book35.jpg)
│   │       └── templates/                    # 14 Thymeleaf views
│   │           ├── layout.html               # Layout chung
│   │           ├── index.html                # Trang chủ
│   │           ├── shop.html                 # Danh sách sản phẩm
│   │           ├── login.html                # Đăng nhập
│   │           ├── register.html             # Đăng ký
│   │           ├── cart.html                 # Giỏ hàng
│   │           ├── checkout.html             # Thanh toán
│   │           ├── order-history.html        # Lịch sử đơn hàng
│   │           ├── order-detail.html         # Chi tiết đơn hàng
│   │           └── admin/
│   │               ├── dashboard.html        # Dashboard admin
│   │               ├── products.html         # Quản lý sản phẩm
│   │               ├── product-form.html     # Thêm/sửa sản phẩm
│   │               ├── orders.html           # Quản lý đơn hàng
│   │               └── revenue.html          # Xem doanh thu + biểu đồ
```

### Giải thích luồng MVC

```
Trình duyệt (View)
    ↓ HTTP Request
Controller (@Controller)
    ↓ Gọi
Service (@Service)         → Xử lý nghiệp vụ
    ↓ Gọi
Repository (JPA)           → Thao tác database
    ↓
Entity (@Entity)           → Ánh xạ bảng trong DB
    ↓
SQL Server

    Return
Controller → Model (gửi dữ liệu) → View (Thymeleaf) → HTML → Trình duyệt
```

---

## 4. DATABASE

### 4.1. Sơ đồ quan hệ

```
users (1) ───< (N) orders
users (1) ───< (1) carts
carts (1) ───< (N) cart_items
orders (1) ───< (N) order_details
categories (1) ───< (N) products
products (1) ───< (N) cart_items
products (1) ───< (N) order_details
```

### 4.2. Các bảng

#### users
| Column | Type | Ghi chú |
|--------|------|---------|
| id | INT (PK, Identity) | |
| username | NVARCHAR(50) | UNIQUE |
| password | NVARCHAR(255) | Mã hóa BCrypt |
| full_name | NVARCHAR(100) | |
| email | NVARCHAR(100) | NULL |
| phone | NVARCHAR(20) | NULL |
| address | NVARCHAR(255) | NULL |
| role | NVARCHAR(20) | 'ADMIN' hoặc 'CUSTOMER' |
| created_at | DATETIME2 | Tự động |

#### categories
| Column | Type | Ghi chú |
|--------|------|---------|
| id | INT (PK, Identity) | |
| name | NVARCHAR(100) | UNIQUE |

#### products
| Column | Type | Ghi chú |
|--------|------|---------|
| id | INT (PK, Identity) | |
| name | NVARCHAR(200) | |
| author | NVARCHAR(100) | NULL |
| description | TEXT | NULL |
| price | FLOAT | |
| stock | INT | Tồn kho |
| image | NVARCHAR(255) | Tên file ảnh |
| category_id | INT (FK → categories) | NULL |
| created_at | DATETIME2 | Tự động |

#### carts
| Column | Type | Ghi chú |
|--------|------|---------|
| id | INT (PK, Identity) | |
| user_id | INT (FK → users) | UNIQUE (1 user chỉ 1 giỏ) |

#### cart_items
| Column | Type | Ghi chú |
|--------|------|---------|
| id | INT (PK, Identity) | |
| cart_id | INT (FK → carts) | |
| product_id | INT (FK → products) | |
| quantity | INT | |

#### orders
| Column | Type | Ghi chú |
|--------|------|---------|
| id | INT (PK, Identity) | |
| user_id | INT (FK → users) | |
| order_date | DATETIME2 | Tự động |
| total_amount | FLOAT | Tổng tiền |
| status | NVARCHAR(20) | 'NEW' / 'SHIPPED' / 'PAID' |
| recipient_name | NVARCHAR(100) | |
| phone | NVARCHAR(20) | |
| address | NVARCHAR(255) | |

#### order_details
| Column | Type | Ghi chú |
|--------|------|---------|
| id | INT (PK, Identity) | |
| order_id | INT (FK → orders) | |
| product_id | INT (FK → products) | |
| quantity | INT | |
| price | FLOAT | Giá tại thời điểm mua |

---

## 5. CÁC CHỨC NĂNG CHI TIẾT

### 5.1. Chức năng khách hàng

#### Đăng ký
- **URL**: `GET /register` (hiển thị form), `POST /register` (xử lý)
- **Controller**: `AuthController.registerForm()` + `register()`
- **Validation**: `RegisterDto` dùng `@NotBlank`, `@Size(min=3)`, `@Size(min=6)` cho password
- **Xử lý**: `UserService.register()` kiểm tra username tồn tại → mã hóa BCrypt → lưu → redirect login

#### Đăng nhập
- **URL**: `GET /login` (form), `POST /login` (xử lý bởi Spring Security)
- **Custom**: `CustomUserDetailsService` load user từ DB
- **Thành công**: redirect `/`; **Thất bại**: `/login?error=true`

#### Đăng xuất
- **URL**: `GET /logout`
- Xóa session, redirect về trang chủ

#### Xem sản phẩm + phân trang
- **URL**: `GET /` hoặc `GET /shop`
- **Controller**: `HomeController.home()` + `shop()`
- **Phân trang**: Spring Data Pageable, mỗi trang 12 sản phẩm
- **Tham số**: `page` (số trang), `name` (tên), `category` (danh mục), `sort` ('asc'/'desc')

#### Tìm kiếm
- Theo tên: `name` parameter → `findByNameContainingIgnoreCase()`
- Theo danh mục: `category` parameter → `findByCategoryId()`
- Kết hợp cả hai: `findByNameContainingIgnoreCaseAndCategoryId()`

#### Sắp xếp theo giá
- `sort=asc`: Sắp tăng dần (`Sort.by("price").ascending()`)
- `sort=desc`: Sắp giảm dần (`Sort.by("price").descending()`)

#### Giỏ hàng
- **Thêm**: `POST /cart/add/{productId}` → tạo/cập nhật CartItem
- **Sửa số lượng**: `POST /cart/update/{itemId}?quantity=N`
- **Xóa**: `POST /cart/remove/{itemId}`
- **Thanh toán**: 2 bước:
  1. `GET /cart/checkout` → form nhập thông tin giao hàng
  2. `POST /cart/checkout` → tạo Order + OrderDetails, xóa Cart
- **Kiểm tra login**: Nếu chưa login → redirect `/login`

### 5.2. Chức năng admin

#### Quản lý sản phẩm
| Hành động | URL | Ghi chú |
|-----------|-----|---------|
| Xem | `GET /admin/products` | Phân trang, lọc theo tên/danh mục |
| Thêm | `GET /admin/products/add` + `POST /admin/products/add` | Upload ảnh |
| Sửa | `GET /admin/products/edit/{id}` + `POST /admin/products/edit/{id}` | |
| Xóa | `GET /admin/products/delete/{id}` | |

#### Quản lý đơn hàng
- **Xem**: `GET /admin/orders` (phân trang)
- **Cập nhật trạng thái**: `POST /admin/orders/update-status` với `orderId` + `status`
- **Trạng thái**: `NEW` (Mới) → `SHIPPED` (Đã vận chuyển) → `PAID` (Đã thanh toán)

#### Xem doanh thu
- `GET /admin/orders/revenue` → form chọn ngày
- `POST /admin/orders/revenue?date=YYYY-MM-DD` → hiển thị:
  1. **Tổng doanh thu ngày** (RevenueDto.dayRevenue)
  2. **Tổng doanh thu tháng** (RevenueDto.monthRevenue)
  3. **Tổng doanh thu năm** (RevenueDto.yearRevenue)
  4. **Biểu đồ cột 7 ngày** (dùng Chart.js, dữ liệu từ `OrderService.getDailyRevenue7Days()`)

---

## 6. LUỒNG HOẠT ĐỘNG

### 6.1. Luồng đặt hàng
```
User vào /shop → chọn sản phẩm → /cart/add/{id}
    ↓
Kiểm tra login? Nếu chưa → redirect /login
    ↓
Thêm vào Cart
    ↓
User vào /cart → xem giỏ hàng
    ↓
Sửa số lượng / Xóa
    ↓
Nhấn "Thanh toán" → /cart/checkout
    ↓
Điền thông tin giao hàng → submit
    ↓
Tạo Order + OrderDetail → Xóa Cart
    ↓
redirect /order/history
```

### 6.2. Luồng admin xem doanh thu
```
Admin vào /admin/orders/revenue
    ↓
Chọn ngày (date picker) → submit
    ↓
Controller gọi:
  - OrderService.getRevenueByDay(date)
  - OrderService.getRevenueByMonth(year, month)
  - OrderService.getRevenueByYear(year)
  - OrderService.getDailyRevenue7Days(date)
    ↓
Repository: native query SUM(total_amount) GROUP BY date
    ↓
Trả về RevenueDto → hiển thị trên view
    ↓
Chart.js vẽ biểu đồ cột từ dữ liệu dailyRevenueList
```

---

## 7. SPRING SECURITY & PHÂN QUYỀN

### Cấu hình (`SecurityConfig.java`)

```java
.authorizeHttpRequests(config -> config
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/cart/**").hasAnyRole("CUSTOMER", "ADMIN")
    .requestMatchers("/order/**").hasAnyRole("CUSTOMER", "ADMIN")
    .requestMatchers("/register", "/login", "/css/**", "/js/**",
                     "/images/**", "/").permitAll()
    .requestMatchers("/shop/**", "/api/**").permitAll()
    .anyRequest().authenticated()
)
```

### Vai trò (Roles)
- **ROLE_ADMIN**: admin - được vào tất cả, bao gồm /admin/**
- **ROLE_CUSTOMER**: khách hàng - được dùng /cart/**, /order/**

### Cơ chế xác thực
1. `CustomUserDetailsService` implements `UserDetailsService`
2. `loadUserByUsername()`: lấy user từ DB, trả về `UserDetails` với `SimpleGrantedAuthority("ROLE_" + role)`
3. `DaoAuthenticationProvider` kết hợp `UserDetailsService` + `BCryptPasswordEncoder`
4. Form login mặc định của Spring Security

---

## 8. GIẢI THÍCH CODE TỪNG TẦNG

### 8.1. Tầng Entity (Model)
Ánh xạ bảng trong database. Dùng JPA annotations:
- `@Entity`: đánh dấu là entity
- `@Table`: chỉ định tên bảng
- `@Id` + `@GeneratedValue`: khóa chính tự tăng
- `@Column`: chỉ định cột
- `@ManyToOne` / `@OneToMany` / `@OneToOne`: quan hệ
- `@JoinColumn`: khóa ngoại
- `@PrePersist`: tự động gán giá trị trước khi insert

### 8.2. Tầng Repository (DAO)
Kế thừa `JpaRepository<Entity, ID>` → có sẵn CRUD:
- `findAll()`, `findById()`, `save()`, `deleteById()`
- Query methods: `findByNameContainingIgnoreCase()`
- `@Query`: custom JPQL / native SQL

### 8.3. Tầng Service (Business Logic)
- `@Service`: Spring Bean
- Xử lý nghiệp vụ: kiểm tra tồn kho, tính tổng tiền, tạo đơn hàng
- `@Transactional`: đảm bảo toàn vẹn dữ liệu (rollback nếu lỗi)
- Gọi repository để thao tác DB

### 8.4. Tầng Controller
- `@Controller`: trả về View (Thymeleaf)
- `@GetMapping` / `@PostMapping`: ánh xạ URL
- `@RequestParam`: lấy tham số từ request
- `@PathVariable`: lấy tham số từ URL
- `@ModelAttribute`: binding dữ liệu form vào object
- `Model.addAttribute()`: gửi dữ liệu xuống View
- `RedirectAttributes`: gửi message khi redirect

### 8.5. Tầng View (Thymeleaf)
- `th:each`: lặp
- `th:if` / `th:unless`: rẽ nhánh
- `th:text`: hiển thị text
- `th:src`: đường dẫn ảnh
- `th:action`: action của form
- `th:field`: binding với DTO
- `th:classappend`: thêm class động
- `sec:authorize`: kiểm tra quyền
- `#numbers.formatDecimal`: format số
- `#temporals.format`: format ngày

---

## 9. CÁC CÂU HỎI VẤN ĐÁP THƯỜNG GẶP

### Q1: Tại sao dùng Spring Boot thay vì Spring thuần?
→ Spring Boot tự cấu hình (auto-configuration), nhúng Tomcat, giảm boilerplate, có starter dependencies.

### Q2: Spring MVC là gì?
→ Mô hình Model-View-Controller. Model: dữ liệu (Entity, DTO). View: giao diện (Thymeleaf). Controller: xử lý request.

### Q3: JPA và Hibernate khác nhau thế nào?
→ JPA là specification (đặc tả), Hibernate là implementation (triển khai). Spring Data JPA dùng Hibernate làm mặc định.

### Q4: Tại sao dùng interface Repository mà không dùng class?
→ Spring Data JPA tự động sinh implementation khi runtime dựa trên tên method.

### Q5: `@Transactional` dùng để làm gì?
→ Đảm bảo tất cả thao tác trong method đều thành công hoặc đều rollback (ACID). Dùng trong `createOrder()` để combo: tạo order + giảm stock + xóa cart.

### Q6: Phân trang Spring Data JPA hoạt động thế nào?
→ `Pageable` interface chứa `page` (số trang) + `size` (kích thước). `PageRepository` trả về `Page<T>` có `getContent()`, `getTotalPages()`, `getTotalElements()`.

### Q7: Tại sao ảnh sách không lưu trong database?
→ Lưu file trên ổ cứng (static/images/) nhanh hơn, giảm dung lượng DB, dễ backup. DB chỉ lưu tên file.

### Q8: Spring Security hoạt động thế nào?
→ `SecurityFilterChain` chứa chuỗi filter. `DaoAuthenticationProvider` xác thực. `UserDetailsService` load user. `BCryptPasswordEncoder` mã hóa/matching password.

### Q9: Thymeleaf khác JSP thế nào?
→ Thymeleaf là template engine hiện đại, dùng attribute trong HTML (th:xxx), tự nhiên hơn, có thể mở file HTML trực tiếp mà không cần server. JSP dùng tag library.

### Q10: Native query vs JPQL khác nhau thế nào?
→ JPQL: truy vấn trên entity (độc lập DB). Native: truy vấn SQL thuần (phụ thuộc DB). Ví dụ: doanh thu dùng native `CAST(order_date AS DATE)` vì cần hàm SQL Server đặc thù.

### Q11: DTO để làm gì?
→ DTO (Data Transfer Object) chỉ chứa dữ liệu cần thiết, tránh expose Entity ra ngoài. Ví dụ: `RegisterDto` chỉ có username, password, fullName,... không có id, role, createdAt.

### Q12: Cascade và orphanRemoval là gì?
→ `cascade = CascadeType.ALL`: khi save/delete entity cha thì entity con cũng được xử lý theo. `orphanRemoval = true`: khi xóa entity con khỏi collection thì tự động xóa khỏi DB.

### Q13: FetchType.LAZY là gì?
→ LAZY: chỉ load dữ liệu khi thực sự truy cập (tối ưu). EAGER: load ngay. Ví dụ `@ManyToOne(fetch = FetchType.LAZY)` ở Product.category.

### Q14: Hàm `getRevenueBetween` tính doanh thu thế nào?
→ `SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.orderDate BETWEEN start AND end`. `COALESCE` trả về 0 nếu SUM NULL.

### Q15: Biểu đồ doanh thu vẽ bằng gì?
→ Chart.js (thư viện JavaScript). Controller trả về danh sách 7 ngày (label + revenue). View dùng `<canvas>` + Chart.js để vẽ biểu đồ cột.

---

## Tài khoản mặc định

| Tài khoản | Mật khẩu | Vai trò |
|-----------|----------|---------|
| admin | 123456 | ADMIN |
| nguyenvana | 123456 | CUSTOMER |
| tranthib | 123456 | CUSTOMER |
