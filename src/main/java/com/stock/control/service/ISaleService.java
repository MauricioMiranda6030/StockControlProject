package com.stock.control.service;

import com.stock.control.dto.SaleDTO;
import com.stock.control.dto.SaleViewDTO;
import com.stock.control.entity.Sale;

import java.time.LocalDate;
import java.util.List;

public interface ISaleService {

    Long saveSale(SaleDTO saleDto);

    List<Sale> getAllSales();

    List<Sale> getSalesByDate(LocalDate date);

    List<SaleViewDTO> getAllSalesViewDto();

    List<SaleViewDTO> getSalesByDateDto(LocalDate value);

    void deleteSaleById(Long id);
}
