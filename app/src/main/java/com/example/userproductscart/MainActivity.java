package com.example.userproductscart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.userproductscart.databinding.ActivityMainBinding;
import com.example.userproductscart.model.Cart;
import com.example.userproductscart.model.Inventory;
import com.example.userproductscart.model.Product;
import com.example.userproductscart.model.Variant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private MyApp app;
    private Cart cart = new Cart() ;
    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        app = (MyApp) getApplicationContext();
        loadData();
        setUpProductsList();
    }

    private void loadData() {
        if(app.isOffline()){
            app.showToast(this, "Unable to save. You are offline!");
            return;
        }

        app.showLoadingDialog(this);

        app.db.collection("inventory")
                .document("products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Inventory inventory = documentSnapshot.toObject(Inventory.class);
                            products = inventory.products;
                        }
                        else
                            products = new ArrayList<>();
                        setUpProductsList();
                        app.hideLoadingDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        app.hideLoadingDialog();
                        app.showToast(MainActivity.this, e.getMessage());
                        e.printStackTrace();
                    }
                });
    }


    private void setUpProductsList() {
        List<Product> products = getProducts();

        ProductsAdaptor adaptor = new ProductsAdaptor(this, products, cart);
        b.productList.setAdapter(adaptor);
        b.productList.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Product> getProducts() {
       return Arrays.asList(
                new Product("Bread", Arrays.asList(
                        new Variant("big", 10)
                        , new Variant("small", 20)
                        , new Variant("medium", 30)
                ))
                , new Product("Apple", 30, 1)
                , new Product("Kiwi", Arrays.asList(
                        new Variant("1kg", 100)
                ))
        );
    }

    public void updateCartSummary(){
        if(cart.noOfItems == 0){
            b.checkout.setVisibility(View.GONE);
        } else {
            b.checkout.setVisibility(View.VISIBLE);

            b.cartSummary.setText("Total : Rs. " + cart.subTotal + "\n" + cart.noOfItems + " items");
        }
    }
    

}