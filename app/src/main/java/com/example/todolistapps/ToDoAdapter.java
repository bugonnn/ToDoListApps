package com.example.todolistapps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {
    private List<ToDoItem> todoList;

    public ToDoAdapter(List<ToDoItem> todoList) {
        this.todoList = todoList;
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate rv
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new ToDoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoAdapter.ToDoViewHolder holder, int position) {
        ToDoItem item = todoList.get(position);
        holder.whatToDoTextView.setText(item.getWhatToDo());
        holder.timeTextView.setText(item.getTime());
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView whatToDoTextView;
        TextView timeTextView;

        public ToDoViewHolder(View itemView) {
            super(itemView);
            whatToDoTextView = itemView.findViewById(R.id.tvWhatToDo);
            timeTextView = itemView.findViewById(R.id.tvTime);
        }
    }
}