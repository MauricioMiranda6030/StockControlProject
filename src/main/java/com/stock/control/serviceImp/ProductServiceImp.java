package com.stock.control.serviceImp;

import com.stock.control.dto.ProductDTO;
import com.stock.control.entity.Product;
import com.stock.control.mapper.IProductMapper;
import com.stock.control.repository.IProductRepository;
import com.stock.control.service.IProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateStock(List<ProductDTO> productsDto) {
        for (ProductDTO p : productsDto){
            productRepository.updateStock(p.getId(),p.getStock() - p.getAmountToSell());
        }
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
    public List<ProductDTO> getProductsDtoByName(String name) {
        return getProductsByName(name).stream()
                .map(IProductMapper.INSTANCE::productToProductDto)
                .toList();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
}
