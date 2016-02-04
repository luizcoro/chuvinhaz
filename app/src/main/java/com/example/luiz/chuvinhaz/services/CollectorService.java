package com.example.luiz.chuvinhaz.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Toast;

import com.example.luiz.chuvinhaz.MainActivity;
import com.example.luiz.chuvinhaz.R;
import com.example.luiz.chuvinhaz.managers.HardwareManager;
import com.example.luiz.chuvinhaz.managers.MyBatteryManager;
import com.example.luiz.chuvinhaz.models.Inst;
import com.example.luiz.chuvinhaz.models.OperatorSignalStrength;
import com.example.luiz.chuvinhaz.models.SatelliteSignalStrength;
import com.example.luiz.chuvinhaz.models.WifiSignalStrength;
import com.example.luiz.chuvinhaz.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CollectorService extends Service {
	public static final String TAG = CollectorService.class.getSimpleName();

	public static final int state_not_collecting = 0;
	public static final int state_preparing_to_collecting = 1;
	public static final int state_collecting = 2;


	private Inst instance;


	private TelephonyManager mTelephonyManager;
	private LocationManager mLocationManager;
	private SensorManager mSensorManager;
	private WifiManager mWifiManager;

	private boolean sensorRegistered = false;

	private Set<Integer> availableSensors;

	private int collecting = state_not_collecting;

	private PendingIntent googlePendingItent;

	private Handler handler;
	private Runnable set_colleting_true;


	private long mLastLocationMillis = -1;

	//for cpu monitoring
	private Runnable cpu_monitor;
	private long prev_running = 0, prev_total = 0;

//	private Runnable get_cell_info;



	public class LocalBinder extends Binder {
		public CollectorService getService() {
			return CollectorService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {

		Intent resultIntent = new Intent(this, MainActivity.class);
    	PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher_branco)
    		    .setContentTitle(getString(R.string.toasts_chuvinhaz_is_running))
    		    .setOnlyAlertOnce(true)
    		    .setPriority(Notification.PRIORITY_LOW)
				.setContentIntent(resultPendingIntent);

		int mNotificationId = 1;
		startForeground(mNotificationId, mBuilder.build());

		instance = new Inst();

		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		instance.setPhone_type(mTelephonyManager.getPhoneType());
		instance.setDevice_name(HardwareManager.getDeviceName());
		instance.setDevice_screen_size(HardwareManager.getScreen_size(this));
		instance.setSoc_n_cores(HardwareManager.readN_cores());
		instance.setBattery_capacity(MyBatteryManager.getBatteryCapacity(this));

		String networkOperator = mTelephonyManager.getNetworkOperator();

		if (networkOperator != null) {
			instance.setMcc(Integer.parseInt(networkOperator.substring(0, 3)));
			instance.setMnc(Integer.parseInt(networkOperator.substring(3)));
		}

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

		availableSensors = new HashSet<>();
		for(Sensor s : sensors)
		{
			availableSensors.add(s.getType());
		}

		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.ONE_SECOND, 1, mLocListener);
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Constants.ONE_SECOND, 1, mLocListener);

		Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if(lastKnownLocation != null)
			instance.setGps_location(lastKnownLocation.getAccuracy(), lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), lastKnownLocation.getAltitude(), lastKnownLocation.getSpeed());
		else
		{
			lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if(lastKnownLocation != null)
				instance.setNetwork_location(lastKnownLocation.getAccuracy(), lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), lastKnownLocation.getAltitude(), lastKnownLocation.getSpeed());
		}

		handler = new Handler();

		set_colleting_true = new Runnable() {
			@Override
			public void run() {
				collecting = state_collecting;
				Toast.makeText(getApplicationContext(), getString(R.string.toasts_collection_started), Toast.LENGTH_SHORT).show();
			}
		};

		cpu_monitor = new Runnable() {
			@Override
			public void run() {
				long[] cpu_usage = HardwareManager.readCpuUsage();
				double[] load_avg = HardwareManager.readLoadAvg();

				if(load_avg != null) {
					instance.setSoc_cpu_avg_1m(load_avg[0]);
					instance.setSoc_cpu_avg_5m(load_avg[1]);
					instance.setSoc_cpu_avg_15m(load_avg[2]);
					instance.setSoc_active_tasks((int) load_avg[3]);
					instance.setSoc_total_tasks((int) load_avg[4]);
				}

				if(cpu_usage != null) {
					long running = cpu_usage[0] + cpu_usage[1] + cpu_usage[2];
					long total = running + cpu_usage[3];

					instance.setSoc_cpu_usage_percentage(HardwareManager.getCpuUsage(running, total, prev_running, prev_total));

					prev_running = running;
					prev_total = total;
				}

				handler.postDelayed(this, Constants.TWO_SECONDS);
			}
		};

        return new LocalBinder();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

		mLocationManager.removeUpdates(mLocListener);

		if(collecting != state_not_collecting)
			stopCollecting();

		stopForeground(true);
	}

    private final LocationListener mLocListener = new LocationListener()
    {

		@Override
    	public void onLocationChanged(Location loc)
    	{
			mLastLocationMillis = SystemClock.elapsedRealtime();

			if (LocationManager.GPS_PROVIDER.equals(loc.getProvider()))
				instance.setGps_location(loc.getAccuracy(), loc.getLatitude(), loc.getLongitude(), loc.getAltitude(), loc.getSpeed());
			else if (LocationManager.NETWORK_PROVIDER.equals(loc.getProvider()))
				instance.setNetwork_location(loc.getAccuracy(), loc.getLatitude(), loc.getLongitude(), loc.getAltitude(), loc.getSpeed());

    	}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        	//Toast.makeText(getApplicationContext(), "Location status changed: " + provider + ", " + status , Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
        	//Toast.makeText(getApplicationContext(), "Location provider enabled: " + provider, Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onProviderDisabled(String provider) {
        	//Toast.makeText(getApplicationContext(), "Location provider disabled: " + provider, Toast.LENGTH_SHORT).show();

			if (LocationManager.GPS_PROVIDER.equals(provider))
			{
				instance.setGps_location(null);
				instance.setSat_ss_array(null);
			}
			else if (LocationManager.NETWORK_PROVIDER.equals(provider))
			{
				instance.setNetwork_location(null);
			}
        }
    };

    // Listener for signal strength.
    private final PhoneStateListener mListener = new PhoneStateListener()
    {
		@Override
		public void onDataConnectionStateChanged(int state, int networkType) {
			super.onDataConnectionStateChanged(state, networkType);
			instance.setNetwork_type(networkType);
		}

		@Override
    	public void onCellLocationChanged(CellLocation mCellLocation)
    	{
			super.onCellLocationChanged(mCellLocation);

			if(mCellLocation instanceof GsmCellLocation) {
				GsmCellLocation gsmCellLocation= (GsmCellLocation) mCellLocation;
				instance.setCid(gsmCellLocation.getCid() & 0xffff);
				instance.setLac(gsmCellLocation.getLac() & 0xffff);
				instance.setPsc(gsmCellLocation.getPsc() & 0x1ff);
			} else {
				CdmaCellLocation cdmaCellLocation= (CdmaCellLocation) mCellLocation;
				instance.setCid(cdmaCellLocation.getBaseStationId());
				instance.setLac(cdmaCellLocation.getNetworkId());
				instance.setPsc(cdmaCellLocation.getSystemId());
			}

    	}

    	@Override
    	public void onSignalStrengthsChanged(SignalStrength strength) {
			super.onSignalStrengthsChanged(strength);

//			String[] bits = strength.toString().split(" ");
//			Toast.makeText(getApplicationContext(), "gsm signal: " + strength.getGsmSignalStrength() + "\ncdma signal: " + strength.getCdmaDbm() + "\nevdo signal: " + strength.getEvdoDbm() + "\nunknown signal: " + bits[9].substring(1) , Toast.LENGTH_LONG ).show();
//
//			try {
//				Method[] methods = android.telephony.SignalStrength.class
//						.getMethods();
//				for (Method mthd : methods) {
//					if (mthd.getName().equals("getLteSignalStrength")
//							|| mthd.getName().equals("getLteRsrp")
//							|| mthd.getName().equals("getLteRsrq")
//							|| mthd.getName().equals("getLteRssnr")
//							|| mthd.getName().equals("getLteCqi")) {
//						Toast.makeText(getApplicationContext(), mthd.getName() + " "+ mthd.invoke(strength) , Toast.LENGTH_SHORT ).show();
//					}
//				}
//			} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
			if (strength.isGsm()) {
				instance.setGsm_str((strength.getGsmSignalStrength()) < 32 ? (2*strength.getGsmSignalStrength() - 113) : -9999);

			} else {
				instance.setGsm_str(strength.getCdmaDbm());
			}

			//Log.e("ss", strength.isGsm() ? "gsm" : "nao");

			List<NeighboringCellInfo> results = mTelephonyManager.getNeighboringCellInfo();
			List<OperatorSignalStrength> neighbors = new ArrayList<>();

			for (NeighboringCellInfo cell : results) {
				int type = cell.getNetworkType();

				if ("GSM".equals(Constants.getNeighborNetworkType(type))) {
					neighbors.add(new OperatorSignalStrength(type, (cell.getCid() & 0xffff), (cell.getLac() & 0xffff), cell.getPsc(), ((cell.getRssi() < 32) ? (2 * cell.getRssi() - 113) : -9999)));
				} else if ("UMTS".equals(Constants.getNeighborNetworkType(type))) {
					neighbors.add(new OperatorSignalStrength(type, (cell.getCid() & 0xffff), (cell.getLac() & 0xffff), (cell.getPsc() & 0x1ff), cell.getRssi()));
				} else {
					neighbors.add(new OperatorSignalStrength(type, -1, -1, -1, -1));
				}


				//neighbors.add(new OperatorSignalStrength(result.getPsc(), result.getLac(), result.getRssi() < 32 ? -113 + (2 *  result.getRssi()) : -9999));
			}
			instance.setGsm_ss_srray(neighbors);
		}
    };

	private final  BroadcastReceiver wifiReceiver  = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			List<ScanResult> results = mWifiManager.getScanResults();
			List<WifiSignalStrength> wifis = new ArrayList<>();

			for(ScanResult result : results)
			{
				wifis.add(new WifiSignalStrength(result.frequency, result.level));
			}

			instance.setWifi_ss_array(wifis);

			mWifiManager.startScan();
		}
	};

    private final GpsStatus.Listener gpsListener = new GpsStatus.Listener() {

		GpsStatus gpsStatus = null;



		@Override
		public void onGpsStatusChanged(int event) {
			gpsStatus = mLocationManager.getGpsStatus(gpsStatus);

	        switch (event) {
	            case GpsStatus.GPS_EVENT_STARTED:
	            	//Toast.makeText(getApplicationContext(), "gps started" , Toast.LENGTH_SHORT).show();
	                break;
	            case GpsStatus.GPS_EVENT_STOPPED:
	            	//Toast.makeText(getApplicationContext(), "gps stopped", Toast.LENGTH_SHORT).show();
	                break;
	            case GpsStatus.GPS_EVENT_FIRST_FIX:
					instance.setIs_sat_fix(true);
	                break;
	            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
	            	if( gpsStatus != null ) {

						if(mLastLocationMillis != -1)
							instance.setIs_sat_fix((SystemClock.elapsedRealtime() - mLastLocationMillis) < Constants.FIVE_SECONDS);

						Iterator<GpsSatellite> satellites = gpsStatus.getSatellites().iterator();
						List<SatelliteSignalStrength> sats = new ArrayList<>();

						while (satellites.hasNext()) {
							GpsSatellite sat = satellites.next();
							sats.add(new SatelliteSignalStrength(sat.getPrn(), sat.getSnr()));
						}

						instance.setSat_ss_array(sats);
					}
	        }
		}
	};

	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			instance.setBattery_health(intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1));
			instance.setBattery_plugged(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1));
			instance.setBattery_status((intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)));
			instance.setBattery_percentage(((double)intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) / ((double)intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1))) * 100);
			instance.setBattery_temp((double)intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10.d);
			instance.setBattery_voltage(intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1));
		}
	};

	private SensorEventListener sensorListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {

			switch (event.sensor.getType()) {
				case Sensor.TYPE_AMBIENT_TEMPERATURE:
					instance.setAmbient_temp(event.values[0]);
					instance.setAc_ambient_temp(event.accuracy);
					break;

				case Sensor.TYPE_LIGHT:
					instance.setLight(event.values[0]);
					instance.setAc_light(event.accuracy);
					break;

				case Sensor.TYPE_PRESSURE:
					instance.setPressure(event.values[0]);
					instance.setAc_pressure(event.accuracy);
					break;

				case Sensor.TYPE_RELATIVE_HUMIDITY:
					instance.setRelative_humidity(event.values[0]);
					instance.setAc_relative_humidity(event.accuracy);
					break;
				case Sensor.TYPE_PROXIMITY:
					instance.setProximity(event.values[0]);
					instance.setAc_proximity(event.accuracy);
					break;
				case Sensor.TYPE_ACCELEROMETER:
					instance.setAccelerometer(event.values[0], event.values[1], event.values[2]);
					instance.setAc_Accelerometer(event.accuracy);
					break;

			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			switch (sensor.getType()) {
				case Sensor.TYPE_AMBIENT_TEMPERATURE:
					instance.setAc_ambient_temp(accuracy);
					break;

				case Sensor.TYPE_LIGHT:
					instance.setAc_light(accuracy);
					break;

				case Sensor.TYPE_PRESSURE:
					instance.setAc_pressure(accuracy);
					break;

				case Sensor.TYPE_RELATIVE_HUMIDITY:
					instance.setAc_relative_humidity(accuracy);
					break;
				case Sensor.TYPE_PROXIMITY:
					instance.setAc_proximity(accuracy);
					break;
				case Sensor.TYPE_ACCELEROMETER:
					instance.setAc_Accelerometer(accuracy);
					break;

			}
		}
	};

    public Inst getInstance()
    {
    	return instance;
    }

	public void startCollecting()
	{
		if(collecting == state_not_collecting) {
			mTelephonyManager.listen(mListener,  PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_SIGNAL_STRENGTHS |PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);

			mLocationManager.addGpsStatusListener(gpsListener);

			registerReceiver(batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

			if (availableSensors.contains(Sensor.TYPE_AMBIENT_TEMPERATURE)) {
				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_NORMAL);
				sensorRegistered = true;
			}
			if (availableSensors.contains(Sensor.TYPE_PRESSURE)) {
				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_NORMAL);
				sensorRegistered = true;
			}
			if (availableSensors.contains(Sensor.TYPE_RELATIVE_HUMIDITY)) {
				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY), SensorManager.SENSOR_DELAY_NORMAL);
				sensorRegistered = true;
			}
			if (availableSensors.contains(Sensor.TYPE_LIGHT)) {
				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
				sensorRegistered = true;
			}
			if (availableSensors.contains(Sensor.TYPE_ACCELEROMETER)) {
				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
				sensorRegistered = true;
			}
			if (availableSensors.contains(Sensor.TYPE_PROXIMITY)) {
				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
				sensorRegistered = true;
			}


			mWifiManager.startScan();

			handler.post(cpu_monitor);
			//handler.post(get_cell_info);

			Toast.makeText(getApplicationContext(), getString(R.string.toasts_collection_will_start_in), Toast.LENGTH_SHORT).show();

			collecting = state_preparing_to_collecting;
			handler.postDelayed(set_colleting_true, Constants.TEN_SECONDS);
		}
	}

	public void stopCollecting()
	{
		if(collecting == state_not_collecting)
			return;
		else if(collecting == state_preparing_to_collecting) {
			handler.removeCallbacks(set_colleting_true);
			Toast.makeText(getApplicationContext(), getString(R.string.toasts_collection_will_no_start_anymore), Toast.LENGTH_SHORT).show();
		}
		else if(collecting == state_collecting) {
			Toast.makeText(getApplicationContext(), getString(R.string.toasts_collection_stopped), Toast.LENGTH_SHORT).show();
		}

		mLocationManager.removeGpsStatusListener(gpsListener);

		mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);

		unregisterReceiver(batteryInfoReceiver);
		unregisterReceiver(wifiReceiver);

		if (sensorRegistered) {
			mSensorManager.unregisterListener(sensorListener);
			sensorRegistered = false;
		}

		handler.removeCallbacks(cpu_monitor);
		//handler.removeCallbacks(get_cell_info);

		collecting = state_not_collecting;
	}

	public int getState()
	{
		return collecting;
	}
}
