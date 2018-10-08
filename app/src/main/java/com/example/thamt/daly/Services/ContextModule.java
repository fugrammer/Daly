package com.example.thamt.daly.Services;

import android.app.Application;
import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

  Application application;

  public ContextModule(Application application) {
    this.application = application;
  }

  @Provides
  @Named("application_context")
  public Context context() {
    return application.getApplicationContext();
  }

  @Provides
  public Application application() {
    return application;
  }
}
