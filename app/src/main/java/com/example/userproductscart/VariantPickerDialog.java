package com.example.userproductscart;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.userproductscart.databinding.DialogVariantPickerBinding;
import com.example.userproductscart.databinding.VariantItemBinding;
import com.example.userproductscart.model.Cart;
import com.example.userproductscart.model.Product;
import com.example.userproductscart.model.Variant;

public class VariantPickerDialog {

    private Context context;
    private Cart cart;
    private Product product;
    private DialogVariantPickerBinding b;

    public void show(Context context, Cart cart, Product product, final OnWeightPickedListener listener){
        this.context = context;
        this.cart = cart;
        this.product = product;

        b = DialogVariantPickerBinding.inflate(LayoutInflater.from(context));

        new AlertDialog.Builder(context)
                .setTitle("Pick Variants")
                .setView(b.getRoot())
                .setPositiveButton("SELECT",null)
                .show();

        showVariants();

    }

    private void showVariants() {

        for(Variant variant: product.variants){
            VariantItemBinding ib = VariantItemBinding.inflate(LayoutInflater.from(context)
                                                                    , b.getRoot()
                                                                    , true);

            ib.variantName.setText(variant.toString());

            setUpButtons(variant , ib);
        }
    }

    private void setUpButtons(Variant variant, VariantItemBinding ib) {
        ib.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = cart.addToCart(product, variant);
                ib.qty.setText(qty + "");

                if (qty == 1){
                    ib.remove.setVisibility(View.VISIBLE);
                    ib.qty.setVisibility(View.VISIBLE);
                }
            }
        });

        ib.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = cart.removeFromCart(product, variant);
                ib.qty.setText(qty+"");

                if (qty == 0){
                    ib.remove.setVisibility(View.GONE);
                    ib.qty.setVisibility(View.GONE);
                }
            }
        });
    }

    interface OnWeightPickedListener{
        void onWeightPicked();
        void onWeightPickerCancelled();
    }
}
