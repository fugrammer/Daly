package com.example.thamt.daly.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Task {
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "status")
    public boolean status;
    @PrimaryKey(autoGenerate = true)
    private int id;

    public Task() {
    }

    @Ignore
    public Task(String description) {
        this.status = false;
        this.description = description;
    }

    @Ignore
    public boolean toggleTask() {
        this.status = !this.status;
        return this.status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
