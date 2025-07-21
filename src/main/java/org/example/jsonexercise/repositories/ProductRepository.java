package org.example.jsonexercise.repositories;

import org.example.jsonexercise.entities.Product;
import org.example.jsonexercise.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPriceBetweenAndBuyerIsNullOrderByPriceAsc(BigDecimal priceAfter, BigDecimal priceBefore);

    List<Product> findBySellerAndBuyerIsNotNull(User seller);
}
