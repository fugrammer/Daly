package com.example.thamt.daly.Services.TaskListServices;

import android.content.Context;

import com.example.thamt.daly.DalyApplicationScope;
import com.example.thamt.daly.Services.ContextModule;
import com.example.thamt.daly.Services.TaskListServices.TaskPingUsersService;
import com.example.thamt.daly.TaskList.TaskListViewModel;
import com.example.thamt.daly.TaskList.TaskListViewModelModule;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class, TaskListViewModelModule.class})
public class TaskPingUsersServicesModule {
  @Provides
  @DalyApplicationScope
  public TaskPingUsersService providePingUsersService(@Named("application_context") Context context) {
    return new TaskPingUsersService(context);
  }
}
