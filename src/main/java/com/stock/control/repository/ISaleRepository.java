package com.stock.control.repository;

import com.stock.control.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findAllByDateOfSaleBetween(LocalDate inputDate, LocalDate now);
}
