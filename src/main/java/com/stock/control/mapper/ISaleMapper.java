package com.stock.control.mapper;

import com.stock.control.dto.SaleDTO;
import com.stock.control.entity.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ISaleMapper {

    ISaleMapper INSTANCE = Mappers.getMapper(ISaleMapper.class);

    @Mapping(target = "products", ignore = true)
    SaleDTO saleToSaleDto(Sale sale);

    @Mapping(target = "id", ignore = true)
    Sale saleDtoToSale(SaleDTO saleDto);
}
