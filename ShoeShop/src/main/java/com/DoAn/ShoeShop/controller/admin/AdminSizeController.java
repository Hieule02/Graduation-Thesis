package com.DoAn.ShoeShop.controller.admin;

import com.DoAn.ShoeShop.dto.request.SizeRequest;
import com.DoAn.ShoeShop.dto.respone.SizeResponse;
import com.DoAn.ShoeShop.entity.Size;
import com.DoAn.ShoeShop.service.ISizeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/sizes")
public class AdminSizeController {
    @Autowired
    private ISizeService sizeService;

    @GetMapping("")
    public String index(@PageableDefault(size = 6) Pageable pageable,
                        ModelMap modelMap) {
        Specification<Size> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();
        modelMap.addAttribute("sizes", sizeService.findAll(specification, pageable));
        return "admin/admin-size";
    }

    @PostMapping("/create")
    public String createSize(@RequestParam("size") Integer size,
                             RedirectAttributes redirectAttributes) {
        SizeResponse sizeResponse = sizeService.findBySize(size);
        if (sizeResponse != null) {
            redirectAttributes.addFlashAttribute("fail", "Cỡ " + size + " đã tồn tại!");
            return "redirect:/admin/sizes";
        }
        SizeRequest sizeRequest = new SizeRequest();
        sizeRequest.setSize(size);
        sizeService.createOrUpdate(sizeRequest);
        redirectAttributes.addFlashAttribute("success", "Thêm thành công!");
        return "redirect:/admin/sizes";
    }
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, ModelMap modelMap){
        SizeResponse sizeResponse = sizeService.findById(id);
        modelMap.addAttribute("size", sizeResponse);
        return "admin/update-size";
    }
    @PostMapping("/edit")
    public String updateSize(@ModelAttribute("size") @Valid SizeRequest sizeRequest,
                             BindingResult result,
                             RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "admin/update-size";
        }
        SizeResponse sizeResponse = sizeService.findById(sizeRequest.getId());
        if(sizeResponse.getSize().equals(sizeRequest.getSize())){
            return "redirect:/admin/sizes";
        }
        SizeResponse sizeResponse1 = sizeService.findBySize(sizeRequest.getSize());
        if(sizeResponse1 != null && sizeRequest.getSize().equals(sizeResponse1.getSize())){
            redirectAttributes.addFlashAttribute("fail", "Cỡ "+sizeRequest.getSize()+" đã tồn tại!");
            return "redirect:/admin/sizes/edit?id="+sizeRequest.getId();
        }
        sizeService.createOrUpdate(sizeRequest);
        redirectAttributes.addFlashAttribute("success", "Thêm thành công!");
        return "redirect:/admin/sizes";
    }
    @GetMapping("/delete")
    public String deleteSize(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        sizeService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
        return "redirect:/admin/sizes";
    }
}
