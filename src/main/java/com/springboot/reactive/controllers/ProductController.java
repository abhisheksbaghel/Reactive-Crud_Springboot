package com.springboot.reactive.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.reactive.dtos.ProductDto;
import com.springboot.reactive.dtos.ProductJson;
import com.springboot.reactive.entities.Product;
import com.springboot.reactive.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WebClient webClient;

    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/get/{id}")
    public Mono<Product> getProductById(@PathVariable int id){
        return productRepository.findById(id);
    }

    @GetMapping("/get-all")
    public Flux<Product> getAllProduct(){
        return productRepository.findAll()
                .log("LOG : : Executing from getAllProduct");
    }

    @PostMapping("/add-product")
    public Mono<Product> saveProduct(@RequestBody ProductDto productDto){
        return productRepository
                .save(objectMapper.convertValue(productDto, Product.class));
    }
    @GetMapping("/get-product-data")
    public Flux<Product> getProductJson(){
        return webClient.get().retrieve()
                .bodyToFlux(ProductJson.class)
                .flatMap(productJson -> Flux.fromIterable(productJson.getProducts()))
                .flatMap(productDto ->
                        productRepository.save(objectMapper.convertValue(productDto, Product.class)));
    }

}
