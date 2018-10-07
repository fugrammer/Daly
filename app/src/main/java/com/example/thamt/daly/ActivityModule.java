package com.example.thamt.daly;


import android.app.Activity;
import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Context context;

    ActivityModule(Activity context) {
        this.context = context;
    }

    @Provides
    @Named("activity_context")
    public Context context() {
        return context;
    }
}
