package com.vietphuongdo.shopapp.services;

import com.vietphuongdo.shopapp.dtos.ProductDTO;
import com.vietphuongdo.shopapp.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService{
    @Override
    public Product createProduct(ProductDTO productDTO) {
        return null;
    }

    @Override
    public Product getProductById(Long id) {
        return null;
    }

    @Override
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        return null;
    }

    @Override
    public Product updateProduct(ProductDTO productDTO, Long id) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }

    @Override
    public boolean existByName(String name) {
        return false;
    }
}
