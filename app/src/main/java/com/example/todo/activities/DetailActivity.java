package com.example.todo.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class DetailActivity extends AppCompatActivity {
    MaterialToolbar toolbar;
    TextInputEditText editTitle;
    TextInputEditText editDescription;
    TextInputEditText editDueDate;
    AutoCompleteTextView spinnerPriority;
    ChipGroup chipGroupCategories;
    Button buttonAddCategory;

    Button save;
    Button cancel;

    private List<Priority> availablePriorities = new ArrayList<>();
    private List<Category> selectedCategories = new ArrayList<>();
    private List<Category> allAvailableCategories = new ArrayList<>();
    
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_detail);
        
        db = AppDatabase.getInstance(this);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ensureInitialData();
        loadDataFromDb();
        initializeVariables();
    }

    private void ensureInitialData() {
        // Falls die DB leer ist (erster Start), fügen wir Standardwerte ein
        if (db.priorityDao().getAllPriorities().isEmpty()) {
            db.priorityDao().insert(new Priority("niedrig"));
            db.priorityDao().insert(new Priority("mittel"));
            db.priorityDao().insert(new Priority("hoch"));
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

    private void initializeVariables(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.details_name);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editDueDate = findViewById(R.id.editDueDate);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        chipGroupCategories = findViewById(R.id.chipGroupCategories);
        buttonAddCategory = findViewById(R.id.buttonAddCategory);

        editDueDate.setOnClickListener(v -> showDatePicker());
        buttonAddCategory.setOnClickListener(v -> showCategorySelectionDialog());

        save = findViewById(R.id.buttonSave);
        cancel = findViewById(R.id.buttonCancel);

        List<String> priorityNames = availablePriorities.stream()
                .map(Priority::getName)
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                priorityNames
        );

        spinnerPriority.setAdapter(adapter);

        save.setOnClickListener(saveTodoListener);
        cancel.setOnClickListener(cancelListener);
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String date = dayOfMonth + "." + (monthOfYear + 1) + "." + year1;
                    editDueDate.setText(date);
                }, year, month, day);
        datePickerDialog.show();
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
            // Check based on ID or Name
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
                        if (!selectedCategories.stream().anyMatch(c -> c.getId() == category.getId())) {
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

    private View.OnClickListener saveTodoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String title = editTitle.getText() != null ? editTitle.getText().toString() : "";
            String description = editDescription.getText() != null ? editDescription.getText().toString() : "";
            String priority = spinnerPriority.getText() != null ? spinnerPriority.getText().toString() : "";
            String dueDate = editDueDate.getText() != null ? editDueDate.getText().toString() : "";

            if (title.isEmpty() || priority.isEmpty()) {
                Toast.makeText(DetailActivity.this, "Titel und Priorität sind Pflichtfelder", Toast.LENGTH_SHORT).show();
                return;
            }

            Todo todo = new Todo(title, description, priority, dueDate);
            long todoId = db.todoDao().insert(todo);

            for (Category category : selectedCategories) {
                db.todoDao().insertTodoCategoryJoin(new TodoCategoryJoin((int) todoId, category.getId()));
            }

            finish();
        }
    };

    private View.OnClickListener cancelListener = v -> finish();
}
