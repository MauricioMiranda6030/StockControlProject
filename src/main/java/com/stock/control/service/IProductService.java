package com.stock.control.service;

import com.stock.control.entity.Product;

import java.util.List;

public interface IProductService {

    Product saveProduct(Product product);

    List<Product> getAllProducts();

    List<Product> getProductsByName(String name);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProduct(Product product, Long id);
}
