package com.example.thamt.daly.TaskList;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.thamt.daly.Database.Task;
import com.example.thamt.daly.Database.TaskDao;
import com.example.thamt.daly.Database.TaskFirestoreDao;
import com.example.thamt.daly.Database.TasksDatabase;
import com.example.thamt.daly.Services.UUIDGenerator;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskListViewModel extends AndroidViewModel {
    private static final String TAG = "TaskListViewModel";

    private MutableLiveData<List<Task>> tasks;
    private TaskDao taskDao;
    private ExecutorService executorService;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private UUIDGenerator uuidGenerator;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
        taskDao = TasksDatabase.getInstance(application).taskDao();
        executorService = Executors.newSingleThreadExecutor();
        TaskListViewModelComponent component = DaggerTaskListViewModelComponent.create();
        uuidGenerator = component.getUUIDGenerator();
        db.collection("tasks").
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        List<Task> tasks = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            try {
                                String description = doc.getString("description");
                                boolean status = doc.getBoolean("status");
                                String id = doc.getString("id");
                                taskDao.save(new Task(id, description, status));
                                Log.i(TAG, "Task id:" + String.valueOf(id) + " Task description:" + description);
                            } catch (Exception exception) {
                            }
                        }
                    }
                });
    }

    public LiveData<List<Task>> getTasks() {
        return taskDao.findAll();
    }

    public void createTask(String description) {
        Task task = new Task(uuidGenerator.generateUUID(), description);
        saveTask(task);
    }

    public void saveTask(Task task) {
        TaskFirestoreDao.setTask(task);
        //executorService.execute(() -> taskDao.save(task));
    }

    public void deleteTask(Task task) {
        TaskFirestoreDao.deleteTask(task);
        executorService.execute(() -> taskDao.delete(task));
    }

    public void toggleTask(Task task) {
        task.toggleTask();
        saveTask(task);
    }
}
