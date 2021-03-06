package com.example.thamt.daly.Database;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskFirestoreDao {

  static public void setAllTasks(List<Task> tasks) {
    WriteBatch batch = FirebaseFirestore.getInstance().batch();
    for (Task task : tasks) {
      DocumentReference taskRef = FirebaseFirestore.getInstance().collection("tasks").document(String.valueOf(task.getId()));
      batch.set(taskRef, createMap(task));
    }
    batch.commit();
  }

  static public void setTask(Task task) {
    FirebaseFirestore.getInstance().collection("tasks").document(String.valueOf(task.getId())).set(createMap(task));
  }

  static public void deleteTask(Task task) {
    FirebaseFirestore.getInstance().collection("tasks").document(String.valueOf(task.getId())).delete();
  }

  static private Map<String, Object> createMap(Task task) {
    Map<String, Object> data = new HashMap<>();
    data.put("id", task.getId());
    data.put("description", task.description);
    data.put("status", task.status);
    data.put("checklistName", task.checklistName);
    data.put("dueDate", task.dueDate != null ? task.dueDate.getMillis() : null);
    data.put("order", task.order);
    return data;
  }

  static public Task getTask(QueryDocumentSnapshot doc) {
    String checklistName = doc.getString("checklistName");
    String description = doc.getString("description");
    String id = doc.getString("id");
    boolean status = doc.getBoolean("status");
    Long millis = doc.getLong("dueDate");
    Long order = doc.getLong("order");
    DateTime dueDate = millis == null ? null : new DateTime().withMillis(millis);
    return new Task.Builder(id)
      .addCheckListName(checklistName)
      .setOrder(order)
      .setDescription(description)
      .setStatus(status)
      .setDueDate(dueDate)
      .build();
  }
}
