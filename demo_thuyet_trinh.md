# Demo thuyết trình — BookStore (Web bán sách trực tuyến)

> **Vừa thao tác → vừa chỉ code**  
> Spring Boot MVC + Thymeleaf + SQL Server — Gothic Moderno Dark Theme

---

## Cách dùng tài liệu này

Mỗi bước demo gồm:
1. **Thao tác** — click vào đâu, nhập gì
2. **Màn hình** — người dùng thấy gì
3. **Code** — đường dẫn `file:line` → mở file đó, chỉ vào dòng code tương ứng

---

## 1. Giới thiệu kiến trúc (trình bày trước khi demo)

```
Trình duyệt  ←→  Controller (7 files)
                       ↕
                  Service (6 files)
                       ↕
                  Repository (7 files)  ←→  SQL Server
```

**Spring Boot MVC 3-layer**:
| Layer | Vai trò | File chính |
|-------|---------|-----------|
| **Controller** | Nhận request HTTP, gọi Service, trả template | `controller/*.java` |
| **Service** | Logic nghiệp vụ (tính toán, kiểm tra) | `service/*.java` |
| **Repository** | Truy vấn database qua JPA/Hibernate | `repository/*.java` |
| **Entity** | Ánh xạ bảng database → Java object | `entity/*.java` |
| **DTO** | Validate + truyền dữ liệu giữa layer | `dto/*.java` |
| **Config** | Bảo mật, cấu hình MVC | `config/*.java` |
| **Template** | Giao diện HTML + Thymeleaf | `templates/*.html` |
| **Static** | CSS, JS, ảnh sản phẩm | `static/**` |

---

## 2. Cài đặt & Chạy (demo thật trên máy)

### 2.1. Tạo database

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Mở SSMS → File → Open → `database.sql` → Execute | SSMS hiện "Commands completed successfully" | `database.sql` — tạo DB + 7 bảng + seed 3 user, 8 danh mục, 35 sản phẩm, 6 đơn hàng |

### 2.2. Chạy ứng dụng

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Terminal → `mvn spring-boot:run` | "Started BookStoreApplication in 12s" "Tomcat started on port 8080" | `BookStoreApplication.java:6` — `@SpringBootApplication`, `SpringApplication.run()` |
| Trình duyệt → `http://localhost:8080` | Trang chủ Gothic Moderno hiện ra | — |

### 2.3. Tài khoản

| Vai trò | Username | Password |
|---------|----------|----------|
| **Admin** | `admin` | `123456` |
| Khách | `nguyenvana` | `123456` |
| Khách | `tranthib` | `123456` |

---

## 3. Demo Khách hàng

### 3.1. Trang chủ

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Mở `http://localhost:8080` | Hero banner + danh mục nổi bật (6 ô) + sách mới nhất (grid 4 cột, badge "Mới") | `HomeController.java:35` — `GET /` gọi `productService.findFiltered()` |
| | | `index.html` — template trang chủ (hero, danh mục, sách mới) |
| | | `layout.html:14` — navbar fragment (logo, tìm kiếm, giỏ hàng) |
| | | `style.css:1-17` — biến màu Gothic (Onyx Black, Blood Red, Silver) |

### 3.2. Danh sách sản phẩm + Phân trang

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Click **"Danh mục"** → vào `/shop` | Grid sản phẩm (8-12 sản phẩm/trang) + phân trang dưới cùng | `HomeController.java:61` — `GET /shop` → `productService.findFiltered(name, category, page, 12, sort)` |
| Click số trang 2, 3... | URL chuyển `?page=1`, `?page=2` — dữ liệu thay đổi | `ProductService.java:35` — `findFiltered()` xử lý tìm kiếm + lọc + sort |
| | | `ProductRepository.java:44` — `@Query findFiltered()` JPQL linh hoạt |
| | | `shop.html` — template: sidebar danh mục + grid sản phẩm + phân trang |

### 3.3. Tìm kiếm theo tên

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Gõ `"tình"` vào ô tìm kiếm → Enter | Chỉ hiện sách có chứa "tình" trong tên | `HomeController.java:41` — name param → `productService.findFiltered()` |
| | | `ProductRepository.java:20` — `findByNameContainingIgnoreCase()` — JPQL LIKE không phân biệt hoa/thường |
| | | `ProductRepository.java:28` — `findByNameContainingIgnoreCaseAndCategoryId()` — kết hợp tìm kiếm + lọc danh mục |

