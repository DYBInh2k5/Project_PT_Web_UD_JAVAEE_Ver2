# Demo thuyết trình — BookStore (Web bán sách trực tuyến)

> Spring Boot MVC + Thymeleaf + SQL Server  
> Gothic Moderno Dark Theme

---

## Mục lục

1. [Giới thiệu](#1-giới-thiệu)
2. [Công nghệ & Kiến trúc](#2-công-nghệ--kiến-trúc)
3. [Cài đặt & Chạy ứng dụng](#3-cài-đặt--chạy-ứng-dụng)
4. [Demo Khách hàng](#4-demo-khách-hàng)
5. [Demo Admin](#5-demo-admin)
6. [Cấu trúc mã nguồn](#6-cấu-trúc-mã-nguồn)
7. [Kết luận](#7-kết-luận)

---

## 1. Giới thiệu

**BookStore** là ứng dụng web bán sách trực tuyến, cho phép:
- **Khách hàng**: xem sách, tìm kiếm, lọc danh mục, sắp xếp giá, quản lý giỏ hàng, thanh toán, xem lịch sử đơn hàng.
- **Admin**: quản lý sản phẩm (CRUD + upload ảnh), quản lý đơn hàng (cập nhật trạng thái), thống kê doanh thu (ngày/tháng/năm + biểu đồ cột 7 ngày).

Giao diện thiết kế theo phong cách **Gothic Moderno** — tối, huyền bí, sang trọng.

---

## 2. Công nghệ & Kiến trúc

| Thành phần | Công nghệ |
|-----------|-----------|
| **Backend** | Spring Boot 3.2.0, Spring MVC, Spring Data JPA, Spring Security |
| **Frontend** | Thymeleaf, HTML5, CSS3, Bootstrap 5, Chart.js, Font Awesome |
| **Database** | SQL Server (`localhost\SQLEXPRESS:1433`) |
| **Build** | Maven 3.9.9, Java 21 |

**Kiến trúc 3-layer**:
```
Controller (xử lý request HTTP)
    ↕
Service (logic nghiệp vụ)
    ↕
Repository (truy vấn database qua JPA)
```

**Bảo mật**: Spring Security — form login, phân quyền ADMIN/CUSTOMER, BCrypt cho mật khẩu.

---

## 3. Cài đặt & Chạy ứng dụng

### 3.1. Yêu cầu máy tính
- Java 17+ (khuyến nghị Java 21)
- Maven 3.8+
- SQL Server (đã cài `localhost\SQLEXPRESS`)
- Trình duyệt web (Chrome/Edge/Firefox)

### 3.2. Tạo database + seed dữ liệu

Bước đầu tiên: mở **SQL Server Management Studio (SSMS)**, đăng nhập với tài khoản `sa` / `123456`.

Mở file `database.sql` (File → Open → File → chọn `database.sql`) và chạy toàn bộ (Execute F5).

Script sẽ tạo:
- Database `BookStore` (nếu chưa có)
- 7 bảng: `users`, `categories`, `products`, `carts`, `cart_items`, `orders`, `order_details`
- 3 tài khoản: admin + 2 khách hàng
- 8 danh mục, 35 sản phẩm (có ảnh), 6 đơn hàng mẫu

> Tất cả mật khẩu đều là **123456** (đã mã hóa BCrypt)

### 3.3. Chạy ứng dụng

```powershell
# Mở Terminal tại thư mục BookStore/
cd "D:\HSU\2533Semester 3(2025-2026)\PT web java EE\Project\BookStore"

# Chạy (chọn 1 trong 2 cách):
# Cách 1 — thêm Maven vào PATH tạm:
$env:Path += ";$env:USERPROFILE\.m2\apache-maven-3.9.9\bin"
mvn spring-boot:run

# Cách 2 — gọi trực tiếp:
& "$env:USERPROFILE\.m2\apache-maven-3.9.9\bin\mvn.cmd" spring-boot:run
```

Kết quả:
```
Started BookStoreApplication in 12.368 seconds
Tomcat started on port 8080 (http)
```

### 3.4. Tắt ứng dụng

```powershell
# Nhấn Ctrl+C trong terminal. Nếu bị treo port:
netstat -ano | findstr :8080
taskkill /F /PID <PID>
```

### 3.5. Tài khoản

| Vai trò | Tên đăng nhập | Mật khẩu |
|---------|---------------|----------|
| **Admin** | `admin` | `123456` |
| Khách hàng | `nguyenvana` | `123456` |
| Khách hàng | `tranthib` | `123456` |

---

## 4. Demo Khách hàng

> Mở trình duyệt, truy cập: **http://localhost:8080**

### 4.1. Trang chủ

**Thao tác**: Mở `http://localhost:8080` → thấy ngay trang chủ.

**Nội dung hiển thị**:
- **Hero banner**: ảnh nền Gothic, tiêu đề "BookStore - Nơi tri thức gặp gỡ bóng tối", nút "Khám phá ngay".
- **Danh mục nổi bật**: 6 ô danh mục (Văn học Việt Nam, Văn học nước ngoài, Khoa học, Kinh tế, Triết học, Lịch sử).
- **Sách mới nhất**: grid 4 cột, mỗi sách có ảnh, tên, tác giả, giá, badge "Mới", nút "Thêm giỏ hàng".

**Ý nghĩa**: Giới thiệu tổng quan về cửa hàng, tạo ấn tượng với Gothic theme.

### 4.2. Xem danh sách sản phẩm (phân trang)

**Thao tác**: Click **"Danh mục"** trên navbar → vào `/shop` HOẶC click **"Xem tất cả →"** trong footer.

**Nội dung hiển thị**:
- **Sidebar trái**: danh sách danh mục để lọc, ô tìm kiếm, dropdown sắp xếp giá.
- **Grid sản phẩm**: 8 sản phẩm/trang (hoặc cấu hình khác), mỗi sản phẩm có ảnh, tên, tác giả, giá, tồn kho, nút "Thêm giỏ hàng".
- **Phân trang**: dưới cùng — các nút số trang, "Trước", "Sau".

**Ý nghĩa**: Minh họa phân trang — dữ liệu được chia nhỏ để hiển thị, tránh load quá nhiều.

> Mở DevTools (F12) → tab Network → reload để thấy request có tham số `?page=1`.

### 4.3. Tìm kiếm sản phẩm theo tên

**Thao tác**: 
- Cách 1: Gõ tên sách vào ô tìm kiếm trên navbar → Enter.
- Cách 2: Vào `/shop` → gõ vào ô tìm kiếm bên sidebar → Enter.

**Ví dụ**: Gõ `"tình"` → Enter → chỉ hiện sách có chứa chữ "tình" trong tên.

**Ý nghĩa**: Tìm kiếm không phân biệt hoa/thường, dùng `LIKE %keyword%` trong JPQL.

### 4.4. Lọc theo danh mục

**Thao tạo**: Vào `/shop` → click vào một danh mục bên sidebar trái.

**Ví dụ**: Click **"Khoa học"** → chỉ hiện sách thuộc danh mục Khoa học.

**Ý nghĩa**: Lọc kết hợp với tìm kiếm — vừa gõ tên vừa chọn danh mục để tìm chính xác hơn.

### 4.5. Sắp xếp theo giá

**Thao tác**: Vào `/shop` → chọn dropdown sắp xếp → chọn "Giá: Tăng dần" hoặc "Giá: Giảm dần".

**Ví dụ**: Chọn **"Giá: Tăng dần"** → sách rẻ nhất lên đầu, đắt nhất xuống cuối. Chọn **"Giá: Giảm dần"** → ngược lại.

**Ý nghĩa**: Sắp xếp ở tầng Service bằng `Sort.by("price").ascending()`, kết hợp với phân trang.

### 4.6. Đăng ký tài khoản

**Thao tác**: Click **"Đăng ký"** trên navbar (hoặc `http://localhost:8080/register`).

**Form đăng ký**:
- Tên đăng nhập (bắt buộc, 3-50 ký tự)
- Mật khẩu (bắt buộc, tối thiểu 6 ký tự)
- Họ tên (bắt buộc)
- Email, Số điện thoại, Địa chỉ (không bắt buộc)

**Ví dụ demo**: 
1. Nhập username: `demouser`, password: `123456`, Họ tên: `Người dùng demo`
2. Click **"Đăng ký"**
3. Thành công → redirect về trang đăng nhập, thông báo "Đăng ký thành công"

**Trường hợp lỗi**: Nhập username đã tồn tại (`admin`) → form hiển thị lỗi "Username đã tồn tại".

**Ý nghĩa**: Validation Jakarta (`@NotBlank`, `@Size`), kiểm tra username trùng, BCrypt cho password.

### 4.7. Đăng nhập / Đăng xuất

**Đăng nhập**:
1. Click **"Đăng nhập"** trên navbar (hoặc `http://localhost:8080/login`)
2. Nhập `nguyenvana` / `123456`
3. Click **"Đăng nhập"**
4. Thành công → redirect về trang chủ, navbar hiện tên user + nút Đăng xuất

**Đăng xuất**:
1. Click **"Đăng xuất"**
2. Redirect về trang chủ, navbar trở về trạng thái chưa đăng nhập

**Trường hợp sai mật khẩu**: Hiển thị lỗi "Sai tên đăng nhập hoặc mật khẩu".

**Ý nghĩa**: Spring Security `UsernamePasswordAuthenticationFilter`, session-based auth.

### 4.8. Giỏ hàng — Thêm sản phẩm

**Thao tác (đã đăng nhập)**:
1. Vào `/shop` hoặc trang chủ
2. Click **"Thêm giỏ hàng"** trên một sách bất kỳ
3. Thông báo toast "Đã thêm vào giỏ hàng" (xanh)

**Kiểm tra**: Click icon giỏ hàng trên navbar → vào `/cart` → thấy sản phẩm vừa thêm.

**Thêm trùng sản phẩm**: Click "Thêm giỏ hàng" lần nữa cho cùng sách → số lượng tăng lên 2.

**Ý nghĩa**: Service kiểm tra sản phẩm đã có trong giỏ chưa — nếu có thì tăng số lượng, nếu chưa thì tạo CartItem mới.

### 4.9. Giỏ hàng — Cập nhật số lượng & Xóa

**Thao tác**: Vào `/cart` → thấy bảng danh sách sản phẩm.

**Cập nhật số lượng**:
1. Sửa số lượng trong ô input (VD: 2 → 5)
2. Click nút **"Cập nhật"** bên cạnh
3. Thành tiền và tổng tiền cập nhật ngay

**Xóa sản phẩm**:
1. Click nút **"Xóa"** (icon thùng rác) trên một dòng
2. Sản phẩm biến mất khỏi giỏ, tổng tiền giảm

**Xóa về 0**: Nếu sửa số lượng thành 0 hoặc âm → tự động xóa sản phẩm đó.

**Ý nghĩa**: CRUD đầy đủ trên CartItem — cascade từ Cart giúp tự động đồng bộ.

### 4.10. Thanh toán (Checkout)

**Thao tác**:
1. Đã có sản phẩm trong giỏ → click **"Thanh toán"** (hoặc vào `/cart/checkout`)

> **Yêu cầu đăng nhập**: Nếu chưa login → redirect về `/login`, sau khi login xong → redirect về checkout.

2. Form thanh toán hiển thị:
   - **Giỏ hàng** (readonly): danh sách sản phẩm, tổng tiền
   - **Thông tin người nhận**: Họ tên, Số điện thoại, Địa chỉ
3. Nhập thông tin (hoặc để mặc định)
4. Click **"Xác nhận đặt hàng"**

**Kết quả**:
- Thông báo "Đặt hàng thành công!"
- Giỏ hàng trống (tất cả CartItem bị xóa)
- Tồn kho sản phẩm giảm tương ứng
- Redirect về lịch sử đơn hàng

**Trường hợp hết hàng**: Nếu số lượng trong giỏ > tồn kho → throw RuntimeException, rollback transaction.

**Ý nghĩa**: `@Transactional` — tất cả các bước (tạo Order, tạo OrderDetail, trừ stock, xóa giỏ) là 1 atomic transaction.

### 4.11. Lịch sử đơn hàng

**Thao tác**: Click **"Đơn hàng"** trên navbar → vào `/order/history`.

**Hiển thị**:
- Bảng danh sách đơn hàng (mã đơn, ngày đặt, tổng tiền, trạng thái với badge màu)
- Nút "Chi tiết" cho mỗi đơn
- Phân trang

**Xem chi tiết**: Click **"Chi tiết"** → vào `/order/detail/{id}`:
- Thông tin đơn hàng (mã, ngày, trạng thái)
- Thông tin người nhận
- Bảng sản phẩm (ảnh, tên, giá, số lượng, thành tiền)
- Tổng tiền

**Bảo mật**: Controller kiểm tra user sở hữu đơn hàng — nếu không phải chủ → redirect về lịch sử.

**Ý nghĩa**: Phân trang cho đơn hàng, bảo mật ownership check.

---

## 5. Demo Admin

### 5.1. Đăng nhập Admin

**Thao tác**:
1. Click **Đăng xuất** nếu đang đăng nhập khách
2. Click **Đăng nhập** → nhập `admin` / `123456`
3. Sau khi login, navbar hiện thêm mục **"Admin"** (chỉ hiện với role ADMIN)

> Nếu truy cập `/admin` khi chưa login → redirect về `/login`.  
> Nếu truy cập `/admin` với role CUSTOMER → HTTP 403 Forbidden.

**Ý nghĩa**: Spring Security filter chain — `requestMatchers("/admin/**").hasRole("ADMIN")`.

### 5.2. Dashboard Admin

**Thao tác**: Click **"Admin"** trên navbar → vào `/admin`.

**Hiển thị**: Trang dashboard với 3 nút điều hướng:
- **Quản lý sản phẩm** → `/admin/products`
- **Quản lý đơn hàng** → `/admin/orders`
- **Xem doanh thu** → `/admin/orders/revenue`

### 5.3. Quản lý sản phẩm — Danh sách

**Thao tác**: Vào `/admin/products`.

**Hiển thị**:
- **Bộ lọc**: tìm theo tên + chọn danh mục
- **Nút "Thêm sản phẩm"**: màu đỏ Blood Red
- **Bảng sản phẩm**: 10 sản phẩm/trang — ảnh thumbnail, tên, tác giả, giá, tồn kho, danh mục, nút Sửa / Xóa
- **Phân trang**: dưới cùng

### 5.4. Quản lý sản phẩm — Thêm mới

**Thao tác**:
1. Click **"Thêm sản phẩm"**
2. Form:
   - Tên sách, Tác giả
   - Danh mục (dropdown)
   - Giá, Số lượng
   - Mô tả (textarea)
   - Upload ảnh (chọn file .jpg/.png)
3. Click **"Lưu"**

**Kết quả**: Sản phẩm mới xuất hiện trong danh sách, ảnh hiển thị ngay.

**Cơ chế upload ảnh**: File được copy vào `target/classes/static/images/` với tên UUID + tên gốc (tránh trùng). Database chỉ lưu tên file.

### 5.5. Quản lý sản phẩm — Sửa & Xóa

**Sửa**:
1. Click nút **"Sửa"** (icon bút chì) trên một sản phẩm
2. Form hiển thị dữ liệu cũ, có preview ảnh hiện tại
3. Sửa thông tin, chọn ảnh mới (hoặc để ảnh cũ)
4. Click **"Cập nhật"**

**Xóa**:
1. Click nút **"Xóa"** (icon thùng rác) trên một sản phẩm
2. Sản phẩm biến mất khỏi danh sách
3. File ảnh trên disk cũng bị xóa

**Ý nghĩa**: Khi xóa sản phẩm, controller gọi `Files.delete()` để xóa ảnh trên disk trước khi xóa khỏi DB.

### 5.6. Quản lý đơn hàng

**Thao tác**: Vào `/admin/orders`.

**Hiển thị**: Bảng danh sách đơn hàng (phân trang) — mã đơn, khách hàng, ngày đặt, tổng tiền, trạng thái.

**Cập nhật trạng thái**:
1. Mỗi đơn hàng có dropdown trạng thái: `NEW`, `SHIPPED`, `PAID`
2. Chọn trạng thái mới (VD: `SHIPPED`)
3. Click **"Cập nhật"**
4. Badge trạng thái chuyển màu tương ứng:
   - `NEW`: badge vàng
   - `SHIPPED`: badge xanh dương
   - `PAID`: badge xanh lá

**Ý nghĩa**: Quy trình đơn hàng cơ bản — tiếp nhận (NEW) → vận chuyển (SHIPPED) → hoàn tất (PAID).

### 5.7. Xem doanh thu

**Thao tác**: Vào menu **"Doanh thu"** (hoặc `/admin/orders/revenue`).

**Bước 1**: Trang hiển thị form chọn ngày.

**Bước 2**: Chọn một ngày trong quá khứ (VD: ngày hôm qua hoặc 17/06/2026) → Click **"Xem"**.

**Kết quả** (sau POST):
- **Doanh thu ngày**: tổng tiền của các đơn hàng trong ngày đã chọn (định dạng `#.### VNĐ`)
- **Doanh thu tháng**: tổng tiền của tháng chứa ngày đã chọn
- **Doanh thu năm**: tổng tiền của năm chứa ngày đã chọn
- **Biểu đồ cột 7 ngày**: Chart.js bar chart hiển thị doanh thu từng ngày trong 7 ngày gần nhất (từ ngày chọn trở về trước). Trục X là nhãn ngày (VD: "11/6", "12/6"...), trục Y là doanh thu (VNĐ). Hover vào cột → tooltip hiển thị chính xác số tiền.

> **Lưu ý**: Nếu chọn ngày không có đơn hàng → doanh thu = 0, biểu đồ vẫn hiển thị cột 0.

**Cơ chế kỹ thuật**:
1. Controller nhận date → gọi 4 method của `OrderService`
2. `getRevenueByDay`, `getRevenueByMonth`, `getRevenueByYear` → `OrderRepository.getRevenueBetween()` với JPQL `SUM(o.totalAmount)`
3. `getDailyRevenue7Days` → native SQL `GROUP BY CAST(order_date AS DATE)`, kết quả map vào HashMap, tạo 7 mục (ngày không có đơn → revenue = 0), trả về `List<Map<String, Object>>` với key "label" và "revenue"
4. Controller dùng Jackson để serialize labels và values thành JSON strings → gửi xuống template
5. Template dùng `th:inline="text"` với `JSON.parse()` để khởi tạo Chart.js

---

## 6. Cấu trúc mã nguồn

### 6.1. Sơ đồ tổng thể

```
BookStore/
├── src/main/java/com/bookstore/
│   ├── BookStoreApplication.java          # @SpringBootApplication — điểm khởi đầu
│   ├── config/
│   │   ├── SecurityConfig.java            # Bảo mật: login, phân quyền, BCrypt
│   │   └── WebMvcConfig.java              # Cấu hình static resources
│   ├── controller/                        # 7 controllers
│   │   ├── HomeController.java            # GET /, /shop — public
│   │   ├── AuthController.java            # GET/POST login, register
│   │   ├── CartController.java            # Giỏ hàng + checkout
│   │   ├── OrderController.java           # Lịch sử đơn hàng
│   │   └── admin/
│   │       ├── AdminController.java       # Dashboard
│   │       ├── ProductController.java     # CRUD sản phẩm + upload ảnh
│   │       └── OrderManageController.java # Quản lý đơn + doanh thu
│   ├── dto/                               # 2 DTOs
│   │   ├── RegisterDto.java              # Validation form đăng ký
│   │   └── RevenueDto.java               # Dữ liệu doanh thu
│   ├── entity/                            # 7 entities
│   │   ├── User.java, Product.java, Category.java
│   │   ├── Cart.java, CartItem.java
│   │   └── Order.java, OrderDetail.java
│   ├── repository/                        # 7 JPA repositories
│   │   └── OrderRepository.java           # Thống kê doanh thu (JPQL + native SQL)
│   └── service/                           # 6 services
│       └── OrderService.java              # Logic đơn hàng + doanh thu
├── src/main/resources/
│   ├── application.properties             # Cấu hình DB, JPA, upload, Thymeleaf
│   ├── static/
│   │   ├── css/style.css                  # Gothic Moderno theme (600+ dòng)
│   │   ├── images/                        # 39 ảnh sản phẩm (book1.jpg - book39.jpg)
│   │   └── js/
│   │       ├── main.js                    # JS chính (Chart.js, tooltip...)
│   │       └── chart.umd.min.js           # Chart.js (local, không phụ thuộc CDN)
│   └── templates/                         # 14 Thymeleaf templates
│       ├── layout.html                    # Layout chung (head, navbar, footer, scripts)
│       ├── index.html, shop.html          # Trang chủ, cửa hàng
│       ├── login.html, register.html      # Đăng nhập, đăng ký
│       ├── cart.html, checkout.html       # Giỏ hàng, thanh toán
│       ├── order-history.html, order-detail.html  # Lịch sử đơn hàng
│       └── admin/
│           ├── dashboard.html             # Dashboard
│           ├── products.html, product-form.html   # CRUD sản phẩm
│           ├── orders.html                # Quản lý đơn hàng
│           └── revenue.html               # Thống kê doanh thu + Chart.js
├── database.sql                           # Script tạo DB + seed dữ liệu
└── README.md / YEU_CAU.md                 # Tài liệu dự án
```

### 6.2. Luồng xử lý chính

**Luồng mua hàng**:
```
Khách xem sản phẩm → Thêm vào giỏ → Đăng nhập → Checkout → Tạo đơn hàng
                                                                    ↓
                                                            Trừ stock
                                                                    ↓
                                                            Xóa giỏ hàng
                                                                    ↓
                                                     Redirect → lịch sử đơn
```

**Luồng admin**:
```
Admin login → Dashboard → Quản lý sản phẩm (CRUD)
                        → Quản lý đơn hàng (cập nhật trạng thái)
                        → Xem doanh thu (chọn ngày → biểu đồ)
```

**Luồng doanh thu (quan trọng)**:
```
Form chọn ngày → POST /admin/orders/revenue
    → OrderManageController.revenue()
        → getRevenueByDay()     → JPQL SUM WHERE orderDate BETWEEN
        → getRevenueByMonth()   → JPQL SUM
        → getRevenueByYear()    → JPQL SUM
        → getDailyRevenue7Days() → native SQL GROUP BY date + HashMap 7 ngày
    → Jackson serialize labels/values → JSON strings
    → Template: th:inline="text" + JSON.parse → Chart.js bar chart
```

### 6.3. Database

**7 bảng** quan hệ:
```
users ──1:1── carts ──1:n── cart_items ──n:1── products
  │                                        n:1
  └──1:n── orders ──1:n── order_details ────┘
                    │
              categories ──1:n── products
```

**Seed data**: 3 user, 8 danh mục, 35 sản phẩm, 6 đơn hàng + 11 chi tiết.

### 6.4. Bảo mật

- **Phân quyền URL**: `/admin/**` → ADMIN, `/cart/**` và `/order/**` → đã đăng nhập, còn lại → public.
- **Mật khẩu**: BCrypt (12 rounds).
- **CSRF**: Bật mặc định, Thymeleaf tự động thêm `_csrf` hidden field.
- **Ownership check**: Chi tiết đơn hàng — kiểm tra user sở hữu đơn.

---

## 7. Kết luận

### Những gì đã làm được

| Yêu cầu | Trạng thái |
|---------|-----------|
| 1 admin + 2 khách hàng | ✅ 3 user trong database.sql |
| 30+ sản phẩm, mỗi sản phẩm có ảnh | ✅ 35 sản phẩm, 39 ảnh |
| Đăng ký / Đăng nhập / Đăng xuất | ✅ Spring Security + BCrypt |
| Xem sản phẩm + phân trang | ✅ 8-10 sản phẩm/trang |
| Tìm kiếm theo tên | ✅ JPQL LIKE + không phân biệt hoa/thường |
| Lọc theo danh mục | ✅ Kết hợp với tìm kiếm |
| Sắp xếp theo giá (tăng/giảm) | ✅ Sort.by("price") |
| Giỏ hàng (thêm/sửa/xóa) | ✅ CRUD trên CartItem |
| Thanh toán (yêu cầu login) | ✅ @Transactional + trừ stock |
| Lịch sử đơn hàng + chi tiết | ✅ Ownership check |
| Admin CRUD sản phẩm + upload ảnh | ✅ UUID filename + xóa ảnh disk |
| Admin quản lý đơn hàng + cập nhật trạng thái | ✅ NEW → SHIPPED → PAID |
| Admin xem doanh thu (ngày/tháng/năm + biểu đồ 7 ngày) | ✅ Chart.js bar chart |
| Giao diện Gothic Moderno | ✅ Onyx Black, Blood Red, Silver |
| Comment tiếng Việt toàn bộ code | ✅ Tất cả file Java, template, CSS, JS |
| Seed dữ liệu bằng SQL thuần | ✅ database.sql |

### Hạn chế & Hướng phát triển

- Chưa có chức năng quên mật khẩu / đổi mật khẩu
- Chưa có giỏ hàng persistent khi chưa login (lưu trong session)
- Chưa có thống kê dashboard (tổng đơn, tổng user, sản phẩm bán chạy)
- Chưa có API REST (chỉ MVC thuần)
- Chart.js load local — có thể cache để tối ưu tốc độ

---

> **Sinh viên**: [Tên sinh viên]  
> **Môn học**: PT Web Java EE  
> **Mã đề tài**: Web bán sách trực tuyến
