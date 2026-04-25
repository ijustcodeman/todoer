package com.example.todo.activities;

import android.app.DatePickerDialog;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todo.R;
import com.example.todo.data.AppDatabase;
import com.example.todo.models.Category;
import com.example.todo.models.Priority;
import com.example.todo.models.Todo;
import com.example.todo.models.TodoCategoryJoin;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class DetailActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private TextInputEditText editTitle;
    private TextInputEditText editDescription;
    private TextInputEditText editDueDate;
    private AutoCompleteTextView spinnerPriority;
    private SwitchMaterial switchCompleted;
    private ChipGroup chipGroupCategories;

    private List<Priority> availablePriorities = new ArrayList<>();
    private List<Category> selectedCategories = new ArrayList<>();
    private List<Category> allAvailableCategories = new ArrayList<>();
    
    private AppDatabase db;
    private int editingTodoId = -1;
    private Todo editingTodo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_detail);
        
        db = AppDatabase.getInstance(this);
        editingTodoId = getIntent().getIntExtra("todo_id", -1);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ensureInitialData();
        loadDataFromDb();
        initializeUI();
        
        if (editingTodoId != -1) {
            loadExistingTodo();
        }
    }

    private void loadExistingTodo() {
        editingTodo = db.todoDao().getTodoById(editingTodoId);
        
        if (editingTodo != null) {
            editTitle.setText(editingTodo.getTitle());
            editDescription.setText(editingTodo.getDescription());
            editDueDate.setText(editingTodo.getDueDate());
            switchCompleted.setChecked(editingTodo.isCompleted());
            
            Priority p = availablePriorities.stream()
                    .filter(priority -> priority.getId() == editingTodo.getPriorityId())
                    .findFirst().orElse(null);
            if (p != null) {
                spinnerPriority.setText(p.getName(), false);
            }
            
            selectedCategories = db.todoDao().getCategoriesForTodo(editingTodoId);
            updateCategoryChips();
            toolbar.setTitle("Bearbeiten");
        }
    }

    private void ensureInitialData() {
        if (db.priorityDao().getAllPriorities().isEmpty()) {
            db.priorityDao().insert(new Priority("niedrig", 1));
            db.priorityDao().insert(new Priority("mittel", 2));
            db.priorityDao().insert(new Priority("hoch", 3));
        }
        if (db.categoryDao().getAllCategories().isEmpty()) {
            db.categoryDao().insert(new Category("Arbeit", android.R.drawable.ic_menu_my_calendar));
            db.categoryDao().insert(new Category("Privat", android.R.drawable.ic_lock_idle_lock));
            db.categoryDao().insert(new Category("Einkauf", android.R.drawable.ic_menu_add));
        }
    }

    private void loadDataFromDb() {
        availablePriorities = db.priorityDao().getAllPriorities();
        allAvailableCategories = db.categoryDao().getAllCategories();
    }

    private void initializeUI(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.details_name);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editDueDate = findViewById(R.id.editDueDate);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        switchCompleted = findViewById(R.id.switchCompleted);
        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        Button buttonAddCategory = findViewById(R.id.buttonAddCategory);

        editDueDate.setOnClickListener(v -> showDatePicker());
        buttonAddCategory.setOnClickListener(v -> showCategorySelectionDialog());

        Button save = findViewById(R.id.buttonSave);
        Button cancel = findViewById(R.id.buttonCancel);

        List<String> priorityNames = availablePriorities.stream()
                .map(Priority::getName)
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                priorityNames
        );

        spinnerPriority.setAdapter(adapter);

        save.setOnClickListener(v -> saveTodo());
        cancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this,
                (view, y, m, d) -> {
                    String date = d + "." + (m + 1) + "." + y;
                    editDueDate.setText(date);
                }, year, month, day).show();
    }

    private void showCategorySelectionDialog() {
        if (allAvailableCategories.isEmpty()) {
            Toast.makeText(this, "Keine Kategorien vorhanden", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] categoryNames = new String[allAvailableCategories.size()];
        boolean[] checkedItems = new boolean[allAvailableCategories.size()];

        for (int i = 0; i < allAvailableCategories.size(); i++) {
            categoryNames[i] = allAvailableCategories.get(i).getName();
            for (Category selected : selectedCategories) {
                if (selected.getId() == allAvailableCategories.get(i).getId()) {
                    checkedItems[i] = true;
                    break;
                }
            }
        }

        new AlertDialog.Builder(this)
                .setTitle("Kategorien wählen")
                .setMultiChoiceItems(categoryNames, checkedItems, (dialog, which, isChecked) -> {
                    Category category = allAvailableCategories.get(which);
                    if (isChecked) {
                        if (selectedCategories.stream().noneMatch(c -> c.getId() == category.getId())) {
                            selectedCategories.add(category);
                        }
                    } else {
                        selectedCategories.removeIf(c -> c.getId() == category.getId());
                    }
                })
                .setPositiveButton("OK", (dialog, which) -> updateCategoryChips())
                .setNegativeButton("Abbrechen", null)
                .show();
    }

    private void updateCategoryChips() {
        chipGroupCategories.removeAllViews();
        for (Category category : selectedCategories) {
            Chip chip = new Chip(this);
            chip.setText(category.getName());
            chip.setChipIconResource(category.getIconResId());
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> {
                selectedCategories.removeIf(c -> c.getId() == category.getId());
                updateCategoryChips();
            });
            chipGroupCategories.addView(chip);
        }
    }

    private void playJingle() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            MediaPlayer mp = MediaPlayer.create(this, notification);
            if (mp != null) {
                mp.setOnCompletionListener(MediaPlayer::release);
                mp.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveTodo() {
        String title = editTitle.getText() != null ? editTitle.getText().toString().trim() : "";
        String description = editDescription.getText() != null ? editDescription.getText().toString().trim() : "";
        String selectedPriorityName = spinnerPriority.getText() != null ? spinnerPriority.getText().toString() : "";
        String dueDate = editDueDate.getText() != null ? editDueDate.getText().toString() : "";
        boolean isCompleted = switchCompleted.isChecked();

        if (title.isEmpty() || selectedPriorityName.isEmpty()) {
            Toast.makeText(this, "Titel und Priorität sind Pflichtfelder", Toast.LENGTH_SHORT).show();
            return;
        }

        Priority p = availablePriorities.stream()
                .filter(priority -> priority.getName().equals(selectedPriorityName))
                .findFirst().orElse(null);
        
        if (p == null) return;

        if (editingTodoId == -1) {
            Todo todo = new Todo(title, description, p.getId(), dueDate, isCompleted);
            long todoId = db.todoDao().insert(todo);
            for (Category category : selectedCategories) {
                db.todoDao().insertTodoCategoryJoin(new TodoCategoryJoin((int) todoId, category.getId()));
            }
            if (isCompleted) playJingle();
        } else {
            if (!editingTodo.isCompleted() && isCompleted) playJingle();

            editingTodo.setTitle(title);
            editingTodo.setDescription(description);
            editingTodo.setPriorityId(p.getId());
            editingTodo.setDueDate(dueDate);
            editingTodo.setCompleted(isCompleted);
            db.todoDao().update(editingTodo);
            
            db.todoDao().deleteCategoriesForTodo(editingTodoId);
            for (Category category : selectedCategories) {
                db.todoDao().insertTodoCategoryJoin(new TodoCategoryJoin(editingTodoId, category.getId()));
            }
        }
        finish();
    }
}
