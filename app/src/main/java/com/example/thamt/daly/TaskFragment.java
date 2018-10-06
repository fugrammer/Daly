package com.example.thamt.daly;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class TaskFragment extends Fragment {

    private int mColumnCount = 1;
    private View.OnClickListener onItemClickListener;
    private View.OnClickListener onAddClickListener;
    private TaskRecyclerViewAdapter viewAdapter;
    private TaskListViewModel viewModel;
    private View view;
    private Button createTaskButton;
    private EditText taskEditText;

    public TaskFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel =
                ViewModelProviders.of(getActivity()).get(TaskListViewModel.class);

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
            viewModel.createTask(taskEditText.getText().toString());
            taskEditText.setText("");
        });
    }

    private void createListeners() {
        onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskRecyclerViewAdapter.ViewHolder viewHolder = (TaskRecyclerViewAdapter.ViewHolder) view.getTag();
                if (viewHolder.task != null) {
                    viewModel.toggleTask(viewHolder.task);
                }
            }
        };
    }

    private void observeViewModel(final TaskListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getTasks().observe(this, tasks -> {
            if (tasks != null) {
                viewAdapter.setData(tasks);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        viewAdapter = new TaskRecyclerViewAdapter(getContext(), onItemClickListener);
        recyclerView.setAdapter(viewAdapter);


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
