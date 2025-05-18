package com.example.drinkstore;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserIndexActivity extends AppCompatActivity {

    public static List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_index);

        RecyclerView recyclerView = findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

         categories = new ArrayList<>();
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

        Intent intent = new Intent(this,AdminPageActivity.class);
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            if (Objects.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "admin@admin.com")){ //pass:admin1
                startActivity(intent);
            }
        }
    }
    private void onCategorySelected(String category) {
        Intent intent = new Intent(this, CategoryItemsActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void saveShopContact(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_CONTACTS}, 1010);
        } else {
            insertShopContact();
        }
    }

    private void insertShopContact() {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        "ItalDiszkont Központ")
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, "+36 1 234 5678")
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(this, "Kapcsolat elmentve!", Toast.LENGTH_SHORT).show();
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
            Toast.makeText(this, "Hiba történt a mentéskor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1010 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            insertShopContact();
        } else {
            Toast.makeText(this, "A névjegy mentéséhez engedély szükséges", Toast.LENGTH_SHORT).show();
        }
    }

}
