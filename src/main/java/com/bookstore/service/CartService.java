package com.bookstore.service;

import com.bookstore.entity.Cart;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.Product;
import com.bookstore.entity.User;
import com.bookstore.repository.CartItemRepository;
import com.bookstore.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

// Service xử lý logic nghiệp vụ giỏ hàng (Cart): thêm, sửa, xóa, xóa toàn bộ, tính tổng tiền
// Phụ thuộc: CartRepository và CartItemRepository để thao tác với CSDL
// Mỗi user chỉ có một giỏ hàng duy nhất, được xác định qua Cart.findByUserId
// Luồng chính: thêm sản phẩm -> cập nhật số lượng -> thanh toán (tạo đơn) -> xóa giỏ
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // Lấy giỏ hàng của user, nếu chưa có giỏ hàng thì tạo mới và lưu vào CSDL
    // Đảm bảo mỗi user luôn có một giỏ hàng để thao tác (tránh lỗi null pointer)
    // Bước 1: Tìm giỏ hàng theo userId
    // Bước 2: Nếu đã tồn tại -> trả về giỏ hàng hiện có
    // Bước 3: Nếu chưa có -> tạo giỏ hàng mới với user tương ứng, lưu và trả về
    public Cart getOrCreateCart(User user) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(user.getId());
        if (cartOpt.isPresent()) {
            // User đã có giỏ hàng từ trước -> trả về luôn
            return cartOpt.get();
        }
        // User chưa có giỏ hàng -> tạo giỏ hàng mới
        Cart cart = new Cart(user);
        return cartRepository.save(cart);  // Lưu và trả về giỏ hàng đã có ID
    }

    // Tìm giỏ hàng theo user (có thể trả về null nếu user chưa có giỏ hàng)
    // Khác với getOrCreateCart: không tạo mới nếu chưa có
    // Dùng để kiểm tra giỏ hàng trước khi thao tác
    public Cart findByUser(User user) {
        return cartRepository.findByUserId(user.getId()).orElse(null);
    }

    // Thêm sản phẩm vào giỏ hàng với số lượng chỉ định
    // Bước 1: Kiểm tra sản phẩm đã có trong giỏ chưa (dựa trên cartId và productId)
    // Bước 2: Nếu đã có -> tăng số lượng hiện tại lên (cộng dồn, không thay thế)
    // Bước 3: Nếu chưa có -> tạo CartItem mới với số lượng ban đầu
    // Edge case: quantity <= 0 sẽ tạo item với số lượng âm (cần kiểm tra trước khi gọi)
    public CartItem addItem(Cart cart, Product product, int quantity) {
        Optional<CartItem> existing = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
        if (existing.isPresent()) {
            // Sản phẩm đã tồn tại trong giỏ -> cộng dồn số lượng
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        }
        // Sản phẩm chưa có trong giỏ -> tạo mới CartItem
        CartItem item = new CartItem(cart, product, quantity);
        return cartItemRepository.save(item);
    }

    // Cập nhật số lượng của một CartItem theo itemId
    // Bước 1: Tìm CartItem theo ID
    // Bước 2: Nếu không tìm thấy -> bỏ qua (không làm gì, không ném lỗi)
    // Bước 3: Nếu quantity <= 0 -> xóa item khỏi giỏ (hành vi "xóa nếu số lượng <= 0")
    // Bước 4: Nếu quantity > 0 -> cập nhật số lượng mới và lưu lại
    // Edge case: item không tồn tại -> xử lý an toàn bằng cách kiểm tra null
    public void updateQuantity(Integer itemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item != null) {
            if (quantity <= 0) {
                // Số lượng <= 0: xóa sản phẩm khỏi giỏ hàng
                cartItemRepository.delete(item);
            } else {
                // Số lượng > 0: cập nhật số lượng mới
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        }
        // Nếu item không tồn tại -> không làm gì, tránh lỗi
    }

    // Xóa một CartItem khỏi giỏ hàng theo itemId
    // Lưu ý: nếu itemId không tồn tại, deleteById không ném lỗi (Spring Data JPA behavior)
    public void removeItem(Integer itemId) {
        cartItemRepository.deleteById(itemId);
    }

    // Xóa tất cả CartItem thuộc một Cart (xóa toàn bộ giỏ hàng)
    // @Transactional đảm bảo toàn bộ thao tác xóa được thực hiện trong một transaction
    // Được gọi sau khi tạo đơn hàng thành công (clearCart trong OrderService.createOrder)
    // Nếu không có @Transactional, deleteByCartId có thể gây LazyInitializationException
    @Transactional
    public void clearCart(Cart cart) {
        cartItemRepository.deleteByCartId(cart.getId());  // Xóa tất cả item có cartId tương ứng
    }

    // Tính tổng tiền của toàn bộ giỏ hàng
    // Sử dụng Stream API: duyệt qua tất cả CartItem trong giỏ
    // Với mỗi item: lấy giá sản phẩm (product.price) * số lượng (item.quantity)
    // mapToDouble chuyển mỗi item thành giá trị double, sum() tính tổng
    // Edge case: giỏ hàng rỗng -> trả về 0.0 (stream rỗng -> sum = 0)
    public double getTotal(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}
