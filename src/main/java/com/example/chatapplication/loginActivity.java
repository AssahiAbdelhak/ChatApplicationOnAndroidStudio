package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginActivity extends AppCompatActivity {

     EditText emailInput,pass,phoneNum;
     MaterialButton login_btn;
     TextView forgotPass,haventAcc;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailET);
        phoneNum = findViewById(R.id.phoneET);
        pass = findViewById(R.id.passET);
        login_btn = findViewById(R.id.login_btn);
        forgotPass = findViewById(R.id.forgot_pass_text);
        haventAcc = findViewById(R.id.haventAcc);

        login_btn.setOnClickListener(v -> loginUser());
        haventAcc.setOnClickListener(v -> goToRegesteration());
    }

    private void goToRegesteration() {
        startActivity(new Intent(loginActivity.this,registerActivity.class));
        finish();
    }


    private void loginUser() {
        String email = emailInput.getText().toString();
        String password = pass.getText().toString();
        String phone = phoneNum.getText().toString();
// validate also phone don't forget !!!!!!
        if(validateData(email,password)){
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(loginActivity.this,MainActivity.class);
                        intent.putExtra("phone_num",phone);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(loginActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private boolean validateData(String email, String password) {
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailInput.setError("Please, insert a valid number phone");
            return false;
        }
        if(password.length()<6){
            emailInput.setError("Your password is too short");
            return false;
        }
        return true;
    }
}