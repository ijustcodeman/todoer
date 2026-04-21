package com.example.todo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.models.Category;
import com.example.todo.models.Todo;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<Todo> todoList;

    public TodoAdapter(ArrayList<Todo> todoList) {
        this.todoList = todoList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, meta;
        LinearLayout layoutIcons;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            description = itemView.findViewById(R.id.textDescription);
            meta = itemView.findViewById(R.id.textMeta);
            layoutIcons = itemView.findViewById(R.id.layoutIcons);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Todo todo = todoList.get(position);

        holder.title.setText(todo.getTitle());
        holder.description.setText(todo.getDescription());
        
        String metaText = todo.getPriority();
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
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void addTodo(Todo todo) {
        todoList.add(todo);
        notifyItemInserted(todoList.size() - 1);
    }

    public void removeTodo(int position){
        todoList.remove(position);
        notifyItemRemoved(position);
    }
}
