package com.stock.control.repository;

import com.stock.control.entity.Product;
import com.stock.control.entity.Sale;
import com.stock.control.entity.SaleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISaleDetailsRepository extends JpaRepository<SaleDetails, Long> {

    List<SaleDetails> findAllBySaleId(Long id);

    void deleteAllBySale(Sale sale);

    boolean existsByProduct(Product product);

    SaleDetails findBySale(Sale sale);
}
