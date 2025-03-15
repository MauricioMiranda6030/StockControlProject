package com.stock.control.service;

import com.stock.control.dto.SaleDTO;
import com.stock.control.entity.Sale;
import com.stock.control.entity.SaleDetails;

import java.util.List;

public interface ISaleDetailsService {

    void saveSaleDetails(SaleDTO saleDto, Long id);

    List<SaleDetails> getSaleDetailsBySaleId(Long id);
}
