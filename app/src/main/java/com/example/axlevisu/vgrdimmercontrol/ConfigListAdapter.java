package com.example.axlevisu.vgrdimmercontrol;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axlevisu on 31/3/16.
 */
public class ConfigListAdapter extends ArrayAdapter<Configuration> {
    private ArrayList<Configuration> items;
    private int layoutResourceId;
    private Context context;
    private ConfigurationHolder holder;
    public ConfigListAdapter(Context context, int layoutResourceId, ArrayList<Configuration> items){
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class ConfigurationHolder{
        Configuration configuration;
        EditText hours;
        EditText minutes;
        EditText speed;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ConfigurationHolder holder = null;

        LayoutInflater  inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new ConfigurationHolder();

        holder.configuration = items.get(position);
        int i = holder.configuration.getSpeed();
//        Toast.makeText(getContext(),"speed: " + String.valueOf(i), Toast.LENGTH_SHORT).show();
        holder.hours = (EditText)row.findViewById(R.id.input_hour);
        holder.minutes = (EditText)row.findViewById(R.id.input_minute);
        holder.speed = (EditText)row.findViewById(R.id.input_speed);
        if(row != null && holder.configuration !=null){
            holder.hours.setText(String.valueOf(holder.configuration.getHours()));}
        if(row != null && holder.configuration !=null){
            holder.minutes.setText(String.valueOf(holder.configuration.getMinutes()));}
        if(row != null && holder.configuration !=null){
            holder.speed.setText(String.valueOf(holder.configuration.getSpeed()));}
//        holder.minutes.setText(holder.configuration.getMinutes());
//        holder.speed.setText(holder.configuration.getSpeed());
        setTextChangeListener(holder);

        row.setTag(holder);
        return row;
    }

    public void setTextChangeListener(final ConfigurationHolder holder){
        holder.hours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length()==0){
                    holder.configuration.setHours(0);
                }
                else{
                    holder.configuration.setHours(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    holder.configuration.setHours(0);
                }
                else{
                    holder.configuration.setHours(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                notifyDataSetChanged();
            }
        });
        holder.minutes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length()==0){
                    holder.configuration.setMinutes(0);
                }
                else{
                    holder.configuration.setMinutes(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    holder.configuration.setMinutes(0);
                }
                else{
                    holder.configuration.setMinutes(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                notifyDataSetChanged();
            }
        });
        holder.speed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length()==0){
                    holder.configuration.setSpeed(0);
                }
                else{
                holder.configuration.setSpeed(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    holder.configuration.setSpeed(0);
                }
                else{
                    holder.configuration.setSpeed(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                notifyDataSetChanged();
            }
        });
    }

}
