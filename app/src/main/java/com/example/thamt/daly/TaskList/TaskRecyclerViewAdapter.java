package com.example.thamt.daly.TaskList;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.thamt.daly.Database.Task;
import com.example.thamt.daly.R;

import java.util.ArrayList;
import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {

  private static final String TAG = "TaskRecyclerViewAdapter";
  private List<Task> tasks;
  private View.OnClickListener onItemClickListener;
  private Context context;

  public TaskRecyclerViewAdapter(Context context, View.OnClickListener onItemClickListener) {
    this.tasks = new ArrayList<>();
    this.onItemClickListener = onItemClickListener;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.fragment_task, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.bind(tasks.get(position));
  }

  @Override
  public int getItemCount() {
    return tasks == null ? 0 : tasks.size();
  }

  public void setData(List<Task> newData) {
    this.tasks = newData;
    notifyDataSetChanged();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public final View view;
    public final CheckBox checkBox;
    public final TextView contentView;
    public final View strikeoutView;

    @Nullable
    public Task task;

    public ViewHolder(View view) {
      super(view);
      this.view = view;
      checkBox = view.findViewById(R.id.checkBox);
      contentView = view.findViewById(R.id.content);
      strikeoutView = view.findViewById(R.id.divider);
      view.setTag(this);
      checkBox.setOnClickListener(v -> onItemClickListener.onClick(view));
      view.setOnClickListener(onItemClickListener);
    }

    public void bind(Task task) {
      if (task != null) {
        this.task = task;
        boolean completed = task.status;
        checkBox.setChecked(completed);
        contentView.setText(task.description);
        if (completed) {
          strikeoutView.setVisibility(View.VISIBLE);
        } else {
          strikeoutView.setVisibility(View.GONE);
        }
      }
    }
  }
}
