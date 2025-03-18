package com.stock.control.mapper;


import com.stock.control.dto.ProductForSaleDTO;
import com.stock.control.dto.ProductSaveDto;
import com.stock.control.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IProductMapper {

    IProductMapper INSTANCE = Mappers.getMapper(IProductMapper.class);

    @Mapping(source = "name", target = "name")
    ProductForSaleDTO productToProductDto(Product product);
    Product productDtoToProduct(ProductForSaleDTO productForSaleDto);

    ProductSaveDto productToProductSaveDto(Product product);
    Product productSaveDtoToProduct(ProductSaveDto productSaveDto);

}
