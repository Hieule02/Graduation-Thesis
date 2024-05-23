package com.DoAn.ShoeShop.controller.admin;

import com.DoAn.ShoeShop.dto.request.ColorRequest;
import com.DoAn.ShoeShop.dto.respone.ColorResponse;
import com.DoAn.ShoeShop.entity.Color;
import com.DoAn.ShoeShop.service.IColorService;
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
@RequestMapping("/admin/colors")
public class AdminColorController {
    @Autowired
    private IColorService colorService;

    @GetMapping("")
    public String index(@PageableDefault(size = 6) Pageable pageable,
                        ModelMap modelMap) {
        Specification<Color> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction();
        modelMap.addAttribute("colors", colorService.findAll(specification, pageable));
        return "admin/admin-color";
    }

    @PostMapping("/create")
    public String createColor(@RequestParam("color") String color,
                              RedirectAttributes redirectAttributes) {
        ColorResponse colorResponse = colorService.findByColor(color);
        if (colorResponse != null) {
            redirectAttributes.addFlashAttribute("fail", "Màu " + color + " đã tồn tại!");
            return "redirect:/admin/colors";
        }
        ColorRequest colorRequest = new ColorRequest();
        colorRequest.setColor(color);
        colorService.createOrUpdate(colorRequest);
        redirectAttributes.addFlashAttribute("success", "Thêm mới thành công!");
        return "redirect:/admin/colors";
    }

    @GetMapping("/edit")
    public String showUpdateForm(@RequestParam("id") Long id, ModelMap modelMap) {
        ColorResponse colorResponse = colorService.findById(id);
        modelMap.addAttribute("color", colorResponse);
        return "admin/update-color";
    }
    @PostMapping("/edit")
    public String updateColor(@ModelAttribute("color") @Valid ColorRequest colorRequest,
                              BindingResult result,
                              RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "admin/update-color";
        }
        ColorResponse colorResponse = colorService.findById(colorRequest.getId());
        if(colorResponse.getColor().equals(colorRequest.getColor())){
            return "redirect:/admin/colors";
        }
        ColorResponse colorResponse1 = colorService.findByColor(colorRequest.getColor());
        if(colorResponse1 != null && colorRequest.getColor().equals(colorResponse1.getColor())){
            redirectAttributes.addFlashAttribute("fail", "Màu "+colorRequest.getColor()+" đã tồn tại!");
            return "redirect:/admin/colors/edit?id="+colorRequest.getId();
        }
        colorService.createOrUpdate(colorRequest);
        redirectAttributes.addFlashAttribute("success", "Thêm thành công!");
        return "redirect:/admin/colors";
    }
    @GetMapping("/delete")
    public String deleteColor(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        colorService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
        return "redirect:/admin/colors";
    }
}
