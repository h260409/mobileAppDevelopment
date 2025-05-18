package com.example.drinkstore;

import java.util.List;

public interface ProductCallback {
    void onProductsReady(List<Product> products);
}
