package com.example.userproductscart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    public void show(Context context, Cart cart, Product product, final OnVariantPickedListener listener){
        this.context = context;
        this.cart = cart;
        this.product = product;

        b = DialogVariantPickerBinding.inflate(LayoutInflater.from(context));

        new AlertDialog.Builder(context)
                .setTitle("Pick Variants")
                .setView(b.getRoot())
                .setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int qty = cart.totalVariantQtyMap.get(product.name);
                        if (qty > 0)
                            listener.onQtyUpdated(qty);
                        else
                            listener.onRemoved();
                    }
                })
                .setNegativeButton("REMOVE ALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cart.removeAllVariantsFromCart(product);
                        listener.onRemoved();
                    }
                })
                .show();

        showVariants();

    }

    private void showVariants() {

        for(Variant variant: product.variants){
            VariantItemBinding ib = VariantItemBinding.inflate(LayoutInflater.from(context)
                                                                    , b.getRoot()
                                                                    , true);

            ib.variantName.setText(variant.nameAndPriceString());

            showPreviouslyQty(variant, ib);
            setUpButtons(variant , ib);
        }
    }

    private void showPreviouslyQty(Variant variant, VariantItemBinding ib) {
        int qty = cart.getVariantQty(product, variant);

        if (qty > 0){
            ib.remove.setVisibility(View.VISIBLE);
            ib.qty.setVisibility(View.VISIBLE);

            ib.qty.setText(qty + "");
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

    public interface OnVariantPickedListener {
        void onQtyUpdated(int qty);
        void onRemoved();
    }
}