### 3.4. Lọc danh mục

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Click **"Khoa học"** bên sidebar | Chỉ hiện sách danh mục Khoa học | `HomeController.java:67` — category param → `findFiltered()` với `categoryId` |
| Vừa gõ tên vừa chọn danh mục | Kết quả kết hợp cả 2 điều kiện | `ProductService.java:50-56` — switch case: name+category / chỉ name / chỉ category / không filter |
| | | `shop.html` — sidebar `th:each` lặp danh mục, `th:href="@{/shop(category=${cat.id})}"` |

### 3.5. Sắp xếp theo giá

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Chọn dropdown "Giá: Tăng dần" | Sách rẻ nhất lên đầu | `HomeController.java:67` — sort param (`"asc"` / `"desc"`) |
| Chọn "Giá: Giảm dần" | Sách đắt nhất lên đầu | `ProductService.java:38-42` — `Sort.by("price").ascending()` / `.descending()` + `PageRequest.of(page, size, sort)` |
| | | `shop.html` — form GET với select option value="asc"/"desc" |

### 3.6. Đăng ký

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Click **"Đăng ký"** → nhập `demouser` / `123456` | Form validation, redirect về login, thông báo "Đăng ký thành công" | `AuthController.java:40` — `GET /register` trả form |
| Nhập username đã tồn tại (`admin`) | Lỗi "Username đã tồn tại" hiện trên form | `AuthController.java:54` — `POST /register` xử lý |
| | | `RegisterDto.java:13-35` — `@NotBlank`, `@Size(min=3, max=50)` validation |
| | | `UserService.java:31` — `register()`: kiểm tra trùng → BCrypt → set role "CUSTOMER" → save |
| | | `register.html` — form + `th:errors` hiển thị lỗi inline |

### 3.7. Đăng nhập / Đăng xuất

| Thao tác | Màn hình | Code |
|----------|----------|------|
| **Login**: nhập `nguyenvana` / `123456` | Redirect về trang chủ, navbar hiện tên + nút Đăng xuất | `SecurityConfig.java:41` — `SecurityFilterChain`: form login, permitAll |
| **Login sai**: nhập sai mật khẩu | Lỗi "Sai tên đăng nhập hoặc mật khẩu" | `SecurityConfig.java:30` — BCrypt `PasswordEncoder` + `DaoAuthenticationProvider` |
| **Logout**: click **"Đăng xuất"** | Redirect về trang chủ, navbar về trạng thái anonymous | `CustomUserDetailsService.java:33` — `loadUserByUsername()`: tìm user trong DB, trả về `UserDetails` với role |
| | | `login.html` — form login với `th:action="@{/login}"` |
| | | `layout.html:30` — navbar: `sec:authorize="isAuthenticated()"` / `isAnonymous()` |

### 3.8. Giỏ hàng — Thêm

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Click **"Thêm giỏ hàng"** trên một sách | Toast "Đã thêm vào giỏ hàng" | `CartController.java:68` — `POST /cart/add/{productId}` |
| Vào `/cart` | Sản phẩm hiện trong bảng giỏ hàng | `CartController.java:49` — `GET /cart`: gọi `cartService.getOrCreateCart()` |
| Click "Thêm" lần nữa cho cùng sách | Số lượng tăng lên 2 (không trùng dòng) | `CartService.java:55` — `addItem()`: nếu sách đã có trong giỏ → tăng `quantity`, nếu chưa → tạo `CartItem` mới |
| | | `CartService.java:32` — `getOrCreateCart()`: tìm cart theo userId, nếu null → tạo mới |
| | | `Cart.java:16` — entity: `@OneToMany(cascade = ALL, orphanRemoval = true)` tự động đồng bộ |

### 3.9. Giỏ hàng — Sửa / Xóa

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Sửa số lượng → click **Cập nhật** | Thành tiền thay đổi, tổng tiền cập nhật | `CartController.java:90` — `POST /cart/update/{itemId}` |
| Sửa số lượng thành 0 → Cập nhật | Sản phẩm tự động biến mất | `CartService.java:74` — `updateQuantity()`: nếu quantity ≤ 0 → gọi `removeItem()` |
| Click **Xóa** (icon thùng rác) | Sản phẩm biến mất khỏi giỏ | `CartController.java:101` — `POST /cart/remove/{itemId}` |
| | | `CartService.java:91` — `removeItem()`: xóa CartItem theo ID (JPA cascade tự động đồng bộ) |
| | | `cart.html` — bảng giỏ hàng với form cập nhật/xóa từng dòng |

