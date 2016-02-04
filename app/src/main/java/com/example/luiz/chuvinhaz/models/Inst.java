package com.example.luiz.chuvinhaz.models;

import java.util.Iterator;
import java.util.List;

/**
 * Created by luiz on 05/05/15.
 */
public class Inst {

    private boolean is_sat_fix;

    private Device device;
    private Soc soc;
    private DeviceNetwork device_network;

    private Loc gps_location;
    private Loc network_location;

    private List<OperatorSignalStrength> gsm_ss_srray;
    private List<SatelliteSignalStrength> sat_ss_array;
    private List<WifiSignalStrength> wifi_ss_array;

    private Battery battery;
    private Sensors sensors;

    public Inst() {

        is_sat_fix = false;
        device = new Device();
        soc = new Soc();
        device_network = new DeviceNetwork();

        gps_location = null;
        network_location = null;

        gsm_ss_srray = null;
        sat_ss_array = null;
        wifi_ss_array = null;

        battery = new Battery();
        sensors = new Sensors();

    }

    public void setInstance(Inst to)
    {
        to.setIs_sat_fix(is_sat_fix);
        to.setDevice_name(device.getName());
        to.setPhone_type(device.getType());
        to.setDevice_screen_size(device.getScreen_size());
        to.setSoc_cpu_avg_1m(soc.getCpu_avg_1m());
        to.setSoc_cpu_avg_5m(soc.getCpu_avg_5m());
        to.setSoc_cpu_avg_15m(soc.getCpu_avg_15m());
        to.setSoc_active_tasks(soc.getActive_tasks());
        to.setSoc_total_tasks(soc.getTotal_tasks());
        to.setSoc_cpu_usage_percentage(soc.getCpu_usage_percentage());
        to.setSoc_n_cores(soc.getN_cores());
        to.setNetwork_type(device_network.getNetwork_type());
        to.setLac(device_network.getLac());
        to.setCid(device_network.getCid());
        to.setGsm_str(device_network.getGsm_str());
        to.setGsm_ss_srray(gsm_ss_srray);
        to.setSat_ss_array(sat_ss_array);
        to.setWifi_ss_array(wifi_ss_array);
        to.setGps_location(gps_location);
        to.setNetwork_location(network_location);
        to.setBattery_health(battery.getHealth());
        to.setBattery_plugged(battery.getPlugged());
        to.setBattery_status(battery.getStatus());
        to.setBattery_percentage(battery.getPercentage());
        to.setBattery_capacity(battery.getCapacity());
        to.setBattery_temp(battery.getTemp());
        to.setBattery_voltage(battery.getVoltage());
        to.setAmbient_temp(sensors.getAmbient_temp());
        to.setLight(sensors.getLight());
        to.setPressure(sensors.getPressure());
        to.setRelative_humidity(sensors.getRelative_humidity());
        to.setProximity(sensors.getProximity());
        double[] accelerometer = sensors.getAccelerometer();
        to.setAccelerometer(accelerometer[0], accelerometer[1], accelerometer[2]);
        to.setAc_ambient_temp(sensors.getAc_ambient_temp());
        to.setAc_light(sensors.getAc_light());
        to.setAc_pressure(sensors.getAc_pressure());
        to.setAc_relative_humidity(sensors.getAc_relative_humidity());
        to.setAc_proximity(sensors.getAc_proximity());
        to.setAc_Accelerometer(sensors.getAc_Accelerometer());


    }

    public void setIs_sat_fix(boolean is_fix) {
        is_sat_fix = is_fix;
    }

    public void setDevice_name(String device_name) {
        device.setName(device_name);
    }

    public void setPhone_type(int type)
    {
        device.setType(type);
    }

    public void setDevice_screen_size(double size)
    {
        device.setScreen_size(size);
    }

    public void setSoc_cpu_avg_1m(double cpu_avg)
    {
        soc.setCpu_avg_1m(cpu_avg);
    }

    public void setSoc_cpu_avg_5m(double cpu_avg)
    {
        soc.setCpu_avg_5m(cpu_avg);
    }

    public void setSoc_cpu_avg_15m(double cpu_avg)
    {
        soc.setCpu_avg_15m(cpu_avg);
    }

    public void setSoc_active_tasks(int tasks)
    {
        soc.setActive_tasks(tasks);
    }

