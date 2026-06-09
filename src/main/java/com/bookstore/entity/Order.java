package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Lớp thực thể đại diện cho một đơn hàng trong hệ thống BookStore, được tạo sau khi khách hàng hoàn tất thanh toán.
// Entity này ánh xạ tới bảng "orders". Đơn hàng ghi nhận thông tin người đặt (User), ngày đặt, tổng tiền, trạng thái
// xử lý và thông tin giao hàng. Mỗi đơn hàng chứa nhiều OrderDetail (OneToMany), mỗi OrderDetail ghi lại chi tiết
// từng sản phẩm đã mua. Trạng thái đơn hàng có thể là NEW (mới tạo), SHIPPED (đã giao) hoặc PAID (đã thanh toán).
// Quan hệ chính:
// - Nhiều Order thuộc một User (ManyToOne).
// - Một Order có nhiều OrderDetail (OneToMany).
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng, khóa chính của bảng orders, được sinh tự động bởi cơ sở dữ liệu

    // Quan hệ ManyToOne với User: nhiều đơn hàng có thể thuộc về cùng một người dùng.
    // FetchType.LAZY giúp trì hoãn load thông tin người dùng cho đến khi thực sự cần.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;                      // Khách hàng đã đặt đơn hàng này, khóa ngoại tham chiếu đến bảng users

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;        // Ngày và giờ đặt hàng, được tự động gán khi persist

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;             // Tổng số tiền của đơn hàng, được tính từ tổng (số lượng * giá) của các OrderDetail

    @Column(nullable = false, columnDefinition = "NVARCHAR(20)")
    private String status;                  // Trạng thái xử lý đơn hàng: "NEW" (mới tạo), "SHIPPED" (đã giao), "PAID" (đã thanh toán)

    @Column(name = "recipient_name", columnDefinition = "NVARCHAR(100)")
    private String recipientName;           // Tên người nhận hàng, hỗ trợ tiếng Việt

    @Column(length = 20)
    private String phone;                   // Số điện thoại của người nhận hàng, độ dài tối đa 20 ký tự

    @Column(columnDefinition = "NVARCHAR(255)")
    private String address;                 // Địa chỉ giao hàng chi tiết, hỗ trợ tiếng Việt

    // Quan hệ OneToMany với OrderDetail: một đơn hàng có nhiều chi tiết đơn hàng.
    // mappedBy = "order" cho biết trường "order" bên phía OrderDetail là chủ sở hữu quan hệ.
    // cascade = CascadeType.ALL: mọi thao tác trên Order đều được áp dụng cho OrderDetail.
    // orphanRemoval = true: tự động xóa OrderDetail khỏi database khi bị loại khỏi danh sách.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();  // Danh sách chi tiết từng sản phẩm trong đơn hàng

    // Phương thức này được gọi tự động trước khi entity được persist lần đầu tiên nhờ annotation @PrePersist.
    // Gán ngày giờ hiện tại cho orderDate nếu chưa được thiết lập và gán trạng thái mặc định là "NEW".
    @PrePersist
    protected void onCreate() {
        if (orderDate == null) orderDate = LocalDateTime.now();
        if (status == null) status = "NEW";
    }

    public Order() {}

    public Order(User user, Double totalAmount, String recipientName, String phone, String address) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.recipientName = recipientName;
        this.phone = phone;
        this.address = address;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public List<OrderDetail> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<OrderDetail> orderDetails) { this.orderDetails = orderDetails; }
}
