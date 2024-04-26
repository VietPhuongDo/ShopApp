package com.vietphuongdo.shopapp.controllers;

import com.vietphuongdo.shopapp.dtos.ProductDTO;
import com.vietphuongdo.shopapp.dtos.ProductImageDTO;
import com.vietphuongdo.shopapp.entities.Product;
import com.vietphuongdo.shopapp.entities.ProductImage;
import com.vietphuongdo.shopapp.responses.ProductListResponse;
import com.vietphuongdo.shopapp.responses.ProductResponse;
import com.vietphuongdo.shopapp.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    //create a single product
    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //upload image of product
    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ) {
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size()>ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("You can upload max 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                // Kiểm tra kích thước file và định dạng
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                // Lưu file và cập nhật thumbnail trong DTO
                String filename = storeFile(file);
                //lưu vào đối tượng product trong DB
                ProductImage productImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .build()
                );
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //store file image of a single product
    private String storeFile(MultipartFile file) throws IOException {
        if(!isImageFile(file) || file.getOriginalFilename()==null) {
            throw new IOException("Invalid file format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + " _" + filename;
        java.nio.file.Path uploadDir = Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return  contentType != null && contentType.startsWith("image/");
    }

    //get a list of product
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);

        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(
                ProductListResponse.builder()
                        .products(products)
                       .totalPages(totalPages)
                       .build());
    }

    // get a single product
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") long productId
    ) {
        try {
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.productMapper(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    //delete a product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(String.format("Product with id = %d is deleted", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //update a product
    @PutMapping("{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") long id,
            @Valid @RequestBody ProductDTO productDTO
    ) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //generate fake products data
//    @PostMapping("/generateFakeProducts")
//    private ResponseEntity<String> generateFakeProducts() {
//        Faker faker = new Faker();
//        for(int i=0;i<1000;i++){
//            String productName = faker.commerce().productName();
//            if(productService.existsByName(productName)){
//                continue;
//            }
//            ProductDTO productDTO = ProductDTO.builder()
//                   .name(productName)
//                   .price((float)faker.number().numberBetween(10000, 1000000))
//                   .description(faker.lorem().paragraph())
//                    .thumbnail("")
//                   .categoryId((long)faker.number().numberBetween(1,3))
//                   .build();
//            try {
//                productService.createProduct(productDTO);
//            } catch (DataNotFoundException e) {
//                return ResponseEntity.badRequest().body(e.getMessage());
//            }
//        }
//        return ResponseEntity.ok("Fake products generated successfully");
//    }
}
