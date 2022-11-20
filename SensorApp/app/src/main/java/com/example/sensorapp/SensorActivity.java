package com.example.sensorapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    //  Accelerometr
    //  Gyroscope

    private static final String IS_SUBTITLE_VISIBLE = "IS_VISIBLE";
    private SensorManager sensorManager;
    private List<Sensor> sensorsList;
    private SensorAdapter adapter;
    private RecyclerView recyclerView;
    private boolean subtitleVisible;
    public static String SENSOR_ID_KEY = "SENSOR_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        if(savedInstanceState != null){
            subtitleVisible = savedInstanceState.getBoolean(IS_SUBTITLE_VISIBLE);
        }

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorsList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (Sensor s: sensorsList) {
            Log.i("SENSOR_INFO",  "Name: " + s.getName() +
                    "\nVendor: " + s.getVendor() +
                    "\nMax return value: " + s.getMaximumRange());
        }


        if(adapter == null) {
            adapter = new SensorAdapter(sensorsList);
            recyclerView.setAdapter(adapter);
        }
        else {
            adapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.show_subtitle:
                subtitleVisible = !subtitleVisible;
                this.invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateSubtitle() {
        int sensorsCount = 0;
        for (Sensor s: sensorsList) {
            sensorsCount++;
        }

        String subtitle = getString(R.string.sensorCount, sensorsCount);
        if(!subtitleVisible) {
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) this;
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sensor_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);

        if(subtitleVisible){
            subtitleItem.setTitle(R.string.hideSubtitle);
        }
        else {
            subtitleItem.setTitle(R.string.showSubtitle);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SUBTITLE_VISIBLE, subtitleVisible);

    }


    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView sensor_name;
        ImageView sensor_icon;
        Sensor sensor;

        public SensorHolder(View view){
            super(view);

            sensor_icon = view.findViewById(R.id.sensor_icon);
            sensor_name = view.findViewById(R.id.sensor_name);

            sensor_name.setOnClickListener(this);
        }

        public void bind(Sensor _sensor){
            this.sensor = _sensor;

            sensor_name.setText(sensor.getName());
            sensor_icon.setImageResource(R.drawable.ic_sensor_icon);
        }


        @Override
        public void onClick(View v) {
            int sensorId = (int) sensor.getType();
            if(sensorId == Sensor.TYPE_MAGNETIC_FIELD) {
                Intent intent = new Intent(v.getContext(), LocationActivity.class);
                intent.putExtra(SENSOR_ID_KEY, sensorId);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(v.getContext(), SensorDetailsActivity.class);
                intent.putExtra(SENSOR_ID_KEY, sensorId);
                startActivity(intent);
            }
        }
    }


    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder>{

        private List<Sensor> sensors;

        public SensorAdapter(List<Sensor> _sensors) {
            this.sensors = _sensors;
        }


        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sensor_list_item, parent, false);

            return new SensorHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensors.get(position);
            holder.bind(sensor);

        }

        @Override
        public int getItemCount() {
            return sensors.size();
        }
    }


}