package com.example.thamt.daly.TaskList;

import android.app.Application;

import com.example.thamt.daly.DalyApplicationScope;
import com.example.thamt.daly.Services.Common.UUIDGenerator;
import com.example.thamt.daly.Services.ContextModule;
import com.example.thamt.daly.Services.TaskListServices.TaskPingUsersService;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class TaskListViewModelModule {
  @Provides
  @DalyApplicationScope
  public TaskListViewModel provideTaskListViewModel(
    Application application,
    UUIDGenerator uuidGenerator,
    TaskPingUsersService pingUsersService) {
    return new TaskListViewModel(application, uuidGenerator, pingUsersService);
  }
}
