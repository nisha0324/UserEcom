package com.example.userproductscart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.userproductscart.databinding.ActivityMainBinding;
import com.example.userproductscart.dialog.UserDetailsDialog;
import com.example.userproductscart.fcmSender.MessageFormatter;
import com.example.userproductscart.fcmSender.MyFCMsender;
import com.example.userproductscart.model.Cart;
import com.example.userproductscart.model.CartItem;
import com.example.userproductscart.model.Inventory;
import com.example.userproductscart.model.Order;
import com.example.userproductscart.model.Product;
import com.example.userproductscart.model.Variant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private MyApp app;
    private Cart cart = new Cart() ;
    private List<Product> products;
    Order order = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        app = (MyApp) getApplicationContext();
        loadData();

        FirebaseMessaging.getInstance()
                .subscribeToTopic("users");

        b.checkoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "ButtonClicked", Toast.LENGTH_SHORT).show();
                showUserDetailsMenu();
               // sendData();
            }
        });

    }

    private void showUserDetailsMenu() {
        new UserDetailsDialog()
                .show(MainActivity.this, order, cart, new UserDetailsDialog.OnUserDataEditedListener() {
                    @Override
                    public void onUserDataEntered(Order order) {

                        //orderId set
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        order.orderId = " " + currentUser.getUid()+ order.orderPlacedTs;


                        app.db.collection("Orders")
                                .document()
                                .set(order)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                                        app.hideLoadingDialog();
                                        sendNotification();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, "Failed to save on cloud", Toast.LENGTH_SHORT).show();
                                        app.hideLoadingDialog();
                                        e.printStackTrace();
                                    }
                                });

                    }

                    @Override
                    public void onCancelled() {

                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void sendNotification() {
        String message = MessageFormatter.getSampleMessage("Test1","Test1","users");

        new MyFCMsender()
                .send(message, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Failure")
                                        .setMessage(e.toString())
                                        .show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("Success")
                                        .setMessage(response.toString())
                                        .show();
                            }
                        });
                    }
                });
    }

    private void loadData() {
        if(app.isOffline()){
            app.showToast(this, "Unable to save. You are offline!");
            return;
        }

        fetchData();
    }

    private void fetchData() {
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

        ProductsAdaptor adaptor = new ProductsAdaptor(this, products, cart);
        b.productList.setAdapter(adaptor);
        b.productList.setLayoutManager(new LinearLayoutManager(this));
    }



    public void updateCartSummary(){
        if(cart.noOfItems == 0){
            b.checkout.setVisibility(View.GONE);
        } else {
            b.checkout.setVisibility(View.VISIBLE);

            b.cartSummary.setText("Total : Rs. " + cart.subTotal + "\n" + cart.noOfItems + " items");
        }
    }


   /* public void setUpCheckOut(){
          b.checkout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                    sendData();
              }
          });
    }*/

    private void sendData() {
        if(app.isOffline()){
            app.showToast(this, "Unable to save. You are offline!");
            return;
        }

        app.showLoadingDialog(this);

        //from cart to order
        order.cartItems = new ArrayList<>(cart.map.values());
        order.subTotal = cart.subTotal;


        //orderId set
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        order.orderId = " " + currentUser.getUid()+ order.orderPlacedTs;


        app.db.collection("Orders")
                .document()
                .set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                        app.hideLoadingDialog();
                        sendNotification();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to save on cloud", Toast.LENGTH_SHORT).show();
                        app.hideLoadingDialog();
                        e.printStackTrace();
                    }
                });

    }

}