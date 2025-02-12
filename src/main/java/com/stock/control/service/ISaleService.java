package com.stock.control.service;

import com.stock.control.entity.Sale;

import java.util.List;

public interface ISaleService {

    Sale saveSale(Sale sale);

    Sale getSale(Long id);

    List<Sale> getAllSales();

    void deleteSale(Long id);

    Sale updateSale(Sale sale, Long id);

}