    public void setSoc_total_tasks(int tasks)
    {
        soc.setTotal_tasks(tasks);
    }

    public void setSoc_cpu_usage_percentage(double percentage)
    {
        soc.setCpu_usage_percentage(percentage);
    }

    public void setSoc_n_cores(int cores)
    {
        soc.setN_cores(cores);
    }

    public void setNetwork_type(int type)
    {
        device_network.setNetwork_type(type);
    }

    public void setLac(int lac) {
        device_network.setLac(lac);
    }

    public void setCid(int cid) {
        device_network.setCid(cid);
    }

    public void setPsc(int psc) {
        device_network.setPsc(psc);
    }

    public void setMcc(int mcc) {
        device_network.setMcc(mcc);
    }


    public void setMnc(int mnc) {
        device_network.setMnc(mnc);
    }

    public void setGsm_str(int gsm_str) {
        device_network.setGsm_str(gsm_str);
    }

    public List<OperatorSignalStrength> getGsm_ss_srray() {
        return gsm_ss_srray;
    }

    public void setGsm_ss_srray(List<OperatorSignalStrength> gsm_ss_srray) {
        this.gsm_ss_srray = gsm_ss_srray;
    }

    public List<SatelliteSignalStrength> getSat_ss_array() {
        return sat_ss_array;
    }

    public void setSat_ss_array(List<SatelliteSignalStrength> sat_ss_array) {
        this.sat_ss_array = sat_ss_array;
    }

    public List<WifiSignalStrength> getWifi_ss_array() {
        return wifi_ss_array;
    }

    public void setWifi_ss_array(List<WifiSignalStrength> wifi_ss_array) {
        this.wifi_ss_array = wifi_ss_array;
    }


    public void setGps_location(float acc, double lat, double lng, double alt, float speed) {
        if(gps_location == null)
            gps_location = new Loc(acc, lat, lng, alt, speed);
        else
            this.gps_location.set(acc, lat, lng, alt, speed);
    }

    public void setGps_location(Loc loc) {
        gps_location = loc;
    }

    public void setNetwork_location(float acc, double lat, double lng, double alt, float speed) {
        if(network_location == null)
            network_location = new Loc(acc, lat,lng, alt, speed);
        else
            this.network_location.set(acc, lat, lng, alt, speed);
    }

    public void setNetwork_location(Loc loc) {
        network_location = loc;
    }

    public void setBattery_health(int health)
    {
        battery.setHealth(health);
    }

    public void setBattery_plugged(int plugged)
    {
        battery.setPlugged(plugged);
    }

    public void setBattery_status(int status) {
        battery.setStatus(status);
    }

    public void setBattery_percentage(double percentage) {
        battery.setPercentage(percentage);
    }

    public void setBattery_capacity(double capacity)
    {
        battery.setCapacity(capacity);
    }

    public void setBattery_temp(double temp) {
        battery.setTemp(temp);
    }

    public void setBattery_voltage(int voltage)
    {
        battery.setVoltage(voltage);
    }

    public void setAmbient_temp(double ambient_temp) {
        sensors.setAmbient_temp(ambient_temp);
    }

    public void setLight(double light) {
        sensors.setLight(light);
    }

    public void setPressure(double pressure) {
        sensors.setPressure(pressure);
    }

    public void setRelative_humidity(double relative_humidity) {
        sensors.setRelative_humidity(relative_humidity);
    }

    public void setAc_ambient_temp(int ac_ambient_temp) {
        sensors.setAc_ambient_temp(ac_ambient_temp);
    }

    public void setAc_light(int ac_light) {
        sensors.setAc_light(ac_light);
    }

    public void setAc_pressure(int ac_pressure) {
        sensors.setAc_pressure(ac_pressure);
    }

    public void setAc_relative_humidity(int ac_relative_humidity) {
        sensors.setAc_relative_humidity(ac_relative_humidity);
    }

    public void setProximity(double proximity) {
        sensors.setProximity(proximity);
    }

    public void setAccelerometer(double x, double y, double z) {
        sensors.setAccelerometer(x, y, z);
    }

    public void setAc_proximity(int ac_proximity){
        sensors.setAc_proximity(ac_proximity);
    }

    public void setAc_Accelerometer(int ac_Accelerometer) {
        sensors.setAc_Accelerometer(ac_Accelerometer);
    }

