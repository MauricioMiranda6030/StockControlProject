package com.stock.control.serviceImp;

import com.stock.control.entity.Product;
import com.stock.control.repository.IProductRepository;
import com.stock.control.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product updateProduct(Product product, Long id) {
        Product productToUpdate = productRepository.findById(id).orElseThrow();
        productToUpdate = product;
        productToUpdate.setId(id);

        return productRepository.save(productToUpdate);
    }
}
