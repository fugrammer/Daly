package com.example.thamt.daly.Services.TaskListServices;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.thamt.daly.R;
import com.example.thamt.daly.TaskList.TaskListViewModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class TaskPingUsersService implements PingUsersServiceInterface {
  private static final String TAG = "TaskPingUsersService";
  private FirebaseFunctions functions;
  private Context context;

  // TODO: Use DI.
  public TaskPingUsersService(
    Context context) {
    functions = FirebaseFunctions.getInstance();
    this.context = context;
  }

  public Task<String> pingUsers(com.example.thamt.daly.Database.Task task) {
    // Create the arguments to the callable function.
    String senderToken =
      PreferenceManager
        .getDefaultSharedPreferences(context)
        .getString(context.getString(R.string.user_token), null);
    Map<String, Object> data = new HashMap<>();
    data.put("text", task.getDescription());
    data.put("senderToken", senderToken);
    return functions
      .getHttpsCallable("pingUsers")
      .call(data)
      .continueWith(new Continuation<HttpsCallableResult, String>() {
        @Override
        public String then(@NonNull Task<HttpsCallableResult> task) {
          Map result = (HashMap) task.getResult().getData();
          return "Success";
        }
      });
  }
}
