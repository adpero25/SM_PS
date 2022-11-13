package com.example.todov5;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TaskListFragment extends Fragment {
    public static final String KEY_EXTRA_TASK_ID = "param";
    private static final String SUBTITLE_STATE = "Subtitle_State";
    private TaskAdapter adapter;
    private RecyclerView recyclerView;
    private boolean subtitleVisible;

    public TaskListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onResume() {
        super.onResume();
        updateView();
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState != null)
        {
            subtitleVisible = (boolean)savedInstanceState.getSerializable(SUBTITLE_STATE);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateView();
        return view;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(subtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.new_task:
                Task task = new Task();
                TaskStorage.getInstance().addTask(task);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getId());
                startActivity(intent);
                /* Powyższa metoda uruchamia istniejącą aktywność MainActivity zawierającą fragment
                    TaskFragment, który wyświetla szczegóły zadania – w tym wypadku będą one puste,
                    gdyż zadanie jest nowe. Po uzupełnieniu wymaganych pól i cofnięciu do poprzedniego widoku,
                    nowe zadanie powinno pojawić się na liście. */
                return true;

            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                getActivity().invalidateOptionsMenu();  //Metoda invalidateOptionsMenu() służy do wymuszenia rekonstrukcji przycisków akcji menu.
                updateSubtitle();
                /* Powyższa metoda uaktualnia liczbę niezrobionych tasków w ppodtytule*/
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(SUBTITLE_STATE, subtitleVisible);

        super.onSaveInstanceState(outState);
    }


    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView dateTextView;
        ImageView iconImageView;
        CheckBox doneCheckBox;
        Task task;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);

            nameTextView = itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            iconImageView = itemView.findViewById(R.id.task_Image_View);
            doneCheckBox = itemView.findViewById(R.id.task_Check_Box);


        }


        public void bind(Task task) {
            this.task = task;

            nameTextView.setText(task.getName());
            if(task.isDone()) {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            dateTextView.setText(task.getDate().toString());

            if(task.getCategory().equals(Category.DOM)){
                iconImageView.setImageResource(R.drawable.ic_house);
            }
            else if(task.getCategory().equals(Category.STUDIA)){
                iconImageView.setImageResource(R.drawable.ic_school);
            }
            else if(task.getCategory().equals(Category.PRACA)){
                iconImageView.setImageResource(R.drawable.ic_work);
            }
            else{
                iconImageView.setImageResource(R.drawable.ic_none);
            }

            doneCheckBox.setChecked(task.isDone());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }

        public CheckBox getCheckBox() {
            return this.doneCheckBox;
        }

        public TextView getNameTextView() {
            return this.nameTextView;
        }
    }



    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
        private List<Task> tasks;

        public TaskAdapter(List<Task> _tasks) {
            this.tasks = _tasks;
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            Task task = tasks.get(position);
            holder.bind(task);

            TextView nameTextView = holder.getNameTextView();
            CheckBox checkBox = holder.getCheckBox();
            checkBox.setChecked(tasks.get(position).isDone());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked);
                if(task.isDone()) {
                    nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else{
                    nameTextView.setPaintFlags(nameTextView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            });

        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }
    }



    private void updateView() {
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();

        if(this.adapter == null) {
            adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        }
        else {
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private void updateSubtitle(){
        TaskStorage taskStorage = TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();

        int toDoTasksCount = 0;
        for (Task t: tasks) {
            if(!t.isDone()){
                toDoTasksCount++;
            }
        }
        String subtitle = getString(R.string.subtitle_format, toDoTasksCount);
        if(!subtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }


}