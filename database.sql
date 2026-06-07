-- ============================================================
-- SCRIPT TẠO DATABASE CHO WEB BÁN SÁCH (BookStore)
-- Công nghệ: Spring Boot MVC + Thymeleaf + SQL Server
-- ============================================================

-- Tạo database
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'BookStore')
BEGIN
    CREATE DATABASE [BookStore]
END
GO

USE [BookStore]
GO

-- ============================================================
-- XÓA BẢNG CŨ NẾU CÓ (thứ tự để tránh lỗi khóa ngoại)
-- ============================================================
IF OBJECT_ID('dbo.order_details', 'U') IS NOT NULL DROP TABLE dbo.order_details
IF OBJECT_ID('dbo.orders', 'U') IS NOT NULL DROP TABLE dbo.orders
IF OBJECT_ID('dbo.cart_items', 'U') IS NOT NULL DROP TABLE dbo.cart_items
IF OBJECT_ID('dbo.carts', 'U') IS NOT NULL DROP TABLE dbo.carts
IF OBJECT_ID('dbo.products', 'U') IS NOT NULL DROP TABLE dbo.products
IF OBJECT_ID('dbo.categories', 'U') IS NOT NULL DROP TABLE dbo.categories
IF OBJECT_ID('dbo.users', 'U') IS NOT NULL DROP TABLE dbo.users
GO

-- ============================================================
-- TẠO CÁC BẢNG
-- ============================================================

CREATE TABLE dbo.users (
    id          INT IDENTITY(1,1) PRIMARY KEY,
    username    NVARCHAR(50)  NOT NULL UNIQUE,
    password    NVARCHAR(255) NOT NULL,
    full_name   NVARCHAR(100) NOT NULL,
    email       NVARCHAR(100) NULL,
    phone       NVARCHAR(20)  NULL,
    address     NVARCHAR(255) NULL,
    role        NVARCHAR(20)  NOT NULL DEFAULT 'CUSTOMER',
    created_at  DATETIME2     NULL DEFAULT GETDATE()
)
GO

CREATE TABLE dbo.categories (
    id   INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL UNIQUE
)
GO

CREATE TABLE dbo.products (
    id          INT IDENTITY(1,1) PRIMARY KEY,
    name        NVARCHAR(200) NOT NULL,
    author      NVARCHAR(100) NULL,
    description NVARCHAR(MAX)  NULL,
    price       FLOAT         NOT NULL,
    stock       INT           NOT NULL DEFAULT 0,
    image       NVARCHAR(255) NULL,
    category_id INT           NULL,
    created_at  DATETIME2     NULL DEFAULT GETDATE(),
    CONSTRAINT FK_products_category FOREIGN KEY (category_id) REFERENCES dbo.categories(id)
)
GO

CREATE TABLE dbo.carts (
    id      INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL UNIQUE,
    CONSTRAINT FK_carts_user FOREIGN KEY (user_id) REFERENCES dbo.users(id)
)
GO

CREATE TABLE dbo.cart_items (
    id         INT IDENTITY(1,1) PRIMARY KEY,
    cart_id    INT NOT NULL,
    product_id INT NOT NULL,
    quantity   INT NOT NULL DEFAULT 1,
    CONSTRAINT FK_cart_items_cart    FOREIGN KEY (cart_id)    REFERENCES dbo.carts(id),
    CONSTRAINT FK_cart_items_product FOREIGN KEY (product_id) REFERENCES dbo.products(id)
)
GO

CREATE TABLE dbo.orders (
    id             INT IDENTITY(1,1) PRIMARY KEY,
    user_id        INT NOT NULL,
    order_date     DATETIME2   NOT NULL DEFAULT GETDATE(),
    total_amount   FLOAT       NOT NULL,
    status         NVARCHAR(20) NOT NULL DEFAULT 'NEW',
    recipient_name NVARCHAR(100) NULL,
    phone          NVARCHAR(20)  NULL,
    address        NVARCHAR(255) NULL,
    CONSTRAINT FK_orders_user FOREIGN KEY (user_id) REFERENCES dbo.users(id)
)
GO

