package com.example.thamt.daly.Chat;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class MessageEntity {
    @ColumnInfo(name = "message")
    public String message;
    @ColumnInfo(name = "timestamp")
    public long timestamp;
    @PrimaryKey
    @NonNull
    private String id;

    public MessageEntity() {
    }

    public MessageEntity(String message, long timestamp, @NonNull String id) {
        this.message = message;
        this.timestamp = timestamp;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
