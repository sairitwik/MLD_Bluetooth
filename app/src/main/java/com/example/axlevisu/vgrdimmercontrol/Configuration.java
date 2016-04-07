package com.example.axlevisu.vgrdimmercontrol;

import java.io.Serializable;

/**
 * Created by axlevisu on 31/3/16.
 */
public class Configuration implements Serializable {
    private static final long serialVersionUID = -5435670920302756945L;
    private int hours = 0;
    private int minutes = 0;
    private int speed = 0;

    public Configuration(int hours, int minutes, int speed){
        this.setHours(hours);
        this.setMinutes(minutes);
        this.setSpeed(speed);
    }
     public void setHours(int h){
         this.hours = h%128;
     }
     public void setMinutes(int m){
         this.minutes = m%60;
     }
     public void setSpeed(int s){
        this.speed = s%16;
     }
     public int getHours(){
         return hours;
     }
     public int getMinutes(){
         return minutes;
     }
     public int getSpeed(){
         return speed;
     }
}
