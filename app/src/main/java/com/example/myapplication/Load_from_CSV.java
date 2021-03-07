package com.example.myapplication;

import android.app.Activity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Load_from_CSV {

    public static Calibration_lib load_from_CSV(FileInputStream fis, Activity a){
        Calibration_lib cal = new Calibration_lib();

        try{
            InputStreamReader isr = new InputStreamReader ( fis ) ;
            BufferedReader buffreader = new BufferedReader ( isr ) ;
            String readString = buffreader.readLine ( ) ; //header
            readString = buffreader.readLine ( ) ;// first row
            while ( readString != null ) {
                String[] parts = readString.split(",");
                try{
                    int num = Integer.parseInt(parts[0]);
                    boolean c = Boolean.parseBoolean(parts[1]);
                    double con = Double.parseDouble(parts[2]);
                    double val = Double.parseDouble(parts[3]);
                    cal.put_point(num, con, val, c);
                }catch (Exception e){
                    e.printStackTrace ( ) ;
                    Toast.makeText(a, "Load error", Toast.LENGTH_LONG).show();
                }
                readString = buffreader.readLine ( ) ;
            }

            isr.close ( ) ;
        } catch ( IOException e ) {
            e.printStackTrace ( ) ;
        }

        return cal;
    }
}
