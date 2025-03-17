package com.stock.control.service;

import com.stock.control.dto.ProductDTO;
import com.stock.control.entity.Product;

import java.util.List;

public interface IProductService {

    void saveProduct(Product product);

    List<Product> getAllProducts();

    List<Product> getProductsByName(String name);

    List<ProductDTO> getProductsDtoByName(String name);

    void updateStock(List<ProductDTO> productsDto);
}
