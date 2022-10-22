package com.example.happymeals;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button addUButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addUButton = findViewById(R.id.add_user_button);
        addUButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                           User user = new User();
                           user.newUser("Mo");
                }
            });
    }
}
