package com.example.thamt.daly.Services;

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UUIDGeneratorModule {
    @Provides
    @NonNull
    @Singleton
    public UUIDGenerator provideUUIDGenerator() {
        return new UUIDGenerator();
    }
}
