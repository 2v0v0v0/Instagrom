package com.fbu.instagrom.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fbu.instagrom.R;
import com.fbu.instagrom.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if(ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick login button");
                String username = binding.usernameEditText.getText().toString();
                String password = binding.passwordEditText.getText().toString();
                loginUser(username, password, view);
            }
        });

        binding.sigupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick sign up button");
                goSignUpActivity();
            }
        });
    }

    private void loginUser(String name, String password, final View v){
        Log.i(TAG, "Attempting to login user: " + name);

        ParseUser.logInInBackground(name, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Snackbar snackbar = Snackbar.make(v , R.string.loginFail, Snackbar.LENGTH_LONG);
                    snackbar.show();
                    binding.passwordEditText.setError("Invalid password");
                    binding.usernameEditText.setError("Invalid username");
                    Log.e(TAG, "Issue with login", e);
                    return;
                }
                goMainActivity();
                Toast.makeText(LoginActivity.this,"Login Success!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void goSignUpActivity(){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }
}