package com.example.thamt.daly.Services;

import android.preference.PreferenceManager;

import com.example.thamt.daly.Database.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MessengingServiceFirestoreDao {

  static public void setToken(String userId, String token) {
    FirebaseFirestore.getInstance().collection("tokens").document(userId).set(createMap(token));
  }

  static private Map<String, Object> createMap(String token) {
    Map<String, Object> data = new HashMap<>();
    data.put("token", token);
    return data;
  }
}
