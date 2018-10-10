package com.example.thamt.daly.TaskList;

import com.example.thamt.daly.ActivityScope;
import com.example.thamt.daly.AppComponent;
import com.example.thamt.daly.Services.Common.UUIDGeneratorModule;
import com.example.thamt.daly.Services.TaskListServices.TaskPingUsersServicesModule;

import dagger.Component;

@Component(dependencies = AppComponent.class, modules = {UUIDGeneratorModule.class, TaskPingUsersServicesModule.class, TaskListViewModelModule.class})
@ActivityScope
public interface TaskListViewModelComponent {
  TaskListViewModel getTaskListViewModel();
}
