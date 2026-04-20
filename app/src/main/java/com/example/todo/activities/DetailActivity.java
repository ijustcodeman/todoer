package com.example.todo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todo.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class DetailActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    TextInputEditText editTitle;
    TextInputEditText editDescription;
    AutoCompleteTextView spinnerPriority;

    Button save;
    Button cancel;

    private String[] priorityItems = {"niedrig", "mittel", "hoch"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeVariables();
    }

    private void initializeVariables(){

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.details_name);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        spinnerPriority = findViewById(R.id.spinnerPriority);

        save = findViewById(R.id.buttonSave);
        cancel = findViewById(R.id.buttonCancel);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                priorityItems
        );

        spinnerPriority.setAdapter(adapter);

        save.setOnClickListener(saveTodoListener);
        cancel.setOnClickListener(cancelListener);
    }

    private View.OnClickListener saveTodoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String title = editTitle.getText() != null ? editTitle.getText().toString() : "";
            String description = editDescription.getText() != null ? editDescription.getText().toString() : "";
            String priority = spinnerPriority.getText() != null ? spinnerPriority.getText().toString() : "";

            if (title.isEmpty() || description.isEmpty() || priority.isEmpty()) {
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("description", description);
            resultIntent.putExtra("priority", priority);

            setResult(RESULT_OK, resultIntent);
            finish();
        }
    };

    private View.OnClickListener cancelListener = v -> finish();
}
