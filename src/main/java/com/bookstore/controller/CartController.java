package com.bookstore.controller;

import com.bookstore.entity.*;
import com.bookstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controller quản lý giỏ hàng: xem, thêm, sửa, xóa, thanh toán
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    // Lấy user đang đăng nhập từ Spring Security
    private User getLoggedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByUsername(auth.getName());
    }

    // Xem giỏ hàng (yêu cầu đăng nhập)
    @GetMapping
    public String viewCart(Model model, Authentication auth) {
        User user = getLoggedUser(auth);
        if (user == null) return "redirect:/login";
        Cart cart = cartService.findByUser(user);
        model.addAttribute("cart", cart);
        if (cart != null) {
            model.addAttribute("total", cartService.getTotal(cart));
        }
        return "cart";
    }

    // Thêm sản phẩm vào giỏ (yêu cầu đăng nhập)
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable Integer productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            Authentication auth,
                            RedirectAttributes ra) {
        User user = getLoggedUser(auth);
        if (user == null) return "redirect:/login";
        Product product = productService.findById(productId);
        if (product == null) {
            ra.addFlashAttribute("error", "Sản phẩm không tồn tại");
            return "redirect:/shop";
        }
        Cart cart = cartService.getOrCreateCart(user);
        cartService.addItem(cart, product, quantity);
        ra.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng");
        return "redirect:/cart";
    }

    // Cập nhật số lượng 1 món trong giỏ
    @PostMapping("/update/{itemId}")
    public String updateQuantity(@PathVariable Integer itemId,
                                 @RequestParam int quantity) {
        cartService.updateQuantity(itemId, quantity);
        return "redirect:/cart";
    }

    // Xóa 1 món khỏi giỏ
    @PostMapping("/remove/{itemId}")
    public String removeItem(@PathVariable Integer itemId) {
        cartService.removeItem(itemId);
        return "redirect:/cart";
    }

    // Hiển thị form thanh toán (yêu cầu login, giỏ không được rỗng)
    @GetMapping("/checkout")
    public String checkoutForm(Model model, Authentication auth) {
        User user = getLoggedUser(auth);
        if (user == null) return "redirect:/login";
        Cart cart = cartService.findByUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        model.addAttribute("total", cartService.getTotal(cart));
        model.addAttribute("user", user);
        return "checkout";
    }

    // Xử lý thanh toán: tạo đơn hàng, xóa giỏ
    @PostMapping("/checkout")
    public String checkout(@RequestParam String recipientName,
                           @RequestParam String phone,
                           @RequestParam String address,
                           Authentication auth,
                           RedirectAttributes ra) {
        User user = getLoggedUser(auth);
        if (user == null) return "redirect:/login";
        Cart cart = cartService.findByUser(user);
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        try {
            orderService.createOrder(user, cart, recipientName, phone, address);
            ra.addFlashAttribute("success", "Đặt hàng thành công!");
            return "redirect:/order/history";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart/checkout";
        }
    }
}
