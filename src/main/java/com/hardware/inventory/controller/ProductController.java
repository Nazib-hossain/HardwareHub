package com.hardware.inventory.controller;

import com.hardware.inventory.model.Product;
import com.hardware.inventory.service.CategoryService;
import com.hardware.inventory.service.ProductService;
import com.hardware.inventory.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;

    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             SupplierService supplierService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
    }


    @GetMapping
    public String listProducts(@RequestParam(value = "search", required = false) String search,
                               @RequestParam(value = "category", required = false) String category,
                               Model model) {
        model.addAttribute("products", productService.search(search, category));
        model.addAttribute("categories", categoryService.findAll());
        return "products";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("suppliers", supplierService.findAll());
        return "add-product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product,
                             @RequestParam(value = "categoryId", required = false) Long categoryId,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "add-product";
        }

        if (categoryId != null) {
            categoryService.findById(categoryId).ifPresent(product::setCategory);
        }

        productService.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return productService.findById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("categories", categoryService.findAll());
                    model.addAttribute("suppliers", supplierService.findAll());
                    return "edit-product";
                })
                .orElse("redirect:/products");
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute("product") Product product,
                                @RequestParam(value = "categoryId", required = false) Long categoryId,
                                RedirectAttributes redirectAttributes) {
        product.setId(id);
        if (categoryId != null) {
            categoryService.findById(categoryId).ifPresent(product::setCategory);
        }

        productService.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        return "redirect:/products";
    }
}
