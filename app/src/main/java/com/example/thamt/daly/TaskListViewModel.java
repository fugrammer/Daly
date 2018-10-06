package com.example.thamt.daly;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.thamt.daly.Database.Task;
import com.example.thamt.daly.Database.TaskDao;
import com.example.thamt.daly.Database.TasksDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Task>> tasks;
    private TaskDao taskDao;
    private ExecutorService executorService;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
        taskDao = TasksDatabase.getInstance(application).taskDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Task>> getTasks() {
        return taskDao.findAll();
    }

    public void createTask(String description) {
        Task task = new Task(description);
        saveTask(task);
    }

    public void saveTask(Task task) {
        executorService.execute(() -> taskDao.save(task));
    }

    public void deleteTask(Task task) {
        executorService.execute(() -> taskDao.delete(task));
    }

    public void toggleTask(Task task) {
        task.toggleTask();
        saveTask(task);
    }

}