    public boolean isSat_fix() {
        return is_sat_fix;
    }

    public Device getDevice() {
        return device;
    }

    public Soc getSoc() {
        return soc;
    }

    public DeviceNetwork getDevice_network() {
        return device_network;
    }

    public Loc getGps_location() {return gps_location; }
    public Loc getNetwork_location() {return network_location; }

    public Battery getBattery() {
        return battery;
    }

    public Sensors getSensors() {
        return sensors;
    }

    public String getGsm_type_array_instance()
    {
        if(gsm_ss_srray == null || gsm_ss_srray.isEmpty())
            return "";

        StringBuilder builder = new StringBuilder();

        Iterator<OperatorSignalStrength> it = gsm_ss_srray.iterator();
        builder.append(it.next().getType());

        while(it.hasNext())
            builder.append("|").append(it.next().getType());

        return builder.toString();
    }

    public String getGsm_cid_array_instance()
    {
        if(gsm_ss_srray == null || gsm_ss_srray.isEmpty())
            return "";

        StringBuilder builder = new StringBuilder();

        Iterator<OperatorSignalStrength> it = gsm_ss_srray.iterator();
        builder.append(it.next().getCid());

        while(it.hasNext())
            builder.append("|").append(it.next().getCid());

        return builder.toString();
    }

    public String getGsm_lac_array_instance()
    {
        if(gsm_ss_srray == null || gsm_ss_srray.isEmpty())
            return "";

        StringBuilder builder = new StringBuilder();

        Iterator<OperatorSignalStrength> it = gsm_ss_srray.iterator();
        builder.append(it.next().getLac());

        while(it.hasNext())
            builder.append("|").append(it.next().getLac());

        return builder.toString();
    }

    public String getGsm_psc_array_instance()
    {
        if(gsm_ss_srray == null || gsm_ss_srray.isEmpty())
            return "";

        StringBuilder builder = new StringBuilder();

        Iterator<OperatorSignalStrength> it = gsm_ss_srray.iterator();
        builder.append(it.next().getPsc());

        while(it.hasNext())
            builder.append("|").append(it.next().getPsc());

        return builder.toString();
    }



    public String getGsm_ss_array_instance()
    {
        if(gsm_ss_srray == null || gsm_ss_srray.isEmpty())
            return "";

        StringBuilder builder = new StringBuilder();

        Iterator<OperatorSignalStrength> it = gsm_ss_srray.iterator();
        builder.append(it.next().getSignal_strength());

        while(it.hasNext())
            builder.append("|").append(it.next().getSignal_strength());

        return builder.toString();
    }


    public String getSatellite_pnr_array_instance()
    {
        if(sat_ss_array == null || sat_ss_array.isEmpty())
            return "";

        StringBuilder builder = new StringBuilder();

        Iterator<SatelliteSignalStrength> it = sat_ss_array.iterator();
        builder.append(it.next().getPnr());

        while(it.hasNext())
            builder.append("|").append(it.next().getPnr());

        return builder.toString();
    }

    public String getSatellite_ss_array_instance()
    {
        if(sat_ss_array == null || sat_ss_array.isEmpty())
            return "";

        StringBuilder builder = new StringBuilder();

        Iterator<SatelliteSignalStrength> it = sat_ss_array.iterator();
        builder.append(it.next().getSignal_strength());

        while(it.hasNext())
            builder.append("|").append(it.next().getSignal_strength());

        return builder.toString();
    }

    public String getWifi_frequency_array_instance()
    {
        if(wifi_ss_array == null || wifi_ss_array.isEmpty())
            return "";

        StringBuilder builder = new StringBuilder();

        Iterator<WifiSignalStrength> it = wifi_ss_array.iterator();
        builder.append(it.next().getFrequency());

        while(it.hasNext())
            builder.append("|").append(it.next().getFrequency());

        return builder.toString();
    }

    public String getWifi_ss_array_instance()
    {
        if(wifi_ss_array == null || wifi_ss_array.isEmpty())
            return "";

        StringBuilder builder = new StringBuilder();

        Iterator<WifiSignalStrength> it = wifi_ss_array.iterator();
        builder.append(it.next().getRssi());

        while(it.hasNext())
            builder.append("|").append(it.next().getRssi());

        return builder.toString();
    }

