package com.bookstore.config;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// Component tự động khởi tạo dữ liệu mẫu (seed data) khi ứng dụng chạy lần đầu
// implements CommandLineRunner để Spring Boot tự gọi run() sau khi context khởi tạo xong
@Component
public class DataInitializer implements CommandLineRunner {

    // Repository để thao tác với bảng users
    @Autowired
    private UserRepository userRepository;

    // Repository để thao tác với bảng categories (danh mục sách)
    @Autowired
    private CategoryRepository categoryRepository;

    // Repository để thao tác với bảng products (sản phẩm)
    @Autowired
    private ProductRepository productRepository;

    // Repository để thao tác với bảng orders (đơn hàng)
    @Autowired
    private OrderRepository orderRepository;

    // Repository để thao tác với bảng order_details (chi tiết đơn hàng)
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    // Mã hóa mật khẩu, inject từ SecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hàm tự động chạy khi ứng dụng khởi động, chỉ seed dữ liệu khi database trống
    @Override
    @Transactional  // Đảm bảo toàn bộ quá trình seed là một giao dịch, rollback nếu lỗi
    public void run(String... args) {
        // Kiểm tra nếu đã có người dùng thì bỏ qua seed (tránh trùng lặp mỗi lần restart)
        if (userRepository.count() > 0) return;

        // Mã hóa mật khẩu mặc định "123456" dùng chung cho tất cả tài khoản seed
        String encodedPassword = passwordEncoder.encode("123456");

        // ============================================================
        // NGƯỜI DÙNG (3 tài khoản mẫu)
        // Gồm 1 admin + 2 khách hàng, tất cả dùng chung mật khẩu "123456"
        // ============================================================

        // Tài khoản quản trị viên: có quyền truy cập trang /admin/** để quản lý hệ thống
        userRepository.save(new User("admin", encodedPassword,
                "Quản trị viên", "admin@bookstore.com", "0901234567",
                "Số 1, Đường ABC, Quận 1, TP.HCM", "ADMIN"));

        // Khách hàng thứ nhất: Nguyễn Văn A, username nguyenvana, vai trò CUSTOMER
        User customer1 = userRepository.save(new User("nguyenvana", encodedPassword,
                "Nguyễn Văn A", "vana@gmail.com", "0912345678",
                "Số 10, Đường Lê Lợi, Quận 1, TP.HCM", "CUSTOMER"));

        // Khách hàng thứ hai: Trần Thị B, username tranthib, vai trò CUSTOMER
        User customer2 = userRepository.save(new User("tranthib", encodedPassword,
                "Trần Thị B", "thib@gmail.com", "0923456789",
                "Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM", "CUSTOMER"));

        // ============================================================
        // DANH MỤC (8 thể loại sách)
        // Mỗi danh mục sẽ được gán cho các sản phẩm ở bước tiếp theo
        // ============================================================
        List<Category> categories = categoryRepository.saveAll(List.of(
                new Category("Văn học Việt Nam"),        // index 0: sáng tác trong nước
                new Category("Văn học nước ngoài"),      // index 1: dịch thuật quốc tế
                new Category("Khoa học - Kỹ thuật"),     // index 2: sách khoa học phổ thông
                new Category("Kinh tế - Kinh doanh"),    // index 3: tài chính, quản trị
                new Category("Giáo dục - Kỹ năng sống"), // index 4: phát triển bản thân
                new Category("Thiếu nhi"),                // index 5: sách cho trẻ em
                new Category("Lịch sử - Chính trị"),     // index 6: sử sách và chính luận
                new Category("Công nghệ thông tin")      // index 7: lập trình, tin học
        ));

        // Gán biến cho từng danh mục để dễ đọc khi tạo sản phẩm
        Category vanHocVN = categories.get(0);
        Category vanHocNN = categories.get(1);
        Category khoaHoc = categories.get(2);
        Category kinhTe = categories.get(3);
        Category giaoDuc = categories.get(4);
        Category thieuNhi = categories.get(5);
        Category lichSu = categories.get(6);
        Category cntt = categories.get(7);

        // ============================================================
        // SẢN PHẨM (35 cuốn sách)
        // Mỗi sản phẩm gồm: tên, tác giả, mô tả, giá, tồn kho, ảnh bìa, danh mục
        // Phân bổ đều qua 8 danh mục, mỗi danh mục từ 4-5 cuốn
        // ============================================================

        // --- Văn học Việt Nam (5 cuốn) ---
        List<Product> savedProducts = productRepository.saveAll(List.of(
                new Product("Tôi thấy hoa vàng trên cỏ xanh", "Nguyễn Nhật Ánh",
                        "Truyện dài về tuổi thơ và tình cảm gia đình", 95000.0, 50, "book1.jpg", vanHocVN),
                new Product("Cho tôi xin một vé đi tuổi thơ", "Nguyễn Nhật Ánh",
                        "Cuốn sách đưa người đọc về với tuổi thơ đầy kỷ niệm", 85000.0, 45, "book2.jpg", vanHocVN),
                new Product("Cánh đồng bất tận", "Nguyễn Ngọc Tư",
                        "Tập truyện ngắn đặc sắc của nhà văn trẻ", 72000.0, 30, "book3.jpg", vanHocVN),
                new Product("Số đỏ", "Vũ Trọng Phụng",
                        "Kiệt tác văn học hiện thực phê phán", 68000.0, 35, "book4.jpg", vanHocVN),
                new Product("Chí Phèo", "Nam Cao",
                        "Truyện ngắn kinh điển của văn học Việt Nam", 45000.0, 40, "book5.jpg", vanHocVN),

                // --- Văn học nước ngoài (5 cuốn) ---
                new Product("Nhà giả kim", "Paulo Coelho",
                        "Cuốn sách truyền cảm hứng sống nổi tiếng thế giới", 79000.0, 60, "book6.jpg", vanHocNN),
                new Product("Trăm năm cô đơn", "Gabriel Garcia Marquez",
                        "Tác phẩm văn học kinh điển thế giới", 120000.0, 25, "book7.jpg", vanHocNN),
                new Product("Hoàng tử bé", "Antoine de Saint-Exupery",
                        "Câu chuyện triết học dành cho mọi lứa tuổi", 56000.0, 70, "book8.jpg", vanHocNN),
                new Product("Đắc nhân tâm", "Dale Carnegie",
                        "Cuốn sách nổi tiếng về nghệ thuật giao tiếp", 89000.0, 55, "book9.jpg", vanHocNN),
                new Product("Ông già và biển cả", "Ernest Hemingway",
                        "Câu chuyện về nghị lực phi thường của con người", 65000.0, 40, "book10.jpg", vanHocNN),

                // --- Khoa học - Kỹ thuật (5 cuốn) ---
                new Product("Vật lý của tương lai", "Michio Kaku",
                        "Khám phá khoa học và công nghệ tương lai", 150000.0, 20, "book11.jpg", khoaHoc),
                new Product("Vũ trụ trong một vỏ hạt", "Stephen Hawking",
                        "Giải thích các bí ẩn về vũ trụ", 165000.0, 18, "book12.jpg", khoaHoc),
                new Product("Thuyết tương đối cho mọi người", "Albert Einstein",
                        "Giải thích thuyết tương đối đơn giản", 110000.0, 15, "book13.jpg", khoaHoc),
                new Product("Khoa học và công nghệ nano", "Trần Văn Sơn",
                        "Tổng quan về công nghệ nano", 200000.0, 10, "book14.jpg", khoaHoc),
                new Product("Sapiens: Lược sử loài người", "Yuval Noah Harari",
                        "Cuốn sách lịch sử bán chạy nhất thế giới", 135000.0, 40, "book15.jpg", khoaHoc),

                // --- Kinh tế - Kinh doanh (5 cuốn) ---
                new Product("Cha giàu cha nghèo", "Robert Kiyosaki",
                        "Bài học tài chính dành cho người trẻ", 105000.0, 50, "book16.jpg", kinhTe),
                new Product("Nghĩ giàu và làm giàu", "Napoleon Hill",
                        "Triết lý làm già từ những người thành công", 99000.0, 45, "book17.jpg", kinhTe),
                new Product("Khởi nghiệp tinh gọn", "Eric Ries",
                        "Phương pháp khởi nghiệp hiệu quả", 125000.0, 30, "book18.jpg", kinhTe),
                new Product("Từ tốt đến vĩ đại", "Jim Collins",
                        "Bí quyết xây dựng công ty vĩ đại", 130000.0, 25, "book19.jpg", kinhTe),
                new Product("Bí mật tư duy triệu phú", "T. Harv Eker",
                        "Thay đổi tư duy tài chính", 95000.0, 35, "book20.jpg", kinhTe),

                // --- Giáo dục - Kỹ năng sống (4 cuốn) ---
                new Product("7 thói quen của người thành đạt", "Stephen Covey",
                        "Kỹ năng phát triển bản thân toàn diện", 115000.0, 50, "book21.jpg", giaoDuc),
                new Product("Đọc vị bất kỳ ai", "David J. Lieberman",
                        "Kỹ năng đọc hiểu tâm lý con người", 89000.0, 40, "book22.jpg", giaoDuc),
                new Product("Tư duy nhanh và chậm", "Daniel Kahneman",
                        "Khám phá cách thức hoạt động của não bộ", 140000.0, 20, "book23.jpg", giaoDuc),
                new Product("Rèn luyện trí não", "Ryuta Kawashima",
                        "Bài tập phát triển trí não hiệu quả", 78000.0, 30, "book24.jpg", giaoDuc),

                // --- Thiếu nhi (5 cuốn) ---
                new Product("Dế mèn phiêu lưu ký", "Tô Hoài",
                        "Truyện thiếu nhi kinh điển Việt Nam", 52000.0, 80, "book25.jpg", thieuNhi),
                new Product("Kính vạn hoa", "Nguyễn Nhật Ánh",
                        "Bộ truyện thiếu nhi nhiều tập hấp dẫn", 65000.0, 60, "book26.jpg", thieuNhi),
                new Product("Harry Potter và hòn đá phù thủy", "J.K. Rowling",
                        "Tập đầu trong series Harry Potter nổi tiếng", 95000.0, 75, "book27.jpg", thieuNhi),
                new Product("Conan tập 1", "Gosho Aoyama",
                        "Truyện tranh trinh thám nổi tiếng", 25000.0, 100, "book28.jpg", thieuNhi),
                new Product("Doraemon tập 1", "Fujiko F. Fujio",
                        "Truyện tranh thiếu nhi kinh điển Nhật Bản", 25000.0, 100, "book29.jpg", thieuNhi),

                // --- Lịch sử - Chính trị (3 cuốn) ---
                new Product("Việt Nam sử lược", "Trần Trọng Kim",
                        "Cuốn sách lịch sử Việt Nam tiêu biểu", 180000.0, 15, "book30.jpg", lichSu),
                new Product("Đại Việt sử ký toàn thư", "Ngô Sĩ Liên",
                        "Bộ sử lớn nhất về lịch sử Việt Nam", 350000.0, 10, "book31.jpg", lichSu),
                new Product("Bàn về khởi nghĩa", "Nguyễn Khắc Cần",
                        "Phân tích các cuộc khởi nghĩa trong lịch sử", 145000.0, 20, "book32.jpg", lichSu),

                // --- Công nghệ thông tin (3 cuốn) ---
                new Product("Java căn bản", "Nguyễn Văn Hiệu",
                        "Giáo trình lập trình Java từ cơ bản đến nâng cao", 120000.0, 40, "book33.jpg", cntt),
                new Product("Spring Boot thực chiến", "Trần Minh Quân",
                        "Hướng dẫn xây dựng ứng dụng với Spring Boot", 160000.0, 35, "book34.jpg", cntt),
                new Product("Cấu trúc dữ liệu và giải thuật", "Nguyễn Đình Thân",
                        "Sách tham khảo về cấu trúc dữ liệu và giải thuật", 140000.0, 30, "book35.jpg", cntt)
        ));

        // Gán biến cho các sản phẩm sẽ xuất hiện trong đơn hàng (11 sản phẩm được chọn)
        Product p1 = savedProducts.get(0);   // Tôi thấy hoa vàng trên cỏ xanh
        Product p6 = savedProducts.get(5);   // Nhà giả kim
        Product p7 = savedProducts.get(6);   // Trăm năm cô đơn
        Product p8 = savedProducts.get(7);   // Hoàng tử bé
        Product p9 = savedProducts.get(8);   // Đắc nhân tâm
        Product p11 = savedProducts.get(10); // Vật lý của tương lai
        Product p16 = savedProducts.get(15); // Cha giàu cha nghèo
        Product p21 = savedProducts.get(20); // 7 thói quen của người thành đạt
        Product p25 = savedProducts.get(24); // Dế mèn phiêu lưu ký
        Product p27 = savedProducts.get(26); // Harry Potter và hòn đá phù thủy
        Product p33 = savedProducts.get(32); // Java căn bản

        // Lấy lại đối tượng User đã được persist từ database bằng username
        // Dùng orElseThrow() vì chắc chắn người dùng đã được tạo ở bước trên
        User uA = userRepository.findByUsername("nguyenvana").orElseThrow();
        User uB = userRepository.findByUsername("tranthib").orElseThrow();

        // Lấy thời điểm hiện tại để tính ngày đặt hàng (mốc thời gian cho đơn hàng)
        LocalDateTime now = LocalDateTime.now();

        // ============================================================
        // ĐƠN HÀNG (6 đơn hàng mẫu)
        // Mỗi đơn hàng chứa: khách hàng, tổng tiền, thông tin giao hàng, ngày đặt, trạng thái
        // Ngày đặt lùi dần từ 1 đến 7 ngày trước hiện tại để có dữ liệu lịch sử
        // Trạng thái gồm: NEW (mới), PAID (đã thanh toán), SHIPPED (đã giao)
        // ============================================================

        // Đơn hàng 1 - Nguyễn Văn A - 190.000đ - đã thanh toán - cách đây 1 ngày
        Order o1 = new Order(uA, 190000.0, "Nguyễn Văn A", "0912345678",
                "Số 10, Đường Lê Lợi, Quận 1, TP.HCM");
        o1.setOrderDate(now.minusDays(1));
        o1.setStatus("PAID");
        o1 = orderRepository.save(o1);

        // Đơn hàng 2 - Trần Thị B - 245.000đ - đã giao - cách đây 2 ngày
        Order o2 = new Order(uB, 245000.0, "Trần Thị B", "0923456789",
                "Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM");
        o2.setOrderDate(now.minusDays(2));
        o2.setStatus("SHIPPED");
        o2 = orderRepository.save(o2);

        // Đơn hàng 3 - Nguyễn Văn A - 150.000đ - mới đặt - cách đây 3 ngày
        Order o3 = new Order(uA, 150000.0, "Nguyễn Văn A", "0912345678",
                "Số 10, Đường Lê Lợi, Quận 1, TP.HCM");
        o3.setOrderDate(now.minusDays(3));
        o3.setStatus("NEW");
        o3 = orderRepository.save(o3);

        // Đơn hàng 4 - Nguyễn Văn A - 320.000đ - đã thanh toán - cách đây 5 ngày
        Order o4 = new Order(uA, 320000.0, "Nguyễn Văn A", "0912345678",
                "Số 10, Đường Lê Lợi, Quận 1, TP.HCM");
        o4.setOrderDate(now.minusDays(5));
        o4.setStatus("PAID");
        o4 = orderRepository.save(o4);

        // Đơn hàng 5 - Trần Thị B - 175.000đ - đã giao - cách đây 6 ngày
        Order o5 = new Order(uB, 175000.0, "Trần Thị B", "0923456789",
                "Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM");
        o5.setOrderDate(now.minusDays(6));
        o5.setStatus("SHIPPED");
        o5 = orderRepository.save(o5);

        // Đơn hàng 6 - Nguyễn Văn A - 280.000đ - đã thanh toán - cách đây 7 ngày
        Order o6 = new Order(uA, 280000.0, "Nguyễn Văn A", "0912345678",
                "Số 10, Đường Lê Lợi, Quận 1, TP.HCM");
        o6.setOrderDate(now.minusDays(7));
        o6.setStatus("PAID");
        o6 = orderRepository.save(o6);

        // ============================================================
        // CHI TIẾT ĐƠN HÀNG (11 dòng, tương ứng 6 đơn)
        // Mỗi OrderDetail gồm: đơn hàng, sản phẩm, số lượng, đơn giá tại thời điểm mua
        // Đơn 1: 1 dòng, Đơn 2: 3 dòng, Đơn 3: 1 dòng, Đơn 4: 2 dòng, Đơn 5: 2 dòng, Đơn 6: 2 dòng
        // ============================================================
        orderDetailRepository.saveAll(List.of(
                new OrderDetail(o1, p1, 2, 95000.0),   // Đơn 1: 2 cuốn Tôi thấy hoa vàng trên cỏ xanh
                new OrderDetail(o2, p6, 1, 79000.0),   // Đơn 2: 1 cuốn Nhà giả kim
                new OrderDetail(o2, p9, 1, 89000.0),   // Đơn 2: 1 cuốn Đắc nhân tâm
                new OrderDetail(o2, p16, 1, 105000.0), // Đơn 2: 1 cuốn Cha giàu cha nghèo
                new OrderDetail(o3, p21, 1, 115000.0), // Đơn 3: 1 cuốn 7 thói quen
                new OrderDetail(o4, p7, 1, 120000.0),  // Đơn 4: 1 cuốn Trăm năm cô đơn
                new OrderDetail(o4, p8, 2, 56000.0),   // Đơn 4: 2 cuốn Hoàng tử bé
                new OrderDetail(o5, p25, 2, 52000.0),  // Đơn 5: 2 cuốn Dế mèn phiêu lưu ký
                new OrderDetail(o5, p27, 1, 95000.0),  // Đơn 5: 1 cuốn Harry Potter
                new OrderDetail(o6, p11, 1, 150000.0), // Đơn 6: 1 cuốn Vật lý của tương lai
                new OrderDetail(o6, p33, 1, 120000.0)  // Đơn 6: 1 cuốn Java căn bản
        ));
    }
}
