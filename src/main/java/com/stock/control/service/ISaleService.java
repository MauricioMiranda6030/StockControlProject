package com.stock.control.service;

import com.stock.control.dto.SaleDTO;
import com.stock.control.entity.Sale;

import java.util.List;

public interface ISaleService {

    void saveSale(SaleDTO saleDto);

    Sale getSale(Long id);

    List<Sale> getAllSales();

    List<SaleDTO> getAllDtoSales();

    void deleteSale(Long id);
}