    public String getGsm_type_array_string()
    {
        if(gsm_ss_srray == null || gsm_ss_srray.isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder();

        Iterator<OperatorSignalStrength> it = gsm_ss_srray.iterator();
        builder.append("[").append(it.next().getType());

        while(it.hasNext())
            builder.append(",").append(it.next().getType());

        builder.append("]");

        return builder.toString();
    }

    public String getGsm_cid_array_string()
    {
        if(gsm_ss_srray == null || gsm_ss_srray.isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder();

        Iterator<OperatorSignalStrength> it = gsm_ss_srray.iterator();
        builder.append("[").append(it.next().getCid());

        while(it.hasNext())
            builder.append(",").append(it.next().getCid());

        builder.append("]");

        return builder.toString();
    }

    public String getGsm_lac_array_string()
    {
        if(gsm_ss_srray == null || gsm_ss_srray.isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder();

        Iterator<OperatorSignalStrength> it = gsm_ss_srray.iterator();
        builder.append("[").append(it.next().getLac());

        while(it.hasNext())
            builder.append(",").append(it.next().getLac());

        builder.append("]");

        return builder.toString();
    }

    public String getGsm_psc_array_string()
    {
        if(gsm_ss_srray == null || gsm_ss_srray.isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder();

        Iterator<OperatorSignalStrength> it = gsm_ss_srray.iterator();
        builder.append("[").append(it.next().getPsc());

        while(it.hasNext())
            builder.append(",").append(it.next().getPsc());

        builder.append("]");

        return builder.toString();
    }

    public String getGsm_ss_array_string()
    {
        if(gsm_ss_srray == null || gsm_ss_srray.isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder();

        Iterator<OperatorSignalStrength> it = gsm_ss_srray.iterator();
        builder.append("[").append(it.next().getSignal_strength());

        while(it.hasNext())
            builder.append(",").append(it.next().getSignal_strength());

        builder.append("]");

        return builder.toString();
    }

    public String getSatellite_pnr_array_string()
    {
        if(sat_ss_array == null || sat_ss_array.isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder();

        Iterator<SatelliteSignalStrength> it = sat_ss_array.iterator();
        builder.append("[").append(it.next().getPnr());

        while(it.hasNext())
            builder.append(",").append(it.next().getPnr());

        builder.append("]");

        return builder.toString();
    }

    public String getSatellite_ss_array_string()
    {
        if(sat_ss_array == null || sat_ss_array.isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder();

        Iterator<SatelliteSignalStrength> it = sat_ss_array.iterator();
        builder.append("[").append(it.next().getSignal_strength());

        while(it.hasNext())
            builder.append(",").append(it.next().getSignal_strength());

        builder.append("]");

        return builder.toString();
    }

    public String getWifi_frequency_array_string()
    {
        if(wifi_ss_array == null || wifi_ss_array.isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder();

        Iterator<WifiSignalStrength> it = wifi_ss_array.iterator();
        builder.append("[").append(it.next().getFrequency());

        while(it.hasNext())
            builder.append(",").append(it.next().getFrequency());

        builder.append("]");

        return builder.toString();
    }

    public String getWifi_ss_array_string()
    {
        if(wifi_ss_array == null || wifi_ss_array.isEmpty())
            return "[]";

        StringBuilder builder = new StringBuilder();

        Iterator<WifiSignalStrength> it = wifi_ss_array.iterator();
        builder.append("[").append(it.next().getRssi());

        while(it.hasNext())
            builder.append(",").append(it.next().getRssi());

        builder.append("]");

        return builder.toString();
    }

    public String getGps_accuracyString() {
        return (gps_location == null ? "?" : String.valueOf(gps_location.getAccuracy()));
    }

    public String getGps_latString() {
        return (gps_location == null ? "?" : String.valueOf(gps_location.getLatitude()));
    }

    public String getGps_lngString() {
        return (gps_location == null ? "?" : String.valueOf(gps_location.getLongitude()));
    }

    public String  getGps_altitudeString() {
        return (gps_location == null ? "?" : String.valueOf(gps_location.getAltitude()));
    }

    public String  getGps_speedString() {
        return (gps_location == null ? "?" : String.valueOf(gps_location.getSpeed()));
    }

    public String getNetwork_accuracyString() {
        return (network_location == null ? "?" : String.valueOf(network_location.getAccuracy()));
    }

    public String getNetwork_latString() {
        return (network_location == null ? "?" : String.valueOf(network_location.getLatitude()));
    }

    public String getNetwork_lngString() {
        return (network_location == null ? "?" : String.valueOf(network_location.getLongitude()));
    }

    public String  getNetwork_altitudeString() {
        return (network_location == null ? "?" : String.valueOf(network_location.getAltitude()));
    }

    public String  getNetwork_speedString() {
        return (network_location == null ? "?" : String.valueOf(network_location.getSpeed()));
    }

    public double getGps_accuracy() {
        return (gps_location == null ? -1.d : gps_location.getAccuracy());
    }

    public double getGps_latitude() {
        return (gps_location == null ? -1.d : gps_location.getLatitude());
    }

    public double getGps_longitude() {
        return (gps_location == null ? -1.d : gps_location.getLongitude());
    }

    public double  getGps_altitude() {
        return (gps_location == null ? -1.d : gps_location.getAltitude());
    }

    public double  getGps_speed() {
        return (gps_location == null ? -1.d : gps_location.getSpeed());
    }

    public double getNetwork_accuracy() {
        return (network_location == null ? -1.d : network_location.getAccuracy());
    }

    public double getNetwork_latitude() {
        return (network_location == null ? -1.d : network_location.getLatitude());
    }

    public double getNetwork_longitude() {
        return (network_location == null ? -1.d : network_location.getLongitude());
    }

    public double  getNetwork_altitude() {
        return (network_location == null ? -1.d : network_location.getAltitude());
    }

    public double  getNetwork_speed() {
        return (network_location == null ? -1.d : network_location.getSpeed());
    }


    @Override
    public String toString() {
        return device.getName() +
                ',' + device.getType() +
                ',' + device.getScreen_size() +

                ',' + soc.getCpu_avg_1m() +
                ',' + soc.getCpu_avg_5m() +
                ',' + soc.getCpu_avg_15m() +
                ',' + soc.getActive_tasks() +
                ',' + soc.getTotal_tasks() +
                ',' + soc.getCpu_usage_percentage() +
                ',' + soc.getN_cores() +

                ',' + device_network.getNetwork_type() +
                ',' + device_network.getCid() +
                ',' + device_network.getLac() +
                ',' + device_network.getPsc() +
                ',' + device_network.getMcc() +
                ',' + device_network.getMnc() +
                ',' + device_network.getGsm_str() +

                ',' + getGsm_type_array_instance() +
                ',' + getGsm_cid_array_instance() +
                ',' + getGsm_lac_array_instance() +
                ',' + getGsm_psc_array_instance() +
                ',' + getGsm_ss_array_instance() +

                ',' + (is_sat_fix ? 1 : 0) +
                ',' + getSatellite_pnr_array_instance() +
                ',' + getSatellite_ss_array_instance() +

                ',' + getWifi_ss_array_instance() +
                ',' + getWifi_frequency_array_instance() +

                ',' + gps_location.getAccuracy() +
                ',' + gps_location.getLatitude() +
                ',' + gps_location.getLongitude() +
                ',' + gps_location.getLatitude() +
                ',' + gps_location.getSpeed() +

                ',' + network_location.getAccuracy() +
                ',' + network_location.getLatitude() +
                ',' + network_location.getLongitude() +
                ',' + network_location.getLatitude() +
                ',' + network_location.getSpeed() +

                ',' + battery.getHealth() +
                ',' + battery.getPlugged() +
                ',' + battery.getStatus() +
                ',' + battery.getPercentage() +
                ',' + battery.getCapacity() +
                ',' + battery.getTemp() +
                ',' + battery.getVoltage() +

                ',' + sensors.getAmbient_temp() +
                ',' + sensors.getLight() +
                ',' + sensors.getPressure() +
                ',' + sensors.getRelative_humidity() +
                ',' + sensors.getProximity() +
                "," + sensors.getAccelerometerX() + '|' + sensors.getAccelerometerY() + '|' + sensors.getAccelerometerZ() +
                "," + sensors.getAc_ambient_temp() +
                ',' + sensors.getAc_light() +
                ',' + sensors.getAc_pressure() +
                ',' + sensors.getAc_relative_humidity() +
                ',' + sensors.getAc_proximity() +
                ',' + sensors.getAc_Accelerometer();
    }
}
