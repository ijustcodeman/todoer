package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    FloatingActionButton fabAddTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeVariables();
    }

    private void initializeVariables(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);

        fabAddTodo = findViewById(R.id.fabAddTodo);
        fabAddTodo.setOnClickListener(addTodoListener);
    }

    private View.OnClickListener addTodoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            startActivity(intent);
        }
    };
}

