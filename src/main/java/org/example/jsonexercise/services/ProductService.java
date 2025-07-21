package org.example.jsonexercise.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.jsonexercise.dtos.ImportProductDto;
import org.example.jsonexercise.dtos.UnsoldProductInfoDto;
import org.example.jsonexercise.entities.Product;
import org.example.jsonexercise.entities.User;
import org.example.jsonexercise.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.modelMapper = new ModelMapper();
    }

    public void importData() throws IOException {
        Path path = Path.of("src/main/resources/jsons/products.json");
        List<String> lines = Files.readAllLines(path);
        ImportProductDto[] fromJson = this.gson.fromJson(String.join("", lines), ImportProductDto[].class);
        for (ImportProductDto product : fromJson) {
            if (product.getName() == null || product.getName().length() < 3) {
                System.out.printf("Error: Invalid product name: %s%n", product.getName());
                continue;
            }

            Product mapped = this.modelMapper.map(product, Product.class);
            mapped.setSeller(this.getRandomUser(false));
            mapped.setBuyer(this.getRandomUser(true));
            mapped.setCategories(this.categoryService.getRandomCategories());

            this.productRepository.save(mapped);

        }
    }

    private User getRandomUser(boolean canReturnNull) {
        Random random = new Random();

        if (canReturnNull) {
            boolean nullResult = random.nextBoolean();

            if (nullResult) {
                return null;
            }
        }

        return this.userService.getRandomUser();
    }

    public void getUnsoldProductsInRange(double lower, double upper){
        BigDecimal lowerBound = BigDecimal.valueOf(lower);
        BigDecimal upperBound = BigDecimal.valueOf(upper);
        List<Product> products = this.productRepository.findByPriceBetweenAndBuyerIsNullOrderByPriceAsc(lowerBound, upperBound);

        List<UnsoldProductInfoDto> result = new ArrayList<>();

        for (Product product : products) {
            result.add(new UnsoldProductInfoDto(product));
        }

        String json = this.gson.toJson(result);
        System.out.println(json);

    }
}
