package com.example.happymeals;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button addUButton;
    Button addStorButton;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User user = new User();

        addUButton = findViewById(R.id.add_user_button);
        addUButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                           user.newUser(context, "Mo");
                }
            });

        addStorButton = findViewById(R.id.add_stor_button);
        addStorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Storage stor = new Storage("Fridge 2");
                user.addStorage(context, stor);
            }
        });
    }
}
