package com.example.thamt.daly.Database;

import android.arch.persistence.room.TypeConverter;

import org.joda.time.DateTime;

public class DateTimeTypeConverter {
  @TypeConverter
  public static DateTime toDate(Long millis) {
    return millis == null ? null : new DateTime().withMillis(millis);
  }

  @TypeConverter
  public static Long toLong(DateTime dateTime) {
    return dateTime == null ? null : dateTime.getMillis();
  }
}
