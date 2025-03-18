package com.stock.control.serviceImp;

import com.stock.control.dto.SaleDTO;
import com.stock.control.dto.SaleViewDTO;
import com.stock.control.entity.Sale;
import com.stock.control.mapper.ISaleMapper;
import com.stock.control.repository.ISaleRepository;
import com.stock.control.service.ISaleDetailsService;
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

    @Autowired
    private ISaleDetailsService saleDetailsService;

    @Override
    @Transactional
    public Long saveSale(SaleDTO saleDto){
        Sale sale = ISaleMapper.INSTANCE.saleDtoToSale(saleDto);
        sale.setDateOfSale(LocalDate.now());

        return saleRepository.save(sale).getId();
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public List<Sale> getSalesByDate(LocalDate date) {
        return saleRepository.findAllByDateOfSaleBetween(date, LocalDate.now());
    }

    @Override
    public List<SaleViewDTO> getAllSalesViewDto() {
        return toSalesViewDto(getAllSales());
    }

    @Override
    public List<SaleViewDTO> getSalesByDateDto(LocalDate date) {
        return toSalesViewDto(getSalesByDate(date));
    }

    private List<SaleViewDTO> toSalesViewDto(List<Sale> sales){
        return sales.stream()
                .map(s -> new SaleViewDTO(s,saleDetailsService.getSaleDetailsBySaleId(s.getId())))
                .toList();
    }
}
