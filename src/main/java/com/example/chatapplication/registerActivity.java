package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registerActivity extends AppCompatActivity {

    EditText phoneNum,emailAdd,passInput,conformPassInput;
    MaterialButton createAccBtn;
    TextView alreadyHaveAcc;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        alreadyHaveAcc = findViewById(R.id.already_have_ac);
        phoneNum = findViewById(R.id.phone_num);
        emailAdd = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.pass_input);
        conformPassInput = findViewById(R.id.conforme_pass_input);
        createAccBtn = findViewById(R.id.login_btn);

        alreadyHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registerActivity.this,loginActivity.class));
                finish();
            }
        });

        createAccBtn.setOnClickListener(v->createuserAccount());

    }

    private void createuserAccount() {
        String email = emailAdd.getText().toString();
        String password = passInput.getText().toString();
        String phone = phoneNum.getText().toString();

        if(validateData(phone,email,password)){
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(registerActivity.this,"Your Account Has Been Created",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(registerActivity.this,MainActivity.class));
                        finish();
                    }else{
                        Toast.makeText(registerActivity.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private boolean validateData(String phone,String email, String password) {
        if(!Patterns.PHONE.matcher(phone).matches()){
            phoneNum.setError("Please, insert a valid number phone");
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailAdd.setError("Please, insert a valid number phone");
            return false;
        }
        if(password.length()<6){
            passInput.setError("Your password is too short");
            return false;
        }
        if(password.equals(conformPassInput.getText().toString())){
            conformPassInput.setError("Your password and conforme password are not equal");
            return false;
        }
        return true;
    }
}