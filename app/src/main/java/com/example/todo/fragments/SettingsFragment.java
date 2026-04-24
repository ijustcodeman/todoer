package com.example.todo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.todo.R;
import com.example.todo.data.AppDatabase;
import com.example.todo.models.Category;
import com.example.todo.models.Priority;

import java.util.Comparator;
import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat {

    private AppDatabase db;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        db = AppDatabase.getInstance(requireContext());

        Preference manageCategories = findPreference("manage_categories");
        if (manageCategories != null) {
            manageCategories.setOnPreferenceClickListener(preference -> {
                showManageCategoriesDialog();
                return true;
            });
        }

        Preference managePriorities = findPreference("manage_priorities");
        if (managePriorities != null) {
            managePriorities.setOnPreferenceClickListener(preference -> {
                showManagePrioritiesDialog();
                return true;
            });
        }
    }

    private void showManageCategoriesDialog() {
        List<Category> categories = db.categoryDao().getAllCategories();
        String[] names = categories.stream().map(Category::getName).toArray(String[]::new);

        new AlertDialog.Builder(requireContext())
                .setTitle("Kategorien verwalten")
                .setItems(names, (dialog, which) -> showEditCategoryDialog(categories.get(which)))
                .setPositiveButton("Neu", (dialog, which) -> showEditCategoryDialog(null))
                .setNegativeButton("Schließen", null)
                .show();
    }

    private void showEditCategoryDialog(Category category) {
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_category, null);
        EditText editName = view.findViewById(R.id.editCategoryName);
        LinearLayout iconContainer = view.findViewById(R.id.iconContainer);

        final int[] selectedIcon = {android.R.drawable.ic_menu_agenda}; // Default
        int[] icons = {
                android.R.drawable.ic_menu_my_calendar,
                android.R.drawable.ic_lock_idle_lock,
                android.R.drawable.ic_menu_add,
                android.R.drawable.ic_menu_agenda,
                android.R.drawable.ic_menu_call,
                android.R.drawable.ic_menu_camera
        };

        if (category != null) {
            editName.setText(category.getName());
            selectedIcon[0] = category.getIconResId();
        }

        for (int iconRes : icons) {
            ImageView img = new ImageView(requireContext());
            img.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            img.setImageResource(iconRes);
            img.setPadding(10, 10, 10, 10);
            img.setAlpha(iconRes == selectedIcon[0] ? 1.0f : 0.3f);
            img.setOnClickListener(v -> {
                selectedIcon[0] = iconRes;
                for (int i = 0; i < iconContainer.getChildCount(); i++) {
                    iconContainer.getChildAt(i).setAlpha(0.3f);
                }
                v.setAlpha(1.0f);
            });
            iconContainer.addView(img);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(category == null ? "Neue Kategorie" : "Kategorie bearbeiten")
                .setView(view)
                .setPositiveButton("Speichern", (dialog, which) -> {
                    String name = editName.getText().toString();
                    if (category == null) {
                        db.categoryDao().insert(new Category(name, selectedIcon[0]));
                    } else {
                        category.setName(name);
                        category.setIconResId(selectedIcon[0]);
                        db.categoryDao().update(category);
                    }
                });

        if (category != null) {
            builder.setNeutralButton("Löschen", (dialog, which) -> {
                db.categoryDao().delete(category);
            });
        }

        builder.setNegativeButton("Abbrechen", null).show();
    }

    private void showManagePrioritiesDialog() {
        List<Priority> priorities = db.priorityDao().getAllPriorities();
        priorities.sort(Comparator.comparingInt(Priority::getRank).reversed());
        String[] names = priorities.stream().map(p -> p.getName() + " (Rang: " + p.getRank() + ")").toArray(String[]::new);

        new AlertDialog.Builder(requireContext())
                .setTitle("Prioritäten verwalten")
                .setItems(names, (dialog, which) -> showEditPriorityDialog(priorities.get(which)))
                .setPositiveButton("Neu", (dialog, which) -> showEditPriorityDialog(null))
                .setNegativeButton("Schließen", null)
                .show();
    }

    private void showEditPriorityDialog(Priority priority) {
        View layout = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_priority, null);
        EditText editName = layout.findViewById(R.id.editPriorityName);
        EditText editRank = layout.findViewById(R.id.editPriorityRank);

        if (priority != null) {
            editName.setText(priority.getName());
            editRank.setText(String.valueOf(priority.getRank()));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(priority == null ? "Neue Priorität" : "Priorität bearbeiten")
                .setView(layout)
                .setPositiveButton("Speichern", (dialog, which) -> {
                    String name = editName.getText().toString();
                    int rank = 0;
                    try {
                        rank = Integer.parseInt(editRank.getText().toString());
                    } catch (NumberFormatException ignored) {}

                    if (priority == null) {
                        db.priorityDao().insert(new Priority(name, rank));
                    } else {
                        priority.setName(name);
                        priority.setRank(rank);
                        db.priorityDao().update(priority);
                    }
                });

        if (priority != null) {
            builder.setNeutralButton("Löschen", (dialog, which) -> {
                // PRÜFUNG: Wird diese Priorität noch von ToDos verwendet?
                int count = db.todoDao().getTodoCountByPriority(priority.getId());
                if (count > 0) {
                    Toast.makeText(requireContext(), 
                        "Löschen nicht möglich: Diese Priorität wird von " + count + " ToDos verwendet.", 
                        Toast.LENGTH_LONG).show();
                } else {
                    db.priorityDao().delete(priority);
                    Toast.makeText(requireContext(), "Priorität gelöscht.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        builder.setNegativeButton("Abbrechen", null).show();
    }
}
