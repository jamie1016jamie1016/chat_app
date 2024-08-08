package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ResetActivity extends AppCompatActivity {

    private TextInputEditText editTextEmailReset;
    private Button buttonReset;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        editTextEmailReset = findViewById(R.id.editTextEmailReset);
        buttonReset = findViewById(R.id.buttonReset);


        auth = FirebaseAuth.getInstance();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailReset.getText().toString();
                if (!email.equals("")){
                    resetPassword(email);
                }
            }
        });

    }

    public void resetPassword(String email){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ResetActivity.this,"Please check your email !",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ResetActivity.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}