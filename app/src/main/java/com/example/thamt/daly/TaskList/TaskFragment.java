package com.example.thamt.daly.TaskList;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.thamt.daly.Database.Task;
import com.example.thamt.daly.R;
import com.example.thamt.daly.Services.Common.UUIDGeneratorModule;
import com.example.thamt.daly.Services.ContextModule;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.android.support.DaggerApplication;

public class TaskFragment extends Fragment {
  private static final String TAG = "TaskFragment";
  private int mColumnCount = 1;
  private View.OnClickListener onItemClickListener;
  private TaskRecyclerViewAdapter viewAdapter;
  private View view;
  private Button createTaskButton;
  private EditText taskEditText;
  private TaskListViewModel viewModel;
  private String checklistName;

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

    TaskListViewModelComponent component = DaggerTaskListViewModelComponent.builder()
      .contextModule(new ContextModule(getActivity().getApplication()))
      .build();

    viewModel = component.getTaskListViewModel();

    createListeners();
    observeViewModel(viewModel);
  }

  private void initialiseViews() {
    createTaskButton = view.findViewById(R.id.button);
    taskEditText = view.findViewById(R.id.editText);

    taskEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (!s.toString().isEmpty()) {
          createTaskButton.setEnabled(true);
        } else {
          createTaskButton.setEnabled(false);
        }
      }
    });

    createTaskButton.setOnClickListener(v -> {
      viewModel.createTask(checklistName, taskEditText.getText().toString());
      taskEditText.setText("");
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
}
