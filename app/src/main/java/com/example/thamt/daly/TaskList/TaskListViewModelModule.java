package com.example.thamt.daly.TaskList;

import android.app.Application;

import com.example.thamt.daly.ActivityScope;
import com.example.thamt.daly.Services.Common.UUIDGenerator;
import com.example.thamt.daly.Services.TaskListServices.TaskPingUsersService;

import dagger.Module;
import dagger.Provides;

@Module
public class TaskListViewModelModule {
  @Provides
  @ActivityScope
  public TaskListViewModel provideTaskListViewModel(
    Application application,
    UUIDGenerator uuidGenerator,
    TaskPingUsersService pingUsersService) {
    return new TaskListViewModel(application, uuidGenerator, pingUsersService);
  }
}
