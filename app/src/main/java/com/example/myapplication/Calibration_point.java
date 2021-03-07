package com.example.myapplication;

public class Calibration_point {
    int point_num;
    private double concentration;
    private double value;
    private double cor_value;
    private boolean use_it;


    public Calibration_point(int point_num, double concentration, double value, boolean use_it) {
        this.point_num = point_num;
        this.concentration = concentration;
        this.value = value;
        this.use_it = use_it;
    }

    public int get_num() {
        return point_num;
    }
    public double get_concentration() {
        return concentration;
    }
    public double get_value() {
        return value;
    }
    public boolean is_used() {
        return use_it;
    }
    public void set_cor_value(double cor_value) {
        this.cor_value = cor_value;
    }
    public double get_cor_value() { return cor_value; }
}
