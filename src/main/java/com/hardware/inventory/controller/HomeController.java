package com.hardware.inventory.controller;

import com.hardware.inventory.service.CategoryService;
import com.hardware.inventory.service.ProductService;
import com.hardware.inventory.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    public HomeController(ProductService productService,
                          CategoryService categoryService,
                          SupplierService supplierService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalProducts", productService.count());
        model.addAttribute("totalCategories", categoryService.count());
        model.addAttribute("totalSuppliers", supplierService.count());
        model.addAttribute("lowStockCount", productService.countLowStock(5));
        model.addAttribute("recentProducts", productService.findAll());
        model.addAttribute("featuredProducts", productService.findFeatured(8));
        return "index";
    }
}
