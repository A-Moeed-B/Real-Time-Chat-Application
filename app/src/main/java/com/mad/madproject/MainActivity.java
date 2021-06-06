package com.mad.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView messageText;
    EditText chatName;
    private DatabaseReference chatDB;
    private EditText sendMessage;
    //private ArrayList messages;
    private String messages;
    MessageService messageService;
    boolean isBound=false;
    Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // messages=new ArrayList<String>();
        super.onCreate(savedInstanceState);
        messages="";
        setContentView(R.layout.activity_main);

        chatName=findViewById(R.id.chatname);
        btnLogout=findViewById(R.id.btn_logout);
        messageText=findViewById(R.id.message);

        sendMessage=findViewById(R.id.editText_message);
        Intent serviceIntent=new Intent(getApplicationContext(),MessageService.class);
        serviceIntent.putExtra("MESSAGE",messages);
        startService(serviceIntent);
        if(!isBound)
        {
            Intent intent =new Intent(this,MessageService.class);
            if(bindService(intent,connection, Context.BIND_AUTO_CREATE))
            {
                //connected
            }
            else
            {
                //error
            }
        }
        else
        {
            //already connected
        }
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        chatDB= FirebaseDatabase.getInstance().getReference("message");
        chatDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent serviceIntent=new Intent(getApplicationContext(),MessageService.class);
                serviceIntent.putExtra("MESSAGE",snapshot.getValue().toString());
                startService(serviceIntent);
                messages=messages+"\n"+snapshot.getValue().toString();
                messageText.setText(messages);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void onClickSend(View view)
    {
        if(TextUtils.isEmpty(chatName.getText().toString().trim()))
        {
            Toast.makeText(this, "Enter a chat name", Toast.LENGTH_SHORT).show();

        }
        else {
            chatDB.setValue(chatName.getText().toString().trim()+": "+sendMessage.getText().toString());
            sendMessage.setText("");
        }
    }
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MessageService.LocalBinder binder=(MessageService.LocalBinder)service;
            messageService=binder.getService();
            isBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound=false;
        }
    };
}