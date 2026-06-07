# BookStore - Web bán sách trực tuyến

## Giới thiệu
Ứng dụng web bán sách trực tuyến xây dựng bằng Spring Boot MVC + Thymeleaf + SQL Server.
Giao diện **Gothic Moderno** theme — tối, huyền bí, sang trọng (Onyx Black, Blood Red, Silver).

## Công nghệ
| Thành phần | Công nghệ |
|-----------|-----------|
| Backend | Spring Boot 3.2.0, Spring MVC, Spring Data JPA, Spring Security |
| Frontend | Thymeleaf, HTML5, CSS3, Bootstrap 5, Chart.js, Font Awesome |
| Database | SQL Server (localhost\SQLEXPRESS:1433) |
| Build | Maven 3.9.9, Java 17+ |

## Cấu trúc dự án

```
BookStore/
├── src/main/java/com/bookstore/
│   ├── config/           # Security, WebMvc, DataInitializer
│   ├── controller/
│   │   ├── admin/        # AdminController, ProductController, OrderManageController
│   │   ├── AuthController.java
│   │   ├── CartController.java
│   │   ├── HomeController.java
│   │   └── OrderController.java
│   ├── dto/              # RegisterDto, RevenueDto
│   ├── entity/           # User, Product, Category, Cart, CartItem, Order, OrderDetail
│   ├── repository/       # 7 JPA repositories
│   └── service/          # 6 services
├── src/main/resources/
│   ├── static/
│   │   ├── css/style.css     # Gothic Moderno theme
│   │   ├── images/           # 39 ảnh sản phẩm
│   │   └── js/main.js
│   ├── templates/
│   │   ├── admin/            # dashboard, products, product-form, orders, revenue
│   │   ├── cart.html, checkout.html, index.html, layout.html
│   │   ├── login.html, register.html, shop.html
│   │   └── order-detail.html, order-history.html
│   └── application.properties
├── database.sql          # Script tạo DB + seed data
├── DESIGN.md             # Design system Gothic Moderno
└── YEU_CAU.md            # Yêu cầu chức năng
```

## Chức năng chính

### Khách hàng
- **Đăng ký / Đăng nhập / Đăng xuất** — form-based auth, Spring Security
- **Xem sản phẩm** — phân trang, danh sách lưới có ảnh, giá, tồn kho
- **Tìm kiếm** — theo tên sách và theo loại (danh mục)
- **Sắp xếp** — theo giá tăng dần / giảm dần
- **Giỏ hàng** — thêm, sửa số lượng, xóa, thanh toán (yêu cầu login)
- **Lịch sử đơn hàng** — xem chi tiết từng đơn

### Admin
- **Quản lý sản phẩm** — xem, thêm, sửa, xóa (kèm upload ảnh)
- **Quản lý đơn hàng** — xem, cập nhật trạng thái (NEW, SHIPPED, PAID)
- **Doanh thu** — chọn ngày, xem tổng doanh thu ngày/tháng/năm + biểu đồ cột 7 ngày (Chart.js)

## Database

7 entities: `User`, `Product`, `Category`, `Cart`, `CartItem`, `Order`, `OrderDetail`.

- SQL Server: `localhost\SQLEXPRESS:1433`
- Database: `BookStore`
- User: `sa` / `123456`
- Seed data: 1 admin + 2 customers, 8 categories, 35 products, 6+ orders (tự động insert qua `DataInitializer.java`)

## Cài đặt & Chạy

### Yêu cầu
- Java 17+
- Maven 3.8+
- SQL Server (có sẵn `BookStore` database)

### Bước 1: Tạo database
Chạy script `database.sql` trong SQL Server Management Studio.

### Bước 2: Chạy ứng dụng
```bash
cd BookStore
mvn spring-boot:run
```

### Tắt ứng dụng
Nhấn `Ctrl+C` trong cửa sổ terminal đang chạy Maven. Nếu bị treo port:
```bash
# Tìm PID trên cổng 8080
netstat -ano | findstr :8080
# Kill process (thay PID bằng số tìm được)
taskkill /F /PID <PID>
```

Truy cập: **http://localhost:8080**

### Tài khoản
| Vai trò | Tài khoản | Mật khẩu |
|---------|-----------|----------|
| Admin | `admin` | 123456 |
| Customer | `nguyenvana` | 123456 |
| Customer | `tranthib` | 123456 |

## Thiết kế giao diện
Giao diện theo phong cách **Gothic Moderno**:
- **Màu sắc**: Onyx Black (#1A1A1A), Blood Red (#8B0000), Silver (#C0C0C0)
- **Font chữ**: UnifrakturMaguntia (heading), Inter (body)
- **Hiệu ứng**: border silver, hover scale, shadow glow đỏ, scrollbar tùy chỉnh
- **Responsive**: collapse trên mobile (<768px)

## API Endpoints

### Public
| Method | Path | Mô tả |
|--------|------|-------|
| GET | `/` | Trang chủ |
| GET | `/shop` | Danh sách sản phẩm |
| GET | `/login` | Đăng nhập |
| GET | `/register` | Đăng ký |

### Customer
| Method | Path | Mô tả |
|--------|------|-------|
| POST | `/register` | Xử lý đăng ký |
| GET | `/cart` | Giỏ hàng |
| POST | `/cart/add/{id}` | Thêm vào giỏ |
| POST | `/cart/update/{id}` | Cập nhật số lượng |
| GET | `/cart/remove/{id}` | Xóa khỏi giỏ |
| POST | `/cart/checkout` | Thanh toán |
| GET | `/orders` | Lịch sử đơn hàng |
| GET | `/orders/{id}` | Chi tiết đơn hàng |

### Admin (`/admin/**` — yêu cầu đăng nhập)
| Method | Path | Mô tả |
|--------|------|-------|
| GET | `/admin` | Dashboard |
| GET | `/admin/products` | Danh sách sản phẩm |
| GET | `/admin/products/add` | Form thêm |
| POST | `/admin/products/add` | Thêm sản phẩm |
| GET | `/admin/products/edit/{id}` | Form sửa |
| POST | `/admin/products/edit/{id}` | Cập nhật |
| GET | `/admin/products/delete/{id}` | Xóa sản phẩm |
| GET | `/admin/orders` | Quản lý đơn hàng |
| POST | `/admin/orders/update-status/{id}` | Cập nhật trạng thái |
| GET | `/admin/orders/revenue` | Xem doanh thu |
