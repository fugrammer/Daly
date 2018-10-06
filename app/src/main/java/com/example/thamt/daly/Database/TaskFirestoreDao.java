package com.example.thamt.daly.Database;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TaskFirestoreDao {

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
        return data;
    }
}
