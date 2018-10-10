package com.example.thamt.daly.TaskList;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.thamt.daly.Database.Task;
import com.example.thamt.daly.R;

import org.joda.time.DateTime;

import java.util.Calendar;

public class TaskCreateDialog extends Dialog implements CalendarDatePickerDialogFragment.OnDateSetListener {
  private static final String FRAG_TAG_DATE_PICKER = "FRAG_TAG_DATE_PICKER";
  private Context context;
  private TextView dueDateTextView;
  private EditText taskDescriptionEditText;
  private OnTaskEnteredListener listener;
  @Nullable
  private Task task;

  public TaskCreateDialog(@NonNull Context context, Fragment fragment, Task task) {
    super(context);
    setContentView(R.layout.dialog);
    this.context = context;
    createDialog(fragment, task);
  }

  public TaskCreateDialog(@NonNull Context context, Fragment fragment) {
    super(context);
    setContentView(R.layout.dialog);
    this.context = context;
    createDialog(fragment, new Task());
  }

  @Override
  public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int month, int day) {
    dueDateTextView.setText(context.getString(R.string.date_picker_result_values, day, month, year));
    DateTime dt = new DateTime().withYear(year).withDayOfMonth(day).withMonthOfYear(month);
    task.setDueDate(dt);
  }

  public TaskCreateDialog setOnTaskEnteredListener(OnTaskEnteredListener listener) {
    this.listener = listener;
    return this;
  }

  private void createDialog(Fragment fragment, Task task) {
    this.task = task;
    dueDateTextView = findViewById(R.id.dueDateBtn);
    taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText);
    Button createTaskBtn = findViewById(R.id.task_create_btn);
    createTaskBtn.setOnClickListener(v -> {
      dismiss();
      if (listener != null) {
        task.setDescription(taskDescriptionEditText.getText().toString());
        listener.onTaskEntered(task);
      }
    });

    findViewById(R.id.dueDateBtn).setOnClickListener(v -> {
      DateTime today = DateTime.now();
      CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
        .setOnDateSetListener(this)
        .setFirstDayOfWeek(Calendar.SUNDAY)
        .setPreselectedDate(today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth())
        .setDateRange(null, null)
        .setDoneText("Set")
        .setCancelText("Cancel")
        .setThemeDark();
      cdp.show(fragment.getFragmentManager(), FRAG_TAG_DATE_PICKER);
    });
  }

  public interface OnTaskEnteredListener {
    void onTaskEntered(Task task);
  }
}
