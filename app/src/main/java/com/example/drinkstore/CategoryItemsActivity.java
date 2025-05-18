
package com.example.drinkstore;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemsActivity extends AppCompatActivity {

    private FirebaseFirestore db ;
    private CollectionReference items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);

        String category = getIntent().getStringExtra("category");
        TextView title = findViewById(R.id.categoryTitle);
        title.setText(category);

        RecyclerView recyclerView = findViewById(R.id.itemsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Product> products = new ArrayList<>();
        ProductAdapter adapter = new ProductAdapter(products);
        recyclerView.setAdapter(adapter);


        db=FirebaseFirestore.getInstance();
        items= db.collection("Products");
        ProductFetcher fetcher = new ProductFetcher();
        fetcher.fetchProductsByCategoryOrdered(category, new ProductCallback() {
            @Override
            public void onProductsReady(List<Product> fetchedProducts) {
                products.clear();
                products.addAll(fetchedProducts);

                runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        });
    }


    public void cancel(View view) {
        finish();
    }
}
