package com.example.luiz.chuvinhaz.models;

import com.example.luiz.chuvinhaz.utils.Utils;

/**
 * Created by luiz on 8/16/15.
 */
public class Soc {


    // /proc/loadavg
    private double cpu_avg_1m;
    private double cpu_avg_5m;
    private double cpu_avg_15m;
    private int active_tasks;
    private int total_tasks;

    // /proc/stat
    private double cpu_usage_percentage;
    private int n_cores;

    public Soc() {
        cpu_avg_1m = -1d;
        cpu_avg_5m = -1d;
        cpu_avg_15m = -1d;
        active_tasks = -1;
        total_tasks = -1;

        cpu_usage_percentage = -1d;
        n_cores = -1;
    }

    public double getCpu_avg_1m() {
        return cpu_avg_1m;
    }

    public void setCpu_avg_1m(double cpu_avg_1m) {
        this.cpu_avg_1m = cpu_avg_1m;
    }

    public double getCpu_avg_5m() {
        return cpu_avg_5m;
    }

    public void setCpu_avg_5m(double cpu_avg_5m) {
        this.cpu_avg_5m = cpu_avg_5m;
    }

    public double getCpu_avg_15m() {
        return cpu_avg_15m;
    }

    public void setCpu_avg_15m(double cpu_avg_15m) {
        this.cpu_avg_15m = cpu_avg_15m;
    }

    public int getActive_tasks() {
        return active_tasks;
    }

    public void setActive_tasks(int active_tasks) {
        this.active_tasks = active_tasks;
    }

    public int getTotal_tasks() {
        return total_tasks;
    }

    public void setTotal_tasks(int total_tasks) {
        this.total_tasks = total_tasks;
    }

    public double getCpu_usage_percentage() {
        return cpu_usage_percentage;
    }

    public void setCpu_usage_percentage(double cpu_usage_percentage) {
        this.cpu_usage_percentage = cpu_usage_percentage;
    }

    public int getN_cores() {
        return n_cores;
    }

    public void setN_cores(int n_cores) {
        this.n_cores = n_cores;
    }

    public String getCpu_avg_1mString() {
        return Utils.attributeToString(cpu_avg_1m, -1d);
    }

    public String getCpu_avg_5mString() {
        return Utils.attributeToString(cpu_avg_5m, -1d);
    }

    public String getCpu_avg_15mString() {
        return Utils.attributeToString(cpu_avg_15m, -1d);
    }

    public String getActive_tasksString() {
        return Utils.attributeToString(active_tasks, -1);
    }

    public String getTotal_tasksString() {
        return Utils.attributeToString(total_tasks, -1);
    }

    public String getCpu_usage_percentageString() {
        return Utils.attributeToString(Utils.round(cpu_usage_percentage, 2), -1d);
    }

    public String getN_coresString() {
        return Utils.attributeToString(n_cores, -1);
    }
}
