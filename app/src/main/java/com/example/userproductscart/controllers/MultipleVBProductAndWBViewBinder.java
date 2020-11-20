package com.example.userproductscart.controllers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.userproductscart.MainActivity;
import com.example.userproductscart.VariantPickerDialog;
import com.example.userproductscart.WeightPicker;
import com.example.userproductscart.databinding.ActivityMainBinding;
import com.example.userproductscart.databinding.ProductItemWbMultiVbBinding;
import com.example.userproductscart.databinding.WeightPickerDialogBinding;
import com.example.userproductscart.model.Cart;
import com.example.userproductscart.model.Product;

public class MultipleVBProductAndWBViewBinder {

    private ProductItemWbMultiVbBinding b;
    private Cart cart;
    private Product product;

    public void bind(ProductItemWbMultiVbBinding b, Product product, Cart cart){
        this.b = b;
        this.product = product;
        this.cart = cart;

        b.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });

        b.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        if (product.type == Product.WEIGHT_BASED)
            showWeightPickerDialog();
        else
            showVariantPickerDialog();
    }

    private void showVariantPickerDialog() {
        Context context = b.getRoot().getContext();

        new VariantPickerDialog()
                .show(context, cart, product, new VariantPickerDialog.OnVariantPickedListener() {
                    @Override
                    public void onQtyUpdated(int qty) {
                        updateQty(qty + "");
                    }

                    @Override
                    public void onRemoved() {
                             hideQty();
                    }
                });
    }

    private void showWeightPickerDialog() {
        Context context = b.getRoot().getContext(); //TODO

        new WeightPicker()
                .show(context, cart, product, new WeightPicker.OnWeightPickedListener() {
                    @Override
                    public void onWeightPicked(int kg, int g) {
                        updateQty(kg + "kg " + g + "");
                    }

                    @Override
                    public void onRemoved() {
                           hideQty();
                    }
                });
    }

    private void hideQty() {
        b.qtyGroup.setVisibility(View.GONE);
        b.addBtn.setVisibility(View.VISIBLE);

        updateCheckOutSummary();
    }

    private void updateQty(String s) {
        b.qtyGroup.setVisibility(View.VISIBLE);
        b.addBtn.setVisibility(View.GONE);

        b.quantity.setText(s);
        updateCheckOutSummary();
    }

    private void updateCheckOutSummary() {
        Context context = b.getRoot().getContext();

        if (context instanceof MainActivity){
            ((MainActivity) context).updateCartSummary();
        }else
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
    }
}
