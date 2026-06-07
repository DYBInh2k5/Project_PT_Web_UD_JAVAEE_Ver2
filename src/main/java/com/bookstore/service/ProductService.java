package com.bookstore.service;

import com.bookstore.entity.Product;
import com.bookstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> findAll(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public Page<Product> findFiltered(String name, Integer categoryId, int page, int size, String sort) {
        Pageable pageable;
        if ("asc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("price").ascending());
        } else if ("desc".equals(sort)) {
            pageable = PageRequest.of(page, size, Sort.by("price").descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by("id").descending());
        }
        if (name != null && !name.trim().isEmpty() && categoryId != null) {
            return productRepository.findByNameContainingIgnoreCaseAndCategoryId(name.trim(), categoryId, pageable);
        } else if (name != null && !name.trim().isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
        } else if (categoryId != null) {
            return productRepository.findByCategoryId(categoryId, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public Product findById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Integer id) {
        productRepository.deleteById(id);
    }
}
