package org.example.jsonexercise.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.transaction.Transactional;
import org.example.jsonexercise.dtos.ImportUserDto;
import org.example.jsonexercise.dtos.UserWithSoldProductsDto;
import org.example.jsonexercise.entities.Product;
import org.example.jsonexercise.entities.User;
import org.example.jsonexercise.repositories.ProductRepository;
import org.example.jsonexercise.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private Gson gson;
    private ModelMapper modelMapper;


    public UserService(UserRepository userRepository, ProductRepository productRepository) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.modelMapper = new ModelMapper();
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public void importData() throws IOException {
        Path path = Path.of("src/main/resources/jsons/users.json");
        List<String> lines = Files.readAllLines(path);

        ImportUserDto[] fromJson = gson.fromJson(String.join("", lines), ImportUserDto[].class);

        for (ImportUserDto user : fromJson) {
            if (user.getLastName() == null || user.getLastName().length() < 3) {
                System.out.printf("Error: Invalid lastname: %s%n", user.getLastName());
                continue;
            }

            User mapped = this.modelMapper.map(user, User.class);

            this.userRepository.save(mapped);
        }

    }

    public User getRandomUser() {
        Random rand = new Random();
        long total = this.userRepository.count();
        if (total == 0) {
            return null;
        }

        while (true) {
            long id = rand.nextLong(total) + 1;
            Optional<User> user = this.userRepository.findById(id);
            if (user.isPresent()) {
                return user.get();
            }
        }

    }

    @Transactional
    public void getUsersWithSoldProducts() {
        List<User> users = this.userRepository.findAllWithSoldProductsAndBuyerNotNull();

        for (User user : users) {
            List<Product> soldProducts = this.productRepository.findBySellerAndBuyerIsNotNull(user);
            user.setSoldProducts(soldProducts);

        }


        UserWithSoldProductsDto[] result = this.modelMapper.map(users, UserWithSoldProductsDto[].class);
        String json = this.gson.toJson(result);
        System.out.println(json);

    }
}
