package com.stock.control.service;

import com.stock.control.entity.SaleDetails;

import java.util.List;

public interface ISaleDetailsService {

    SaleDetails saveSaleDetails(SaleDetails saleDetails);

    SaleDetails getSaleDetailsByID(Long id);

    List<SaleDetails> getAllSaleDetails();

    void deleteSaleDetailsByID(Long id);

    SaleDetails updateSaleDetails(SaleDetails saleDetails, Long id);


}
