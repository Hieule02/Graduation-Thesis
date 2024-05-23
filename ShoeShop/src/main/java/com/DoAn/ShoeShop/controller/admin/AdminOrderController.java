package com.DoAn.ShoeShop.controller.admin;

import com.DoAn.ShoeShop.dto.respone.OrderResponse;
import com.DoAn.ShoeShop.entity.Order;
import com.DoAn.ShoeShop.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {
    @Autowired
    private IOrderService orderService;
    @GetMapping("")
    public String index(@PageableDefault(size = 6) Pageable pageable,
                        ModelMap modelMap){
        Specification<Order> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();
        Page<OrderResponse> orderResponses = orderService.findAll(specification, pageable);
        modelMap.addAttribute("orders", orderResponses);
        return "admin/admin-order";
    }
    @GetMapping("/detail")
    public String orderDetail(@RequestParam("id") Long id,
                              ModelMap modelMap){
        OrderResponse orderResponse = orderService.findById(id);

        modelMap.addAttribute("order", orderResponse);
        return "admin/order-detail";
    }

    @GetMapping("/confirm")
    public String confirmOrder(@RequestParam("id") Long id,
                               RedirectAttributes redirectAttributes){
        orderService.confirmOrder(id);
        redirectAttributes.addFlashAttribute("success", "Đã xác nhận giao hàng!");
        return "redirect:/admin/orders/detail?id=" + id;
    }
}
