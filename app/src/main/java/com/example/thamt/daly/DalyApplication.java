package com.example.thamt.daly;

import android.app.Application;

import com.example.thamt.daly.Services.ContextModule;
import com.example.thamt.daly.TaskList.DaggerTaskListViewModelComponent;
import com.example.thamt.daly.TaskList.TaskListViewModelComponent;

import net.danlew.android.joda.JodaTimeAndroid;

public class DalyApplication extends Application {
  public static DalyApplication instance;
  private static AppComponent appComponent;
  private static TaskListViewModelComponent taskListViewModelComponent;

    @Override
    public void onCreate() {
      super.onCreate();
      JodaTimeAndroid.init(this);
      instance = this;
      appComponent = buildComponent();
    }

  protected AppComponent buildComponent() {
    return DaggerAppComponent.builder()
      .contextModule(new ContextModule(this))
      .build();
  }

  public TaskListViewModelComponent getTaskListViewModelComponent() {
    if (taskListViewModelComponent == null) {
      taskListViewModelComponent = DaggerTaskListViewModelComponent.builder().appComponent(appComponent).build();
    }
    return taskListViewModelComponent;
  }

  public void clearTaskListViewModelComponent() {
    taskListViewModelComponent = null;
  }
}
