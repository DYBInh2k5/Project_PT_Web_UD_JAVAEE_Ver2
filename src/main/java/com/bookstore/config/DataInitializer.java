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

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Tự động chạy khi ứng dụng khởi động (nếu DB trống)
    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) return;  // Nếu đã có dữ liệu thì bỏ qua

        String encodedPassword = passwordEncoder.encode("123456");  // Mật khẩu mặc định

        // === NGƯỜI DÙNG ===
        userRepository.save(new User("admin", encodedPassword,
                "Quản trị viên", "admin@bookstore.com", "0901234567",
                "Số 1, Đường ABC, Quận 1, TP.HCM", "ADMIN"));

        User customer1 = userRepository.save(new User("nguyenvana", encodedPassword,
                "Nguyễn Văn A", "vana@gmail.com", "0912345678",
                "Số 10, Đường Lê Lợi, Quận 1, TP.HCM", "CUSTOMER"));

        User customer2 = userRepository.save(new User("tranthib", encodedPassword,
                "Trần Thị B", "thib@gmail.com", "0923456789",
                "Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM", "CUSTOMER"));

        // === DANH MỤC (8 loại) ===
        List<Category> categories = categoryRepository.saveAll(List.of(
                new Category("Văn học Việt Nam"),
                new Category("Văn học nước ngoài"),
                new Category("Khoa học - Kỹ thuật"),
                new Category("Kinh tế - Kinh doanh"),
                new Category("Giáo dục - Kỹ năng sống"),
                new Category("Thiếu nhi"),
                new Category("Lịch sử - Chính trị"),
                new Category("Công nghệ thông tin")
        ));

        Category vanHocVN = categories.get(0);
        Category vanHocNN = categories.get(1);
        Category khoaHoc = categories.get(2);
        Category kinhTe = categories.get(3);
        Category giaoDuc = categories.get(4);
        Category thieuNhi = categories.get(5);
        Category lichSu = categories.get(6);
        Category cntt = categories.get(7);

        // === SẢN PHẨM (35 cuốn sách) ===
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
                new Product("7 thói quen của người thành đạt", "Stephen Covey",
                        "Kỹ năng phát triển bản thân toàn diện", 115000.0, 50, "book21.jpg", giaoDuc),
                new Product("Đọc vị bất kỳ ai", "David J. Lieberman",
                        "Kỹ năng đọc hiểu tâm lý con người", 89000.0, 40, "book22.jpg", giaoDuc),
                new Product("Tư duy nhanh và chậm", "Daniel Kahneman",
                        "Khám phá cách thức hoạt động của não bộ", 140000.0, 20, "book23.jpg", giaoDuc),
                new Product("Rèn luyện trí não", "Ryuta Kawashima",
                        "Bài tập phát triển trí não hiệu quả", 78000.0, 30, "book24.jpg", giaoDuc),
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
                new Product("Việt Nam sử lược", "Trần Trọng Kim",
                        "Cuốn sách lịch sử Việt Nam tiêu biểu", 180000.0, 15, "book30.jpg", lichSu),
                new Product("Đại Việt sử ký toàn thư", "Ngô Sĩ Liên",
                        "Bộ sử lớn nhất về lịch sử Việt Nam", 350000.0, 10, "book31.jpg", lichSu),
                new Product("Bàn về khởi nghĩa", "Nguyễn Khắc Cần",
                        "Phân tích các cuộc khởi nghĩa trong lịch sử", 145000.0, 20, "book32.jpg", lichSu),
                new Product("Java căn bản", "Nguyễn Văn Hiệu",
                        "Giáo trình lập trình Java từ cơ bản đến nâng cao", 120000.0, 40, "book33.jpg", cntt),
                new Product("Spring Boot thực chiến", "Trần Minh Quân",
                        "Hướng dẫn xây dựng ứng dụng với Spring Boot", 160000.0, 35, "book34.jpg", cntt),
                new Product("Cấu trúc dữ liệu và giải thuật", "Nguyễn Đình Thân",
                        "Sách tham khảo về cấu trúc dữ liệu và giải thuật", 140000.0, 30, "book35.jpg", cntt)
        ));

        Product p1 = savedProducts.get(0);
        Product p6 = savedProducts.get(5);
        Product p7 = savedProducts.get(6);
        Product p8 = savedProducts.get(7);
        Product p9 = savedProducts.get(8);
        Product p11 = savedProducts.get(10);
        Product p16 = savedProducts.get(15);
        Product p21 = savedProducts.get(20);
        Product p25 = savedProducts.get(24);
        Product p27 = savedProducts.get(26);
        Product p33 = savedProducts.get(32);

        // Fetch persisted users by username
        User uA = userRepository.findByUsername("nguyenvana").orElseThrow();
        User uB = userRepository.findByUsername("tranthib").orElseThrow();

        LocalDateTime now = LocalDateTime.now();

        // === ĐƠN HÀNG (6 đơn) ===
        Order o1 = new Order(uA, 190000.0, "Nguyễn Văn A", "0912345678",
                "Số 10, Đường Lê Lợi, Quận 1, TP.HCM");
        o1.setOrderDate(now.minusDays(1));
        o1.setStatus("PAID");
        o1 = orderRepository.save(o1);

        Order o2 = new Order(uB, 245000.0, "Trần Thị B", "0923456789",
                "Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM");
        o2.setOrderDate(now.minusDays(2));
        o2.setStatus("SHIPPED");
        o2 = orderRepository.save(o2);

        Order o3 = new Order(uA, 150000.0, "Nguyễn Văn A", "0912345678",
                "Số 10, Đường Lê Lợi, Quận 1, TP.HCM");
        o3.setOrderDate(now.minusDays(3));
        o3.setStatus("NEW");
        o3 = orderRepository.save(o3);

        Order o4 = new Order(uA, 320000.0, "Nguyễn Văn A", "0912345678",
                "Số 10, Đường Lê Lợi, Quận 1, TP.HCM");
        o4.setOrderDate(now.minusDays(5));
        o4.setStatus("PAID");
        o4 = orderRepository.save(o4);

        Order o5 = new Order(uB, 175000.0, "Trần Thị B", "0923456789",
                "Số 20, Đường Nguyễn Huệ, Quận Bình Thạnh, TP.HCM");
        o5.setOrderDate(now.minusDays(6));
        o5.setStatus("SHIPPED");
        o5 = orderRepository.save(o5);

        Order o6 = new Order(uA, 280000.0, "Nguyễn Văn A", "0912345678",
                "Số 10, Đường Lê Lợi, Quận 1, TP.HCM");
        o6.setOrderDate(now.minusDays(7));
        o6.setStatus("PAID");
        o6 = orderRepository.save(o6);

        // === CHI TIẾT ĐƠN HÀNG (11 dòng) ===
        orderDetailRepository.saveAll(List.of(
                new OrderDetail(o1, p1, 2, 95000.0),
                new OrderDetail(o2, p6, 1, 79000.0),
                new OrderDetail(o2, p9, 1, 89000.0),
                new OrderDetail(o2, p16, 1, 105000.0),
                new OrderDetail(o3, p21, 1, 115000.0),
                new OrderDetail(o4, p7, 1, 120000.0),
                new OrderDetail(o4, p8, 2, 56000.0),
                new OrderDetail(o5, p25, 2, 52000.0),
                new OrderDetail(o5, p27, 1, 95000.0),
                new OrderDetail(o6, p11, 1, 150000.0),
                new OrderDetail(o6, p33, 1, 120000.0)
        ));
    }
}
