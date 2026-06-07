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

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart getOrCreateCart(User user) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(user.getId());
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        }
        Cart cart = new Cart(user);
        return cartRepository.save(cart);
    }

    public Cart findByUser(User user) {
        return cartRepository.findByUserId(user.getId()).orElse(null);
    }

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

    public void removeItem(Integer itemId) {
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public void clearCart(Cart cart) {
        cartItemRepository.deleteByCartId(cart.getId());
    }

    public double getTotal(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}
