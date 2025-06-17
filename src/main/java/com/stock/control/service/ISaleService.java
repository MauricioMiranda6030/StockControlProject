package com.stock.control.service;

import com.stock.control.dto.SaleDTO;
import com.stock.control.dto.SaleViewDTO;
import com.stock.control.entity.Sale;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {

    Long saveSale(SaleDTO saleDto);

    List<Sale> getAllSales();

    List<SaleViewDTO> getAllSalesViewDto();

    List<SaleViewDTO> findSalesByDatesAndCode(LocalDate dateFrom, LocalDate dateTo, String code);

    void deleteSaleById(Long id);

    void createPdfReport(List<SaleViewDTO> sales, String totalAmount, String totalCurrency);

    void createClientReport(LocalDate dateFrom, LocalDate dateTo);
}
