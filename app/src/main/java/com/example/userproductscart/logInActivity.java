package com.example.userproductscart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.userproductscart.databinding.ActivityLogInBinding;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;


public class logInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 0303;
    ActivityLogInBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setupGoogleSignIn();

    }

    private void setupGoogleSignIn() {
        b.signInGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build()
                );

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN
                );
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("TAG", "" + firebaseUser.getUid());
                startActivity(new Intent(logInActivity.this, MainActivity.class));
            } else {

            }
        }
    }
}