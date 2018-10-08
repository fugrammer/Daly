package com.example.thamt.daly.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Task {
  @ColumnInfo(name = "description")
  public String description;
  @ColumnInfo(name = "status")
  public boolean status;
  @ColumnInfo(name = "checklistName")
  public String checklistName;

  @PrimaryKey
  @NonNull
  private String id;

  public Task() {
  }

  @Ignore
  public Task(String checklistName, String id, String description, boolean status) {
    this.checklistName = checklistName;
    this.id = id;
    this.status = status;
    this.description = description;
  }

  @Ignore
  public boolean toggleTask() {
    this.status = !this.status;
    return this.status;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
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

  public String getChecklistName() {
    return checklistName;
  }

  public void setChecklistName(String checklistName) {
    this.checklistName = checklistName;
  }
}
