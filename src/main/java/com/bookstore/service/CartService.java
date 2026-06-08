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

// Xử lý logic giỏ hàng: thêm, sửa, xóa, thanh toán
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // Lấy giỏ hàng của user, nếu chưa có thì tạo mới
    public Cart getOrCreateCart(User user) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(user.getId());
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        }
        Cart cart = new Cart(user);
        return cartRepository.save(cart);
    }

    // Tìm giỏ hàng theo user (có thể null)
    public Cart findByUser(User user) {
        return cartRepository.findByUserId(user.getId()).orElse(null);
    }

    // Thêm sản phẩm vào giỏ (nếu đã có thì tăng số lượng)
    public CartItem addItem(Cart cart, Product product, int quantity) {
        Optional<CartItem> existing = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        }
        CartItem item = new CartItem(cart, product, quantity);
        return cartItemRepository.save(item);
    }

    // Cập nhật số lượng (nếu <= 0 thì xóa)
    public void updateQuantity(Integer itemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(itemId).orElse(null);
        if (item != null) {
            if (quantity <= 0) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        }
    }

    // Xóa 1 món khỏi giỏ
    public void removeItem(Integer itemId) {
        cartItemRepository.deleteById(itemId);
    }

    // Xóa toàn bộ giỏ hàng (dùng sau khi thanh toán)
    @Transactional
    public void clearCart(Cart cart) {
        cartItemRepository.deleteByCartId(cart.getId());
    }

    // Tính tổng tiền giỏ hàng
    public double getTotal(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}
