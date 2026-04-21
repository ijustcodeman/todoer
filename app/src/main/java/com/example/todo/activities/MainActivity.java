package com.example.todo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.adapters.TodoAdapter;
import com.example.todo.models.Todo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    FloatingActionButton fabAddTodo;

    private TodoAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        fabAddTodo = findViewById(R.id.fabAddTodo);
        fabAddTodo.setOnClickListener(addTodoListener);

        adapter = new TodoAdapter(new ArrayList<>());

        recyclerView = findViewById(R.id.recyclerViewTodos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        enableSwipeToDelete();
    }

    private ActivityResultLauncher<Intent> addTodoLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Todo todo = (Todo) result.getData().getSerializableExtra("todo");
                            if (todo != null) {
                                adapter.addTodo(todo);
                            }
                        }
                    }
            );

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private View.OnClickListener addTodoListener = v -> {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        addTodoLauncher.launch(intent);
    };

    private void enableSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getBindingAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            return;
                        }
                        adapter.notifyItemChanged(position);
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Löschen")
                                .setMessage("Möchtest du dieses TODO wirklich löschen?")
                                .setPositiveButton("Ja", (dialog, which) -> {
                                    adapter.removeTodo(position);
                                })
                                .setNegativeButton("Nein", (dialog, which) -> {
                                    dialog.dismiss();
                                })
                                .show();
                    }
                };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView);
    }
}
