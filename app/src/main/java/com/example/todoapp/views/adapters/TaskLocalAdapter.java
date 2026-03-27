package com.example.todoapp.views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;

import java.util.List;

import com.example.todoapp.models.TaskLocal;

public class TaskLocalAdapter extends RecyclerView.Adapter<TaskLocalAdapter.ItemViewHolder> {

    List<TaskLocal> tasks;

    public TaskLocalAdapter(List<TaskLocal> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        TaskLocal task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name, dueDate, group;
        CheckBox isCompleted;

        public ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.title);
            dueDate = itemView.findViewById(R.id.time);
            group = itemView.findViewById(R.id.group);
            isCompleted = itemView.findViewById(R.id.checkBox);
        }

        @SuppressLint("SetTextI18n")
        public void bind(TaskLocal task) {
            name.setText(task.getName());
            dueDate.setText(task.getDueDate());
            group.setText(task.getGroup());
            isCompleted.setChecked(task.isCompleted());
        }
    }
}
