package com.example.todo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.todo.data.AppDatabase;
import com.example.todo.models.Priority;
import com.example.todo.models.Todo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnTodoClickListener {

    private MaterialToolbar toolbar;
    private TodoAdapter adapter;
    private AppDatabase db;
    
    private enum SortCriteria { 
        DATE_ASC, DATE_DESC, 
        PRIORITY_ASC, PRIORITY_DESC, 
        STATUS_DONE_FIRST, STATUS_OPEN_FIRST 
    }
    private SortCriteria currentSort = SortCriteria.DATE_ASC;

    private final ActivityResultLauncher<Intent> detailActivityLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> { /* UI refresh in onResume */ }
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        
        db = AppDatabase.getInstance(this);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeVariables();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTodos();
    }

    private void loadTodos() {
        List<Todo> todos = db.todoDao().getAllTodos();
        List<Priority> priorities = db.priorityDao().getAllPriorities();
        
        Map<Integer, Priority> priorityMap = priorities.stream()
                .collect(Collectors.toMap(Priority::getId, p -> p));

        for (Todo todo : todos) {
            todo.setCategories(db.todoDao().getCategoriesForTodo(todo.getId()));
            Priority p = priorityMap.get(todo.getPriorityId());
            if (p != null) {
                todo.setPriorityName(p.getName());
                todo.setPriorityRank(p.getRank());
            } else {
                todo.setPriorityName("Keine");
                todo.setPriorityRank(0);
            }
        }
        
        sortAndDisplay(todos);
    }
    
    private void sortAndDisplay(List<Todo> todos) {
        Comparator<Todo> comparator;
        
        switch (currentSort) {
            case PRIORITY_DESC:
                comparator = Comparator.comparingInt(Todo::getPriorityRank).reversed();
                break;
            case PRIORITY_ASC:
                comparator = Comparator.comparingInt(Todo::getPriorityRank);
                break;
            case STATUS_DONE_FIRST:
                comparator = (t1, t2) -> Boolean.compare(t2.isCompleted(), t1.isCompleted());
                break;
            case STATUS_OPEN_FIRST:
                comparator = (t1, t2) -> Boolean.compare(t1.isCompleted(), t2.isCompleted());
                break;
            case DATE_DESC:
                comparator = (t1, t2) -> compareDates(t2.getDueDate(), t1.getDueDate());
                break;
            case DATE_ASC:
            default:
                comparator = (t1, t2) -> compareDates(t1.getDueDate(), t2.getDueDate());
                break;
        }
        
        todos.sort(comparator);
        adapter.setTodoList(new ArrayList<>(todos));
    }

    private int compareDates(String dateStr1, String dateStr2) {
        SimpleDateFormat sdf = new SimpleDateFormat("d.M.yyyy", Locale.getDefault());
        try {
            boolean empty1 = dateStr1 == null || dateStr1.isEmpty();
            boolean empty2 = dateStr2 == null || dateStr2.isEmpty();
            
            if (empty1 && empty2) return 0;
            if (empty1) return 1;
            if (empty2) return -1;
            
            Date d1 = sdf.parse(dateStr1);
            Date d2 = sdf.parse(dateStr2);
            return d1.compareTo(d2);
        } catch (ParseException e) {
            return 0;
        }
    }

    private void initializeVariables(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        FloatingActionButton fabAddTodo = findViewById(R.id.fabAddTodo);
        fabAddTodo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            detailActivityLauncher.launch(intent);
        });

        adapter = new TodoAdapter(new ArrayList<>(), this);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTodos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        enableSwipeToDelete(recyclerView);
    }

    @Override
    public void onTodoClick(Todo todo) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("todo_id", todo.getId());
        detailActivityLauncher.launch(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.sort_by_date_asc) {
            currentSort = SortCriteria.DATE_ASC;
        } else if (id == R.id.sort_by_date_desc) {
            currentSort = SortCriteria.DATE_DESC;
        } else if (id == R.id.sort_by_priority_asc) {
            currentSort = SortCriteria.PRIORITY_ASC;
        } else if (id == R.id.sort_by_priority_desc) {
            currentSort = SortCriteria.PRIORITY_DESC;
        } else if (id == R.id.sort_by_status_done) {
            currentSort = SortCriteria.STATUS_DONE_FIRST;
        } else if (id == R.id.sort_by_status_open) {
            currentSort = SortCriteria.STATUS_OPEN_FIRST;
        } else {
            return super.onOptionsItemSelected(item);
        }
        
        loadTodos();
        return true;
    }

    private void enableSwipeToDelete(RecyclerView recyclerView) {
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
                        if (position == RecyclerView.NO_POSITION) return;
                        
                        Todo todoToDelete = adapter.getTodoAt(position);
                        
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Löschen")
                                .setMessage("Möchtest du dieses TODO wirklich löschen?")
                                .setPositiveButton("Ja", (dialog, which) -> {
                                    db.todoDao().delete(todoToDelete);
                                    loadTodos();
                                })
                                .setNegativeButton("Nein", (dialog, which) -> adapter.notifyItemChanged(position))
                                .setOnCancelListener(dialog -> adapter.notifyItemChanged(position))
                                .show();
                    }
                };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(recyclerView);
    }
}
