package com.bookstore.controller;

import com.bookstore.entity.*;
import com.bookstore.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controller quản lý giỏ hàng: xem, thêm, sửa, xóa sản phẩm và thanh toán
// Base URL: /cart - Yêu cầu người dùng phải đăng nhập (Authentication object được inject)
// Phụ thuộc: CartService (thao tác giỏ hàng), ProductService (tìm sản phẩm),
//            UserService (lấy thông tin user), OrderService (tạo đơn hàng khi thanh toán)
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

    // Helper: Lấy thông tin User đang đăng nhập từ Spring Security Authentication
    // Spring Security tự động inject Authentication vào controller method khi có @AuthenticationPrincipal
    // hoặc khi khai báo tham số Authentication auth (Spring Security Resolver tự động làm việc này)
    // Authentication.getName() trả về username của người dùng đã được xác thực
    // B1: Kiểm tra auth == null hoặc chưa được xác thực -> trả về null (chưa đăng nhập)
    // B2: Dùng username từ auth.getName() để truy vấn User từ database qua UserService
    private User getLoggedUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return null;
        return userService.findByUsername(auth.getName());
    }

    // Xử lý GET /cart - Xem giỏ hàng (yêu cầu đăng nhập)
    // B1: Lấy user đang đăng nhập, nếu null thì redirect sang /login
    // B2: Gọi CartService.findByUser(user) để lấy giỏ hàng của user (có thể null nếu chưa có giỏ)
    // B3: Đưa giỏ hàng vào Model; nếu cart != null thì tính tổng tiền và đưa vào Model
    // B4: Trả về template "cart"
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

    // Xử lý POST /cart/add/{productId} - Thêm sản phẩm vào giỏ hàng (yêu cầu đăng nhập)
    // Tham số: productId (ID sản phẩm từ URL path), quantity (số lượng, mặc định 1)
    // B1: Lấy user đăng nhập, nếu null redirect /login
    // B2: Tìm Product theo productId, nếu không tồn tại -> flash error + redirect /shop
    // B3: Gọi CartService.getOrCreateCart(user) để lấy giỏ hiện tại hoặc tạo mới nếu chưa có
    // B4: Gọi CartService.addItem(cart, product, quantity) để thêm sản phẩm vào giỏ
    // B5: Flash thông báo thành công + redirect /cart
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

    // Xử lý POST /cart/update/{itemId} - Cập nhật số lượng của một mục trong giỏ hàng
    // Tham số: itemId (ID của CartItem từ URL path), quantity (số lượng mới từ form)
    // Gọi CartService.updateQuantity(itemId, quantity) để cập nhật số lượng
    // Sau đó redirect về /cart để xem giỏ hàng đã cập nhật
    @PostMapping("/update/{itemId}")
    public String updateQuantity(@PathVariable Integer itemId,
                                 @RequestParam int quantity) {
        cartService.updateQuantity(itemId, quantity);
        return "redirect:/cart";
    }

    // Xử lý POST /cart/remove/{itemId} - Xóa một mục khỏi giỏ hàng
    // Tham số: itemId (ID của CartItem từ URL path)
    // Gọi CartService.removeItem(itemId) để xóa mục khỏi giỏ hàng
    // Sau đó redirect về /cart
    @PostMapping("/remove/{itemId}")
    public String removeItem(@PathVariable Integer itemId) {
        cartService.removeItem(itemId);
        return "redirect:/cart";
    }

    // Xử lý GET /cart/checkout - Hiển thị form thanh toán (yêu cầu đăng nhập, giỏ không được rỗng)
    // B1: Lấy user đăng nhập, nếu null redirect /login
    // B2: Lấy giỏ hàng của user, nếu null hoặc rỗng -> redirect /cart (không có gì để thanh toán)
    // B3: Đưa cart, total (tổng tiền), user vào Model để form thanh toán hiển thị thông tin
    // B4: Trả về template "checkout"
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

    // Xử lý POST /cart/checkout - Xử lý thanh toán: tạo đơn hàng và xóa giỏ hàng
    // Tham số: recipientName (tên người nhận), phone (số điện thoại), address (địa chỉ giao hàng)
    // B1: Lấy user đăng nhập, nếu null redirect /login
    // B2: Lấy giỏ hàng của user, nếu null hoặc rỗng -> redirect /cart
    // B3: Gọi OrderService.createOrder(user, cart, ...) để tạo đơn hàng mới
    //     (OrderService sẽ chuyển CartItem thành OrderItem, tính tổng tiền, xóa giỏ hàng)
    // B4: Nếu thành công -> flash success + redirect /order/history (xem lịch sử đơn hàng)
    // B5: Nếu có ngoại lệ -> flash error + redirect lại /cart/checkout để nhập lại
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
