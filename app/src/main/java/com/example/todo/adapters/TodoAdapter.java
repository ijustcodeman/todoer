package com.example.todo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.models.Todo;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<Todo> todoList;

    public TodoAdapter(ArrayList<Todo> todoList) {
        this.todoList = todoList;
    }

    // 🔹 ViewHolder (ein einzelnes Item)
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, priority;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textTitle);
            description = itemView.findViewById(R.id.textDescription);
            priority = itemView.findViewById(R.id.textMeta);
        }
    }

    // 🔹 Layout erstellen
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);

        return new ViewHolder(view);
    }

    // 🔹 Daten reinladen
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Todo todo = todoList.get(position);

        holder.title.setText(todo.getTitle());
        holder.description.setText(todo.getDescription());
        holder.priority.setText(todo.getPriority());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    // 🔹 Neues Todo hinzufügen
    public void addTodo(Todo todo) {
        todoList.add(todo);
        notifyItemInserted(todoList.size() - 1);
    }

    public void removeTodo(int position){
        todoList.remove(position);
        notifyItemRemoved(position);
    }
}