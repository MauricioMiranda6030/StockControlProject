package com.stock.control.mapper;


import com.stock.control.dto.ProductDTO;
import com.stock.control.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IProductMapper {

    IProductMapper INSTANCE = Mappers.getMapper(IProductMapper.class);

    @Mapping(source = "name", target = "name")
    ProductDTO productToProductDto(Product product);

    Product productDtoToProduct(ProductDTO productDto);

}
