package com.example.thamt.daly.Services.Common;

import android.support.annotation.NonNull;

import com.example.thamt.daly.DalyApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class UUIDGeneratorModule {
    @Provides
    @NonNull
    @DalyApplicationScope
    public UUIDGenerator provideUUIDGenerator() {
        return new UUIDGenerator();
    }
}
