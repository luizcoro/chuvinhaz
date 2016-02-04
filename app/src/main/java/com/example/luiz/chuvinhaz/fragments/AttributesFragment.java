package com.example.luiz.chuvinhaz.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.luiz.chuvinhaz.ChuvinhaZ;
import com.example.luiz.chuvinhaz.R;
import com.example.luiz.chuvinhaz.models.Inst;
import com.example.luiz.chuvinhaz.models.User;
import com.example.luiz.chuvinhaz.services.CollectorService;
import com.example.luiz.chuvinhaz.utils.Constants;
import com.example.luiz.chuvinhaz.views.adapters.AttributeArrayAdapter;
import com.example.luiz.chuvinhaz.views.adapters.VariableListItem;

import java.util.ArrayList;

/**
 * Created by luiz on 10/25/15.
 */
public class AttributesFragment extends ListFragment {

    private ChuvinhaZ mApp;
    private AttributeArrayAdapter adapter;
    private Handler handler;
    private Runnable check_variables;
    private Inst old_instance;
    private int act;
    private int act_conf;

    private boolean fix_first_time = true;

    private boolean firstTime = true;

    public AttributesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = (ChuvinhaZ) getActivity().getApplicationContext();
        adapter = new AttributeArrayAdapter(getActivity(), generateData());

        setListAdapter(adapter);

