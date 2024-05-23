package com.DoAn.ShoeShop.controller.admin;

import com.DoAn.ShoeShop.dto.respone.OrderResponse;
import com.DoAn.ShoeShop.entity.Order;
import com.DoAn.ShoeShop.service.IOrderService;
import com.DoAn.ShoeShop.service.IProductService;
import com.DoAn.ShoeShop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IProductService productService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOrderService orderService;

    @GetMapping("")
    public String index(@PageableDefault(size = 3, sort = "createAt") Pageable pageable,
                        ModelMap modelMap) {
        int ordersNumber = orderService.countOrders();
        int productsNumber = productService.countProducts();
        int usersNumber = userService.countCustomer();
        Long monthIncome = orderService.getThisMonthIncome();
        if (monthIncome == null) {
            monthIncome = 0l;
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);

        Specification<Order> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), "Chờ xác nhận");
        Page<OrderResponse> unconfirmedOrders = orderService.findUnconfirmedOrders(specification, pageable);

        modelMap.addAttribute("productsNumber", productsNumber);
        modelMap.addAttribute("ordersNumber", ordersNumber);
        modelMap.addAttribute("usersNumber", usersNumber);
        modelMap.addAttribute("monthIncome", currencyFormatter.format(monthIncome));
        modelMap.addAttribute("unconfirmedOrders", unconfirmedOrders);
        return "admin/admin-homepage";
    }
}
