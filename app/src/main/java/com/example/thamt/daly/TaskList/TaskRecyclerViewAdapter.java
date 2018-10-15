package com.example.thamt.daly.TaskList;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thamt.daly.Database.Task;
import com.example.thamt.daly.R;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.ArrayList;
import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>
  implements ItemTouchHelperAdapter {

  private static final String TAG = "TaskRecyclerViewAdapter";
  private List<Task> tasks;
  private View.OnClickListener onItemClickListener;
  private View.OnLongClickListener onItemLongClickListener;
  private View.OnTouchListener onItemTouchListener;
  private Context context;

  public TaskRecyclerViewAdapter(Context context,
                                 View.OnClickListener onItemClickListener,
                                 View.OnLongClickListener onItemLongClickListener,
                                 View.OnTouchListener onItemTouchListener) {
    this.tasks = new ArrayList<>();
    this.onItemClickListener = onItemClickListener;
    this.onItemLongClickListener = onItemLongClickListener;
    this.onItemTouchListener = onItemTouchListener;
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

  @Override
  public void onItemMove(int fromPosition, int toPosition) {
    notifyItemMoved(fromPosition, toPosition);
  }

  public long getHighestTaskOrder() {
    if (tasks.size() > 0) {
      return tasks.get(0).order;
    } else {
      return -1;
    }
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public final View view;
    public final CheckBox checkBox;
    public final TextView contentView;
    public final View strikeoutView;
    public final TextView dueDateTextview;

    @Nullable
    public Task task;

    public ViewHolder(View view) {
      super(view);
      this.view = view;
      checkBox = view.findViewById(R.id.checkBox);
      contentView = view.findViewById(R.id.content);
      strikeoutView = view.findViewById(R.id.divider);
      dueDateTextview = view.findViewById(R.id.dueDateTextView);
      View space = view.findViewById(R.id.clickSpace);
      space.setTag(this);
      view.setTag(this);
      checkBox.setOnClickListener(v -> onItemClickListener.onClick(view));
      space.setOnClickListener(onItemClickListener);
      space.setOnLongClickListener(onItemLongClickListener);
      ImageView imageView = view.findViewById(R.id.slidingButton);
      imageView.setTag(this);
      imageView.setOnTouchListener(onItemTouchListener);
    }

    public void bind(Task task) {
      if (task != null) {
        this.task = task;
        boolean completed = task.status;
        checkBox.setChecked(completed);
        contentView.setText(task.description);
        DateTime dt = task.dueDate;
        DateTime today = DateTime.now();
        if (dt == null) {
          dueDateTextview.setVisibility(View.GONE);
        } else {
          dueDateTextview.setVisibility(View.VISIBLE);
          dueDateTextview.setText(context.getString(
            R.string.due_date_task, dt.getDayOfMonth(),
            dt.getMonthOfYear(),
            dt.getYear(),
            Days.daysBetween(today, dt).getDays()));
        }
        if (completed) {
          strikeoutView.setVisibility(View.VISIBLE);
        } else {
          strikeoutView.setVisibility(View.GONE);
        }
      }
    }
  }
}
