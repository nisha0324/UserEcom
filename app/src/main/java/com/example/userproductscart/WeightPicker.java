package com.example.userproductscart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.example.userproductscart.databinding.WeightPickerDialogBinding;
import com.example.userproductscart.model.Cart;
import com.example.userproductscart.model.Product;

public class WeightPicker {
    private WeightPickerDialogBinding b;
    private Cart cart;
    private Product product;

    public void show(Context context, Cart cart, Product product, final VariantPickerDialog.OnWeightPickedListener listener){
        b = WeightPickerDialogBinding.inflate(LayoutInflater.from(context));

        this.cart = cart;
        this.product = product;

        new AlertDialog.Builder(context)
                .setTitle("Pick Weight")
                .setView(b.getRoot())
                .setPositiveButton("SeleCT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int kg = b.numberPickerKg.getValue();
                        int g = b.numberPickerG.getValue();

                        if (kg == 0 && g == 0){
                            return;
                        }

                        changeInCart(kg + (g/1000f));
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onWeightPickerCancelled();
                    }
                })
                .show();

        setupNumberPicker();
    }

    private void setupNumberPicker() {
        float quantity = product.minQty;
        b.numberPickerKg.setMinValue((int) (quantity / 1000));
        b.numberPickerKg.setMaxValue(10);
        b.numberPickerG.setMinValue((int) (quantity % 1000) / 50);
        b.numberPickerG.setMaxValue(19);

        b.numberPickerKg.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + " " + "kg";
            }
        });

        b.numberPickerG.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return (value * 50) + " " + "g";
            }
        });

        updateFirstItemViewInNumberPicker(b.numberPickerKg);
        updateFirstItemViewInNumberPicker(b.numberPickerG);

    }

    private void updateFirstItemViewInNumberPicker(NumberPicker numberPickerKg) {
        View firstItem = numberPickerKg.getChildAt(0);
        if (firstItem != null){
            firstItem.setVisibility(View.INVISIBLE);
        }
    }

    private void changeInCart(float qty) {
        cart.updateWeightBasedProductQuantity(product, qty);
    }

    interface OnWeightPickedListener{
        void onWeightPicked(int kg, int g);
        void onWeightPickerCancelled();
    }
}
