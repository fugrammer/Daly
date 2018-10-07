package com.example.thamt.daly.TaskList;

import com.example.thamt.daly.Services.UUIDGenerator;
import com.example.thamt.daly.Services.UUIDGeneratorModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = UUIDGeneratorModule.class)
@Singleton
public interface TaskListViewModelComponent {
    UUIDGenerator getUUIDGenerator();
}
