package com.vietphuongdo.shopapp.services;

import com.vietphuongdo.shopapp.dtos.ProductDTO;
import com.vietphuongdo.shopapp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO);
    Product getProductById(Long id);
    Page<Product> getAllProducts(PageRequest pageRequest);
    Product updateProduct(ProductDTO productDTO,Long id);
    void deleteProduct(Long id);
    boolean existByName(String name);
}
