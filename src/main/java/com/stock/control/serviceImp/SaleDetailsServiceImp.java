package com.stock.control.serviceImp;

import com.stock.control.dto.SaleDTO;
import com.stock.control.entity.Sale;
import com.stock.control.entity.SaleDetails;
import com.stock.control.mapper.IProductMapper;
import com.stock.control.mapper.ISaleMapper;
import com.stock.control.repository.ISaleDetailsRepository;
import com.stock.control.service.ISaleDetailsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleDetailsServiceImp implements ISaleDetailsService{

    @Autowired
    private ISaleDetailsRepository saleDetailsRepository;

    @Override
    @Transactional
    public void saveSaleDetails(SaleDTO saleDto, Long id) {
        Sale sale = ISaleMapper.INSTANCE.saleDtoToSale(saleDto);
        sale.setId(id);

        saleDto.getProducts()
                .forEach(product -> {
                            SaleDetails saleDetails = new SaleDetails();
                            saleDetails.setSale(sale);
                            saleDetails.setProduct(IProductMapper.INSTANCE.productDtoToProduct(product));
                            saleDetails.setQuantity(product.getAmountToSell());

                            saleDetailsRepository.save(saleDetails);
                        }
                );
    }

    @Override
    public List<SaleDetails> getSaleDetailsBySaleId(Long id) {
        return saleDetailsRepository.findAllBySaleId(id);
    }
}
