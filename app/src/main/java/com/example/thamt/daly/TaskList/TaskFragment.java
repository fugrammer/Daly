package com.example.thamt.daly.TaskList;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.thamt.daly.Common.MovableFloatingActionButton;
import com.example.thamt.daly.DalyApplication;
import com.example.thamt.daly.Database.Task;
import com.example.thamt.daly.Database.TaskFirestoreDao;
import com.example.thamt.daly.R;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TaskFragment extends Fragment implements TaskCreateDialog.OnTaskEnteredListener {
  private static final String TAG = "TASK_FRAGMENT";

  private OnClickListener onItemClickListener;
  private View.OnLongClickListener onItemLongClickListener;
  private View.OnTouchListener onItemTouchListener;
  private TaskRecyclerViewAdapter viewAdapter;
  private ItemTouchHelper itemTouchHelper;
  private View view;
  private String checklistName;
  private TaskListViewModel viewModel;
  private MovableFloatingActionButton fab;
  private List<Task> filteredResult;

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
    });
  }

  private void createListeners() {
    onItemClickListener = view -> {
      TaskRecyclerViewAdapter.ViewHolder viewHolder = (TaskRecyclerViewAdapter.ViewHolder) view.getTag();
      if (viewHolder.task != null) {
        viewModel.toggleTask(viewHolder.task);
      }
    };
    onItemLongClickListener = view1 -> {
      TaskRecyclerViewAdapter.ViewHolder viewHolder = (TaskRecyclerViewAdapter.ViewHolder) view1.getTag();
      if (viewHolder.task != null) {
        final Dialog dialog = new TaskCreateDialog(getActivity(), this, viewHolder.task).setOnTaskEnteredListener(this);
        dialog.show();
      }
      return true;
    };
  }

  private void observeViewModel(final TaskListViewModel viewModel) {
    viewModel.getTasks().observe(this, tasks -> {
      if (tasks != null) {
        filteredResult = tasks.stream()
          .sorted((o1, o2) -> {
            if (o1.status && o2.status || !o1.status && !o2.status) {
              return Long.compare(-o1.order, -o2.order);
            }
            return o1.status ? 1 : -1;
          })
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
    LinearLayoutManager manager = new LinearLayoutManager(context);
    recyclerView.setLayoutManager(manager);

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
      new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean isLongPressDragEnabled() {
          return false;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
          int sourcePos = viewHolder.getAdapterPosition();
          int destPos = target.getAdapterPosition();

          if (sourcePos < destPos) {
            for (int i = sourcePos; i < destPos; i++) {
              swapTasksOrders(filteredResult.get(i), filteredResult.get(i + 1));
              Collections.swap(filteredResult, i, i + 1);
            }
          } else {
            for (int i = sourcePos; i > destPos; i--) {
              swapTasksOrders(filteredResult.get(i), filteredResult.get(i - 1));
              Collections.swap(filteredResult, i, i - 1);
            }
          }
          viewAdapter.onItemMove(sourcePos, destPos);
          return true;
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
          super.clearView(recyclerView, viewHolder);
          // Action finished
          // Update tasks orders on FireBase after user has stop moving task.
          for (Task task : filteredResult) {
            TaskFirestoreDao.setTask(task);
          }
        }

        private void swapTasksOrders(Task a, Task b) {
          long temp = a.order;
          a.order = b.order;
          b.order = temp;
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
    itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
    itemTouchHelper.attachToRecyclerView(recyclerView);

    onItemTouchListener = (view, motionEvent) -> {
      if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
        TaskRecyclerViewAdapter.ViewHolder viewHolder = (TaskRecyclerViewAdapter.ViewHolder) view.getTag();
        itemTouchHelper.startDrag(viewHolder);
      }
      return false;
    };

    viewAdapter = new TaskRecyclerViewAdapter(
      getContext(),
      onItemClickListener,
      onItemLongClickListener,
      onItemTouchListener);
    recyclerView.setAdapter(viewAdapter);


    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
      manager.getOrientation());
    recyclerView.addItemDecoration(dividerItemDecoration);

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
    if (task.order == -1) {
      task.order = viewAdapter.getHighestTaskOrder() + 1;
    }
    viewModel.createTask(task);
  }
}