### 3.10. Thanh toán (Checkout)

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Click **"Thanh toán"** | Nếu chưa login → redirect `/login` → login xong → quay lại checkout | `CartController.java:112` — `GET /cart/checkout` (kiểm tra giỏ rỗng) |
| Nhập thông tin người nhận → click **"Xác nhận"** | "Đặt hàng thành công!" → giỏ trống → redirect lịch sử đơn hàng | `CartController.java:134` — `POST /cart/checkout`: gọi `orderService.createOrder()` |
| | | `OrderService.java:46` — `createOrder()`: `@Transactional` — tạo Order → duyệt CartItem → trừ stock → tạo OrderDetail → save Order → clearCart |
| | | `Order.java:36` — `@PrePersist` tự động set `orderDate` và `status = "NEW"` |
| | | `checkout.html` — form thông tin giao hàng + tóm tắt đơn hàng |

### 3.11. Lịch sử đơn hàng

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Click **"Đơn hàng"** trên navbar | Bảng danh sách đơn + phân trang + badge trạng thái màu | `OrderController.java:45` — `GET /order/history`: gọi `orderService.findByUser()` phân trang |
| Click **"Chi tiết"** trên một đơn | Thông tin đơn + người nhận + bảng sản phẩm + tổng tiền | `OrderController.java:65` — `GET /order/detail/{id}`: kiểm tra ownership (nếu không phải chủ → redirect) |
| | | `order-history.html` — bảng + badge NEW(vàng)/SHIPPED(xanh dương)/PAID(xanh lá) |
| | | `order-detail.html` — thông tin + bảng sản phẩm |

---

## 4. Demo Admin

### 4.1. Đăng nhập Admin

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Đăng nhập `admin` / `123456` | Navbar hiện thêm menu **"Admin"** | `SecurityConfig.java:46` — `.requestMatchers("/admin/**").hasRole("ADMIN")` |
| Vào `http://localhost:8080/admin` trực tiếp | Nếu chưa login → redirect `/login` | `SecurityConfig.java:48` — `.loginPage("/login")` |
| Login với role CUSTOMER vào `/admin` | HTTP 403 Forbidden | — |

### 4.2. Dashboard

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Click **"Admin"** trên navbar → `/admin` | Dashboard với 3 nút: Quản lý sản phẩm / Đơn hàng / Doanh thu | `AdminController.java:17` — `GET /admin` → `admin/dashboard.html` |
| | | `admin/dashboard.html` — Gothic style cards với icon |

### 4.3. Quản lý sản phẩm — Danh sách

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Vào `/admin/products` | Bảng sản phẩm (10/trang) + filter tên/danh mục + nút Thêm/Sửa/Xóa + phân trang | `ProductController.java:46` — `GET /admin/products`: `productService.findFiltered(name, catId, page, 10, null)` |
| | | `admin/products.html` — bảng + filter form + phân trang |

### 4.4. Quản lý sản phẩm — Thêm (kèm upload ảnh)

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Click **"Thêm sản phẩm"** → điền form → chọn file ảnh → **Lưu** | Sản phẩm mới xuất hiện trong danh sách, ảnh hiển thị | `ProductController.java:66` — `GET /admin/products/add` → form |
| | | `ProductController.java:82` — `POST /admin/products/add`: lưu product + xử lý upload ảnh |
| | | `ProductController.java:169` — `saveImage()`: tạo UUID filename, copy file vào `target/classes/static/images/` |
| | | `ProductController.java:25` — `UPLOAD_DIR = System.getProperty("user.dir") + "/target/classes/static/images/"` |
| | | `admin/product-form.html` — form với input type="file" |
| | | `application.properties:20` — `spring.servlet.multipart.max-file-size=10MB` |

### 4.5. Quản lý sản phẩm — Sửa & Xóa

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Click **Sửa** → sửa thông tin → chọn ảnh mới (hoặc giữ ảnh cũ) → **Cập nhật** | Thông tin sản phẩm thay đổi | `ProductController.java:105` — `GET /admin/products/edit/{id}`: load sản phẩm + preview ảnh |
| | | `ProductController.java:122` — `POST /admin/products/edit/{id}`: update thông tin + xử lý ảnh mới (nếu có) |
| Click **Xóa** | Sản phẩm biến mất, ảnh trên disk cũng bị xóa | `ProductController.java:154` — `GET /admin/products/delete/{id}`: xóa file ảnh → xóa DB |

