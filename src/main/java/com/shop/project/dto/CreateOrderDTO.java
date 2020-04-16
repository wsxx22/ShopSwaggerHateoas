package com.shop.project.dto;

import com.shop.project.entity.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {
    private List<Product> products;

    public List<Product> getProducts(){
        return new ArrayList<>(products);
    }
}
