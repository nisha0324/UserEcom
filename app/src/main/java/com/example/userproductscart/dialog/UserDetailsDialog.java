package com.example.userproductscart.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.userproductscart.databinding.UserDetailsDialogBinding;
import com.example.userproductscart.model.Cart;
import com.example.userproductscart.model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserDetailsDialog {

    private UserDetailsDialogBinding b;
    private Order order;
    private Cart cart;


    public void show(final Context context, final Order order, final Cart cart, final OnUserDataEditedListener listener){
        this.order = order;
        this.cart = cart;

        //Inflate
        b = UserDetailsDialogBinding.inflate(
                LayoutInflater.from(context)
        );

        //Create dialog
        new AlertDialog.Builder(context)
                .setTitle("Enter User Details")
                .setView(b.getRoot())
                .setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(areProductDetailsValid())
                            listener.onUserDataEntered(UserDetailsDialog.this.order);
                        else
                            Toast.makeText(context, "Invalid details!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onCancelled();
                    }
                })
                .show();

        preFillPreviousDetails();
    }

    private void preFillPreviousDetails() {

        b.userName.setText(order.userName);
        b.userAddress.setText(order.UserAddress);
        b.userPhoneNo.setText(order.userPhoneNo);

    }

    private boolean areProductDetailsValid() {

        String address = b.userAddress.getText().toString().trim();
        String phoneNo = b.userPhoneNo.getText().toString().trim();
        String name = b.userName.getText().toString().trim();

        if (!(name.isEmpty() && address.isEmpty() && phoneNo.isEmpty())){

            //from cart to order
            order.cartItems = new ArrayList<>(cart.map.values());
            order.subTotal = cart.subTotal;


            order.inItOrder(name, phoneNo, address);

            return true;
        }

        return false;
    }


   public interface OnUserDataEditedListener{
        void onUserDataEntered(Order order);
        void onCancelled();
    }
}
