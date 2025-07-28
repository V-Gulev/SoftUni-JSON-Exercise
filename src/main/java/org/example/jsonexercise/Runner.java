package org.example.jsonexercise;

import org.example.jsonexercise.services.CategoryService;
import org.example.jsonexercise.services.ProductService;
import org.example.jsonexercise.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;


    public Runner(UserService userService, CategoryService categoryService, ProductService productService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Working..");
        userService.getUsersWithSoldProducts();
    }
}
