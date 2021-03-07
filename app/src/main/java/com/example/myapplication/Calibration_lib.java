package com.example.myapplication;

import java.util.ArrayList;

public class Calibration_lib {

    ArrayList<Calibration_point> data = new ArrayList<Calibration_point>();
    int calibration_capacity;
    private double slope=0.0;
    private double intercept = 0.0;
    private double intercept_with_0 = 0.0;
    private double zero_line_intercept = 0.0;

    private double RSD = 0.0;

    private int point_count = 0;
    private double summ_con = 0.0;
    private double summ_val = 0.0;
    private double summ_con2 = 0.0;
    private double summ_conXval = 0.0;
    private double summ_val_val_cor2 = 0.0;
    private double summ_cor_val = 0.0;

    private double current_zero = 0.0;
    private boolean use_current_zero = false;

    private double unknow_concentration = 0;
    private double unknow_value = 0;

    public void make_calibration() {
        int used_points = 0;
        for(Calibration_point c: data) {
            if(c.is_used()) used_points++;
        }
        if (used_points>1) {
            calc_summs();
            calc_intercept();
            calc_slope();
            calc_cors();
            calc_RSD();
            calc_zero_intercept();
        }

    }

    public double calc_unknown(double value) {
        unknow_value = value;
        unknow_concentration = (value- zero_line_intercept)/slope;
        return unknow_concentration;
    }

    private void calc_RSD() {
        RSD = 100*Math.sqrt(summ_val_val_cor2/point_count)/(summ_cor_val/point_count);
    }

    private void calc_cors() {
        for(Calibration_point c: data) {
            if(c.is_used()) {

                c.set_cor_value(c.get_concentration()*slope + intercept);
                summ_val_val_cor2 += Math.pow(c.get_value()-c.get_cor_value(), 2);
                summ_cor_val += c.get_cor_value();
            }
        }
    }
    private void calc_zero_intercept(){
        if(use_current_zero) {
            intercept_with_0 = current_zero - get_point(1).get_cor_value();
            zero_line_intercept = current_zero;
        }
        else {
            intercept_with_0 = 0;
            Calibration_point p = get_point(1);
            if (p!=null)  zero_line_intercept = p.get_cor_value();
        }
    }

    private void calc_intercept() {
        intercept = ((summ_val/point_count)-(summ_con*summ_conXval) / (point_count*summ_con2))*(point_count*summ_con2)/
                (point_count*summ_con2-summ_con*summ_con);
    }

    private void calc_slope() {
        slope = (summ_conXval-summ_con*intercept)/summ_con2;

    }

    private void calc_summs() {
        for(Calibration_point c: data) {
            if(c.is_used()) {
                summ_val += c.get_value();
                summ_con += c.get_concentration();
                summ_conXval += c.get_value()*c.get_concentration();
                summ_con2 += c.get_concentration()*c.get_concentration();
                point_count ++;
            }
        }
    }
    public void set_cerrent_zero(double curr_zero, boolean use_curr_zero){
        this.current_zero = curr_zero;
        this.use_current_zero = use_curr_zero;
    }

    public void put_point(int point_num, double concentration, double value, boolean use_it){
        data.add(new Calibration_point(point_num, concentration, value, use_it));
    }
    public Calibration_point get_point(int point_num){
        for(Calibration_point c: data) {
            if(c.point_num == point_num) return c;
        }
        return null;
    }

    public double get_RSD(){
        return RSD;
    }

    public String get_calibration_as_string(){
        String r = new String();

        for(Calibration_point c: data) {
            String num = String.valueOf(c.get_num());
            String usege = String.valueOf(c.is_used());
            String concentration = String.valueOf(c.get_concentration());
            String value = String.valueOf(c.get_value());
            String point_row = num + "," + usege + "," + concentration + "," + value;
            r = r + "\n" + point_row;
        }
        return r;
    }

    public void print() {
        for(Calibration_point c: data) {
            System.out.println("======" + c.get_num() + "======");
            System.out.println("conc " + c.get_concentration());
            System.out.println("value " + c.get_value());
            System.out.println("use " + c.is_used());
        }
        System.out.println("============");
        System.out.println("slope = " + slope);
        System.out.println("intercept = " + intercept);
        System.out.println("RSD = " + RSD);
    }

    public static void main(String[] args) {
        Calibration_lib cal = new Calibration_lib();

        cal.put_point(1, 0, 0.053, true);
        cal.put_point(2, 0.005,	0.075, true);
        cal.put_point(3, 0.01,	0.097, true);
        cal.put_point(4, 0.02,	0.141, true);
        cal.put_point(5, 0.03,	0.188, true);
        cal.put_point(6, 0.04,	0.233, true);
        cal.put_point(7, 0.05,	0.28, true);
        cal.put_point(8, 0.06,	0.324, true);
        cal.put_point(9, 0.07, 0.369, true);



        cal.print();
        System.out.println("unknown conc " + cal.calc_unknown(0.188));

    }

    public double get_max_con() {
        double con_max = get_max_con_points();
        if(unknow_concentration>con_max) con_max = unknow_concentration;
        return con_max;
    }
    public double get_max_con_points() {
        double con_max = 0;

        for (Calibration_point c : data) {
            if(c.is_used()) {
                double x = c.get_concentration();
                if (x > con_max) con_max = x;
            }
        }
        return con_max;
    }

    public double get_max_val() {
        double val_max = get_max_val_points();
        if (unknow_value > val_max) val_max = unknow_value;
        return val_max;
    }

    public double get_max_val_points() {
        double val_max = 0;
        for (Calibration_point c : data) {
            if(c.is_used()) {
                double x = c.get_value();
                if (x > val_max) val_max = x;
            }
        }
        return val_max;
    }
    public double get_max_cor_val() {
        double val_max = 0;

        for (Calibration_point c : data) {
            if(c.is_used()) {
                double x = c.get_cor_value();
                if (x > val_max) val_max = x;
            }
        }
        return val_max;
    }
    public double get_min_cor_val() {
        double val_min = data.get(0).get_cor_value();
        for (Calibration_point c : data) {
            if(c.is_used()) {
                double x = c.get_cor_value();
                if (x < val_min) val_min = x;
            }
        }
        return val_min;
    }
    public double get_min_con() {
        double con_min = data.get(0).get_concentration();
        for (Calibration_point c : data) {
            if(c.is_used()) {
                double x = c.get_concentration();
                if (x < con_min) con_min = x;
            }
        }
        return con_min;
    }
    public int get_point_capacity(){
        return data.size();
    }
    public int get_used_point_capacity(){
        int capacity = 0;
        for (Calibration_point c : data) {
            if (c.is_used()) {
                capacity++;
            }
        }
        return capacity;

    }
    public double[] get_calibration_line(){
        double [] coords = new double[4];
        coords[0] = get_max_con_points();
        coords[1] = get_max_cor_val();
        coords[2] = get_min_con();
        coords[3] = get_min_cor_val();
        return coords;
    }
    public double[] get_calibration_line_with_current0(){
        double [] coords = get_calibration_line();
        coords[1] += intercept_with_0;
        coords[3] += intercept_with_0;
        return coords;
    }
    public double get_uncnown_con(){
        return unknow_concentration;
    }
    public double get_uncnown_val(){
        return unknow_value;
    }
}