### 4.6. Quản lý đơn hàng

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Vào `/admin/orders` | Bảng đơn hàng (phân trang) — mã, khách, ngày, tổng tiền, trạng thái + dropdown cập nhật | `OrderManageController.java:35` — `GET /admin/orders`: `orderService.findAll(page, 10)` |
| Chọn trạng thái mới (VD: SHIPPED) → click **Cập nhật** | Badge chuyển màu tương ứng | `OrderManageController.java:49` — `POST /admin/orders/update-status` |
| | | `OrderService.java:107` — `updateStatus()`: set status mới → save |
| | | `admin/orders.html` — bảng + dropdown + badge theo status |

### 4.7. Xem doanh thu + Biểu đồ Chart.js

| Thao tác | Màn hình | Code |
|----------|----------|------|
| Vào menu **"Doanh thu"** → `/admin/orders/revenue` | Form chọn ngày | `OrderManageController.java:61` — `GET /admin/orders/revenue` (chưa có data) |
| Chọn ngày → click **"Xem"** | 3 thẻ: doanh thu ngày / tháng / năm + biểu đồ cột 7 ngày | `OrderManageController.java:77` — `POST /admin/orders/revenue`: xử lý |
| | | `OrderService.java:120` — `getRevenueByDay()` → `OrderRepository.getRevenueBetween()` JPQL `SUM` |
| | | `OrderService.java:131` — `getRevenueByMonth()` → JPQL `SUM` theo tháng |
| | | `OrderService.java:141` — `getRevenueByYear()` → JPQL `SUM` theo năm |
| | | `OrderService.java:159` — `getDailyRevenue7Days()`: native SQL `GROUP BY CAST(order_date AS DATE)` + HashMap 7 ngày |
| | | `OrderRepository.java:36` — `@Query "SELECT COALESCE(SUM(o.totalAmount),0) FROM Order o WHERE o.orderDate BETWEEN :start AND :end"` |
| | | `OrderRepository.java:49` — `@Query(value = "SELECT CAST(o.order_date AS DATE), SUM(o.total_amount) FROM orders o ... GROUP BY CAST(o.order_date AS DATE)", nativeQuery = true)` |
| | | `RevenueDto.java:14-25` — 4 fields: dayRevenue, monthRevenue, yearRevenue, dailyRevenueList |
| Hover vào cột biểu đồ | Tooltip hiển thị số tiền chính xác (VNĐ) | `OrderManageController.java:85-91` — Jackson `ObjectMapper` serialize labels/values → JSON strings |
| | | `admin/revenue.html:86` — `<script th:inline="text">` + `JSON.parse('${labelsJson}')` khởi tạo Chart.js |
| | | `layout.html:10` — `<script th:src="@{/js/chart.umd.min.js}">` — Chart.js local |
| | | `chart.umd.min.js` — thư viện Chart.js (bar chart) |

---

## 5. Cấu trúc mã nguồn (trình bày tổng quan)

