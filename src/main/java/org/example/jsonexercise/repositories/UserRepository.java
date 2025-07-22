package org.example.jsonexercise.repositories;

import jakarta.transaction.Transactional;
import org.example.jsonexercise.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT DISTINCT u FROM User u JOIN u.soldProducts p WHERE p.buyer IS NOT NULL ORDER BY u.lastName, u.firstName")
    List<User> findAllWithSoldProductsAndBuyerNotNull();
}