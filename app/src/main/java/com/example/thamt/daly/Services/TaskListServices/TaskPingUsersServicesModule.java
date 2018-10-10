package com.example.thamt.daly.Services.TaskListServices;

import android.content.Context;

import com.example.thamt.daly.ActivityScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class TaskPingUsersServicesModule {
  @Provides
  @ActivityScope
  public TaskPingUsersService providePingUsersService(@Named("application_context") Context context) {
    return new TaskPingUsersService(context);
  }
}
