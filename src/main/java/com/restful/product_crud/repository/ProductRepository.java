package com.restful.product_crud.repository;

import com.restful.product_crud.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Integer id);
    Optional<Product> findFirstByOrderByIdDesc();

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE products", nativeQuery = true)
    void truncateTable();
}
