package com.example.konect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword, edtConfirm;
    private Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();
        initListener();

    }

    private void initUI() {
        edtEmail = findViewById(R.id.edt_email_sign_up);
        edtPassword = findViewById(R.id.edt_password_sign_up);
        edtConfirm = findViewById(R.id.edt_confirm);
        btnRegister = findViewById(R.id.btn_sign_up);
    }

    private void initListener() {
        btnRegister.setOnClickListener(view -> {
            if(edtEmail.getText().toString().isEmpty()){
                Toast.makeText(SignUpActivity.this,"mời bạn nhập Email !",Toast.LENGTH_SHORT).show();
            }
            else if (!edtPassword.getText().toString().trim().equals(edtConfirm.getText().toString().trim())) {
                Toast.makeText(SignUpActivity.this,"mật khẩu xác nhận không đúng xin nhập lại !",Toast.LENGTH_SHORT).show();
            }else{
                onClickSignUp();
            }
        });
    }

    private void onClickSignUp() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Đăng ký thất bại.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });

    }
}