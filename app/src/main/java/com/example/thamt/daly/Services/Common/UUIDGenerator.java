package com.example.thamt.daly.Services.Common;

import android.util.Log;

import java.util.UUID;

public class UUIDGenerator {
    public UUIDGenerator() {
      Log.i("SCOPING", "UUIDGenerator: constructor");
    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
