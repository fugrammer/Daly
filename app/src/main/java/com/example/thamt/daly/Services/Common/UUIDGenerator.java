package com.example.thamt.daly.Services.Common;

import java.util.UUID;

public class UUIDGenerator {
    public UUIDGenerator() {
    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
