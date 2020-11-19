package com.example.userproductscart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.userproductscart.databinding.ActivityMainBinding;
import com.example.userproductscart.model.Cart;
import com.example.userproductscart.model.Product;
import com.example.userproductscart.model.Variant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setUpProductsList();
    }

    private void showVariantPicker() {
        Product product = new Product("rice");
        product.variants = Arrays.asList(
                new Variant("1kg", 50)
                , new Variant("5kg", 250)
        );

         new VariantPickerDialog().show(this,new Cart(), product,null);

    }

    private void setUpProductsList() {
        List<Product> products = getProducts();

        ProductsAdaptor adaptor = new ProductsAdaptor(this, products);
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