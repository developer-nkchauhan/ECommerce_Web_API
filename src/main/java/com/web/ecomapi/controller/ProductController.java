package com.web.ecomapi.controller;

import com.web.ecomapi.model.Product;
import com.web.ecomapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService service;

//    @Autowired
    /*ProductController(ProductService productService) {
        this.service = productService;
    }*/

    /*@RequestMapping("/")
    public String greet() {
        return "Hello, Welcome to EcomAPI Project.";
    }*/

    @GetMapping("/products")
    public ResponseEntity<List<Product>> fetchProducts() {
        return new ResponseEntity<>(service.getProducts(), HttpStatus.OK);
    }

    @PostMapping("/products")
    public void addProduct(@RequestBody Product product) {
        service.addProduct(product);
    }

    @GetMapping("/products/{prodId}")
    public ResponseEntity<Product> fetchProductById(@PathVariable int prodId) {
        Product product = service.getProductById(prodId);
        if(product != null) {
            return new ResponseEntity<>(product, HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile) {
        try {
            System.out.println("Hit the AddProduct method..");
            Product prod = service.addProductWithImage(product,imageFile);
            return new ResponseEntity<>(prod,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int id) {
        Product prod = service.getProductById(id);
        if(prod == null) {
            return ResponseEntity.notFound().build();
        }
        byte[] imageFile = prod.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(prod.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/product/{prodId}/image")
    public ResponseEntity<String> updateImageByProductId(@PathVariable int prodId,@RequestPart Product product,@RequestPart MultipartFile imageFile) {
        try {
            Product prod = service.updateProductById(prodId,product,imageFile);
            if(prod == null) {
                throw new Exception("Given id not found");
            }
            return new ResponseEntity<>("Updated Successfully",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/product/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String value) {
        List<Product> products = service.searchProducts(value);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

}
