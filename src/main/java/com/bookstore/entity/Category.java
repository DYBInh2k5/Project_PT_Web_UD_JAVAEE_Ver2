package com.bookstore.entity;

import jakarta.persistence.*;
import java.util.List;

// Lưu danh mục sách (VD: Kinh tế, Văn học, Khoa học...)
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                     // ID tự tăng

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(100)")
    private String name;                    // Tên danh mục (duy nhất)

    @OneToMany(mappedBy = "category")
    private List<Product> products;         // Danh sách sách thuộc danh mục này

    public Category() {}

    public Category(String name) { this.name = name; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
