package org.example.jsonexercise.repositories;

import org.example.jsonexercise.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT new org.example.jsonexercise.repositories.CategoryStatsProjection(c.name, size(p), avg(p.price), sum(p.price)) FROM Category c JOIN c.products AS p group by c ORDER BY size(p) DESC")
    List<CategoryStatsProjection> findCategoryStats();
}
