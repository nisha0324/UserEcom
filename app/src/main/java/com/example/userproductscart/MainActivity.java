package com.example.userproductscart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.userproductscart.databinding.ActivityMainBinding;
import com.example.userproductscart.model.Cart;
import com.example.userproductscart.model.Product;
import com.example.userproductscart.model.Variant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

         showVariantPicker();
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
        return new ArrayList<>();
    }
}