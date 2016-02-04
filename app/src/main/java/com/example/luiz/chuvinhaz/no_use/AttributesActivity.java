//package com.example.luiz.chuvinhaz.activities;
//
//import android.app.ListActivity;
//import android.content.res.Resources;
//import android.os.Handler;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.example.luiz.chuvinhaz.R;
//import com.example.luiz.chuvinhaz.views.adapters.VariableListItem;
//import com.example.luiz.chuvinhaz.views.adapters.AttributeArrayAdapter;
//import com.example.luiz.chuvinhaz.ChuvinhaZ;
//import com.example.luiz.chuvinhaz.models.Inst;
//import com.example.luiz.chuvinhaz.models.OperatorSignalStrength;
//import com.example.luiz.chuvinhaz.models.SatelliteSignalStrength;
//import com.example.luiz.chuvinhaz.models.WifiSignalStrength;
//import com.example.luiz.chuvinhaz.services.CollectorService;
//import com.example.luiz.chuvinhaz.utils.Constants;
//
//import java.util.ArrayList;
//
//public class AttributesActivity extends ListActivity {
//
//    private ChuvinhaZ mApp;
//    private AttributeArrayAdapter adapter;
//    private Handler handler;
//    private Runnable check_variables;
//    private Inst old_instance;
//
//    private boolean firstTime;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mApp = (ChuvinhaZ) getApplication();
//
//        adapter = new AttributeArrayAdapter(this, generateData());
//        handler = new Handler();
//        check_variables = new CheckVariablesRunnable();
//        handler.postDelayed(new CheckVariablesRunnable(), 2000);
//        firstTime = true;
//        setListAdapter(adapter);
//
//
//
//        if(!mApp.isCollectorConnected())
//            Toast.makeText(getApplicationContext(), "Start collecting for update", Toast.LENGTH_LONG).show();
//        else if(mApp.getCollectorServiceState() == CollectorService.state_preparing_to_collecting)
//            Toast.makeText(getApplicationContext(), "Wait", Toast.LENGTH_LONG).show();
//
//        handler = new Handler();
//        check_variables = new CheckVariablesRunnable();
//        handler.postDelayed(new CheckVariablesRunnable(), 2000);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        handler.removeCallbacks(check_variables);
//    }
//
//    private ArrayList<VariableListItem> generateData(){
//        Resources resources = getResources();
//        ArrayList<VariableListItem> items = new ArrayList<VariableListItem>();
//
//        items.add(new VariableListItem("DEVICE_NAME",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("PHONE_TYPE",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("DEVICE_SCREEN_SIZE",resources.getString(R.string.others_default_value)));
//
//        items.add(new VariableListItem("CPU_AVG_LOAD_1M",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("CPU_AVG_LOAD_5M",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("CPU_AVG_LOAD_15M",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("CPU_ACTIVE_TASKS",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("CPU_TOTAL_TASKS",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("CPU_USAGE",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("CPU_N_CORE",resources.getString(R.string.others_default_value)));
//
//        items.add(new VariableListItem("NETWORK_TYPE",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("LAC",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("CID",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("MCC",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("MNC",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("GSM_STR",resources.getString(R.string.others_default_value)));
//
//        items.add(new VariableListItem("GSM_CID_ARRAY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("GSM_LAC_ARRAY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("GSM_SS_ARRAY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("SAT_PNR_ARRAY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("SAT_SS_ARRAY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("FREQ_SS_ARRAY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("WIFI_SS_ARRAY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("GPS_ACCURACY",resources.getString(R.string.others_default_value)));
//
//        items.add(new VariableListItem("GPS_LAT",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("GPS_LNG",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("NETWORK_ACCURACY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("NETWORK_LAT",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("NETWORK_LNG",resources.getString(R.string.others_default_value)));
//
//        items.add(new VariableListItem("SPEED",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("ALTITUDE",resources.getString(R.string.others_default_value)));
//
//        items.add(new VariableListItem("BATTERY_HEALTH",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("BATTERY_PLUGGED",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("BATTERY_STATUS",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("BATTERY_PERCENTAGE",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("BATTERY_CAPACITY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("BATTERY_TEMP",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("BATTERY_VOLTAGE",resources.getString(R.string.others_default_value)));
//
//        items.add(new VariableListItem("AMBIENT_TEMP",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("LIGH",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("PRESSURE",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("RELATIVE_HUMIDITY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("PROXIMITY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("ACCELEROMETER_ARRAY", resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("AMBIENTE_TEMP_ACCURACY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("LIGHT_ACCURACY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("PRESSURE_ACCURACY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("RELATIVE_HUMIDITY_ACCURACY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("PROXIMITY",resources.getString(R.string.others_default_value)));
//        items.add(new VariableListItem("ACCELEROMETER_ACCURACY",resources.getString(R.string.others_default_value)));
//
//        return items;
//    }
//
//    private void firstCheck()
//    {
//
//        if(mApp.isCollectorConnected() && mApp.getCollectorServiceState() == CollectorService.state_collecting)
//        {
//            Inst instance = mApp.getCollectorServiceInstance();
//
//            updateView(Constants.DEVICE_NAME, instance.getDevice().getName());
//            updateView(Constants.PHONE_TYPE, instance.getDevice().getTypeString());
//            updateView(Constants.DEVICE_SCREEN_SIZE, instance.getDevice().getScreen_sizeString());
//
//            updateView(Constants.CPU_AVG_1M, instance.getSoc().getCpu_avg_1mString());
//            updateView(Constants.CPU_AVG_5M, instance.getSoc().getCpu_avg_5mString());
//            updateView(Constants.CPU_AVG_15M, instance.getSoc().getCpu_avg_15mString());
//            updateView(Constants.CPU_ACTIVE_TASKS, instance.getSoc().getActive_tasksString());
//            updateView(Constants.CPU_TOTAL_TASKS, instance.getSoc().getTotal_tasksString());
//            updateView(Constants.CPU_USAGE_PERCENTAGE, instance.getSoc().getCpu_usage_percentageString());
//            updateView(Constants.CPU_N_CORES, instance.getSoc().getN_coresString());
//
//            updateView(Constants.NETWORK_TYPE, instance.getDevice_network().getNetworkTypeString());
//            updateView(Constants.LAC, instance.getDevice_network().getLacString());
//            updateView(Constants.CID, instance.getDevice_network().getCidString());
//            updateView(Constants.MCC, instance.getDevice_network().getMccString());
//            updateView(Constants.MNC, instance.getDevice_network().getMncString());
//            updateView(Constants.GSM_STR, instance.getDevice_network().getGsm_strString());
//
//            updateView(Constants.GSM_CID_ARRAY, instance.getGsm_cid_array_string());
//            updateView(Constants.GSM_LAC_ARRAY, instance.getGsm_lac_array_string());
//            updateView(Constants.GSM_SS_ARRAY, instance.getGsm_ss_array_string());
//            updateView(Constants.SAT_PNR_ARRAY, instance.getSatellite_pnr_array_string());
//            updateView(Constants.SAT_SS_ARRAY, instance.getSatellite_ss_array_string());
//            updateView(Constants.WIFI_FREQ_ARRAY, instance.getWifi_frequency_array_string());
//            updateView(Constants.WIFI_SS_ARRAY, instance.getWifi_ss_array_string());
//
//            updateView(Constants.GPS_ACCURACY, instance.getLocations().getGpsAccuracyString());
//            updateView(Constants.GPS_LAT, instance.getLocations().getGpsLatString());
//            updateView(Constants.GPS_LNG, instance.getLocations().getGpsLngString());
//            updateView(Constants.NETWORK_ACCURACY, instance.getLocations().getNetworkAccuracyString());
//            updateView(Constants.NETWORK_LAT, instance.getLocations().getNetworkLatString());
//            updateView(Constants.NETWORK_LNG, instance.getLocations().getNetworkLngString());
//
//            updateView(Constants.SPEED, instance.getSpeedString());
//            updateView(Constants.ALTITUDE, instance.getAltitudeString());
//
//            updateView(Constants.BATTERY_HEALTH, instance.getBattery().getHealthString());
//            updateView(Constants.BATTERY_PLUGGED, instance.getBattery().getPluggedString());
//            updateView(Constants.BATTERY_STATUS, instance.getBattery().getStatusString());
//            updateView(Constants.BATTERY_PERCENTAGE, instance.getBattery().getPercentageString());
//            updateView(Constants.BATTERY_CAPACITY, instance.getBattery().getCapacityString());
//            updateView(Constants.BATTERY_TEMP, instance.getBattery().getTempString());
//            updateView(Constants.BATTERY_VOLTAGE, instance.getBattery().getVoltageString());
//
//            updateView(Constants.AMBIENT_TEMP, instance.getSensors().getAmbient_tempString());
//            updateView(Constants.LIGHT, instance.getSensors().getLightString());
//            updateView(Constants.PRESSURE, instance.getSensors().getPressureString());
//            updateView(Constants.RELATIVE_HUMIDITY, instance.getSensors().getRelative_humidityString());
//            updateView(Constants.PROXIMITY, instance.getSensors().getProximityString());
//            updateView(Constants.ACCELEROMETER_ARRAY, "[" + instance.getSensors().getAccelerometerXString() + "," + instance.getSensors().getAccelerometerYString() + "," + instance.getSensors().getAccelerometerZString() + "]");
//            updateView(Constants.AMBIENT_TEMP_ACCURACY, instance.getSensors().getAc_ambient_tempString());
//            updateView(Constants.LIGHT_ACCURACY, instance.getSensors().getAc_lightString());
//            updateView(Constants.PRESSURE_ACCURACY, instance.getSensors().getAc_pressureString());
//            updateView(Constants.RELATIVE_HUMIDITY_ACCURACY, instance.getSensors().getAc_relative_humidityString());
//            updateView(Constants.PROXIMITY_ACCURACY, instance.getSensors().getAc_proximityString());
//            updateView(Constants.ACCELEROMETER_ACCURACY, instance.getSensors().getAc_accelerometerString());
//
//            adapter.notifyDataSetChanged();
//            old_instance = new Inst();
//            instance.setInstance(old_instance);
//        }
//
//        firstTime = false;
//
//    }
//
//    private class CheckVariablesRunnable implements Runnable {
//        @Override
//        public void run() {
//
//            if(mApp.isCollectorConnected() && mApp.getCollectorServiceState() == CollectorService.state_collecting)
//            {
//                if(firstTime)
//                    firstCheck();
//
//                Inst instance = mApp.getCollectorServiceInstance();
//
//                // device
//                if(!instance.getDevice().getName().equals(old_instance.getDevice().getName())) {
//                    updateView(Constants.DEVICE_NAME, instance.getDevice().getName());
//                    old_instance.setDevice_name(instance.getDevice().getName());
//                }
//
//                if(instance.getDevice().getType() != (old_instance.getDevice().getType())) {
//                    updateView(Constants.PHONE_TYPE, instance.getDevice().getTypeString());
//                    old_instance.setPhone_type(instance.getDevice().getType());
//                }
//
//                if(instance.getDevice().getScreen_size() != (old_instance.getDevice().getScreen_size())) {
//                    updateView(Constants.DEVICE_SCREEN_SIZE, instance.getDevice().getScreen_sizeString());
//                    old_instance.setDevice_screen_size(instance.getDevice().getScreen_size());
//                }
//
//                //soc
//                if(instance.getSoc().getCpu_avg_1m() != (old_instance.getSoc().getCpu_avg_1m())) {
//                    updateView(Constants.CPU_AVG_1M, instance.getSoc().getCpu_avg_1mString());
//                    old_instance.setSoc_cpu_avg_1m(instance.getSoc().getCpu_avg_1m());
//                }
//
//                if(instance.getSoc().getCpu_avg_5m() != (old_instance.getSoc().getCpu_avg_5m())) {
//                    updateView(Constants.CPU_AVG_5M, instance.getSoc().getCpu_avg_5mString());
//                    old_instance.setSoc_cpu_avg_5m(instance.getSoc().getCpu_avg_5m());
//                }
//
//                if(instance.getSoc().getCpu_avg_15m() != (old_instance.getSoc().getCpu_avg_15m())) {
//                    updateView(Constants.CPU_AVG_15M, instance.getSoc().getCpu_avg_15mString());
//                    old_instance.setSoc_cpu_avg_15m(instance.getSoc().getCpu_avg_15m());
//                }
//
//                if(instance.getSoc().getActive_tasks() != (old_instance.getSoc().getActive_tasks())) {
//                    updateView(Constants.CPU_ACTIVE_TASKS, instance.getSoc().getActive_tasksString());
//                    old_instance.setSoc_active_tasks(instance.getSoc().getActive_tasks());
//                }
//
//                if(instance.getSoc().getTotal_tasks() != (old_instance.getSoc().getTotal_tasks())) {
//                    updateView(Constants.CPU_TOTAL_TASKS, instance.getSoc().getTotal_tasksString());
//                    old_instance.setSoc_total_tasks(instance.getSoc().getTotal_tasks());
//                }
//
//                if(instance.getSoc().getCpu_usage_percentage() != (old_instance.getSoc().getCpu_usage_percentage())) {
//                    updateView(Constants.CPU_USAGE_PERCENTAGE, instance.getSoc().getCpu_usage_percentageString());
//                    old_instance.setSoc_cpu_usage_percentage(instance.getSoc().getCpu_usage_percentage());
//                }
//
//                if(instance.getSoc().getN_cores() != (old_instance.getSoc().getN_cores())) {
//                    updateView(Constants.CPU_N_CORES, instance.getSoc().getN_coresString());
//                    old_instance.setSoc_n_cores(instance.getSoc().getN_cores());
//                }
//
//                //device_network
//                if(instance.getDevice_network().getNetwork_type() != (old_instance.getDevice_network().getNetwork_type())) {
//                    updateView(Constants.NETWORK_TYPE, instance.getDevice_network().getNetworkTypeString());
//                    old_instance.setNetwork_type(instance.getDevice_network().getNetwork_type());
//                }
//
//
//                //arrays
//                if(instance.getDevice_network().getLac() != old_instance.getDevice_network().getLac()) {
//                    updateView(Constants.LAC, instance.getDevice_network().getLacString());
//                    old_instance.setLac(instance.getDevice_network().getLac());
//                }
//
//                if(instance.getDevice_network().getCid() != old_instance.getDevice_network().getCid()) {
//                    updateView(Constants.CID, instance.getDevice_network().getCidString());
//                    old_instance.setCid(instance.getDevice_network().getCid());
//                }
//
//                if(instance.getDevice_network().getMcc() != old_instance.getDevice_network().getMcc()) {
//                    updateView(Constants.MCC, instance.getDevice_network().getMccString());
//                    old_instance.setMcc(instance.getDevice_network().getMcc());
//                }
//
//                if(instance.getDevice_network().getMnc() != old_instance.getDevice_network().getMnc()) {
//                    updateView(Constants.MNC, instance.getDevice_network().getMncString());
//                    old_instance.setMnc(instance.getDevice_network().getMnc());
//                }
//
//                if(instance.getDevice_network().getGsm_str() != old_instance.getDevice_network().getGsm_str()) {
//                    updateView(Constants.GSM_STR, instance.getDevice_network().getGsm_strString());
//                    old_instance.setGsm_str(instance.getDevice_network().getGsm_str());
//                }
//
//                if(!instance.getGsm_cid_array_string().equals(old_instance.getGsm_cid_array_string())) {
//                    updateView(Constants.GSM_CID_ARRAY, instance.getGsm_cid_array_string());
//                }
//
//                if(!instance.getGsm_lac_array_string().equals(old_instance.getGsm_lac_array_string())) {
//                    updateView(Constants.GSM_LAC_ARRAY, instance.getGsm_lac_array_string());
//                }
//
//                if(!instance.getGsm_ss_array_string().equals(old_instance.getGsm_ss_array_string())) {
//                    updateView(Constants.GSM_SS_ARRAY, instance.getGsm_ss_array_string());
//                }
//
//                if(!instance.getSatellite_pnr_array_string().equals(old_instance.getSatellite_pnr_array_string())) {
//                    updateView(Constants.SAT_PNR_ARRAY, instance.getSatellite_pnr_array_string());
//                }
//
//                if(!instance.getSatellite_ss_array_string().equals(old_instance.getSatellite_ss_array_string())) {
//                    updateView(Constants.SAT_SS_ARRAY, instance.getSatellite_ss_array_string());
//                }
//
//                if(!instance.getWifi_frequency_array_string().equals(old_instance.getWifi_frequency_array_string())) {
//                    updateView(Constants.WIFI_FREQ_ARRAY, instance.getWifi_frequency_array_string());
//                }
//
//                if(!instance.getWifi_ss_array_string().equals(old_instance.getWifi_ss_array_string())) {
//                    updateView(Constants.WIFI_SS_ARRAY, instance.getWifi_ss_array_string());
//                }
//
//
//                //locations
//                if(instance.getLocations().getGps_accuracy() != old_instance.getLocations().getGps_accuracy()) {
//                    updateView(Constants.GPS_ACCURACY, instance.getLocations().getGpsAccuracyString());
//                    old_instance.setGpsAccuracy(instance.getLocations().getGps_accuracy());
//                }
//
//                if(instance.getLocations().getGps_lat() != old_instance.getLocations().getGps_lat()) {
//                    updateView(Constants.GPS_LAT, instance.getLocations().getGpsLatString());
//                    old_instance.setGps_lat(instance.getLocations().getGps_lat());
//                }
//
//                if(instance.getLocations().getGps_lng() != old_instance.getLocations().getGps_lng()) {
//                    updateView(Constants.GPS_LNG, instance.getLocations().getGpsLngString());
//                    old_instance.setGps_lng(instance.getLocations().getGps_lng());
//                }
//
//                if(instance.getLocations().getNetwork_accuracy() != old_instance.getLocations().getNetwork_accuracy()) {
//                    updateView(Constants.NETWORK_ACCURACY, instance.getLocations().getNetworkAccuracyString());
//                    old_instance.setNetworkAccuracy(instance.getLocations().getNetwork_accuracy());
//                }
//
//                if(instance.getLocations().getNetwork_lat() != old_instance.getLocations().getNetwork_lat()) {
//                    updateView(Constants.NETWORK_LAT, instance.getLocations().getNetworkLatString());
//                    old_instance.setNetwork_lat(instance.getLocations().getNetwork_lat());
//                }
//
//                if(instance.getLocations().getNetwork_lng() != old_instance.getLocations().getNetwork_lng()) {
//                    updateView(Constants.NETWORK_LNG, instance.getLocations().getNetworkLngString());
//                    old_instance.setNetwork_lng(instance.getLocations().getNetwork_lng());
//                }
//
//                if(instance.getSpeed() != old_instance.getSpeed()) {
//                    updateView(Constants.SPEED, instance.getSpeedString());
//                    old_instance.setSpeed(instance.getSpeed());
//                }
//
//
//                if(instance.getAltitude() != old_instance.getAltitude()) {
//                    updateView(Constants.ALTITUDE, instance.getAltitudeString());
//                    old_instance.setAltitude(instance.getAltitude());
//                }
//
//                //battery
//                if(instance.getBattery().getHealth() != old_instance.getBattery().getHealth()) {
//                    updateView(Constants.BATTERY_HEALTH, instance.getBattery().getHealthString());
//                    old_instance.setBattery_health(instance.getBattery().getHealth());
//                }
//
//                if(instance.getBattery().getPlugged() != old_instance.getBattery().getPlugged()) {
//                    updateView(Constants.BATTERY_PLUGGED, instance.getBattery().getPluggedString());
//                    old_instance.setBattery_plugged(instance.getBattery().getPlugged());
//                }
//
//                if(instance.getBattery().getStatus() != old_instance.getBattery().getStatus()) {
//                    updateView(Constants.BATTERY_STATUS, instance.getBattery().getStatusString());
//                    old_instance.setBattery_status(instance.getBattery().getStatus());
//                }
//
//                if(instance.getBattery().getPercentage() != old_instance.getBattery().getPercentage()) {
//                    updateView(Constants.BATTERY_PERCENTAGE, instance.getBattery().getPercentageString());
//                    old_instance.setBattery_percentage(instance.getBattery().getPercentage());
//                }
//
//                if(instance.getBattery().getCapacity() != old_instance.getBattery().getCapacity()) {
//                    updateView(Constants.BATTERY_CAPACITY, instance.getBattery().getCapacityString());
//                    old_instance.setBattery_capacity(instance.getBattery().getCapacity());
//                }
//
//                if(instance.getBattery().getTemp() != old_instance.getBattery().getTemp()) {
//                    updateView(Constants.BATTERY_TEMP, instance.getBattery().getTempString());
//                    old_instance.setBattery_temp(instance.getBattery().getTemp());
//                }
//
//                if(instance.getBattery().getVoltage() != old_instance.getBattery().getVoltage()) {
//                    updateView(Constants.BATTERY_VOLTAGE, instance.getBattery().getVoltageString());
//                    old_instance.setBattery_voltage(instance.getBattery().getVoltage());
//                }
//
//
//                //sensors
//                if(instance.getSensors().getAmbient_temp() != old_instance.getSensors().getAmbient_temp()) {
//                    updateView(Constants.AMBIENT_TEMP, instance.getSensors().getAmbient_tempString());
//                    old_instance.setAmbient_temp(instance.getSensors().getAmbient_temp());
//                }
//
//                if(instance.getSensors().getLight() != old_instance.getSensors().getLight()) {
//                    updateView(Constants.LIGHT, instance.getSensors().getLightString());
//                    old_instance.setLight(instance.getSensors().getLight());
//                }
//
//                if(instance.getSensors().getPressure() != old_instance.getSensors().getPressure()) {
//                    updateView(Constants.PRESSURE, instance.getSensors().getPressureString());
//                    old_instance.setPressure(instance.getSensors().getPressure());
//                }
//
//
//                if(instance.getSensors().getRelative_humidity() != old_instance.getSensors().getRelative_humidity()) {
//                    updateView(Constants.RELATIVE_HUMIDITY, instance.getSensors().getRelative_humidityString());
//                    old_instance.setRelative_humidity(instance.getSensors().getRelative_humidity());
//                }
//
//                if(instance.getSensors().getProximity() != old_instance.getSensors().getProximity()) {
//                    updateView(Constants.PROXIMITY, instance.getSensors().getProximityString());
//                    old_instance.setProximity(instance.getSensors().getProximity());
//                }
//
//                if( !instance.getSensors().getAccelerometerXString().equals(old_instance.getSensors().getAccelerometerXString()) || !instance.getSensors().getAccelerometerYString().equals(old_instance.getSensors().getAccelerometerYString()) || !instance.getSensors().getAccelerometerZString().equals(old_instance.getSensors().getAccelerometerZString())) {
//                    updateView(Constants.ACCELEROMETER_ARRAY, "[" + instance.getSensors().getAccelerometerXString() + "," + instance.getSensors().getAccelerometerYString() + "," + instance.getSensors().getAccelerometerZString() + "]");
//                    double[] accelerometer = instance.getSensors().getAccelerometer();
//                    old_instance.setAccelerometer(accelerometer[0], accelerometer[1], accelerometer[2]);
//                }
//
//                if(instance.getSensors().getAc_ambient_temp() != old_instance.getSensors().getAc_ambient_temp()) {
//                    updateView(Constants.AMBIENT_TEMP_ACCURACY, instance.getSensors().getAc_ambient_tempString());
//                    old_instance.setAc_ambient_temp(instance.getSensors().getAc_ambient_temp());
//                }
//
//                if(instance.getSensors().getAc_light() != old_instance.getSensors().getAc_light()) {
//                    updateView(Constants.LIGHT_ACCURACY, instance.getSensors().getAc_lightString());
//                    old_instance.setAc_light(instance.getSensors().getAc_light());
//                }
//
//                if(instance.getSensors().getAc_pressure() != old_instance.getSensors().getAc_pressure()) {
//                    updateView(Constants.PRESSURE_ACCURACY, instance.getSensors().getAc_pressureString());
//                    old_instance.setAc_pressure(instance.getSensors().getAc_pressure());
//                }
//
//                if(instance.getSensors().getAc_relative_humidity() != old_instance.getSensors().getAc_relative_humidity()) {
//                    updateView(Constants.RELATIVE_HUMIDITY_ACCURACY, instance.getSensors().getAc_relative_humidityString());
//                    old_instance.setAc_relative_humidity(instance.getSensors().getAc_relative_humidity());
//                }
//
//                if(instance.getSensors().getAc_proximity() != old_instance.getSensors().getAc_proximity()) {
//                    updateView(Constants.PROXIMITY_ACCURACY, instance.getSensors().getAc_proximityString());
//                    old_instance.setAc_proximity(instance.getSensors().getAc_proximity());
//                }
//
//                if(instance.getSensors().getAc_Accelerometer() != old_instance.getSensors().getAc_Accelerometer()) {
//                    updateView(Constants.ACCELEROMETER_ACCURACY, instance.getSensors().getAc_accelerometerString());
//                    old_instance.setAc_Accelerometer(instance.getSensors().getAc_Accelerometer());
//                }
//
//                //best separetad
//                old_instance.setGsm_ss_srray(new ArrayList<OperatorSignalStrength>(instance.getGsm_ss_srray()));
//                old_instance.setSat_ss_array(new ArrayList<SatelliteSignalStrength>(instance.getSat_ss_array()));
//                old_instance.setWifi_ss_array(new ArrayList<WifiSignalStrength>(instance.getWifi_ss_array()));
//            }
//
//            adapter.notifyDataSetChanged();
//            handler.postDelayed(check_variables, 2000);
//        }
//    }
//
//    private void updateView(int index, String value){
//
//        adapter.getItem(index).setValue(value);
//    }
//}
