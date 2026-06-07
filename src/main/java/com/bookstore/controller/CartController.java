package com.bookstore.controller;

import com.bookstore.entity.*;
import com.bookstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    private User getLoggedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByUsername(auth.getName());
    }

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

    @PostMapping("/update/{itemId}")
    public String updateQuantity(@PathVariable Integer itemId,
                                 @RequestParam int quantity) {
        cartService.updateQuantity(itemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove/{itemId}")
    public String removeItem(@PathVariable Integer itemId) {
        cartService.removeItem(itemId);
        return "redirect:/cart";
    }

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
