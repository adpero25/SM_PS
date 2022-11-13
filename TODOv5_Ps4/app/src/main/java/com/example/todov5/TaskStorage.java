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
            task.setDone(i % 3 == 1);

            if(i%3 == 0){
                task.setCategory(Category.STUDIA);
            }
            else if (i%7 == 0){
                task.setCategory(Category.DOM);
            }
            else if(i%11 == 0){
                task.setCategory(Category.PRACA);
            }
            else{
                task.setCategory(Category.BRAK);
            }

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

    public void addTask(Task task){
        this.tasks.add(task);
    }
}
