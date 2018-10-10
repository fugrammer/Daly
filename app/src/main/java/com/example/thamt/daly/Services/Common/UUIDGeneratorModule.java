package com.example.thamt.daly.Services.Common;

import android.support.annotation.NonNull;

import com.example.thamt.daly.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class UUIDGeneratorModule {
    @Provides
    @NonNull
    @ActivityScope
    public UUIDGenerator provideUUIDGenerator() {
        return new UUIDGenerator();
    }
}
