package com.stock.control.service;

import com.stock.control.dto.ProductForSaleDTO;
import com.stock.control.dto.ProductSaveDTO;
import com.stock.control.entity.Product;

import java.util.List;

public interface IProductService {

    void saveProduct(ProductSaveDTO productDto);

    List<Product> getAllProducts();

    List<ProductSaveDTO> getAllProductsDto();

    List<Product> getProductsByName(String name);

    List<ProductSaveDTO> getProductsByNameSaveDto(String name);

    List<ProductForSaleDTO> getProductsDtoByName(String name);

    void updateStock(List<ProductForSaleDTO> productsDto);

    void addStock(ProductForSaleDTO productDto);

    void createPdfReport();

    void deleteProduct(ProductSaveDTO productDto);
}
