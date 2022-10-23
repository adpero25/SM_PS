package com.example.todov5;

import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();
    private List<Task> tasks;

    private TaskStorage() {
        tasks = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Task task = new Task();
            task.setName("Nowe zadanie" + i);
            task.setDone(i % 3 == 0);
            tasks.add(task);
        }
    }

    public static TaskStorage getInstance() { return taskStorage; }

    public List<Task> getTasks() {
        return this.tasks;
    }

    public Task getTask(UUID taskId) {
        for (Task task :tasks) {
            if(task.getId().equals(taskId)){
                return task;
            }
        }
        return null;
    }

}
