package com.DoAn.ShoeShop.controller.user;

import com.DoAn.ShoeShop.dto.respone.ProductResponse;
import com.DoAn.ShoeShop.entity.Cart;
import com.DoAn.ShoeShop.security.UserSecurity;
import com.DoAn.ShoeShop.service.ICartService;
import com.DoAn.ShoeShop.service.ICategoryService;
import com.DoAn.ShoeShop.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private ICartService cartService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IProductService productService;
    @GetMapping("")
    public String index(ModelMap modelMap){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();
        List<Cart> carts = cartService.findByUserId(currentUser.getId());

        Long cartTotal = cartService.getCartTotal(currentUser.getId());
        if(cartTotal == null){
            cartTotal = 0l;
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        modelMap.addAttribute("carts", carts);
        modelMap.addAttribute("user", currentUser);
        modelMap.addAttribute("cartTotal", currencyFormatter.format(cartTotal));
        modelMap.addAttribute("categories", categoryService.getAll());
        return "user/cart";
    }
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("color") String color,
                            @RequestParam("size") String size,
                            @RequestParam("quantity") int quantity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();
        ProductResponse product = productService.findById(productId);
        Cart cart = cartService.findExistCart(currentUser.getId(), productId, color, size);
        if(cart == null){
            cart = new Cart();
            cart.setImage(product.getImage());
            cart.setPrice(product.getPrice());
            cart.setUserId(currentUser.getId());
            cart.setProductName(product.getName());
            cart.setProductId(product.getId());
            cart.setSize(size);
            cart.setColor(color);
            cart.setQuantity(quantity);
            cart.setTotal(cart.getPrice() * cart.getQuantity());
        } else {
            cart.setQuantity(cart.getQuantity() + quantity);
            cart.setTotal(cart.getPrice() * cart.getQuantity());
        }

        cartService.save(cart);
        return "redirect:/carts";
    }
    @PostMapping("/update")
    public String updateCart(@RequestParam("productId") Long productId,
                             @RequestParam("color") String color,
                             @RequestParam("size") String size,
                             @RequestParam("quantity") int quantity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserSecurity currentUser = (UserSecurity) authentication.getPrincipal();
        Cart cart = cartService.findExistCart(currentUser.getId(), productId, color, size);
        cart.setQuantity(quantity);
        cart.setTotal(cart.getPrice() * quantity);
        cartService.save(cart);
        return "redirect:/carts";
    }
    @GetMapping("/delete")
    public String deleteCartByProduct(@RequestParam("id") Long productId,
                                      @RequestParam("color") String color,
                                      @RequestParam("size") String size){
        cartService.deleteByProductIdAndColorAndSize(productId, color, size);
        return "redirect:/carts";
    }
}