CREATE TABLE dbo.order_details (
    id         INT IDENTITY(1,1) PRIMARY KEY,
    order_id   INT NOT NULL,
    product_id INT NOT NULL,
    quantity   INT   NOT NULL,
    price      FLOAT NOT NULL,
    CONSTRAINT FK_order_details_order   FOREIGN KEY (order_id)   REFERENCES dbo.orders(id),
    CONSTRAINT FK_order_details_product FOREIGN KEY (product_id) REFERENCES dbo.products(id)
)
GO

-- ============================================================
-- CHÈN DỮ LIỆU MẪU
-- ============================================================

INSERT INTO dbo.users (username, password, full_name, email, phone, address, role, created_at) VALUES
('admin',     '$2a$10$GqCyMKncAD6zH0t8oEYKUOKu5EWlirkTfwTWL0zRn281S28li/rMG', N'Quản trị viên', 'admin@bookstore.com', '0901234567', N'Số 1, Đường ABC, Quận 1, TP.HCM', 'ADMIN', GETDATE()),
('nguyenvana', '$2a$10$GqCyMKncAD6zH0t8oEYKUOKu5EWlirkTfwTWL0zRn281S28li/rMG', N'Nguyễn Văn A', 'vana@gmail.com', '0912345678', N'Số 10, Đường Lê Lợi, Quận 1, TP.HCM', 'CUSTOMER', GETDATE()),
('tranthib',   '$2a$10$GqCyMKncAD6zH0t8oEYKUOKu5EWlirkTfwTWL0zRn281S28li/rMG', N'Trần Thị B', 'thib@gmail.com', '0923456789', N'Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM', 'CUSTOMER', GETDATE())
GO

INSERT INTO dbo.categories (name) VALUES
(N'Văn học Việt Nam'),
(N'Văn học nước ngoài'),
(N'Khoa học - Kỹ thuật'),
(N'Kinh tế - Kinh doanh'),
(N'Giáo dục - Kỹ năng sống'),
(N'Thiếu nhi'),
(N'Lịch sử - Chính trị'),
(N'Công nghệ thông tin')
GO

