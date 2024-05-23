package com.DoAn.ShoeShop.controller.user;

import com.DoAn.ShoeShop.dto.respone.OrderResponse;
import com.DoAn.ShoeShop.entity.Cart;
import com.DoAn.ShoeShop.entity.Order;
import com.DoAn.ShoeShop.security.UserSecurity;
import com.DoAn.ShoeShop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    private IOrderDetailService orderDetailService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private IUserService userService;
    @GetMapping("")
    public String listOrdersByUser(ModelMap modelMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();
        List<OrderResponse> orderList = orderService.findByUserId(currentUser.getId());
        modelMap.addAttribute("categories", categoryService.getAll());
        modelMap.addAttribute("user", userService.findById(currentUser.getId()));
        modelMap.addAttribute("orders", orderList);
        return "user/order";
    }
    @GetMapping("/checkout")
    public String checkout(ModelMap modelMap, RedirectAttributes redirectAttributes){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();

        List<Cart> carts = cartService.findByUserId(currentUser.getId());
        if(carts.isEmpty()){
            redirectAttributes.addFlashAttribute("fail", "Giỏ hàng chưa có sản phẩm nào!");
            return "redirect:/carts";
        }
        if(userService.findById(currentUser.getId()).getAddresses().isEmpty()){
            redirectAttributes.addFlashAttribute("message", "Bạn cần thêm địa chỉ để đặt hàng!");
            return "redirect:/users/address";
        }
        Long cartTotal = cartService.getCartTotal(currentUser.getId());
        Long shippingFee = 30000l;

        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        modelMap.addAttribute("categories", categoryService.getAll());
        modelMap.addAttribute("user", userService.findById(currentUser.getId()));
        modelMap.addAttribute("carts", carts);
        modelMap.addAttribute("cartTotal", currencyFormatter.format(cartTotal));
        modelMap.addAttribute("shippingFee", currencyFormatter.format(shippingFee));
        modelMap.addAttribute("totalAmount", currencyFormatter.format(cartTotal + shippingFee));
        return "user/checkout";
    }
    @PostMapping("/create")
    public String createOrder(@RequestParam("address") String address,
                              @RequestParam("paymentMethod") String paymentMethod){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();
        Order order = orderService.createOrder(currentUser.getId(), address, paymentMethod);
        orderDetailService.saveOrderDetailList(currentUser.getId(), order);
        cartService.deleteByUserId(currentUser.getId());
        return "redirect:/orders";
    }
    @GetMapping("/receive")
    public String receive(@RequestParam("id") Long id){
        orderService.receive(id);
        return "redirect:/orders";
    }

    @GetMapping("/cancel")
    public String cancel(@RequestParam("id") Long id){

        orderService.cancel(id);
        return "redirect:/orders";
    }
}
