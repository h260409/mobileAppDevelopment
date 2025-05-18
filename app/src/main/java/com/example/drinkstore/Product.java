
package com.example.drinkstore;


public class Product {
    public String name;
    public Double price;
    public String category;

    public Product() {
    }

    public Product(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category=category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
