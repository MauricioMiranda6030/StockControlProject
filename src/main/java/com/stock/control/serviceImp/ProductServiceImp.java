package com.stock.control.serviceImp;

import com.stock.control.dto.ProductForSaleDTO;
import com.stock.control.dto.ProductSaveDto;
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
    public void saveProduct(ProductSaveDto productDto) {
        Product productToSave = IProductMapper.INSTANCE.productSaveDtoToProduct(productDto);
        productRepository.save(productToSave);
    }

    @Override
    @Transactional
    public void updateStock(List<ProductForSaleDTO> productsDto) {
        for (ProductForSaleDTO p : productsDto){
            productRepository.updateStock(p.getId(),p.getStock() - p.getAmountToSell());
        }
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductSaveDto> getAllProductsDto() {
        return toDtoList(getAllProducts());
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<ProductSaveDto> getProductsByNameSaveDto(String name) {
        return toDtoList(getProductsByName(name));
    }

    private List<ProductSaveDto> toDtoList(List<Product> products){
        return  products.stream()
                .map(IProductMapper.INSTANCE::productToProductSaveDto)
                .toList();
    }

    @Override
    public List<ProductForSaleDTO> getProductsDtoByName(String name) {
        return getProductsByName(name).stream()
                .map(IProductMapper.INSTANCE::productToProductDto)
                .toList();
    }
}
