package com.example.thamt.daly.TaskList;

import com.example.thamt.daly.DalyApplicationScope;
import com.example.thamt.daly.Services.TaskListServices.TaskPingUsersServicesModule;
import com.example.thamt.daly.Services.TaskListServices.TaskPingUsersService;
import com.example.thamt.daly.Services.Common.UUIDGenerator;
import com.example.thamt.daly.Services.Common.UUIDGeneratorModule;

import dagger.Component;

@Component(modules = {UUIDGeneratorModule.class, TaskPingUsersServicesModule.class, TaskListViewModelModule.class})
@DalyApplicationScope
public interface TaskListViewModelComponent {
  //UUIDGenerator getUUIDGenerator();
  TaskPingUsersService getTaskPingUsersService();

  TaskListViewModel getTaskListViewModel();
}
