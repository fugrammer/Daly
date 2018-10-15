package com.example.thamt.daly.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;

@Entity
public class Task {
  @ColumnInfo(name = "description")
  public String description;
  @ColumnInfo(name = "status")
  public boolean status;
  @ColumnInfo(name = "checklistName")
  public String checklistName;
  @ColumnInfo(name = "dueDate")
  public DateTime dueDate;
  @ColumnInfo(name = "order")
  public long order;

  @Ignore
  public Task(String checklistName, String id, String description, boolean status, DateTime dueDate) {
    this.checklistName = checklistName;
    this.id = id;
    this.status = status;
    this.description = description;
    this.dueDate = dueDate;
  }

  @PrimaryKey
  @NonNull
  private String id;

  public Task() {
    this.order = -1;
  }

  @Ignore
  public boolean toggleTask() {
    this.status = !this.status;
    return this.status;
  }

  public DateTime getDueDate() {
    return dueDate;
  }

  public void setDueDate(DateTime dueDate) {
    this.dueDate = dueDate;
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

  public long getOrder() {
    return order;
  }

  public void setOrder(long order) {
    this.order = order;
  }


  public static class Builder {
    private String checkListName;
    private String id;
    private boolean status;
    private String description;
    private DateTime dueDate;
    private long order;

    public Builder(String id) {
      this.id = id;
      this.order = -1;
    }

    public Builder addCheckListName(String name) {
      this.checkListName = name;
      return this;
    }

    public Builder setDescription(String description) {
      this.description = description;
      return this;
    }

    public Builder setDueDate(DateTime dateTime) {
      this.dueDate = dateTime;
      return this;
    }

    public Builder setOrder(long order) {
      this.order = order;
      return this;
    }

    public Builder setStatus(boolean status) {
      this.status = status;
      return this;
    }

    public Task build() {
      Task task = new Task();
      task.dueDate = dueDate;
      task.order = order;
      task.description = description;
      task.status = status;
      task.id = id;
      task.checklistName = checkListName;
      return task;
    }
  }
}
