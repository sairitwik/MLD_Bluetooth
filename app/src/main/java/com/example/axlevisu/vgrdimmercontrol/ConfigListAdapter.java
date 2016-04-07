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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by axlevisu on 31/3/16.
 */
public class ConfigListAdapter extends ArrayAdapter<Configuration> {
    private ArrayList<Configuration> items;
    private int layoutResourceId;
    private Context context;

    public ConfigListAdapter(Context context, int layoutResourceId, ArrayList<Configuration> items){
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    public static class ConfigurationHolder{
        Configuration configuration;
        TextView hours;
        TextView minutes;
        TextView speed;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ConfigurationHolder holder = null;

        LayoutInflater  inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new ConfigurationHolder();
        holder.configuration = items.get(position);
        holder.hours = (TextView)row.findViewById(R.id.input_hour);
        holder.minutes = (TextView)row.findViewById(R.id.input_minute);
        holder.speed = (TextView)row.findViewById(R.id.input_speed);

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

            }

            @Override
            public void afterTextChanged(Editable s) {

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

            }

            @Override
            public void afterTextChanged(Editable s) {

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

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