        handler = new Handler();
        check_variables = new CheckVariablesRunnable();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attributes, container, false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(!fix_first_time){
            if (isVisibleToUser) {
                if(mApp.isCollectorConnected()) {
                    if (mApp.getCollectorServiceState() == CollectorService.state_not_collecting)
                        Toast.makeText(getContext(), getString(R.string.toasts_start_collecting_to_show_more), Toast.LENGTH_LONG).show();
                    else if (mApp.getCollectorServiceState() == CollectorService.state_preparing_to_collecting)
                        Toast.makeText(getContext(), getString(R.string.toasts_wait), Toast.LENGTH_LONG).show();

                    handler.postDelayed(new CheckVariablesRunnable(), Constants.HALF_SECOND);
                } else
                    Toast.makeText(getContext(), getString(R.string.toasts_system_starting), Toast.LENGTH_LONG).show();
            } else {
                handler.removeCallbacks(check_variables);
            }
        } else
            fix_first_time = false;
    }

    private ArrayList<VariableListItem> generateData(){
        Resources resources = getResources();

        Inst instance = mApp.getCollectorServiceInstance();
        User user = mApp.getUser();
        ArrayList<VariableListItem> items = new ArrayList<VariableListItem>();

        items.add(new VariableListItem("DEVICE_NAME", instance.getDevice().getName()));
        items.add(new VariableListItem("PHONE_TYPE", instance.getDevice().getTypeString()));
        items.add(new VariableListItem("DEVICE_SCREEN_SIZE",instance.getDevice().getScreen_sizeString()));

        items.add(new VariableListItem("ACTIVITY", user.getActString()));
        items.add(new VariableListItem("ACTIVITY_CONFIDENCE", user.getConfidenceActString()));

        items.add(new VariableListItem("CPU_AVG_LOAD_1M",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("CPU_AVG_LOAD_5M",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("CPU_AVG_LOAD_15M",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("CPU_ACTIVE_TASKS",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("CPU_TOTAL_TASKS",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("CPU_USAGE",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("CPU_N_CORE", instance.getSoc().getN_coresString()));

        items.add(new VariableListItem("NETWORK_TYPE",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("CID",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("LAC",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("PSC",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("MCC", instance.getDevice_network().getMccString()));
        items.add(new VariableListItem("MNC",instance.getDevice_network().getMncString()));
        items.add(new VariableListItem("GSM_STR",resources.getString(R.string.others_default_value)));

        items.add(new VariableListItem("GSM_TYPE_ARRAY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("GSM_CID_ARRAY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("GSM_LAC_ARRAY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("GSM_PSC_ARRAY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("GSM_SS_ARRAY",resources.getString(R.string.others_default_value)));

        items.add(new VariableListItem("SAT_FIX",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("SAT_PNR_ARRAY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("SAT_SS_ARRAY",resources.getString(R.string.others_default_value)));

        items.add(new VariableListItem("WIFI_FREQ_ARRAY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("WIFI_SS_ARRAY",resources.getString(R.string.others_default_value)));

        items.add(new VariableListItem("GPS_ACCURACY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("GPS_LAT",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("GPS_LNG",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("GPS_ALTITUDE",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("GPS_SPEED",resources.getString(R.string.others_default_value)));

        items.add(new VariableListItem("NETWORK_ACCURACY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("NETWORK_LAT",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("NETWORK_LNG",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("NETWORK_ALTITUDE",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("NETWORK_SPEED",resources.getString(R.string.others_default_value)));

        items.add(new VariableListItem("BATTERY_HEALTH",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("BATTERY_PLUGGED",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("BATTERY_STATUS",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("BATTERY_PERCENTAGE",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("BATTERY_CAPACITY", instance.getBattery().getCapacityString()));
        items.add(new VariableListItem("BATTERY_TEMP",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("BATTERY_VOLTAGE",resources.getString(R.string.others_default_value)));

        items.add(new VariableListItem("AMBIENT_TEMP",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("LIGHT",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("PRESSURE",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("RELATIVE_HUMIDITY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("PROXIMITY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("ACCELEROMETER_ARRAY", resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("AMBIENT_TEMP_ACCURACY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("LIGHT_ACCURACY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("PRESSURE_ACCURACY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("RELATIVE_HUMIDITY_ACCURACY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("PROXIMITY",resources.getString(R.string.others_default_value)));
        items.add(new VariableListItem("ACCELEROMETER_ACCURACY",resources.getString(R.string.others_default_value)));

        return items;
    }

    private void firstCheck()
    {

        User user = mApp.getUser();

        if(user != null) {
            updateView(Constants.ACT, user.getActString());
            updateView(Constants.ACT_CONFIDENCE, user.getConfidenceActString());
            act = user.getAct();
            act_conf = user.getConfidence_act();
        }

        if(mApp.isCollectorConnected() && mApp.getCollectorServiceState() == CollectorService.state_collecting)
        {
            Inst instance = mApp.getCollectorServiceInstance();

            updateView(Constants.PHONE_TYPE, instance.getDevice().getTypeString());

            updateView(Constants.CPU_AVG_1M, instance.getSoc().getCpu_avg_1mString());
            updateView(Constants.CPU_AVG_5M, instance.getSoc().getCpu_avg_5mString());
            updateView(Constants.CPU_AVG_15M, instance.getSoc().getCpu_avg_15mString());
            updateView(Constants.CPU_ACTIVE_TASKS, instance.getSoc().getActive_tasksString());
            updateView(Constants.CPU_TOTAL_TASKS, instance.getSoc().getTotal_tasksString());
            updateView(Constants.CPU_USAGE_PERCENTAGE, instance.getSoc().getCpu_usage_percentageString());

            updateView(Constants.NETWORK_TYPE, instance.getDevice_network().getNetworkTypeString());
            updateView(Constants.LAC, instance.getDevice_network().getLacString());
            updateView(Constants.CID, instance.getDevice_network().getCidString());
            updateView(Constants.PSC, instance.getDevice_network().getPscString());
            updateView(Constants.MCC, instance.getDevice_network().getMccString());
            updateView(Constants.MNC, instance.getDevice_network().getMncString());
            updateView(Constants.GSM_STR, instance.getDevice_network().getGsm_strString());

            updateView(Constants.GSM_TYPE_ARRAY, instance.getGsm_type_array_string());
            updateView(Constants.GSM_CID_ARRAY, instance.getGsm_cid_array_string());
            updateView(Constants.GSM_LAC_ARRAY, instance.getGsm_lac_array_string());
            updateView(Constants.GSM_PSC_ARRAY, instance.getGsm_psc_array_string());
            updateView(Constants.GSM_SS_ARRAY, instance.getGsm_ss_array_string());

            updateView(Constants.SAT_FIX, (instance.isSat_fix() ? "Yes" : "No" ));
            updateView(Constants.SAT_PNR_ARRAY, instance.getSatellite_pnr_array_string());
            updateView(Constants.SAT_SS_ARRAY, instance.getSatellite_ss_array_string());

            updateView(Constants.WIFI_FREQ_ARRAY, instance.getWifi_frequency_array_string());
            updateView(Constants.WIFI_SS_ARRAY, instance.getWifi_ss_array_string());

            updateView(Constants.GPS_ACCURACY, instance.getGps_accuracyString());
            updateView(Constants.GPS_LAT, instance.getGps_latString());
            updateView(Constants.GPS_LNG, instance.getGps_lngString());
            updateView(Constants.GPS_ALTITUDE, instance.getGps_altitudeString());
            updateView(Constants.GPS_SPEED, instance.getGps_speedString());

            updateView(Constants.NETWORK_ACCURACY, instance.getNetwork_accuracyString());
            updateView(Constants.NETWORK_LAT, instance.getNetwork_latString());
            updateView(Constants.NETWORK_LNG, instance.getNetwork_lngString());
            updateView(Constants.NETWORK_ALTITUDE, instance.getNetwork_altitudeString());
            updateView(Constants.NETWORK_SPEED, instance.getNetwork_speedString());

            updateView(Constants.BATTERY_HEALTH, instance.getBattery().getHealthString());
            updateView(Constants.BATTERY_PLUGGED, instance.getBattery().getPluggedString());
            updateView(Constants.BATTERY_STATUS, instance.getBattery().getStatusString());
            updateView(Constants.BATTERY_PERCENTAGE, instance.getBattery().getPercentageString());
            updateView(Constants.BATTERY_TEMP, instance.getBattery().getTempString());
            updateView(Constants.BATTERY_VOLTAGE, instance.getBattery().getVoltageString());

            updateView(Constants.AMBIENT_TEMP, instance.getSensors().getAmbient_tempString());
            updateView(Constants.LIGHT, instance.getSensors().getLightString());
            updateView(Constants.PRESSURE, instance.getSensors().getPressureString());
            updateView(Constants.RELATIVE_HUMIDITY, instance.getSensors().getRelative_humidityString());
            updateView(Constants.PROXIMITY, instance.getSensors().getProximityString());
            updateView(Constants.ACCELEROMETER_ARRAY, "[" + instance.getSensors().getAccelerometerXString() + "," + instance.getSensors().getAccelerometerYString() + "," + instance.getSensors().getAccelerometerZString() + "]");
            updateView(Constants.AMBIENT_TEMP_ACCURACY, instance.getSensors().getAc_ambient_tempString());
            updateView(Constants.LIGHT_ACCURACY, instance.getSensors().getAc_lightString());
            updateView(Constants.PRESSURE_ACCURACY, instance.getSensors().getAc_pressureString());
            updateView(Constants.RELATIVE_HUMIDITY_ACCURACY, instance.getSensors().getAc_relative_humidityString());
            updateView(Constants.PROXIMITY_ACCURACY, instance.getSensors().getAc_proximityString());
            updateView(Constants.ACCELEROMETER_ACCURACY, instance.getSensors().getAc_accelerometerString());

            adapter.notifyDataSetChanged();
            old_instance = new Inst();

            instance.setInstance(old_instance);

        }

        firstTime = false;

    }

    private class CheckVariablesRunnable implements Runnable {
        @Override
        public void run() {


            User user = mApp.getUser();

            if(user != null) {

                if(user.getAct() != act) {
                    updateView(Constants.ACT, user.getActString());
                    act = user.getAct();
                }

                if(user.getConfidence_act() != act_conf) {
                    updateView(Constants.ACT_CONFIDENCE, user.getConfidenceActString());
                    act_conf = user.getConfidence_act();
                }
            }

            if(mApp.isCollectorConnected() && mApp.getCollectorServiceState() == CollectorService.state_collecting)
            {
                if(firstTime)
                    firstCheck();

                Inst instance = mApp.getCollectorServiceInstance();

                // device

                if(instance.getDevice().getType() != (old_instance.getDevice().getType())) {
                    updateView(Constants.PHONE_TYPE, instance.getDevice().getTypeString());
                    old_instance.setPhone_type(instance.getDevice().getType());
                }

                //soc
                if(instance.getSoc().getCpu_avg_1m() != (old_instance.getSoc().getCpu_avg_1m())) {
                    updateView(Constants.CPU_AVG_1M, instance.getSoc().getCpu_avg_1mString());
                    old_instance.setSoc_cpu_avg_1m(instance.getSoc().getCpu_avg_1m());
                }

                if(instance.getSoc().getCpu_avg_5m() != (old_instance.getSoc().getCpu_avg_5m())) {
                    updateView(Constants.CPU_AVG_5M, instance.getSoc().getCpu_avg_5mString());
                    old_instance.setSoc_cpu_avg_5m(instance.getSoc().getCpu_avg_5m());
                }

                if(instance.getSoc().getCpu_avg_15m() != (old_instance.getSoc().getCpu_avg_15m())) {
                    updateView(Constants.CPU_AVG_15M, instance.getSoc().getCpu_avg_15mString());
                    old_instance.setSoc_cpu_avg_15m(instance.getSoc().getCpu_avg_15m());
                }

                if(instance.getSoc().getActive_tasks() != (old_instance.getSoc().getActive_tasks())) {
                    updateView(Constants.CPU_ACTIVE_TASKS, instance.getSoc().getActive_tasksString());
                    old_instance.setSoc_active_tasks(instance.getSoc().getActive_tasks());
                }

                if(instance.getSoc().getTotal_tasks() != (old_instance.getSoc().getTotal_tasks())) {
                    updateView(Constants.CPU_TOTAL_TASKS, instance.getSoc().getTotal_tasksString());
                    old_instance.setSoc_total_tasks(instance.getSoc().getTotal_tasks());
                }

                if(instance.getSoc().getCpu_usage_percentage() != (old_instance.getSoc().getCpu_usage_percentage())) {
                    updateView(Constants.CPU_USAGE_PERCENTAGE, instance.getSoc().getCpu_usage_percentageString());
                    old_instance.setSoc_cpu_usage_percentage(instance.getSoc().getCpu_usage_percentage());
                }


                //device_network
                if(instance.getDevice_network().getNetwork_type() != (old_instance.getDevice_network().getNetwork_type())) {
                    updateView(Constants.NETWORK_TYPE, instance.getDevice_network().getNetworkTypeString());
                    old_instance.setNetwork_type(instance.getDevice_network().getNetwork_type());
                }

                if(instance.getDevice_network().getCid() != old_instance.getDevice_network().getCid()) {
                    updateView(Constants.CID, instance.getDevice_network().getCidString());
                    old_instance.setCid(instance.getDevice_network().getCid());
                }

                if(instance.getDevice_network().getLac() != old_instance.getDevice_network().getLac()) {
                    updateView(Constants.LAC, instance.getDevice_network().getLacString());
                    old_instance.setLac(instance.getDevice_network().getLac());
                }

                if(instance.getDevice_network().getPsc() != old_instance.getDevice_network().getPsc()) {
                    updateView(Constants.PSC, instance.getDevice_network().getPscString());
                    old_instance.setPsc(instance.getDevice_network().getPsc());
                }

                if(instance.getDevice_network().getMcc() != old_instance.getDevice_network().getMcc()) {
                    updateView(Constants.MCC, instance.getDevice_network().getMccString());
                    old_instance.setMcc(instance.getDevice_network().getMcc());
                }

                if(instance.getDevice_network().getMnc() != old_instance.getDevice_network().getMnc()) {
                    updateView(Constants.MNC, instance.getDevice_network().getMncString());
                    old_instance.setMnc(instance.getDevice_network().getMnc());
                }

                if(instance.getDevice_network().getGsm_str() != old_instance.getDevice_network().getGsm_str()) {
                    updateView(Constants.GSM_STR, instance.getDevice_network().getGsm_strString());
                    old_instance.setGsm_str(instance.getDevice_network().getGsm_str());
                }

                //arrays

                if((!instance.getGsm_type_array_string().equals(old_instance.getGsm_type_array_string()))
                        || (!instance.getGsm_cid_array_string().equals(old_instance.getGsm_cid_array_string()))
                        || (!instance.getGsm_lac_array_string().equals(old_instance.getGsm_lac_array_string()))
                        || (!instance.getGsm_psc_array_string().equals(old_instance.getGsm_psc_array_string()))
                        || (!instance.getGsm_ss_array_string().equals(old_instance.getGsm_ss_array_string()))) {

                    updateView(Constants.GSM_TYPE_ARRAY, instance.getGsm_type_array_string());
                    updateView(Constants.GSM_CID_ARRAY, instance.getGsm_cid_array_string());
                    updateView(Constants.GSM_LAC_ARRAY, instance.getGsm_lac_array_string());
                    updateView(Constants.GSM_PSC_ARRAY, instance.getGsm_psc_array_string());
                    updateView(Constants.GSM_SS_ARRAY, instance.getGsm_ss_array_string());

                    old_instance.setGsm_ss_srray(instance.getGsm_ss_srray());
                }

                if(instance.isSat_fix() != old_instance.isSat_fix()) {
                    updateView(Constants.SAT_FIX, (instance.isSat_fix() ? "Yes" : "No" ));
                    old_instance.setIs_sat_fix(instance.isSat_fix());
                }

                if( (!instance.getSatellite_pnr_array_string().equals(old_instance.getSatellite_pnr_array_string()))
                        || (!instance.getSatellite_ss_array_string().equals(old_instance.getSatellite_ss_array_string()))) {

                    updateView(Constants.SAT_PNR_ARRAY, instance.getSatellite_pnr_array_string());
                    updateView(Constants.SAT_SS_ARRAY, instance.getSatellite_ss_array_string());

                    old_instance.setSat_ss_array(instance.getSat_ss_array());
                }

                if( (!instance.getWifi_frequency_array_string().equals(old_instance.getWifi_frequency_array_string()))
                        || (!instance.getWifi_ss_array_string().equals(old_instance.getWifi_ss_array_string()))) {

                    updateView(Constants.WIFI_FREQ_ARRAY, instance.getWifi_frequency_array_string());
                    updateView(Constants.WIFI_SS_ARRAY, instance.getWifi_ss_array_string());

                    old_instance.setWifi_ss_array(instance.getWifi_ss_array());
                }


                //locations
                if( (instance.getGps_accuracy() != old_instance.getGps_accuracy())
                        || (instance.getGps_latitude() != old_instance.getGps_latitude())
                        || (instance.getGps_longitude() != old_instance.getGps_longitude())
                        || (instance.getGps_altitude() != old_instance.getGps_altitude())
                        || (instance.getGps_speed() != old_instance.getGps_speed())) {

                    updateView(Constants.GPS_ACCURACY, instance.getGps_accuracyString());
                    updateView(Constants.GPS_LAT, instance.getGps_latString());
                    updateView(Constants.GPS_LNG, instance.getGps_lngString());
                    updateView(Constants.GPS_ALTITUDE, instance.getGps_altitudeString());
                    updateView(Constants.GPS_SPEED, instance.getGps_speedString());

                    old_instance.setGps_location(instance.getGps_location());
                }

                if( (instance.getNetwork_accuracy() != old_instance.getNetwork_accuracy())
                        || (instance.getNetwork_latitude() != old_instance.getNetwork_latitude())
                        || (instance.getNetwork_longitude() != old_instance.getNetwork_longitude())
                        || (instance.getNetwork_altitude() != old_instance.getNetwork_altitude())
                        || (instance.getNetwork_speed() != old_instance.getNetwork_speed())) {

                    updateView(Constants.NETWORK_ACCURACY, instance.getNetwork_accuracyString());
                    updateView(Constants.NETWORK_LAT, instance.getNetwork_latString());
                    updateView(Constants.NETWORK_LNG, instance.getNetwork_lngString());
                    updateView(Constants.NETWORK_ALTITUDE, instance.getNetwork_altitudeString());
                    updateView(Constants.NETWORK_SPEED, instance.getNetwork_speedString());

                    old_instance.setNetwork_location(instance.getNetwork_location());
                }

                //battery
                if(instance.getBattery().getHealth() != old_instance.getBattery().getHealth()) {
                    updateView(Constants.BATTERY_HEALTH, instance.getBattery().getHealthString());
                    old_instance.setBattery_health(instance.getBattery().getHealth());
                }

                if(instance.getBattery().getPlugged() != old_instance.getBattery().getPlugged()) {
                    updateView(Constants.BATTERY_PLUGGED, instance.getBattery().getPluggedString());
                    old_instance.setBattery_plugged(instance.getBattery().getPlugged());
                }

                if(instance.getBattery().getStatus() != old_instance.getBattery().getStatus()) {
                    updateView(Constants.BATTERY_STATUS, instance.getBattery().getStatusString());
                    old_instance.setBattery_status(instance.getBattery().getStatus());
                }

                if(instance.getBattery().getPercentage() != old_instance.getBattery().getPercentage()) {
                    updateView(Constants.BATTERY_PERCENTAGE, instance.getBattery().getPercentageString());
                    old_instance.setBattery_percentage(instance.getBattery().getPercentage());
                }

                if(instance.getBattery().getTemp() != old_instance.getBattery().getTemp()) {
                    updateView(Constants.BATTERY_TEMP, instance.getBattery().getTempString());
                    old_instance.setBattery_temp(instance.getBattery().getTemp());
                }

                if(instance.getBattery().getVoltage() != old_instance.getBattery().getVoltage()) {
                    updateView(Constants.BATTERY_VOLTAGE, instance.getBattery().getVoltageString());
                    old_instance.setBattery_voltage(instance.getBattery().getVoltage());
                }


                //sensors
                if(instance.getSensors().getAmbient_temp() != old_instance.getSensors().getAmbient_temp()) {
                    updateView(Constants.AMBIENT_TEMP, instance.getSensors().getAmbient_tempString());
                    old_instance.setAmbient_temp(instance.getSensors().getAmbient_temp());
                }

                if(instance.getSensors().getLight() != old_instance.getSensors().getLight()) {
                    updateView(Constants.LIGHT, instance.getSensors().getLightString());
                    old_instance.setLight(instance.getSensors().getLight());
                }

                if(instance.getSensors().getPressure() != old_instance.getSensors().getPressure()) {
                    updateView(Constants.PRESSURE, instance.getSensors().getPressureString());
                    old_instance.setPressure(instance.getSensors().getPressure());
                }


                if(instance.getSensors().getRelative_humidity() != old_instance.getSensors().getRelative_humidity()) {
                    updateView(Constants.RELATIVE_HUMIDITY, instance.getSensors().getRelative_humidityString());
                    old_instance.setRelative_humidity(instance.getSensors().getRelative_humidity());
                }

                if(instance.getSensors().getProximity() != old_instance.getSensors().getProximity()) {
                    updateView(Constants.PROXIMITY, instance.getSensors().getProximityString());
                    old_instance.setProximity(instance.getSensors().getProximity());
                }

                if( !instance.getSensors().getAccelerometerXString().equals(old_instance.getSensors().getAccelerometerXString()) || !instance.getSensors().getAccelerometerYString().equals(old_instance.getSensors().getAccelerometerYString()) || !instance.getSensors().getAccelerometerZString().equals(old_instance.getSensors().getAccelerometerZString())) {
                    updateView(Constants.ACCELEROMETER_ARRAY, "[" + instance.getSensors().getAccelerometerXString() + "," + instance.getSensors().getAccelerometerYString() + "," + instance.getSensors().getAccelerometerZString() + "]");
                    double[] accelerometer = instance.getSensors().getAccelerometer();
                    old_instance.setAccelerometer(accelerometer[0], accelerometer[1], accelerometer[2]);
                }

                if(instance.getSensors().getAc_ambient_temp() != old_instance.getSensors().getAc_ambient_temp()) {
                    updateView(Constants.AMBIENT_TEMP_ACCURACY, instance.getSensors().getAc_ambient_tempString());
                    old_instance.setAc_ambient_temp(instance.getSensors().getAc_ambient_temp());
                }

                if(instance.getSensors().getAc_light() != old_instance.getSensors().getAc_light()) {
                    updateView(Constants.LIGHT_ACCURACY, instance.getSensors().getAc_lightString());
                    old_instance.setAc_light(instance.getSensors().getAc_light());
                }

                if(instance.getSensors().getAc_pressure() != old_instance.getSensors().getAc_pressure()) {
                    updateView(Constants.PRESSURE_ACCURACY, instance.getSensors().getAc_pressureString());
                    old_instance.setAc_pressure(instance.getSensors().getAc_pressure());
                }

                if(instance.getSensors().getAc_relative_humidity() != old_instance.getSensors().getAc_relative_humidity()) {
                    updateView(Constants.RELATIVE_HUMIDITY_ACCURACY, instance.getSensors().getAc_relative_humidityString());
                    old_instance.setAc_relative_humidity(instance.getSensors().getAc_relative_humidity());
                }

                if(instance.getSensors().getAc_proximity() != old_instance.getSensors().getAc_proximity()) {
                    updateView(Constants.PROXIMITY_ACCURACY, instance.getSensors().getAc_proximityString());
                    old_instance.setAc_proximity(instance.getSensors().getAc_proximity());
                }

                if(instance.getSensors().getAc_Accelerometer() != old_instance.getSensors().getAc_Accelerometer()) {
                    updateView(Constants.ACCELEROMETER_ACCURACY, instance.getSensors().getAc_accelerometerString());
                    old_instance.setAc_Accelerometer(instance.getSensors().getAc_Accelerometer());
                }
            }

            adapter.notifyDataSetChanged();
            handler.postDelayed(check_variables, Constants.TWO_SECONDS);
        }
    }

    private void updateView(int index, String value){

        adapter.getItem(index).setValue(value);
    }
}