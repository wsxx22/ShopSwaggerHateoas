package com.shop.project.controller;

import com.shop.project.dto.CreateProductDTO;
import com.shop.project.entity.Product;
import com.shop.project.service.ProductService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<Product>> getAllProducts(){
        List<Product> products = productService.findAllProducts();
        products.forEach(product -> product.addIf(!product.hasLinks(), () -> linkTo(ProductController.class).slash(product.getId()).withSelfRel()));
        Link link = linkTo(ProductController.class).withSelfRel();
        return ResponseEntity.ok(new CollectionModel<>(products, link));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Optional<Product> productOptional = productService.findById(id);
        return productOptional.map(product -> {
            addLinkToProduct(product);
            return ResponseEntity.ok(product);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<Product> addNewProduct(@RequestBody CreateProductDTO createProductDTO) {
        Optional<Product> addedProduct = productService.addProduct(createProductDTO);
        return addedProduct.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        Optional<Product> productUpdatedOptional = productService.updateProduct(product);
        return productUpdatedOptional.map(productUpdated -> {
            addLinkToProduct(productUpdated);
            return ResponseEntity.ok(productUpdated);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private void addLinkToProduct(Product product) {
        product.add(linkTo(ProductController.class).slash(product.getId()).withSelfRel());
    }

}
