package com.stock.control.serviceImp;

import com.stock.control.dto.SaleDTO;
import com.stock.control.entity.Sale;
import com.stock.control.mapper.ISaleMapper;
import com.stock.control.repository.ISaleRepository;
import com.stock.control.service.ISaleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SaleServiceImp implements ISaleService {

    @Autowired
    private ISaleRepository saleRepository;

    @Override
    @Transactional
    public Long saveSale(SaleDTO saleDto){
        Sale sale = ISaleMapper.INSTANCE.saleDtoToSale(saleDto);
        sale.setDateOfSale(LocalDate.now());

        return saleRepository.save(sale).getId();
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
    public List<SaleDTO> getAllDtoSales() {
        return getAllSales().stream()
                .map(ISaleMapper.INSTANCE::saleToSaleDto)
                .toList();
    }

    @Override
    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }
}
