package com.stock.control.repository;

import com.stock.control.dto.SaleReportDTO;
import com.stock.control.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {

    @Query( "SELECT s FROM Sale s " +
            "WHERE ((:df IS NULL OR :dt IS NULL) OR s.dateOfSale BETWEEN :df AND :dt ) " +
            "AND (:code IS NULL OR s.docId LIKE :code) " +
            "AND (:ex = FALSE OR s.docId != '0000')")
    List<Sale> findAllByDateCodeAndExclude(@Param("df") LocalDate dateFrom,
                                           @Param("dt") LocalDate dateTo,
                                           @Param("code") String code,
                                           @Param("ex") boolean ex);

    @Query( "SELECT NEW com.stock.control.dto.SaleReportDTO( s.clientFullName, s.docId, COUNT(s.id)) FROM Sale s " +
            "WHERE s.dateOfSale BETWEEN :df AND :dt " +
            "AND (:ex = FALSE OR s.docId != '0000') " +
            "GROUP BY s.docId")
    List<SaleReportDTO> getClientReport(@Param("df") LocalDate dateFrom,
                                        @Param("dt") LocalDate dateTo,
                                        @Param("ex") boolean ex);
}