package com.example.thamt.daly;

import android.app.Application;
import android.content.Context;

import com.example.thamt.daly.Services.ContextModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ContextModule.class)
@Singleton
public interface AppComponent {
  Application getApplication();

  @Named("application_context")
  Context context();
}
