package com.example.drinkstore;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductFetcher {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference items;

    public void fetchProductsByCategoryOrdered(String categoryFilter, ProductCallback callback) {
        db.collection("Products")
                .whereEqualTo("category", categoryFilter)
                .orderBy("price")
                .get()
                .addOnCompleteListener(task -> {
                    List<Product> products = new ArrayList<>();

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Product product = document.toObject(Product.class);
                            products.add(product);
                        }
                    }

                    callback.onProductsReady(products);
                });
    }
}
