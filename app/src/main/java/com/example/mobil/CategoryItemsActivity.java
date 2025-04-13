
package com.example.mobil;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CategoryItemsActivity extends AppCompatActivity {

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
        for (int i = 1; i <= 5; i++) {
            products.add(new Product(category + " Product " + i, 990 + i * 100,category));
        }

        ProductAdapter adapter = new ProductAdapter(products);
        recyclerView.setAdapter(adapter);
    }
}
