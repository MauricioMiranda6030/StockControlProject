package com.stock.control.repository;

import com.stock.control.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s FROM Sale s WHERE ((:df IS NULL OR :dt IS NULL) OR s.dateOfSale BETWEEN :df AND :dt ) AND (:code IS NULL OR s.docId LIKE :code)")
    List<Sale> findAllByDateAndCode(@Param("df") LocalDate dateFrom,@Param("dt") LocalDate dateTo,@Param("code") String code);
}