package adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.R;

import java.util.ArrayList;
import java.util.List;

import models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ItemViewHolder>{

    List<Task> tasks = new ArrayList<>();

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView totalTasks;
        private TextView progress;

        public ItemViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleText);
            totalTasks = itemView.findViewById(R.id.totalTasksText);
            progress = itemView.findViewById(R.id.progressText);
        }

        public void bind(Task task) {
            title.setText(task.getTitle());
            totalTasks.setText(String.valueOf(task.getTotalTasks()));
            progress.setText(String.valueOf(task.getProgress()));
        }
    }
}
