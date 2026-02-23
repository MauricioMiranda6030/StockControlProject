package com.stock.control.serviceImp;

import com.stock.control.dto.ProductForSaleDTO;
import com.stock.control.dto.ProductSaveDTO;
import com.stock.control.entity.Product;
import com.stock.control.front.tools.CurrencyFormater;
import com.stock.control.mapper.IProductMapper;
import com.stock.control.repository.IProductRepository;
import com.stock.control.service.IProductService;
import com.stock.control.service.ISaleDetailsService;
import com.stock.control.util.PdfGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements IProductService {

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ISaleDetailsService saleDetailsService;

    @Override
    public void saveProduct(ProductSaveDTO productDto) {
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
    @Transactional
    public void addStock(ProductForSaleDTO productDto) {
        productRepository.updateStock(productDto.getId(), productDto.getStock());
    }

    @Override
    public void createPdfReport() {
        List<ProductSaveDTO> productsDto = this.getAllProductsDto();
        String totalAmountProducts = productsDto.size() + " Productos";

        Double totalPrice = productsDto.stream().mapToDouble(prod -> prod.getPrice() * prod.getStock()).sum();
        String totalPriceFormatted = CurrencyFormater.getCurrency(totalPrice);

        PdfGenerator.createProductsReportPdf(productsDto, totalAmountProducts, totalPriceFormatted);
    }

    @Override
    public void deleteProduct(ProductSaveDTO productDto) {
        Product product =  IProductMapper.INSTANCE.productSaveDtoToProduct(productDto);
        boolean productInSales = saleDetailsService.productExists(product);

        if(productInSales)
            throw new IllegalStateException("Cannot delete a product related to sales");
        else
            productRepository.deleteById(product.getId());
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductSaveDTO> getAllProductsDto() {
        return toDtoList(getAllProducts());
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<ProductSaveDTO> getProductsByNameSaveDto(String name) {
        return toDtoList(getProductsByName(name));
    }

    private List<ProductSaveDTO> toDtoList(List<Product> products){
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
