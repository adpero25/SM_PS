package com.example.todov5;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Date;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_TASK_ID = "taskID";

    TextView nameField;
    Button dateButton;
    CheckBox doneCheckBox;
    Task task;
    View view;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(UUID taskId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TASK_ID, taskId);
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setArguments(bundle);

        return taskFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        task = TaskStorage.getInstance().getTask(taskId);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task, container, false);

        dateButton = view.findViewById(R.id.task_date);
        dateButton.setOnClickListener(v -> {
            task.setDate(new Date());
        });
        dateButton.setEnabled(false);

        nameField = view.findViewById(R.id.task_name);
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                task.setName("Task bez nazwy");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                task.setName(s.toString());
                dateButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        doneCheckBox = view.findViewById(R.id.task_done);
        doneCheckBox.setChecked(task.isDone());
        doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setDone(isChecked);
        });

        nameField.setText(task.getName());
        doneCheckBox.setChecked(task.isDone());

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        task.setName(nameField.toString());
        task.setDate(new Date(1, 2, 3));
        task.setDone(doneCheckBox.isChecked());
    }
}