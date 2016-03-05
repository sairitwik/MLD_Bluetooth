package com.example.axlevisu.vgrdimmercontrol;

/**
 * Created by axlevisu on 3/3/16.
 */


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class AndroidSeekBar extends AppCompatActivity  {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    SeekBar seekBar = (SeekBar)findViewById(R.id.loadSeekBar1);
    final TextView seekBarValue = (TextView)findViewById(R.id.loadText1);

    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
        boolean fromUser) {
            // TODO Auto-generated method stub
            seekBarValue.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }
    });
}
}
