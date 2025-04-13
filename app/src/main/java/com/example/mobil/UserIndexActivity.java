package com.example.mobil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class UserIndexActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_index);

        RecyclerView recyclerView = findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<String> categories = new ArrayList<>();
        categories.add("Wine");
        categories.add("Beer");
        categories.add("Vodka");
        categories.add("Whiskey");
        categories.add("Fruit Liquor");
        categories.add("Beverage");
        categories.add("Fruit Juice");
        categories.add("Coffee");

        CategoryAdapter adapter = new CategoryAdapter(categories, this::onCategorySelected);
        recyclerView.setAdapter(adapter);
    }
    private void onCategorySelected(String category) {
        Intent intent = new Intent(this, CategoryItemsActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
