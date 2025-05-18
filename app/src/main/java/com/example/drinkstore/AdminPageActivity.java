package com.example.drinkstore;

import static com.example.drinkstore.UserIndexActivity.categories;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminPageActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CollectionReference items;

    private EditText categoryEditText;
    private EditText nameEditText;
    private EditText priceEditText;

    private Button addButton;
    private Button updateButton;
    private Button deleteButton;

    private List<Product> products = new ArrayList<>();
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        // Bind views
        categoryEditText = findViewById(R.id.editTextCategory);
        nameEditText = findViewById(R.id.editTextName);
        priceEditText = findViewById(R.id.editTextPrice);
        addButton = findViewById(R.id.buttonAdd);
        updateButton = findViewById(R.id.buttonUpdate);
        deleteButton = findViewById(R.id.buttonDelete);

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.itemRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(products);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        items = db.collection("Products");

        loadProducts();

        // Add button logic
        addButton.setOnClickListener(v -> {
            String category = categoryEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();

            if (category.isEmpty() || name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            int price;
            try {
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Price must be a number!", Toast.LENGTH_SHORT).show();
                return;
            }

            Product newProduct = new Product(name, price, category);

            db.collection("Products")
                    .add(newProduct)
                    .addOnSuccessListener(documentReference -> {
                        products.add(newProduct);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Product added successfully.", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        // Update button logic
        updateButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String category = categoryEditText.getText().toString().trim();
            String priceStr = priceEditText.getText().toString().trim();

            if (name.isEmpty() || category.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            int price;
            try {
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Price must be a number!", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("Products")
                    .whereEqualTo("name", name)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(this, "No product found with this name.", Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                doc.getReference().update("category", category, "price", price);
                            }
                            Toast.makeText(this, "Product updated successfully.", Toast.LENGTH_SHORT).show();
                            recreate();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });

        // Delete button logic
        deleteButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter the product name!", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("Products")
                    .whereEqualTo("name", name)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Toast.makeText(this, "No product found with this name.", Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                doc.getReference().delete();
                            }
                            Toast.makeText(this, "Product deleted successfully.", Toast.LENGTH_SHORT).show();
                            recreate();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }

    private void loadProducts() {
        products.clear();
        ProductFetcher fetcher = new ProductFetcher();

        for (int i = 0; i < categories.size(); i++) {
            fetcher.fetchProductsByCategoryOrdered(categories.get(i), new ProductCallback() {
                @Override
                public void onProductsReady(List<Product> fetchedProducts) {
                    products.addAll(fetchedProducts);
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                }
            });
        }
    }
}
