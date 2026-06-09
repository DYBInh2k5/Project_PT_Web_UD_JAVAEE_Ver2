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
│   ├── BookStoreApplication.java       # Điểm khởi đầu ứng dụng Spring Boot
│   ├── config/                         # Cấu hình ứng dụng
│   │   ├── DataInitializer.java        # Tạo dữ liệu mẫu khi chạy lần đầu
│   │   ├── SecurityConfig.java         # Cấu hình bảo mật (login, phân quyền)
│   │   └── WebMvcConfig.java           # Cấu hình Spring MVC (static resources)
│   ├── controller/
│   │   ├── HomeController.java         # Trang chủ + cửa hàng (public)
│   │   ├── AuthController.java         # Đăng nhập, đăng ký
│   │   ├── CartController.java         # Giỏ hàng (thêm, sửa, xóa, thanh toán)
│   │   ├── OrderController.java        # Lịch sử + chi tiết đơn hàng (khách)
│   │   └── admin/
│   │       ├── AdminController.java    # Dashboard admin
│   │       ├── ProductController.java  # CRUD sản phẩm (admin)
│   │       └── OrderManageController.java  # Quản lý đơn hàng + doanh thu
│   ├── dto/
│   │   ├── RegisterDto.java            # DTO form đăng ký (validation)
│   │   └── RevenueDto.java             # DTO doanh thu (ngày, tháng, năm, biểu đồ)
│   ├── entity/
│   │   ├── User.java                   # Người dùng (admin + customer)
│   │   ├── Product.java                # Sách (sản phẩm)
│   │   ├── Category.java               # Danh mục sách
│   │   ├── Cart.java                   # Giỏ hàng
│   │   ├── CartItem.java               # Món hàng trong giỏ
│   │   ├── Order.java                  # Đơn hàng
│   │   └── OrderDetail.java            # Chi tiết đơn hàng
│   ├── repository/
│   │   ├── UserRepository.java         # Truy vấn user
│   │   ├── ProductRepository.java      # Truy vấn sản phẩm (lọc, tìm kiếm)
│   │   ├── CategoryRepository.java     # Truy vấn danh mục
│   │   ├── CartRepository.java         # Truy vấn giỏ hàng
│   │   ├── CartItemRepository.java     # Truy vấn món hàng trong giỏ
│   │   ├── OrderRepository.java        # Truy vấn đơn hàng + thống kê
│   │   └── OrderDetailRepository.java  # Truy vấn chi tiết đơn
│   └── service/
│       ├── UserService.java            # Logic đăng ký, tra cứu user
│       ├── ProductService.java         # Logic sản phẩm (tìm kiếm, lọc, sort)
│       ├── CategoryService.java        # Logic danh mục
│       ├── CartService.java            # Logic giỏ hàng (thêm, sửa, xóa)
│       ├── OrderService.java           # Logic đơn hàng + doanh thu
│       └── CustomUserDetailsService.java  # Tích hợp Spring Security
├── src/main/resources/
│   ├── application.properties          # Cấu hình ứng dụng (DB, JPA, upload...)
│   ├── static/
│   │   ├── css/style.css               # Gothic Moderno theme CSS
│   │   ├── images/                     # 39 ảnh sản phẩm (book1.jpg - book39.jpg)
│   │   └── js/main.js                  # JavaScript (Chart.js biểu đồ doanh thu)
│   └── templates/
│       ├── layout.html                 # Layout chung (navbar, footer, scripts)
│       ├── index.html                  # Trang chủ (hero, danh mục, sản phẩm mới)
│       ├── shop.html                   # Cửa hàng (lọc, tìm kiếm, phân trang)
│       ├── login.html                  # Đăng nhập
│       ├── register.html               # Đăng ký
│       ├── cart.html                   # Giỏ hàng
│       ├── checkout.html               # Thanh toán
│       ├── order-history.html          # Lịch sử đơn hàng
│       ├── order-detail.html           # Chi tiết đơn hàng
│       └── admin/
│           ├── dashboard.html          # Trang tổng quan admin
│           ├── products.html           # Quản lý sản phẩm
│           ├── product-form.html       # Thêm/sửa sản phẩm
│           ├── orders.html             # Quản lý đơn hàng
│           └── revenue.html            # Thống kê doanh thu + biểu đồ
├── database.sql                        # Script tạo database + seed data
├── DESIGN.md                           # Design system Gothic Moderno
├── YEU_CAU.md                          # Yêu cầu chức năng
└── .gitignore                          # Git ignore (target/, .idea, *.log...)
```

## Chi tiết từng file code

---

### 1. Entity Layer (`entity/`) — Định nghĩa cấu trúc bảng database

#### `User.java`
- **Đường dẫn**: `src/main/java/com/bookstore/entity/User.java`
- **Chức năng**: Ánh xạ bảng `users` trong database. Lưu thông tin tài khoản người dùng.
- **Vai trò**: Admin quản trị hệ thống (role = "ADMIN") và khách hàng (role = "CUSTOMER").
- **Các trường chính**: `id` (khóa chính), `username` (tên đăng nhập, unique), `password` (BCrypt), `fullName`, `email`, `phone`, `address`, `role`, `createdAt`.
- **Quan hệ**: `User` → `Cart` (1-1), `User` → `Order` (1-n).

#### `Product.java`
- **Đường dẫn**: `src/main/java/com/bookstore/entity/Product.java`
- **Chức năng**: Ánh xạ bảng `products`. Lưu thông tin sách.
- **Các trường chính**: `id`, `name` (tên sách), `author` (tác giả), `description` (mô tả), `price` (giá), `stock` (tồn kho), `image` (tên file ảnh), `category` (FK → Category), `createdAt`.
- **Quan hệ**: `Product` → `Category` (n-1), `Product` → `CartItem` (1-n), `Product` → `OrderDetail` (1-n).
- **Đặc biệt**: Ảnh được lưu trong thư mục `static/images/`, database chỉ lưu tên file.

#### `Category.java`
- **Đường dẫn**: `src/main/java/com/bookstore/entity/Category.java`
- **Chức năng**: Ánh xạ bảng `categories`. Lưu danh mục sách (VD: Kinh tế, Văn học, Khoa học...).
- **Các trường chính**: `id`, `name` (tên danh mục, unique), `products` (danh sách sách thuộc danh mục).
- **Quan hệ**: `Category` → `Product` (1-n).

#### `Cart.java`
- **Đường dẫn**: `src/main/java/com/bookstore/entity/Cart.java`
- **Chức năng**: Ánh xạ bảng `carts`. Mỗi user có 1 giỏ hàng duy nhất.
- **Các trường chính**: `id`, `user` (FK → User, unique), `items` (danh sách CartItem).
- **Quan hệ**: `Cart` → `User` (1-1), `Cart` → `CartItem` (1-n, cascade ALL, orphanRemoval).

#### `CartItem.java`
- **Đường dẫn**: `src/main/java/com/bookstore/entity/CartItem.java`
- **Chức năng**: Ánh xạ bảng `cart_items`. Một sản phẩm + số lượng trong giỏ hàng.
- **Các trường chính**: `id`, `cart` (FK → Cart), `product` (FK → Product), `quantity`.
- **Quan hệ**: `CartItem` → `Cart` (n-1), `CartItem` → `Product` (n-1).

#### `Order.java`
- **Đường dẫn**: `src/main/java/com/bookstore/entity/Order.java`
- **Chức năng**: Ánh xạ bảng `orders`. Lưu đơn hàng sau khi khách thanh toán.
- **Các trường chính**: `id`, `user` (FK → User), `orderDate`, `totalAmount`, `status` (NEW/SHIPPED/PAID), `recipientName`, `phone`, `address`, `orderDetails`.
- **Quan hệ**: `Order` → `User` (n-1), `Order` → `OrderDetail` (1-n, cascade ALL, orphanRemoval).
- **Tự động**: `@PrePersist` set `orderDate` và `status = "NEW"` khi tạo.

#### `OrderDetail.java`
- **Đường dẫn**: `src/main/java/com/bookstore/entity/OrderDetail.java`
- **Chức năng**: Ánh xạ bảng `order_details`. Chi tiết từng sản phẩm trong đơn hàng.
- **Các trường chính**: `id`, `order` (FK → Order), `product` (FK → Product), `quantity`, `price` (giá tại thời điểm mua).
- **Quan hệ**: `OrderDetail` → `Order` (n-1), `OrderDetail` → `Product` (n-1).

---

### 2. Repository Layer (`repository/`) — Truy vấn database

#### `UserRepository.java`
- **Đường dẫn**: `src/main/java/com/bookstore/repository/UserRepository.java`
- **Chức năng**: Interface JPA cho bảng `users`.
- **Method quan trọng**:
  - `findByUsername(String)`: Tìm user theo tên đăng nhập (dùng khi login).
  - `existsByUsername(String)`: Kiểm tra username đã tồn tại (dùng khi đăng ký).

#### `ProductRepository.java`
- **Đường dẫn**: `src/main/java/com/bookstore/repository/ProductRepository.java`
- **Chức năng**: Interface JPA cho bảng `products`. Có truy vấn động.
- **Method quan trọng**:
  - `findByNameContainingIgnoreCase(String, Pageable)`: Tìm sản phẩm theo tên (không phân biệt hoa thường).
  - `findByCategoryId(Integer, Pageable)`: Lọc theo danh mục.
  - `findByNameContainingIgnoreCaseAndCategoryId(String, Integer, Pageable)`: Lọc theo cả tên và danh mục.
  - `findFiltered(String, Integer, Pageable)`: Query linh hoạt (@Query JPQL) — cả 2 tham số name và categoryId đều có thể null.

#### `CategoryRepository.java`
- **Đường dẫn**: `src/main/java/com/bookstore/repository/CategoryRepository.java`
- **Chức năng**: Interface JPA cho bảng `categories`. Dùng các method mặc định: `findAll()`, `findById()`, `save()`, `deleteById()`.

#### `CartRepository.java`
- **Đường dẫn**: `src/main/java/com/bookstore/repository/CartRepository.java`
- **Chức năng**: Interface JPA cho bảng `carts`.
- **Method quan trọng**: `findByUserId(Integer)`: Tìm giỏ hàng theo ID người dùng (mỗi user chỉ 1 giỏ).

#### `CartItemRepository.java`
- **Đường dẫn**: `src/main/java/com/bookstore/repository/CartItemRepository.java`
- **Chức năng**: Interface JPA cho bảng `cart_items`.
- **Method quan trọng**:
  - `findByCartIdAndProductId(Integer, Integer)`: Kiểm tra sản phẩm đã có trong giỏ chưa (để tăng số lượng).
  - `deleteByCartId(Integer)`: Xóa toàn bộ giỏ (dùng sau khi thanh toán).

#### `OrderRepository.java`
- **Đường dẫn**: `src/main/java/com/bookstore/repository/OrderRepository.java`
- **Chức năng**: Interface JPA cho bảng `orders`. Có thống kê doanh thu.
- **Method quan trọng**:
  - `findByUserIdOrderByOrderDateDesc(Integer, Pageable)`: Lấy đơn hàng của 1 user, mới nhất trước.
  - `findAllByOrderByOrderDateDesc(Pageable)`: Tất cả đơn hàng (admin), mới nhất trước.
  - `getRevenueBetween(LocalDateTime, LocalDateTime)`: Tính tổng doanh thu trong khoảng thời gian (JPQL SUM).
  - `getDailyRevenue(LocalDateTime, LocalDateTime)`: Doanh thu nhóm theo ngày (native SQL, dùng cho Chart.js).

#### `OrderDetailRepository.java`
- **Đường dẫn**: `src/main/java/com/bookstore/repository/OrderDetailRepository.java`
- **Chức năng**: Interface JPA cho bảng `order_details`. Dùng các method mặc định.

---

### 3. Service Layer (`service/`) — Xử lý logic nghiệp vụ

#### `UserService.java`
- **Đường dẫn**: `src/main/java/com/bookstore/service/UserService.java`
- **Chức năng**: Xử lý đăng ký tài khoản mới và tra cứu user.
- **Method chính**:
  - `register(RegisterDto)`: Kiểm tra username tồn tại → mã hóa password (BCrypt) → set role = "CUSTOMER" → save.
  - `findByUsername(String)`: Tra cứu user (dùng khi login).
  - `findById(Integer)`: Tra cứu theo ID.

#### `ProductService.java`
- **Đường dẫn**: `src/main/java/com/bookstore/service/ProductService.java`
- **Chức năng**: Logic sản phẩm — tìm kiếm, lọc theo danh mục, sắp xếp theo giá.
- **Method chính**:
  - `findAll(int, int)`: Tất cả sản phẩm, phân trang, mới nhất trước.
  - `findFiltered(String, Integer, int, int, String)`: Lọc theo tên + danh mục + sắp xếp giá (asc/desc) + phân trang.
  - `findById(Integer)`: Tìm 1 sản phẩm.
  - `save(Product)`: Thêm hoặc cập nhật.
  - `deleteById(Integer)`: Xóa theo ID.

#### `CategoryService.java`
- **Đường dẫn**: `src/main/java/com/bookstore/service/CategoryService.java`
- **Chức năng**: Logic danh mục.
- **Method chính**: `findAll()`, `findById(Integer)`, `save(Category)`, `deleteById(Integer)`.

#### `CartService.java`
- **Đường dẫn**: `src/main/java/com/bookstore/service/CartService.java`
- **Chức năng**: Logic giỏ hàng — thêm, cập nhật số lượng, xóa, thanh toán.
- **Method chính**:
  - `getOrCreateCart(User)`: Lấy giỏ hàng của user, nếu chưa có thì tạo mới.
  - `addItem(Cart, Product, int)`: Thêm sản phẩm vào giỏ. Nếu sản phẩm đã tồn tại thì tăng số lượng.
  - `updateQuantity(Integer, Integer)`: Cập nhật số lượng. Nếu ≤ 0 thì xóa.
  - `removeItem(Integer)`: Xóa 1 món khỏi giỏ.
  - `clearCart(Cart)`: Xóa toàn bộ giỏ (dùng sau khi thanh toán thành công).
  - `getTotal(Cart)`: Tính tổng tiền giỏ hàng (dùng stream mapToDouble).

#### `OrderService.java`
- **Đường dẫn**: `src/main/java/com/bookstore/service/OrderService.java`
- **Chức năng**: Logic đơn hàng và thống kê doanh thu. Đây là service phức tạp nhất.
- **Method chính**:
  - `createOrder(User, Cart, String, String, String)`: Tạo đơn hàng từ giỏ hàng:
    1. Tạo Order mới (status = "NEW")
    2. Duyệt từng CartItem → kiểm tra tồn kho → trừ stock → tạo OrderDetail
    3. Tính tổng tiền → save Order → xóa giỏ hàng (clearCart)
    4. Nếu không đủ hàng → throw RuntimeException (rollback nhờ @Transactional)
  - `findByUser(Integer, int, int)`: Đơn hàng của 1 user (phân trang).
  - `findAll(int, int)`: Tất cả đơn hàng (admin).
  - `updateStatus(Integer, String)`: Cập nhật trạng thái (NEW → SHIPPED → PAID).
  - `getRevenueByDay(LocalDate)`: Tổng doanh thu 1 ngày.
  - `getRevenueByMonth(int, int)`: Tổng doanh thu 1 tháng.
  - `getRevenueByYear(int)`: Tổng doanh thu 1 năm.
  - `getDailyRevenue7Days(LocalDate)`: Doanh thu 7 ngày gần nhất (dùng HashMap để map, tạo label "dd/MM" cho Chart.js).

#### `CustomUserDetailsService.java`
- **Đường dẫn**: `src/main/java/com/bookstore/service/CustomUserDetailsService.java`
- **Chức năng**: Tích hợp Spring Security — nạp user từ DB khi đăng nhập.
- **Cơ chế**: Implement `UserDetailsService.loadUserByUsername()`. Tìm user trong DB, trả về `UserDetails` với role `"ROLE_ADMIN"` hoặc `"ROLE_CUSTOMER"`.

---

### 4. Controller Layer (`controller/`) — Xử lý request HTTP

#### `HomeController.java`
- **Đường dẫn**: `src/main/java/com/bookstore/controller/HomeController.java`
- **URL**: `GET /` và `GET /shop`
- **Chức năng**: Trang chủ và cửa hàng (public).
- **Xử lý**: Nhận tham số page (phân trang), name (tìm kiếm), category (lọc), sort (sắp xếp giá). Gọi `ProductService.findFiltered()`. Trả về template `index` hoặc `shop`.

#### `AuthController.java`
- **Đường dẫn**: `src/main/java/com/bookstore/controller/AuthController.java`
- **URL**: `GET /login`, `GET /register`, `POST /register`
- **Chức năng**: Xử lý đăng nhập và đăng ký.
- **Chi tiết**: Spring Security tự xử lý POST /login. Controller chỉ render form login. Đăng ký dùng `@Valid RegisterDto`, nếu lỗi validation → trả về form, nếu OK → gọi `UserService.register()` → redirect login.

#### `CartController.java`
- **Đường dẫn**: `src/main/java/com/bookstore/controller/CartController.java`
- **URL**: `/cart/**` (yêu cầu đăng nhập)
- **Chức năng**: Quản lý giỏ hàng.
- **Endpoints**:
  - `GET /cart`: Xem giỏ hàng, hiển thị tổng tiền.
  - `POST /cart/add/{productId}`: Thêm sản phẩm (tạo giỏ nếu chưa có).
  - `POST /cart/update/{itemId}`: Cập nhật số lượng.
  - `POST /cart/remove/{itemId}`: Xóa món khỏi giỏ.
  - `GET /cart/checkout`: Form thanh toán (kiểm tra giỏ không rỗng).
  - `POST /cart/checkout`: Xử lý thanh toán → gọi `OrderService.createOrder()`.
- **Phương thức hỗ trợ**: `getLoggedUser(Authentication)` — lấy user từ Spring Security context.

#### `OrderController.java`
- **Đường dẫn**: `src/main/java/com/bookstore/controller/OrderController.java`
- **URL**: `/order/**` (yêu cầu đăng nhập)
- **Chức năng**: Khách hàng xem lịch sử và chi tiết đơn hàng.
- **Endpoints**:
  - `GET /order/history`: Lịch sử đơn hàng (phân trang).
  - `GET /order/detail/{id}`: Chi tiết 1 đơn. **Bảo mật**: Kiểm tra user sở hữu đơn hàng, nếu không phải chủ thì redirect.
- **Phương thức hỗ trợ**: `getLoggedUser(Authentication)`.

#### `AdminController.java`
- **Đường dẫn**: `src/main/java/com/bookstore/controller/admin/AdminController.java`
- **URL**: `GET /admin` (yêu cầu role ADMIN)
- **Chức năng**: Trang dashboard admin. Đơn giản, chỉ render template `admin/dashboard`.

#### `ProductController.java` (Admin)
- **Đường dẫn**: `src/main/java/com/bookstore/controller/admin/ProductController.java`
- **URL**: `/admin/products/**` (yêu cầu role ADMIN)
- **Chức năng**: CRUD sản phẩm + upload ảnh.
- **Chi tiết quan trọng**:
  - `UPLOAD_DIR = System.getProperty("user.dir") + "/target/classes/static/images/"` — đường dẫn tuyệt đối đến thư mục chứa ảnh upload. Khi chạy `mvn spring-boot:run`, Spring Boot serve static resources từ `target/classes/static/`.
  - `saveImage(MultipartFile)`: Tạo UUID ngẫu nhiên + tên gốc để tránh trùng tên, copy file vào thư mục upload.
  - Khi **xóa sản phẩm**: Xóa cả file ảnh trên disk trước khi xóa khỏi database.
- **Endpoints**:
  - `GET /admin/products`: Danh sách (phân trang + lọc theo tên/danh mục).
  - `GET/POST /admin/products/add`: Form thêm + xử lý thêm.
  - `GET/POST /admin/products/edit/{id}`: Form sửa + xử lý cập nhật.
  - `GET /admin/products/delete/{id}`: Xóa sản phẩm + file ảnh.

#### `OrderManageController.java` (Admin)
- **Đường dẫn**: `src/main/java/com/bookstore/controller/admin/OrderManageController.java`
- **URL**: `/admin/orders/**` (yêu cầu role ADMIN)
- **Chức năng**: Quản lý đơn hàng và thống kê doanh thu.
- **Endpoints**:
  - `GET /admin/orders`: Danh sách đơn hàng (phân trang).
  - `POST /admin/orders/update-status`: Cập nhật trạng thái đơn (NEW → SHIPPED → PAID).
  - `GET /admin/orders/revenue`: Form chọn ngày xem doanh thu.
  - `POST /admin/orders/revenue`: Tính doanh thu ngày/tháng/năm + 7 ngày gần nhất (cho Chart.js). Gọi 4 method của OrderService cùng lúc.

---

### 5. Data Transfer Objects (`dto/`)

#### `RegisterDto.java`
- **Đường dẫn**: `src/main/java/com/bookstore/dto/RegisterDto.java`
- **Chức năng**: Nhận dữ liệu từ form đăng ký + validation Jakarta.
- **Validation**: `@NotBlank` + `@Size` cho username (3-50 ký tự), password (tối thiểu 6 ký tự), fullName.

#### `RevenueDto.java`
- **Đường dẫn**: `src/main/java/com/bookstore/dto/RevenueDto.java`
- **Chức năng**: Chứa dữ liệu doanh thu trả về cho view: `dayRevenue`, `monthRevenue`, `yearRevenue`, `dailyRevenueList` (List Map — mỗi Map chứa day, month, year, label, revenue cho Chart.js).

---

### 6. Configuration (`config/`)

#### `SecurityConfig.java`
- **Đường dẫn**: `src/main/java/com/bookstore/config/SecurityConfig.java`
- **Chức năng**: Cấu hình bảo mật toàn bộ ứng dụng.
- **Chi tiết**:
  - `PasswordEncoder`: BCrypt (mã hóa mật khẩu).
  - `DaoAuthenticationProvider`: Xác thực dùng CustomUserDetailsService + BCrypt.
  - `SecurityFilterChain`: Phân quyền URL — `/admin/**` chỉ ADMIN, `/cart/**` và `/order/**` cần login, còn lại public. Form login custom `/login`. Logout hủy session.

#### `WebMvcConfig.java`
- **Đường dẫn**: `src/main/java/com/bookstore/config/WebMvcConfig.java`
- **Chức năng**: Cấu hình Spring MVC — map URL pattern `/css/**`, `/js/**`, `/images/**` đến thư mục static tương ứng.

#### `DataInitializer.java`
- **Đường dẫn**: `src/main/java/com/bookstore/config/DataInitializer.java`
- **Chức năng**: Tự động tạo dữ liệu mẫu khi ứng dụng khởi động lần đầu (nếu DB trống).
- **Dữ liệu seed**:
  - 3 user: 1 admin + 2 customers (mật khẩu "123456" mã hóa BCrypt).
  - 8 category: Văn học Việt Nam, Văn học nước ngoài, Khoa học, Kinh tế, Kỹ năng sống, Thiếu nhi, Lịch sử, Công nghệ thông tin.
  - 35 sản phẩm sách (từ các category khác nhau, mỗi sản phẩm có ảnh book1.jpg - book35.jpg).
  - 6 đơn hàng + 2 đơn đã hủy (status = SHIPPED/PAID/NEW) với chi tiết đơn hàng.

---

### 7. Application Entry Point

#### `BookStoreApplication.java`
- **Đường dẫn**: `src/main/java/com/bookstore/BookStoreApplication.java`
- **Chức năng**: `@SpringBootApplication` — điểm khởi đầu. Gọi `SpringApplication.run()`.

---

### 8. Cấu hình (`application.properties`)

- **Đường dẫn**: `src/main/resources/application.properties`
- **Chức năng**: Cấu hình server port (8080), kết nối SQL Server (localhost\SQLEXPRESS, database=BookStore), JPA Hibernate (ddl-auto=update), Thymeleaf (cache=false cho dev, UTF-8), upload file (10MB), tắt data.sql (dùng DataInitializer).

---

### 9. Tĩnh (Static Resources)

#### `style.css`
- **Đường dẫn**: `src/main/resources/static/css/style.css`
- **Chức năng**: Gothic Moderno theme — màu sắc (Onyx Black, Blood Red, Silver), font (UnifrakturMaguntia cho heading, Inter cho body), hiệu ứng (border silver, hover scale, shadow glow đỏ), scrollbar tùy chỉnh, responsive.

#### `main.js`
- **Đường dẫn**: `src/main/resources/static/js/main.js`
- **Chức năng**: JavaScript — khởi tạo Chart.js biểu đồ cột doanh thu 7 ngày, toast thông báo, tooltip.

#### `/images/`
- **Đường dẫn**: `src/main/resources/static/images/`
- **Chức năng**: Book1.jpg đến Book39.jpg (39 ảnh sản phẩm). Ảnh upload từ admin cũng được lưu ở đây (qua ProductController).

---

### 10. Templates (`templates/`)

#### `layout.html`
- **Đường dẫn**: `src/main/resources/templates/layout.html`
- **Chức năng**: Layout chung. Định nghĩa 3 fragment:
  - `head(title)`: Meta, CSS (Bootstrap, Font Awesome, style.css).
  - `navbar`: Thanh điều hướng (logo, tìm kiếm, giỏ hàng, đăng nhập/đăng xuất, menu admin).
  - `footer`: Footer 4 cột (thông tin, danh mục, hỗ trợ, liên hệ). Scripts (Bootstrap, Chart.js, main.js).

#### `index.html`
- **Đường dẫn**: `src/main/resources/templates/index.html`
- **Chức năng**: Trang chủ. Hero banner, danh mục nổi bật (6 mục), sản phẩm mới nhất (grid 4 cột, badge "Mới", nút thêm giỏ hàng).

#### `shop.html`
- **Đường dẫn**: `src/main/resources/templates/shop.html`
- **Chức năng**: Trang cửa hàng. Sidebar lọc danh mục, tìm kiếm, sắp xếp giá, grid sản phẩm, phân trang.

#### `login.html`
- **Đường dẫn**: `src/main/resources/templates/login.html`
- **Chức năng**: Form đăng nhập (username + password), hiển thị lỗi nếu sai.

#### `register.html`
- **Đường dẫn**: `src/main/resources/templates/register.html`
- **Chức năng**: Form đăng ký (username, password, fullName, email, phone, address). Validation hiển thị lỗi inline.

#### `cart.html`
- **Đường dẫn**: `src/main/resources/templates/cart.html`
- **Chức năng**: Giỏ hàng — bảng danh sách sản phẩm (ảnh, tên, giá, số lượng, thành tiền), nút cập nhật/xóa, tổng tiền, nút thanh toán.

#### `checkout.html`
- **Đường dẫn**: `src/main/resources/templates/checkout.html`
- **Chức năng**: Form thanh toán — hiển thị giỏ hàng (readonly), form nhập thông tin người nhận (tên, SĐT, địa chỉ), tổng tiền.

#### `order-history.html`
- **Đường dẫn**: `src/main/resources/templates/order-history.html`
- **Chức năng**: Lịch sử đơn hàng — bảng (mã đơn, ngày, tổng tiền, trạng thái với badge màu), nút xem chi tiết, phân trang.

#### `order-detail.html`
- **Đường dẫn**: `src/main/resources/templates/order-detail.html`
- **Chức năng**: Chi tiết 1 đơn hàng — thông tin đơn (mã, ngày, trạng thái), thông tin người nhận, bảng sản phẩm (ảnh, tên, giá, số lượng, thành tiền), tổng tiền.

#### `admin/dashboard.html`
- **Đường dẫn**: `src/main/resources/templates/admin/dashboard.html`
- **Chức năng**: Trang tổng quan admin — các nút điều hướng (quản lý sản phẩm, quản lý đơn hàng, xem doanh thu), Gothic style.

#### `admin/products.html`
- **Đường dẫn**: `src/main/resources/templates/admin/products.html`
- **Chức năng**: Quản lý sản phẩm — bảng (ảnh thumbnail, tên, tác giả, giá, tồn kho, danh mục), filter theo tên/danh mục, nút thêm/sửa/xóa, phân trang.

#### `admin/product-form.html`
- **Đường dẫn**: `src/main/resources/templates/admin/product-form.html`
- **Chức năng**: Form thêm/sửa sản phẩm — tên, tác giả, danh mục (dropdown), giá, số lượng, mô tả (textarea), upload ảnh, preview ảnh.

#### `admin/orders.html`
- **Đường dẫn**: `src/main/resources/templates/admin/orders.html`
- **Chức năng**: Quản lý đơn hàng — bảng (mã đơn, khách hàng, ngày, tổng tiền, trạng thái), dropdown chọn trạng thái (NEW/SHIPPED/PAID), nút cập nhật, phân trang.

#### `admin/revenue.html`
- **Đường dẫn**: `src/main/resources/templates/admin/revenue.html`
- **Chức năng**: Thống kê doanh thu — chọn ngày (input date), hiển thị doanh thu ngày/tháng/năm, Chart.js bar chart (7 ngày gần nhất).

---

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
| POST | `/cart/remove/{id}` | Xóa khỏi giỏ |
| POST | `/cart/checkout` | Thanh toán |
| GET | `/order/history` | Lịch sử đơn hàng |
| GET | `/order/detail/{id}` | Chi tiết đơn hàng |

### Admin (`/admin/**` — yêu cầu đăng nhập + role ADMIN)
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
| POST | `/admin/orders/update-status` | Cập nhật trạng thái |
| GET | `/admin/orders/revenue` | Xem doanh thu |
| POST | `/admin/orders/revenue` | Xử lý doanh thu |

## Lưu ý kỹ thuật
1. **Ảnh sản phẩm**: Upload qua admin được lưu tại `target/classes/static/images/`. Khi chạy `mvn spring-boot:run`, Spring Boot serve static từ `target/classes/static/`, ảnh hiển thị ngay lập tức.
2. **Seed dữ liệu**: `DataInitializer.java` tự động chạy khi DB trống. Nếu muốn reseed, xóa dữ liệu cũ hoặc set `spring.jpa.hibernate.ddl-auto=create`.
3. **Comment code**: Tất cả các file Java, template, CSS, JS đều có comment tiếng Việt chi tiết giải thích từng lớp, method, trường dữ liệu.
4. **Mật khẩu mặc định**: `123456` (đã mã hóa BCrypt).
5. **Encoding**: UTF-8 cho toàn bộ ứng dụng (form, Thymeleaf, database dùng NVARCHAR).
