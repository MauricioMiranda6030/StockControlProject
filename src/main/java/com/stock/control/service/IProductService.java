package com.stock.control.service;

import com.stock.control.dto.ProductDTO;
import com.stock.control.entity.Product;

import java.util.List;

public interface IProductService {

    void saveProduct(Product product);

    void saveAllProducts(List<ProductDTO> products);

    List<Product> getAllProducts();

    List<Product> getProductsByName(String name);

    List<ProductDTO> getProductsDtoByName(String name);

    Product getProductById(Long id);

    void deleteProductById(Long id);
}
