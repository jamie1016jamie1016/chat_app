package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ImageView imageViewBack;
    private TextView textViewChat;
    private EditText editTextMessage;
    private FloatingActionButton fab;
    private RecyclerView rvChat;

    String userName, otherName;

    FirebaseDatabase database;
    DatabaseReference reference;

    MessageAdaptor adaptor;
    List<ModelClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);



        imageViewBack = findViewById(R.id.imageViewBack);
        textViewChat = findViewById(R.id.textViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        fab = findViewById(R.id.fab);
        rvChat = findViewById(R.id.rvChat);

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        userName = getIntent().getStringExtra("userName");
        otherName = getIntent().getStringExtra("otherName");
        textViewChat.setText(otherName);

        database = FirebaseDatabase.getInstance(
                "https://chatapp-63a2f-default-rtdb.asia-southeast1.firebasedatabase.app");
        reference = database.getReference();

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(editTextMessage.getText()==null)){
                    String message = editTextMessage.getText().toString();
                    sendMessage(message);
                    editTextMessage.setText("");
                }
            }
        });

        getMessage();

    }

    public void sendMessage(String message) {
            String key = reference.child("Messages").child(userName).child(otherName).push().getKey();
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("message", message);
            messageMap.put("from", userName);
            reference.child("Messages").child(userName).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        reference.child("Messages").child(otherName).child(userName).child(key).setValue(messageMap);
                    }
                }
            });

    }
    public void getMessage(){
        reference.child("Messages").child(userName).child(otherName)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModelClass modelClass = snapshot.getValue(ModelClass.class);
                list.add(modelClass);
                adaptor.notifyDataSetChanged();
                rvChat.scrollToPosition(list.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adaptor = new MessageAdaptor(list, userName);
        rvChat.setAdapter(adaptor);
    }
}