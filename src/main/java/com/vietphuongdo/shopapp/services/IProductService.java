package com.vietphuongdo.shopapp.services;

import com.vietphuongdo.shopapp.dtos.ProductDTO;
import com.vietphuongdo.shopapp.dtos.ProductImageDTO;
import com.vietphuongdo.shopapp.entities.Product;
import com.vietphuongdo.shopapp.entities.ProductImage;
import com.vietphuongdo.shopapp.exception.DataNotFoundException;
import com.vietphuongdo.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    Product getProductById(long id) throws Exception;
    Page<ProductResponse> getAllProducts(String keyword,Long categoryId, PageRequest pageRequest);
    Product updateProduct(long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(long id) throws DataNotFoundException;
    boolean existsByName(String name);
    ProductImage createProductImage(
            Long productId,
            ProductImageDTO productImageDTO) throws Exception;

}
