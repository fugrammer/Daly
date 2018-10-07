package com.example.thamt.daly.Services;

import java.util.UUID;

public class UUIDGenerator {
    public UUIDGenerator() {
    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
