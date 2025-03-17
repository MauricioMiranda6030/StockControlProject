package com.stock.control.repository;

import com.stock.control.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);

    @Modifying
    @Query("UPDATE Product p SET p.stock = :stock WHERE p.id = :id")
    int updateStock(@Param("id") Long id, @Param("stock") int stock);
}
