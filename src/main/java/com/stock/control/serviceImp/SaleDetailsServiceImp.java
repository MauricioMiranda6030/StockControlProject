package com.stock.control.serviceImp;

import com.stock.control.entity.SaleDetails;
import com.stock.control.repository.ISaleDetailsRepository;
import com.stock.control.service.ISaleDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleDetailsServiceImp implements ISaleDetailsService{

    @Autowired
    private ISaleDetailsRepository saleDetailsRepository;

    @Override
    public SaleDetails saveSaleDetails(SaleDetails saleDetails) {
        return saleDetailsRepository.save(saleDetails);
    }

    @Override
    public SaleDetails getSaleDetailsByID(Long id) {
        return saleDetailsRepository.findById(id).orElseThrow();
    }

    @Override
    public List<SaleDetails> getAllSaleDetails() {
        return saleDetailsRepository.findAll();
    }

    @Override
    public void deleteSaleDetailsByID(Long id) {
        saleDetailsRepository.deleteById(id);
    }

    @Override
    public SaleDetails updateSaleDetails(SaleDetails saleDetails, Long id) {
        SaleDetails saleDetailsUpdate = saleDetailsRepository.findById(id).orElseThrow();
        saleDetailsUpdate = saleDetails;
        saleDetails.setId(id);

        return saleDetailsRepository.save(saleDetailsUpdate);
    }
}