INSERT INTO dbo.products (name, author, description, price, stock, image, category_id, created_at) VALUES
(N'Tôi thấy hoa vàng trên cỏ xanh', N'Nguyễn Nhật Ánh', N'Truyện dài về tuổi thơ và tình cảm gia đình', 95000, 50, 'book1.jpg', 1, GETDATE()),
(N'Cho tôi xin một vé đi tuổi thơ', N'Nguyễn Nhật Ánh', N'Cuốn sách đưa người đọc về với tuổi thơ đầy kỷ niệm', 85000, 45, 'book2.jpg', 1, GETDATE()),
(N'Cánh đồng bất tận', N'Nguyễn Ngọc Tư', N'Tập truyện ngắn đặc sắc của nhà văn trẻ', 72000, 30, 'book3.jpg', 1, GETDATE()),
(N'Số đỏ', N'Vũ Trọng Phụng', N'Kiệt tác văn học hiện thực phê phán', 68000, 35, 'book4.jpg', 1, GETDATE()),
(N'Chí Phèo', N'Nam Cao', N'Truyện ngắn kinh điển của văn học Việt Nam', 45000, 40, 'book5.jpg', 1, GETDATE()),
(N'Nhà giả kim', N'Paulo Coelho', N'Cuốn sách truyền cảm hứng sống nổi tiếng thế giới', 79000, 60, 'book6.jpg', 2, GETDATE()),
(N'Trăm năm cô đơn', N'Gabriel Garcia Marquez', N'Tác phẩm văn học kinh điển thế giới', 120000, 25, 'book7.jpg', 2, GETDATE()),
(N'Hoàng tử bé', N'Antoine de Saint-Exupery', N'Câu chuyện triết học dành cho mọi lứa tuổi', 56000, 70, 'book8.jpg', 2, GETDATE()),
(N'Đắc nhân tâm', N'Dale Carnegie', N'Cuốn sách nổi tiếng về nghệ thuật giao tiếp', 89000, 55, 'book9.jpg', 2, GETDATE()),
(N'Ông già và biển cả', N'Ernest Hemingway', N'Câu chuyện về nghị lực phi thường của con người', 65000, 40, 'book10.jpg', 2, GETDATE()),
(N'Vật lý của tương lai', N'Michio Kaku', N'Khám phá khoa học và công nghệ tương lai', 150000, 20, 'book11.jpg', 3, GETDATE()),
(N'Vũ trụ trong một vỏ hạt', N'Stephen Hawking', N'Giải thích các bí ẩn về vũ trụ', 165000, 18, 'book12.jpg', 3, GETDATE()),
(N'Thuyết tương đối cho mọi người', N'Albert Einstein', N'Giải thích thuyết tương đối đơn giản', 110000, 15, 'book13.jpg', 3, GETDATE()),
(N'Khoa học và công nghệ nano', N'Trần Văn Sơn', N'Tổng quan về công nghệ nano', 200000, 10, 'book14.jpg', 3, GETDATE()),
(N'Sapiens: Lược sử loài người', N'Yuval Noah Harari', N'Cuốn sách lịch sử bán chạy nhất thế giới', 135000, 40, 'book15.jpg', 3, GETDATE()),
(N'Cha giàu cha nghèo', N'Robert Kiyosaki', N'Bài học tài chính dành cho người trẻ', 105000, 50, 'book16.jpg', 4, GETDATE()),
(N'Nghĩ giàu và làm giàu', N'Napoleon Hill', N'Triết lý làm già từ những người thành công', 99000, 45, 'book17.jpg', 4, GETDATE()),
(N'Khởi nghiệp tinh gọn', N'Eric Ries', N'Phương pháp khởi nghiệp hiệu quả', 125000, 30, 'book18.jpg', 4, GETDATE()),
(N'Từ tốt đến vĩ đại', N'Jim Collins', N'Bí quyết xây dựng công ty vĩ đại', 130000, 25, 'book19.jpg', 4, GETDATE()),
(N'Bí mật tư duy triệu phú', N'T. Harv Eker', N'Thay đổi tư duy tài chính', 95000, 35, 'book20.jpg', 4, GETDATE()),
(N'7 thói quen của người thành đạt', N'Stephen Covey', N'Kỹ năng phát triển bản thân toàn diện', 115000, 50, 'book21.jpg', 5, GETDATE()),
(N'Đọc vị bất kỳ ai', N'David J. Lieberman', N'Kỹ năng đọc hiểu tâm lý con người', 89000, 40, 'book22.jpg', 5, GETDATE()),
(N'Tư duy nhanh và chậm', N'Daniel Kahneman', N'Khám phá cách thức hoạt động của não bộ', 140000, 20, 'book23.jpg', 5, GETDATE()),
(N'Rèn luyện trí não', N'Ryuta Kawashima', N'Bài tập phát triển trí não hiệu quả', 78000, 30, 'book24.jpg', 5, GETDATE()),
(N'Dế mèn phiêu lưu ký', N'Tô Hoài', N'Truyện thiếu nhi kinh điển Việt Nam', 52000, 80, 'book25.jpg', 6, GETDATE()),
(N'Kính vạn hoa', N'Nguyễn Nhật Ánh', N'Bộ truyện thiếu nhi nhiều tập hấp dẫn', 65000, 60, 'book26.jpg', 6, GETDATE()),
(N'Harry Potter và hòn đá phù thủy', N'J.K. Rowling', N'Tập đầu trong series Harry Potter nổi tiếng', 95000, 75, 'book27.jpg', 6, GETDATE()),
(N'Conan tập 1', N'Gosho Aoyama', N'Truyện tranh trinh thám nổi tiếng', 25000, 100, 'book28.jpg', 6, GETDATE()),
(N'Doraemon tập 1', N'Fujiko F. Fujio', N'Truyện tranh thiếu nhi kinh điển Nhật Bản', 25000, 100, 'book29.jpg', 6, GETDATE()),
(N'Việt Nam sử lược', N'Trần Trọng Kim', N'Cuốn sách lịch sử Việt Nam tiêu biểu', 180000, 15, 'book30.jpg', 7, GETDATE()),
(N'Đại Việt sử ký toàn thư', N'Ngô Sĩ Liên', N'Bộ sử lớn nhất về lịch sử Việt Nam', 350000, 10, 'book31.jpg', 7, GETDATE()),
(N'Bàn về khởi nghĩa', N'Nguyễn Khắc Cần', N'Phân tích các cuộc khởi nghĩa trong lịch sử', 145000, 20, 'book32.jpg', 7, GETDATE()),
(N'Java căn bản', N'Nguyễn Văn Hiệu', N'Giáo trình lập trình Java từ cơ bản đến nâng cao', 120000, 40, 'book33.jpg', 8, GETDATE()),
(N'Spring Boot thực chiến', N'Trần Minh Quân', N'Hướng dẫn xây dựng ứng dụng với Spring Boot', 160000, 35, 'book34.jpg', 8, GETDATE()),
(N'Cấu trúc dữ liệu và giải thuật', N'Nguyễn Đình Thân', N'Sách tham khảo về cấu trúc dữ liệu và giải thuật', 140000, 30, 'book35.jpg', 8, GETDATE())
GO

