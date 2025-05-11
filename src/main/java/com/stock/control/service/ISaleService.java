package com.stock.control.service;

import com.stock.control.dto.SaleDTO;
import com.stock.control.dto.SaleViewDTO;
import com.stock.control.entity.Sale;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {

    Long saveSale(SaleDTO saleDto);

    List<Sale> getAllSales();

    List<Sale> getSalesByDate(LocalDate dateFrom, LocalDate dateTo);

    List<SaleViewDTO> getAllSalesViewDto();

    List<SaleViewDTO> getSalesByDateDto(LocalDate dateFrom, LocalDate dateTo);

    void deleteSaleById(Long id);
}
