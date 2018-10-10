package com.example.thamt.daly.TaskList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.thamt.daly.Common.MovableFloatingActionButton;
import com.example.thamt.daly.DalyApplication;
import com.example.thamt.daly.Database.Task;
import com.example.thamt.daly.R;

import java.util.List;
import java.util.stream.Collectors;

public class TaskFragment extends Fragment implements TaskCreateDialog.OnTaskEnteredListener {
  private static final String TAG = "TASK_FRAGMENT";

  private int mColumnCount = 1;
  private OnClickListener onItemClickListener;
  private TaskRecyclerViewAdapter viewAdapter;
  private View view;
  private String checklistName;
  private TaskListViewModel viewModel;
  private MovableFloatingActionButton fab;

  public TaskFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      checklistName = getArguments().getString("checklistName", "shared");
    } else {
      checklistName = "shared";
    }

    TaskListViewModelComponent component = DalyApplication.instance.getTaskListViewModelComponent();
    viewModel = component.getTaskListViewModel();

    createListeners();
    observeViewModel(viewModel);
  }

  private void initialiseViews() {
    fab = view.findViewById(R.id.tasklist_fab);

    fab.setOnClickListener(view -> {
      // Create custom dialog object
      final Dialog dialog = new TaskCreateDialog(getActivity(), this).setOnTaskEnteredListener(this);
      dialog.show();
      getFragmentManager();
    });
  }

  private void createListeners() {
    onItemClickListener = view -> {
      TaskRecyclerViewAdapter.ViewHolder viewHolder = (TaskRecyclerViewAdapter.ViewHolder) view.getTag();
      if (viewHolder.task != null) {
        viewModel.toggleTask(viewHolder.task);
      }
    };
  }

  private void observeViewModel(final TaskListViewModel viewModel) {
    viewModel.getTasks().observe(this, tasks -> {
      if (tasks != null) {
        List<Task> filteredResult = tasks.stream()
          .sorted((o1, o2) -> o1.getStatus() ? 1 : o2.getStatus() ? -1 : 0)
          .filter(task -> task.checklistName.equals(checklistName))
          .collect(Collectors.toList());
        viewAdapter.setData(filteredResult);
      }
    });
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_task_list, container, false);

    Context context = view.getContext();
    RecyclerView recyclerView = view.findViewById(R.id.list);
    if (mColumnCount <= 1) {
      recyclerView.setLayoutManager(new LinearLayoutManager(context));
    } else {
      recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
    }
    viewAdapter = new TaskRecyclerViewAdapter(getContext(), onItemClickListener);
    recyclerView.setAdapter(viewAdapter);

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
      @Override
      public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
      }

      @Override
      public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        TaskRecyclerViewAdapter.ViewHolder vH = (TaskRecyclerViewAdapter.ViewHolder) viewHolder;
        if (direction == ItemTouchHelper.LEFT) {
          viewModel.deleteTask(vH.task);
        } else if (direction == ItemTouchHelper.RIGHT) {
          Log.i(TAG, "Pinging users for task: " + vH.task.toString());
          viewModel.pingUsers(vH.task);
          viewAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
        }
      }
    };

    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    initialiseViews();

    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  @Override
  public void onTaskEntered(Task task) {
    task.checklistName = checklistName;
    viewModel.createTask(task);
  }
}
