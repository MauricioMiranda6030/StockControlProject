package com.stock.control.serviceImp;

import com.stock.control.entity.Sale;
import com.stock.control.repository.ISaleRepository;
import com.stock.control.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleServiceImp implements ISaleService {

    @Autowired
    private ISaleRepository saleRepository;

    @Override
    public Sale saveSale(Sale sale) {
        return saleRepository.save(sale);
    }

    @Override
    public Sale getSale(Long id) {
        return saleRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }

    @Override
    public Sale updateSale(Sale sale, Long id) {
        Sale saleToUpdate = saleRepository.findById(id).orElseThrow();
        saleToUpdate = sale;
        saleToUpdate.setId(id);

        return saleRepository.save(saleToUpdate);
    }
}
