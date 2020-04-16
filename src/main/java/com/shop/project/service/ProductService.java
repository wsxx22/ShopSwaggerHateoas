package com.shop.project.service;

import com.shop.project.dto.CreateProductDTO;
import com.shop.project.entity.Product;
import com.shop.project.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> addProduct(CreateProductDTO createProductDTO) {
        Boolean isProductExists = productRepository.existsByName(createProductDTO.getName());
        return isProductExists ? Optional.empty() : Optional.of(saveProduct(createProductDTO));
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    public Optional<Product> updateProduct(Product product) {
        Optional<Product> updateProductOptional = productRepository.findById(product.getId()); // walidacja isBlank w inpucie
        return updateProductOptional.map(p -> productRepository.save(product));
    }

    public Optional<Product> buyProduct(Product orderProduct) {
        Optional<Product> productById = productRepository.findById(orderProduct.getId());

        return productById
                .filter(product -> orderProduct.getAmount() <= product.getAmount())
                .map(product -> {
                    product.setAmount(product.getAmount() - orderProduct.getAmount());
                    productRepository.save(product);
                    return orderProduct;
                });
    }

    private Product saveProduct(CreateProductDTO createProductDTO) {
        Product product = Product.builder()
                .name(createProductDTO.getName())
                .price(createProductDTO.getPrice())
                .amount(createProductDTO.getAmount())
                .build();
        return productRepository.save(product);
    }

}
