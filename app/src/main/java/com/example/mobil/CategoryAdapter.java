package com.example.mobil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public static final Map<String, Integer> categoryIcons = new HashMap<>();

    public interface OnCategoryClickListener {
        void onCategoryClick(String category);
    }

    private List<String> categories;
    private OnCategoryClickListener listener;

    public CategoryAdapter(List<String> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;

        categoryIcons.put("Wine", R.drawable.wine_category);
        categoryIcons.put("Beer", R.drawable.beer_category);
        categoryIcons.put("Whiskey", R.drawable.whiskey_category);
        categoryIcons.put("Vodka", R.drawable.vodka_category);
        categoryIcons.put("Coffee",R.drawable.coffee_category);
        categoryIcons.put("Fruit Liquor",R.drawable.fruit_liquor_category);
        categoryIcons.put("Fruit Juice",R.drawable.fruit_juice_category);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.categoryName.setText(category);

        Integer iconRes = categoryIcons.get(category);
        if (iconRes != null) {
            holder.categoryIcon.setImageResource(iconRes);
        } else {
            holder.categoryIcon.setImageResource(R.drawable.default_category);
        }

        holder.itemView.setOnClickListener(v -> listener.onCategoryClick(category));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
        }
    }
}