```
BookStore/
├── src/main/java/com/bookstore/
│   ├── BookStoreApplication.java          # @SpringBootApplication
│   ├── config/
│   │   ├── SecurityConfig.java            # Bảo mật: login, phân quyền, BCrypt
│   │   └── WebMvcConfig.java              # Cấu hình static resources
│   ├── controller/                        # 7 controllers
│   │   ├── HomeController.java            # GET /, /shop
│   │   ├── AuthController.java            # GET/POST login, register
│   │   ├── CartController.java            # Giỏ hàng + checkout
│   │   ├── OrderController.java           # Lịch sử đơn hàng
│   │   └── admin/
│   │       ├── AdminController.java       # Dashboard
│   │       ├── ProductController.java     # CRUD sản phẩm + upload ảnh
│   │       └── OrderManageController.java # Quản lý đơn + doanh thu
│   ├── dto/
│   │   ├── RegisterDto.java              # Validation form đăng ký
│   │   └── RevenueDto.java               # Dữ liệu doanh thu
│   ├── entity/                            # 7 entities
│   │   ├── User.java, Product.java, Category.java
│   │   ├── Cart.java, CartItem.java
│   │   └── Order.java, OrderDetail.java
│   ├── repository/                        # 7 JPA repositories
│   │   └── OrderRepository.java           # Thống kê doanh thu
│   └── service/                           # 6 services
│       └── OrderService.java              # Logic đơn hàng + doanh thu
├── src/main/resources/
│   ├── application.properties             # Cấu hình DB, JPA, upload
│   ├── static/
│   │   ├── css/style.css                  # Gothic Moderno theme
│   │   ├── images/                        # 39 ảnh sản phẩm
│   │   └── js/
│   │       ├── main.js                    # Tooltip
│   │       └── chart.umd.min.js           # Chart.js local
│   └── templates/                         # 14 Thymeleaf templates
│       ├── layout.html                    # Layout chung
│       ├── index.html, shop.html
│       ├── login.html, register.html
│       ├── cart.html, checkout.html
│       ├── order-history.html, order-detail.html
│       └── admin/ (5 files)
├── database.sql                           # Script tạo DB + seed
└── README.md / YEU_CAU.md / DESIGN.md
```

---

## 6. Database

**7 bảng** quan hệ:
```
users ──1:1── carts ──1:n── cart_items ──n:1── products
  │                                        n:1
  └──1:n── orders ──1:n── order_details ────┘
                    │
              categories ──1:n── products
```

**Seed**: 3 user, 8 danh mục, 35 sản phẩm, 6 đơn hàng + 11 chi tiết.

---

## 7. Bảo mật (SecurityConfig)

| Thành phần | Code |
|-----------|------|
| Phân quyền URL | `SecurityConfig.java:46` — `requestMatchers("/admin/**").hasRole("ADMIN")` |
| | `SecurityConfig.java:47` — `requestMatchers("/cart/**", "/order/**").authenticated()` |
| Form login | `SecurityConfig.java:48` — `.loginPage("/login").permitAll()` |
| BCrypt | `SecurityConfig.java:26` — `BCryptPasswordEncoder` |
| Custom auth | `SecurityConfig.java:30` — `DaoAuthenticationProvider` + `CustomUserDetailsService` |
| Logout | `SecurityConfig.java:49` — `.logout().logoutSuccessUrl("/")` |

---

## 8. Kết luận

### Yêu cầu đạt được

| Yêu cầu | File chính |
|---------|-----------|
| 1 admin + 2 khách hàng | `database.sql` — 3 user |
| 30+ sản phẩm, có ảnh | `database.sql` — 35 sản phẩm, `static/images/` — 39 ảnh |
| Đăng ký / Login / Logout | `AuthController.java:54`, `UserService.java:31`, `SecurityConfig.java:41` |
| Xem sản phẩm + phân trang | `HomeController.java:61`, `ProductService.java:35`, `ProductRepository.java:44` |
| Tìm kiếm theo tên | `ProductRepository.java:20` — `findByNameContainingIgnoreCase` |
| Lọc danh mục | `ProductRepository.java:24` — `findByCategoryId` |
| Sắp xếp theo giá | `ProductService.java:38-42` — `Sort.by("price")` |
| Giỏ hàng (thêm/sửa/xóa) | `CartController.java:68/90/101`, `CartService.java:55/74/91` |
| Thanh toán (yêu cầu login) | `CartController.java:134`, `OrderService.java:46` (`@Transactional`) |
| Admin CRUD sản phẩm + upload ảnh | `ProductController.java:82/122/154`, `ProductController.java:169` (saveImage) |
| Admin quản lý đơn hàng | `OrderManageController.java:49`, `OrderService.java:107` |
| Admin doanh thu + biểu đồ | `OrderManageController.java:77`, `OrderService.java:120-159`, `OrderRepository.java:36/49` |

### Hạn chế

- Chưa có quên mật khẩu / đổi mật khẩu
- Chưa có giỏ hàng tạm khi chưa login
- Chưa có thống kê dashboard (sản phẩm bán chạy, tổng đơn...)
- Chưa có API REST

---

> **Sinh viên**: [Tên sinh viên]  
> **Môn học**: PT Web Java EE  
> **Mã đề tài**: Web bán sách trực tuyến  
> **GitHub**: https://github.com/DYBInh2k5/Project_PT_Web_UD_JAVAEE_Ver2