INSERT INTO dbo.orders (user_id, order_date, total_amount, status, recipient_name, phone, address) VALUES
(2, DATEADD(DAY, -1, GETDATE()), 190000, 'PAID', N'Nguyễn Văn A', '0912345678', N'Số 10, Đường Lê Lợi, Quận 1, TP.HCM'),
(3, DATEADD(DAY, -2, GETDATE()), 245000, 'SHIPPED', N'Trần Thị B', '0923456789', N'Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM'),
(2, DATEADD(DAY, -3, GETDATE()), 150000, 'NEW', N'Nguyễn Văn A', '0912345678', N'Số 10, Đường Lê Lợi, Quận 1, TP.HCM'),
(2, DATEADD(DAY, -5, GETDATE()), 320000, 'PAID', N'Nguyễn Văn A', '0912345678', N'Số 10, Đường Lê Lợi, Quận 1, TP.HCM'),
(3, DATEADD(DAY, -6, GETDATE()), 175000, 'SHIPPED', N'Trần Thị B', '0923456789', N'Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM'),
(2, DATEADD(DAY, -7, GETDATE()), 280000, 'PAID', N'Nguyễn Văn A', '0912345678', N'Số 10, Đường Lê Lợi, Quận 1, TP.HCM')
GO

INSERT INTO dbo.order_details (order_id, product_id, quantity, price) VALUES
(1, 1, 2, 95000),
(2, 6, 1, 79000),
(2, 9, 1, 89000),
(2, 16, 1, 105000),
(3, 21, 1, 115000),
(4, 7, 1, 120000),
(4, 8, 2, 56000),
(5, 25, 2, 52000),
(5, 27, 1, 95000),
(6, 11, 1, 150000),
(6, 33, 1, 120000)
GO

PRINT N'--------------------'
PRINT N'ĐÃ TẠO DATABASE THÀNH CÔNG!'
PRINT N'Tài khoản: admin / nguyenvana / tranthib'
PRINT N'Mật khẩu: 123456'
PRINT N'--------------------'
GO
