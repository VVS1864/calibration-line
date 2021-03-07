package com.example.myapplication;


import java.io.FileOutputStream;
import java.io.IOException;


public class Save_to_CSV {

    //SpreadSheet spreadSheet;
    public Save_to_CSV(Calibration_lib cal, FileOutputStream fos){
        try {
            write_head(fos);
            write_data(cal, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private void write_data(Calibration_lib cal, FileOutputStream fos) throws IOException {
        String s_points = cal.get_calibration_as_string();
        fos.write(s_points.getBytes());
    }

    private void write_head(FileOutputStream fos) throws IOException {
        fos.write("Point, Usege, Concentration, Value".getBytes());
    }
}
