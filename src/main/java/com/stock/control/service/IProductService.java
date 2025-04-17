package com.stock.control.service;

import com.stock.control.dto.ProductForSaleDTO;
import com.stock.control.dto.ProductSaveDto;
import com.stock.control.entity.Product;

import java.util.List;

public interface IProductService {

    void saveProduct(ProductSaveDto productDto);

    List<Product> getAllProducts();

    List<ProductSaveDto> getAllProductsDto();

    List<Product> getProductsByName(String name);

    List<ProductSaveDto> getProductsByNameSaveDto(String name);

    List<ProductForSaleDTO> getProductsDtoByName(String name);

    void updateStock(List<ProductForSaleDTO> productsDto);

    void createPdfReport();

    void deleteProduct(ProductSaveDto productDto);
}
