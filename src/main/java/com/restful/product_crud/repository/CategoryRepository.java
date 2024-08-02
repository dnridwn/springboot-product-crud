package com.restful.product_crud.repository;

import com.restful.product_crud.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE categories", nativeQuery = true)
    void truncateTable();
}
