package com.example.todo.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todo.R;
import com.example.todo.models.Category;
import com.example.todo.models.Todo;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    private List<String> priorityItems = new ArrayList<>();
    private List<Category> selectedCategories = new ArrayList<>();
    private List<Category> allAvailableCategories = new ArrayList<>();

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

        loadMockData();
        initializeVariables();
    }

    private void loadMockData() {
        // In einer echten App kämen diese Daten aus einer DB oder SharedPreferences
        priorityItems.add("niedrig");
        priorityItems.add("mittel");
        priorityItems.add("hoch");

        allAvailableCategories.add(new Category("1", "Arbeit", android.R.drawable.ic_menu_my_calendar));
        allAvailableCategories.add(new Category("2", "Privat", android.R.drawable.ic_lock_idle_lock));
        allAvailableCategories.add(new Category("3", "Einkauf", android.R.drawable.ic_menu_add));
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                priorityItems
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
        String[] categoryNames = new String[allAvailableCategories.size()];
        boolean[] checkedItems = new boolean[allAvailableCategories.size()];

        for (int i = 0; i < allAvailableCategories.size(); i++) {
            categoryNames[i] = allAvailableCategories.get(i).getName();
            checkedItems[i] = selectedCategories.contains(allAvailableCategories.get(i));
        }

        new AlertDialog.Builder(this)
                .setTitle("Kategorien wählen")
                .setMultiChoiceItems(categoryNames, checkedItems, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        if (!selectedCategories.contains(allAvailableCategories.get(which))) {
                            selectedCategories.add(allAvailableCategories.get(which));
                        }
                    } else {
                        selectedCategories.remove(allAvailableCategories.get(which));
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
                selectedCategories.remove(category);
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
                return;
            }

            Todo todo = new Todo(title, description, priority, dueDate, new ArrayList<>(selectedCategories));

            Intent resultIntent = new Intent();
            resultIntent.putExtra("todo", todo);

            setResult(RESULT_OK, resultIntent);
            finish();
        }
    };

    private View.OnClickListener cancelListener = v -> finish();
}
