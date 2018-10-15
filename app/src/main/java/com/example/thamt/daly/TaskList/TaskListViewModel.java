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
import com.example.thamt.daly.Services.Common.UUIDGenerator;
import com.example.thamt.daly.Services.TaskListServices.TaskPingUsersService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctionsException;

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
  private TaskPingUsersService pingUsersService;

  public TaskListViewModel(
    Application application,
    UUIDGenerator uuidGenerator,
    TaskPingUsersService pingUsersService) {
    super(application);
    taskDao = TasksDatabase.getInstance(application).taskDao();
    executorService = Executors.newSingleThreadExecutor();
    this.uuidGenerator = uuidGenerator;
    this.pingUsersService = pingUsersService;

    db.collection("tasks").
      addSnapshotListener(new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(@Nullable QuerySnapshot value,
                            @Nullable FirebaseFirestoreException e) {
          if (e != null) {
            return;
          }
          taskDao.deleteAll();
          for (QueryDocumentSnapshot doc : value) {
            try {
              taskDao.save(TaskFirestoreDao.getTask(doc));
            } catch (Exception exception) {
              Log.e(TAG, "onEvent: ", exception);
            }
          }
        }
      });
  }

  public LiveData<List<Task>> getTasks() {
    return taskDao.findAll();
  }

  public void saveTask(Task task) {
    TaskFirestoreDao.setTask(task);
  }

  // TODO: Update all devices.
  public void deleteTask(Task task) {
    TaskFirestoreDao.deleteTask(task);
    executorService.execute(() -> taskDao.delete(task));
  }

  public void pingUsers(Task task) {
    pingUsersService.pingUsers(task).addOnCompleteListener(new OnCompleteListener<String>() {
      @Override
      public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
        if (!task.isSuccessful()) {
          Exception e = task.getException();
          if (e instanceof FirebaseFunctionsException) {
            FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
            FirebaseFunctionsException.Code code = ffe.getCode();
            Object details = ffe.getDetails();
          }

          // [START_EXCLUDE]
          Log.w(TAG, "addMessage:onFailure", e);
//          Toast.makeText(context, "Failed to ping users", Toast.LENGTH_SHORT).show();
          return;
          // [END_EXCLUDE]
        }

        // [START_EXCLUDE]
        String result = task.getResult();
//        Toast.makeText(context, "Pinged users.", Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void toggleTask(Task task) {
    task.toggleTask();
    saveTask(task);
  }

  public void createTask(Task task) {
    if (task.getId() == null) {
      // New task
      task.setId(uuidGenerator.generateUUID());
    }
    saveTask(task);
  }
}
