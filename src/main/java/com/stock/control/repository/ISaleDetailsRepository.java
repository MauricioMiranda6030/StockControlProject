package com.stock.control.repository;

import com.stock.control.entity.SaleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISaleDetailsRepository extends JpaRepository<SaleDetails, Long> {
}
