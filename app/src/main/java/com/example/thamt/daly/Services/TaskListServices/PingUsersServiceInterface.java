package com.example.thamt.daly.Services.TaskListServices;

import com.google.android.gms.tasks.Task;

public interface PingUsersServiceInterface {
  Task<String> pingUsers(com.example.thamt.daly.Database.Task task);
}
