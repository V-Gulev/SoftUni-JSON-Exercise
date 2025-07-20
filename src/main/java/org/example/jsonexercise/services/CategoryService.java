package org.example.jsonexercise.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.jsonexercise.dtos.ImportCategoryDto;
import org.example.jsonexercise.entities.Category;
import org.example.jsonexercise.repositories.CategoryRepository;
import org.example.jsonexercise.repositories.CategoryStatsProjection;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository) {
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.categoryRepository = categoryRepository;
    }


    public void importData() throws IOException {
        Path path = Path.of("src/main/resources/jsons/categories.json");
        List<String> lines = Files.readAllLines(path);

        ImportCategoryDto[] fromJson = gson.fromJson(String.join("", lines), ImportCategoryDto[].class);

        for (ImportCategoryDto category : fromJson) {
            if (category.getName() == null || category.getName().length() < 3 || category.getName().length() > 15) {
                System.out.printf("Error: Invalid category name: %s%n", category.getName());
                continue;
            }

            Category mapped = this.modelMapper.map(category, Category.class);
            this.categoryRepository.save(mapped);
        }
    }

    public Set<Category> getRandomCategories(){
        Random rand = new Random();
        long total = this.categoryRepository.count();
        long count = rand.nextLong(total);
        Set<Category> result = new HashSet<>();

        for (long i = 0; i < count; i++) {
            long id = rand.nextLong(total) + 1;
            Optional<Category> category = this.categoryRepository.findById(id);

            category.ifPresent(result::add);
        }
        return result;
    }

    public void getCategoryStats() {
        List<CategoryStatsProjection> categoryStats = this.categoryRepository.findCategoryStats();

    }
}
