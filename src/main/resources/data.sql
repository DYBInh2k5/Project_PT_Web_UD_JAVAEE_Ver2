-- Insert users if not exist
IF NOT EXISTS (SELECT 1 FROM users)
INSERT INTO users (username, password, full_name, email, phone, address, role, created_at) VALUES
('admin', '.Bpp2/No9YVgtUnISVDIx9dq1BjgJDXW', N'Quản trị viên', 'admin@bookstore.com', '0901234567', N'Số 1, Đường ABC, Quận 1, TP.HCM', 'ADMIN', GETDATE()),
('nguyenvana', '.Bpp2/No9YVgtUnISVDIx9dq1BjgJDXW', N'Nguyễn Văn A', 'vana@gmail.com', '0912345678', N'Số 10, Đường Lê Lợi, Quận 1, TP.HCM', 'CUSTOMER', GETDATE()),
('tranthib', '.Bpp2/No9YVgtUnISVDIx9dq1BjgJDXW', N'Trần Thị B', 'thib@gmail.com', '0923456789', N'Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM', 'CUSTOMER', GETDATE());

-- Insert categories if not exist
IF NOT EXISTS (SELECT 1 FROM categories)
INSERT INTO categories (name) VALUES
(N'Văn học Việt Nam'),
(N'Văn học nước ngoài'),
(N'Khoa học - Kỹ thuật'),
(N'Kinh tế - Kinh doanh'),
(N'Giáo dục - Kỹ năng sống'),
(N'Thiếu nhi'),
(N'Lịch sử - Chính trị'),
(N'Công nghệ thông tin');

-- Insert products if not exist
IF NOT EXISTS (SELECT 1 FROM products)
INSERT INTO products (name, author, description, price, stock, image, category_id, created_at) VALUES
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
(N'Cấu trúc dữ liệu và giải thuật', N'Nguyễn Đình Thân', N'Sách tham khảo về cấu trúc dữ liệu và giải thuật', 140000, 30, 'book35.jpg', 8, GETDATE());

-- Insert sample orders if not exist
IF NOT EXISTS (SELECT 1 FROM orders)
INSERT INTO orders (user_id, order_date, total_amount, status, recipient_name, phone, address) VALUES
(2, DATEADD(DAY, -1, GETDATE()), 190000, 'PAID', N'Nguyễn Văn A', '0912345678', N'Số 10, Đường Lê Lợi, Quận 1, TP.HCM'),
(3, DATEADD(DAY, -2, GETDATE()), 245000, 'SHIPPED', N'Trần Thị B', '0923456789', N'Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM'),
(2, DATEADD(DAY, -3, GETDATE()), 150000, 'NEW', N'Nguyễn Văn A', '0912345678', N'Số 10, Đường Lê Lợi, Quận 1, TP.HCM'),
(2, DATEADD(DAY, -5, GETDATE()), 320000, 'PAID', N'Nguyễn Văn A', '0912345678', N'Số 10, Đường Lê Lợi, Quận 1, TP.HCM'),
(3, DATEADD(DAY, -6, GETDATE()), 175000, 'SHIPPED', N'Trần Thị B', '0923456789', N'Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM'),
(2, DATEADD(DAY, -7, GETDATE()), 280000, 'PAID', N'Nguyễn Văn A', '0912345678', N'Số 10, Đường Lê Lợi, Quận 1, TP.HCM');

IF NOT EXISTS (SELECT 1 FROM order_details)
INSERT INTO order_details (order_id, product_id, quantity, price) VALUES
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
(6, 33, 1, 120000);
