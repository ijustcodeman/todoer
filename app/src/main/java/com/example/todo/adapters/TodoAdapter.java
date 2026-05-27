package com.example.todo.adapters;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.models.Category;
import com.example.todo.models.Todo;

import java.util.ArrayList;

/**
 * Adapter for the RecyclerView in MainActivity that displays a list of Todo items.
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<Todo> todoList;
    private OnTodoClickListener listener;

    /**
     * Interface for handling click events on individual Todo items in the list.
     */
    public interface OnTodoClickListener {
        /**
         * Called when a Todo item is clicked.
         */
        void onTodoClick(Todo todo);
    }

    /**
     * Constructs a new TodoAdapter.
     */
    public TodoAdapter(ArrayList<Todo> todoList, OnTodoClickListener listener) {
        this.todoList = todoList;
        this.listener = listener;
    }

    /**
     * Updates the data set and refreshes the RecyclerView.
     */
    public void setTodoList(ArrayList<Todo> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    /**
     * Retrieves the Todo item at the specified position.
     */
    public Todo getTodoAt(int position) {
        return todoList.get(position);
    }

    /**
     * ViewHolder class that holds references to the UI components of a single list item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, meta;
        LinearLayout layoutIcons;
        LinearLayout contentLayout;

        /**
         * Constructs a ViewHolder and initializes its views.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            description = itemView.findViewById(R.id.textDescription);
            meta = itemView.findViewById(R.id.textMeta);
            layoutIcons = itemView.findViewById(R.id.layoutIcons);
            contentLayout = itemView.findViewById(R.id.todoContentLayout);
        }
    }

    /**
     * Creates a new ViewHolder by inflating the item layout.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the data of a Todo item to the views in the ViewHolder.
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item in the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo = todoList.get(position);

        // Apply font size based on user preferences
        applyFontSize(holder);

        holder.title.setText(todo.getTitle());
        holder.description.setText(todo.getDescription());
        
        String metaText = todo.getPriorityName();
        if (todo.getDueDate() != null && !todo.getDueDate().isEmpty()) {
            metaText += " | " + todo.getDueDate();
        }
        holder.meta.setText(metaText);

        holder.layoutIcons.removeAllViews();
        if (todo.getCategories() != null) {
            for (Category category : todo.getCategories()) {
                ImageView imageView = new ImageView(holder.itemView.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 40);
                params.setMargins(4, 0, 4, 0);
                imageView.setLayoutParams(params);
                imageView.setImageResource(category.getIconResId());
                holder.layoutIcons.addView(imageView);
            }
        }

        // Apply visual feedback for completed tasks
        if (todo.isCompleted()) {
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.contentLayout.setAlpha(0.5f);
        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.contentLayout.setAlpha(1.0f);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTodoClick(todo);
            }
        });
    }

    /**
     * Applies the font size from shared preferences to the TextViews in the ViewHolder.
     */
    private void applyFontSize(ViewHolder holder) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(holder.itemView.getContext());
        String fontSizeStr = prefs.getString("font_size", "16");
        float size = Float.parseFloat(fontSizeStr);

        holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        holder.description.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 2);
        holder.meta.setTextSize(TypedValue.COMPLEX_UNIT_SP, size - 4);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     */
    @Override
    public int getItemCount() {
        return todoList.size();
    }

    /**
     * Adds a new Todo item to the list and notifies the adapter.
     */
    public void addTodo(Todo todo) {
        todoList.add(todo);
        notifyItemInserted(todoList.size() - 1);
    }

    /**
     * Removes a Todo item from the list at the specified position and notifies the adapter.
     */
    public void removeTodo(int position){
        todoList.remove(position);
        notifyItemRemoved(position);
    }
}
