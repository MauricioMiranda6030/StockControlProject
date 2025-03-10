package com.stock.control.mapper;

import com.stock.control.dto.SaleDTO;
import com.stock.control.entity.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ISaleMapper {

    ISaleMapper INSTANCE = Mappers.getMapper(ISaleMapper.class);

    SaleDTO saleToSaleDto(Sale sale);
    Sale saleDtoToSale(SaleDTO saleDto);
}
