package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class GUI {


    private Calibration_lib cal = new Calibration_lib();

    private CheckBox c_use0;
    private CheckBox c1;
    private CheckBox c2;
    private CheckBox c3;
    private CheckBox c4;
    private CheckBox c5;
    private CheckBox c6;
    private CheckBox c7;
    private CheckBox c8;
    private CheckBox c9;
    private CheckBox c10;

    private TextView current0;
    private TextView con1;
    private TextView con2;
    private TextView con3;
    private TextView con4;
    private TextView con5;
    private TextView con6;
    private TextView con7;
    private TextView con8;
    private TextView con9;
    private TextView con10;

    private TextView val1;
    private TextView val2;
    private TextView val3;
    private TextView val4;
    private TextView val5;
    private TextView val6;
    private TextView val7;
    private TextView val8;
    private TextView val9;
    private TextView val10;

    private TextView unknown_val;
    private TextView unknown_con;

    MainActivity a;

    double scale_x = 1;
    double scale_y = 1;
    int graph_margin = 130;
    int display_w;
    int display_h;

    int arrow_size = 20;
    int axis_lines_size = 10;
    int point_size = 8;
    int text_size = 10;

    double x_max = 10;
    double y_max = 10;

    Paint p;
    Canvas canvas;


    public GUI(MainActivity a){
        this.a = a;
        this.c1 = (CheckBox) a.findViewById(R.id.checkBox1);
        this.c2 = (CheckBox) a.findViewById(R.id.checkBox2);
        this.c3 = (CheckBox) a.findViewById(R.id.checkBox3);
        this.c4 = (CheckBox) a.findViewById(R.id.checkBox4);
        this.c5 = (CheckBox) a.findViewById(R.id.checkBox5);
        this.c6 = (CheckBox) a.findViewById(R.id.checkBox6);
        this.c7 = (CheckBox) a.findViewById(R.id.checkBox7);
        this.c8 = (CheckBox) a.findViewById(R.id.checkBox8);
        this.c9 = (CheckBox) a.findViewById(R.id.checkBox9);
        this.c10 = (CheckBox) a.findViewById(R.id.checkBox10);

        this.con1 = (TextView) a.findViewById(R.id.con1);
        this.con2 = (TextView) a.findViewById(R.id.con2);
        this.con3 = (TextView) a.findViewById(R.id.con3);
        this.con4 = (TextView) a.findViewById(R.id.con4);
        this.con5 = (TextView) a.findViewById(R.id.con5);
        this.con6 = (TextView) a.findViewById(R.id.con6);
        this.con7 = (TextView) a.findViewById(R.id.con7);
        this.con8 = (TextView) a.findViewById(R.id.con8);
        this.con9 = (TextView) a.findViewById(R.id.con9);
        this.con10 = (TextView) a.findViewById(R.id.con10);

        this.val1 = (TextView) a.findViewById(R.id.val1);
        this.val2 = (TextView) a.findViewById(R.id.val2);
        this.val3 = (TextView) a.findViewById(R.id.val3);
        this.val4 = (TextView) a.findViewById(R.id.val4);
        this.val5 = (TextView) a.findViewById(R.id.val5);
        this.val6 = (TextView) a.findViewById(R.id.val6);
        this.val7 = (TextView) a.findViewById(R.id.val7);
        this.val8 = (TextView) a.findViewById(R.id.val8);
        this.val9 = (TextView) a.findViewById(R.id.val9);
        this.val10 = (TextView) a.findViewById(R.id.val10);

        this.current0 = (TextView) a.findViewById(R.id.current0);
        this.c_use0 = (CheckBox) a.findViewById(R.id.checkBoxUse0);

        this.unknown_val = (TextView) a.findViewById(R.id.u_value);
        this.unknown_con = (TextView) a.findViewById(R.id.calc_C);

        Button b_save = (Button) a.findViewById(R.id.btnSave);
        Button b_load = (Button) a.findViewById(R.id.btnLoad);

        c1.setChecked(true);
        c1.setEnabled(false);

        draw();

        View.OnClickListener oclBtn = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSave:
                        save_to_CSV();
                        break;
                    case R.id.btnLoad:
                        load_from_CSV();
                        break;
                }
            }
        };

        a.findViewById(R.id.root).getViewTreeObserver().addOnGlobalFocusChangeListener
                (
                        new ViewTreeObserver.OnGlobalFocusChangeListener() {
                            @Override
                            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                                on_click();
                            }
                        }
                );


        /*
        View.OnFocusChangeListener entry_listener = new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean has_focus){
                System.err.println("err");
                if(!has_focus){
                    Calibration_lib cal = get_numbers();
                    TextView RSD = (TextView)a.findViewById(R.id.RSD_val);
                    RSD.setText(String.valueOf(cal.get_RSD()));
                    System.err.println(String.valueOf(cal.get_RSD()));
                }
            }
        };
        */
        View.OnKeyListener kl = new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //if()
                on_click();
                return false;
            }
        };
        b_save.setOnClickListener(oclBtn);
        b_load.setOnClickListener(oclBtn);
        unknown_val.setOnKeyListener(kl);


    }

    public void on_click(){
        cal = get_numbers();
        TextView RSD = (TextView)a.findViewById(R.id.RSD_val);
        NumberFormat formatter = new DecimalFormat("#0.000");
        RSD.setText( String.valueOf(formatter.format(cal.get_RSD())));
        draw();
    }

    private void load_from_CSV() {
        Intent ii = new Intent(Intent.ACTION_GET_CONTENT);
        ii.addCategory(Intent.CATEGORY_OPENABLE);
        ii.setType("*/*");
        a.startActivityForResult(ii, 2);
    }

    private void save_to_CSV() {
        Intent ii = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        ii.addCategory(Intent.CATEGORY_OPENABLE);
        ii.setType("*/*");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        ii.putExtra(Intent.EXTRA_TITLE, "Calibration-" + dateFormat.format(date) + ".csv");
        a.startActivityForResult(ii, 1);
        System.err.println("save0");
    }

    private void get_point(Calibration_lib cal, int num, TextView con, TextView val, CheckBox c){
        String con_t = con.getText().toString();
        String val_t = val.getText().toString();
        if(!con_t.isEmpty() && !val_t.isEmpty()){
            try {
                double concentration = Double.parseDouble(con_t);
                double value = Double.parseDouble(val_t);
                cal.put_point(num, concentration, value, c.isChecked());

            }catch (NumberFormatException e){
                Toast.makeText(a, "Incorrect concentration or value", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Calibration_lib get_numbers() {
        Calibration_lib cal = new Calibration_lib();
        String val_0 = current0.getText().toString();

        if(!val_0.isEmpty()){
            try {
                double value_0 = Double.parseDouble(val_0);
                cal.set_cerrent_zero(value_0, c_use0.isChecked());
            }catch (NumberFormatException e){
                Toast.makeText(a, "Incorrect current zero value", Toast.LENGTH_LONG).show();
            }
        }
        else{
            cal.set_cerrent_zero(0, false);
        }
        get_point(cal, 1, con1, val1, c1);
        get_point(cal, 2, con2, val2, c2);
        get_point(cal, 3, con3, val3, c3);
        get_point(cal, 4, con4, val4, c4);
        get_point(cal, 5, con5, val5, c5);
        get_point(cal, 6, con6, val6, c6);
        get_point(cal, 7, con7, val7, c7);
        get_point(cal, 8, con8, val8, c8);
        get_point(cal, 9, con9, val9, c9);
        get_point(cal, 10, con10, val10, c10);
        cal.make_calibration();

        String unknown_val_string = unknown_val.getText().toString();
        if(!unknown_val_string.isEmpty() && cal.get_used_point_capacity()>1){
            try {
                double unknown_concentration = cal.calc_unknown(Double.parseDouble(unknown_val_string));
                NumberFormat formatter = new DecimalFormat("#0.0000");
                unknown_con.setText(String.valueOf(formatter.format(unknown_concentration)));
            }catch (NumberFormatException e){
                Toast.makeText(a, "Incorrect value of unknown concentration", Toast.LENGTH_LONG).show();
            }


        }
        return cal;
    }

    public Calibration_lib get_calibration(){
        return this.cal;
    }

    public void set_calibration(Calibration_lib cal){
        this.cal = cal;
        Calibration_point p = cal.get_point(1);
        if(p!= null) {
            val1.setText(String.valueOf(p.get_value()));
        }
        else{
            val1.setText("");
        }
        //load_point_entry(con1, val1, c1, p);
        p = cal.get_point(2);
        load_point_entry(con2, val2, c2, p);
        p = cal.get_point(3);
        load_point_entry(con3, val3, c3, p);
        p = cal.get_point(4);
        load_point_entry(con4, val4, c4, p);
        p = cal.get_point(5);
        load_point_entry(con5, val5, c5, p);
        p = cal.get_point(6);
        load_point_entry(con6, val6, c6, p);
        p = cal.get_point(7);
        load_point_entry(con7, val7, c7, p);
        p = cal.get_point(8);
        load_point_entry(con8, val8, c8, p);
        p = cal.get_point(9);
        load_point_entry(con9, val9, c9, p);
        p = cal.get_point(10);
        load_point_entry(con10, val10, c10, p);
    }

    private void load_point_entry(TextView con, TextView val, CheckBox c, Calibration_point p){
        if(p!= null) {
            con.setText(String.valueOf(p.get_concentration()));
            val.setText(String.valueOf(p.get_value()));
            c.setChecked(p.is_used());
        }
        else{
            con.setText("");
            val.setText("");
            c.setChecked(false);
        }
    }


    private void draw(){
        ImageView image = (ImageView) a.findViewById(R.id.canvas);
        Display d = a.getWindowManager().getDefaultDisplay();
        display_w = d.getWidth();
        display_h = (int)(d.getHeight()/1.6);
        Bitmap b = Bitmap.createBitmap(display_w, display_h, Bitmap.Config.RGB_565);
        canvas = new Canvas(b);
        canvas.drawColor(Color.WHITE);
        p = new Paint();
        set_scale();
        //draw_line(Color.BLUE, 4, 0, 50, 500, 400);
        draw_axises();

        draw_line1();
        draw_line2();
        draw_points();
        draw_X_point();


        image.setImageBitmap(b);
    }

    private void set_scale() {
        double x_max = cal.get_max_con();
        double y_max = cal.get_max_val();
        //HARDCODE!!!
        //x_max = 250;
        //y_max = 0.02;

        if (x_max != 0) {

            this.x_max = x_max;
        }
        if (y_max != 0) {

            this.y_max = y_max;
        }
        scale_x = (display_w-graph_margin*2)/this.x_max;
        scale_y = (display_h-graph_margin*2)/this.y_max;

    }

    private int get_display_coord_x(double real_x){
        int display_x = (int)(real_x*scale_x+graph_margin);
        return display_x;
    }
    private int get_display_coord_y(double real_y){
        int display_y = display_h-(int)(real_y*scale_y+graph_margin);
        return display_y;
    }
    private double get_real_x(int display_size){
        return display_size/scale_x;
    }
    private double get_real_y(int display_size){
        return display_size/scale_y;
    }

    private void draw_line(int color, int line_width, double x1, double y1, double x2, double y2){
        int x1_ = get_display_coord_x(x1);
        int y1_ = get_display_coord_y(y1);
        int x2_ = get_display_coord_x(x2);
        int y2_ = get_display_coord_y(y2);
        p.setColor(color);
        p.setStrokeWidth(line_width);
        p.setAntiAlias(true);
        canvas.drawLine(x1_, y1_, x2_, y2_, p);
    }
    private void draw_text(int color, int text_size, double x, double y, String text) {
        int x_ = get_display_coord_x(x);
        int y_ = get_display_coord_y(y);
        p.setColor(color);
        float scale = a.getResources().getDisplayMetrics().density;
        int pix_text_size = (int)(text_size*scale);
        p.setTextSize(pix_text_size);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, x_, y_, p);
    }
    private void draw_point(int color, int point_size, double x, double y) {
        int x_ = get_display_coord_x(x);
        int y_ = get_display_coord_y(y);
        p.setColor(color);
        canvas.drawRect(x_-point_size, y_+point_size, x_+point_size, y_-point_size, p);
    }

    private void translate(double x, double y){
        int x_ = get_display_coord_x(x);
        int y_ = get_display_coord_y(y);
        canvas.translate(x_, y_);
    }
    private void rotate(int angle, double x, double y){
        int x_ = get_display_coord_x(x);
        int y_ = get_display_coord_y(y);
        canvas.rotate(angle, x_, y_);
    }


    private void draw_axises(){
        double s_x = get_real_x(axis_lines_size);
        double s_y = get_real_y(axis_lines_size);
        double arrow_size_x = get_real_x(arrow_size);
        double arrow_size_y = get_real_y(arrow_size);
        double X_axis = (display_w-graph_margin*2)/scale_x+arrow_size_x*2;
        double Y_axis = (display_h-graph_margin*2)/scale_y+arrow_size_y*2;
        int arrow_text_size = 10;


        NumberFormat formatter = new DecimalFormat("#0.0000");

        //axises lines
        draw_line(Color.BLACK, 3,0, 0, X_axis, 0);
        draw_line(Color.BLACK, 3,0, 0, 0, Y_axis);

        //arrows
        draw_line(Color.BLACK, 3,0, Y_axis, 0-arrow_size_x/3, Y_axis-arrow_size_y);
        draw_line(Color.BLACK, 3,0, Y_axis, 0+arrow_size_x/3, Y_axis-arrow_size_y);

        draw_line(Color.BLACK, 3,X_axis, 0, X_axis-arrow_size_x, 0-arrow_size_y/3);
        draw_line(Color.BLACK, 3,X_axis, 0, X_axis-arrow_size_x, 0+arrow_size_y/3);

        int max_axis_lines = 25;
        //deg lines x


        double deg_x = 0.00001;
        int X_axis_lines = (int) (x_max/deg_x)+2;
        while(X_axis_lines>max_axis_lines) {
            deg_x *= 10;
            X_axis_lines = (int) (x_max/deg_x)+2;
        }

        //deg lines y
        double deg_y = 0.00001;
        int Y_axis_lines = (int) (y_max/deg_y)+2;
        while(Y_axis_lines>max_axis_lines) {
            deg_y *= 10;
            Y_axis_lines = (int) (y_max/deg_y)+2;
        }
        //deg lines x
        double text_y = -s_y*6;
        canvas.save();
        rotate(-90, 0, text_y);
        draw_text(Color.BLACK, arrow_text_size, 0, text_y, "0.0");
        canvas.restore();
        for(int i = 1; i<X_axis_lines; i++){
            double text_x = i*deg_x;
            draw_line(Color.GRAY, 2, i*deg_x, -s_y, i*deg_x, (Y_axis_lines-1)*deg_y+s_y);

            canvas.save();
            rotate(-90, text_x+s_x*0.8, text_y);

            String text = String.valueOf(formatter.format(text_x));
            draw_text(Color.BLACK, arrow_text_size,  text_x+s_x*0.8, text_y, text);
            canvas.restore();
        }
        //deg lines y
        double text_x = -s_x*6;
        draw_text(Color.BLACK, arrow_text_size,  text_x, 0, "0.0");//0.0000

        for(int i = 1; i<Y_axis_lines; i++){
            text_y = i*deg_y;
            draw_line(Color.GRAY, 2, -s_x, i*deg_y, (X_axis_lines-1)*deg_x+s_x, i*deg_y);
            String text = String.valueOf(formatter.format(text_y));

            draw_text(Color.BLACK, arrow_text_size, text_x, text_y-s_y*0.8, text);
        }

    }

    private void draw_points(){
        int[] points = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for(int i: points) {
            Calibration_point p = cal.get_point(i);
            if(p != null && p.is_used()){
                double x = p.get_concentration();
                double y =  p.get_value();
                draw_point(Color.BLUE, point_size, x, y);

                double s_y = get_real_y(axis_lines_size);
                draw_text(Color.RED, text_size, x, s_y*2+y, String.valueOf((p.get_num())));
            }
        }
    }

    private void draw_line1(){
        if(cal.get_point_capacity()>0) {
            double[] coords = cal.get_calibration_line();
            draw_line(Color.RED, 3,  coords[0], coords[1], coords[2], coords[3]);
        }
    }

    private void draw_line2(){
        if(cal.get_point_capacity()>0) {
            double[] coords = cal.get_calibration_line_with_current0();
            draw_line(Color.GREEN, 3,  coords[0], coords[1], coords[2], coords[3]);
        }
    }

    private void draw_X_point(){
        double x = cal.get_uncnown_con();
        double y = cal.get_uncnown_val();
        draw_point(Color.RED, (int)(point_size*1.5), x, y);
    }
}
