package com.example.konect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private TextView txtRegister;
    private EditText edtEmail, edtPassword;
    private Button btnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initUI();
        initListener();
    }

    private void initUI() {
        txtRegister = findViewById(R.id.txt_register);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
    }

    private void initListener() {
        txtRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });

        btnSignIn.setOnClickListener(view -> {
            if (edtEmail.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this,"Mời bạn nhập Email !",Toast.LENGTH_SHORT).show();
            } else if (edtPassword.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this,"Email hoặc mật khẩu không đúng!",Toast.LENGTH_SHORT).show();
            }
            else{
                onClickSignIn();
            }
        });
    }

    private void onClickSignIn() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();


        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(LoginActivity.this,"Email hoặc mật khẩu không đúng!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}