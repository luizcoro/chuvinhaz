//package com.example.luiz.chuvinhaz.services;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.location.GpsSatellite;
//import android.location.GpsStatus;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.net.wifi.ScanResult;
//import android.net.wifi.WifiManager;
//import android.os.MyBatteryManager;
//import android.os.Binder;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.support.v4.app.NotificationCompat;
//import android.telephony.CellLocation;
//import android.telephony.NeighboringCellInfo;
//import android.telephony.PhoneStateListener;
//import android.telephony.SignalStrength;
//import android.telephony.TelephonyManager;
//import android.telephony.cdma.CdmaCellLocation;
//import android.telephony.gsm.GsmCellLocation;
//import android.util.DisplayMetrics;
//import android.view.Display;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//import com.example.luiz.chuvinhaz.MainActivity;
//import com.example.luiz.chuvinhaz.R;
//import com.example.luiz.chuvinhaz.models.Inst;
//import com.example.luiz.chuvinhaz.models.OperatorSignalStrength;
//import com.example.luiz.chuvinhaz.models.SatelliteSignalStrength;
//import com.example.luiz.chuvinhaz.models.WifiSignalStrength;
//import com.example.luiz.chuvinhaz.utils.Constants;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
//public class CollectorService2 extends Service {
//	public static final String TAG = CollectorService2.class.getSimpleName();
//
//	public static final int state_not_collecting = 0;
//	public static final int state_preparing_to_collecting = 1;
//	public static final int state_collecting = 2;
//
//
//	private Inst instance;
//
//
//	private int mNotificationId = 1;
//
//	private TelephonyManager mTelephonyManager;
//	private LocationManager mLocationManager;
//	private SensorManager mSensorManager;
//	private WifiManager mWifiManager;
//
//	private boolean sensorRegistered = false;
//
//	private Set<Integer> availableSensors;
//
//	private NotificationManager mNotifyMgr;
//
//	private int collecting = state_not_collecting;
//
//	private PendingIntent googlePendingItent;
//
//	Handler handler;
//	Runnable set_colleting_true;
//
//
//	//for cpu monitoring
//	Runnable cpu_monitor;
//	long prev_running = 0, prev_total = 0;
//
//
//
//	public class LocalBinder extends Binder {
//		public CollectorService2 getService() {
//			return CollectorService2.this;
//		}
//	}
//
//	@Override
//	public IBinder onBind(Intent intent) {
//
//		Intent resultIntent = new Intent(this, MainActivity.class);
//    	PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
//
//		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher_branco)
//    		    .setContentTitle("Collector is running")
//    		    .setOnlyAlertOnce(true)
//    		    .setPriority(Notification.PRIORITY_LOW)
//				.setContentIntent(resultPendingIntent);
//
//    	startForeground(mNotificationId, mBuilder.build());
//
//		instance = new Inst();
//
//		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		instance.setPhone_type(mTelephonyManager.getPhoneType());
//		instance.setDevice_name(getDeviceName());
//		instance.setDevice_screen_size(getScreen_size());
//		instance.setSoc_n_cores(readN_cores());
//		instance.setBattery_capacity(getBatteryCapacity());
//
//		String networkOperator = mTelephonyManager.getNetworkOperator();
//
//		if (networkOperator != null) {
//			instance.setMcc(Integer.parseInt(networkOperator.substring(0, 3)));
//			instance.setMnc(Integer.parseInt(networkOperator.substring(3)));
//		}
//
//		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//		mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//
//		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
//
//		availableSensors = new HashSet<>();
//		for(Sensor s : sensors)
//		{
//			availableSensors.add(s.getType());
//		}
//
//		mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, mLocListener);
//		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocListener);
//
//		Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//		if(lastKnownLocation != null)
//		{
//			instance.setGps_location(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), lastKnownLocation.getAccuracy());
//		}
//		else
//		{
//			lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//			if(lastKnownLocation != null)
//			{
//				instance.setNetwork_location(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), lastKnownLocation.getAccuracy());
//			}
//
//		}
//
//		handler = new Handler();
//		set_colleting_true = new Runnable() {
//			@Override
//			public void run() {
//				collecting = state_collecting;
//				Toast.makeText(getApplicationContext(), "Collection started", Toast.LENGTH_SHORT).show();
//			}
//		};
//
//		cpu_monitor = new Runnable() {
//			@Override
//			public void run() {
//				long[] cpu_usage = readCpuUsage();
//				double[] load_avg = readLoadAvg();
//
//				if(load_avg != null) {
//					instance.setSoc_cpu_avg_1m(load_avg[0]);
//					instance.setSoc_cpu_avg_5m(load_avg[1]);
//					instance.setSoc_cpu_avg_15m(load_avg[2]);
//					instance.setSoc_active_tasks((int) load_avg[3]);
//					instance.setSoc_total_tasks((int) load_avg[4]);
//				}
//
//				if(cpu_usage != null) {
//					long running = cpu_usage[0] + cpu_usage[1] + cpu_usage[2];
//					long total = running + cpu_usage[3];
//
//					instance.setSoc_cpu_usage_percentage(getCpuUsage(running, total, prev_running, prev_total));
//
//					prev_running = running;
//					prev_total = total;
//				}
//
//				handler.postDelayed(this, Constants.FIVE_SECONDS); //5 seconds
//			}
//		};
//
//        return new LocalBinder();
//	}
//
//	@Override
//	public void onDestroy() {
//
//		super.onDestroy();
//
//		mLocationManager.removeUpdates(mLocListener);
//		stopCollecting();
//
//		stopForeground(true);
//	}
//
//    private final LocationListener mLocListener = new LocationListener()
//    {
//
//
//		@Override
//    	public void onLocationChanged(Location loc)
//    	{
//			instance.setAltitude(loc.getAltitude());
//			instance.setSpeed(loc.getSpeed());
//
//			if(LocationManager.GPS_PROVIDER.equals(loc.getProvider()))
//			{
//				instance.setGps_location(loc.getLatitude(), loc.getLongitude(), loc.getAccuracy());
//			}
//			else if(LocationManager.NETWORK_PROVIDER.equals(loc.getProvider()))
//			{
//				instance.setNetwork_location(loc.getLatitude(), loc.getLongitude(), loc.getAccuracy());
//			}
//    	}
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        	//Toast.makeText(getApplicationContext(), "Location status changed: " + provider + ", " + status , Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//        	//Toast.makeText(getApplicationContext(), "Location provider enabled: " + provider, Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//        	//Toast.makeText(getApplicationContext(), "Location provider disabled: " + provider, Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    // Listener for signal strength.
//    private final PhoneStateListener mListener = new PhoneStateListener()
//    {
//
//		@Override
//		public void onDataConnectionStateChanged(int state, int networkType) {
//			super.onDataConnectionStateChanged(state, networkType);
//			instance.setNetwork_type(networkType);
//		}
//
//		@Override
//    	public void onCellLocationChanged(CellLocation mCellLocation)
//    	{
//			super.onCellLocationChanged(mCellLocation);
//
//			if(mCellLocation instanceof GsmCellLocation) {
//				GsmCellLocation gsmCellLocation= (GsmCellLocation) mCellLocation;
//				instance.setCid(gsmCellLocation.getCid());
//				instance.setLac(gsmCellLocation.getLac());
//			} else if(mCellLocation instanceof CdmaCellLocation) {
//				CdmaCellLocation cdmaCellLocation= (CdmaCellLocation) mCellLocation;
//				instance.setCid(cdmaCellLocation.getBaseStationId());
//				instance.setLac(cdmaCellLocation.getSystemId());
//			}
//
//    	}
//
//    	@Override
//    	public void onSignalStrengthsChanged(SignalStrength strength) {
//			super.onSignalStrengthsChanged(strength);
//
//			if (strength.isGsm()) {
//				instance.setGsm_str(strength.getGsmSignalStrength() < 32 ? -113 + (2 * strength.getGsmSignalStrength()) : -9999);
//
//			} else {
//				instance.setGsm_str(strength.getCdmaDbm());
//			}
//
//			List<NeighboringCellInfo> results = mTelephonyManager.getNeighboringCellInfo();
//			List<OperatorSignalStrength> neighbors = new ArrayList<>();
//			for(NeighboringCellInfo result: results)
//			{
//				neighbors.add(new OperatorSignalStrength(result.getPsc(), result.getLac(), result.getRssi()));
//				//neighbors.add(new OperatorSignalStrength(result.getPsc(), result.getLac(), result.getRssi() < 32 ? -113 + (2 *  result.getRssi()) : -9999));
//			}
//			instance.setGsm_ss_srray(neighbors);
//		}
//    };
//
//	private final  BroadcastReceiver wifiReceiver  = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//
//			List<ScanResult> results = mWifiManager.getScanResults();
//			List<WifiSignalStrength> wifis = new ArrayList<>();
//
//			for(ScanResult result : results)
//			{
//					wifis.add(new WifiSignalStrength(result.frequency, result.level));
//			}
//
//			instance.setWifi_ss_array(wifis);
//
//			mWifiManager.startScan();
//		}
//	};
//
//    private final GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
//
//		GpsStatus gpsStatus = null;
//
//
//		@Override
//		public void onGpsStatusChanged(int event) {
//			gpsStatus = mLocationManager.getGpsStatus(gpsStatus);
//
//	        switch (event) {
//	            case GpsStatus.GPS_EVENT_STARTED:
//	            	//Toast.makeText(getApplicationContext(), "gps started" , Toast.LENGTH_SHORT).show();
//	                break;
//	            case GpsStatus.GPS_EVENT_STOPPED:
//	            	//Toast.makeText(getApplicationContext(), "gps stopped", Toast.LENGTH_SHORT).show();
//	                break;
//	            case GpsStatus.GPS_EVENT_FIRST_FIX:
//	            	Toast.makeText(getApplicationContext(), "gps first fix event", Toast.LENGTH_SHORT).show();
//	                break;
//	            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
//	            	if( gpsStatus != null ) {
//						Iterator<GpsSatellite> satellites = gpsStatus.getSatellites().iterator();
//						List<SatelliteSignalStrength> sats = new ArrayList<>();
//
//						while (satellites.hasNext()) {
//							GpsSatellite sat = satellites.next();
//							sats.add(new SatelliteSignalStrength(sat.getPrn(), sat.getSnr() < 32 ? -113 + (2 *sat.getSnr()) : -9999));
//
//							//sats.add(new SatelliteSignalStrength(sat.getPrn(), sat.getSnr()));
//						}
//
//						instance.setSat_ss_array(sats);
//					}
//	        }
//		}
//	};
//
//
//
//	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			instance.setBattery_health(intent.getIntExtra(MyBatteryManager.EXTRA_HEALTH, -1));
//			instance.setBattery_plugged(intent.getIntExtra(MyBatteryManager.EXTRA_PLUGGED, -1));
//			instance.setBattery_status((intent.getIntExtra(MyBatteryManager.EXTRA_STATUS, -1)));
//			instance.setBattery_percentage(((double) intent.getIntExtra(MyBatteryManager.EXTRA_LEVEL, -1) / (double) intent.getIntExtra(MyBatteryManager.EXTRA_SCALE, -1)) * 100);
//			instance.setBattery_temp(intent.getIntExtra(MyBatteryManager.EXTRA_TEMPERATURE, 0) / 10.d);
//			instance.setBattery_voltage((double)intent.getIntExtra(MyBatteryManager.EXTRA_VOLTAGE, -1));
//		}
//	};
//
//	private SensorEventListener sensorListener = new SensorEventListener() {
//		@Override
//		public void onSensorChanged(SensorEvent event) {
//
//			switch (event.sensor.getType()) {
//				case Sensor.TYPE_AMBIENT_TEMPERATURE:
//					instance.setAmbient_temp(event.values[0]);
//					instance.setAc_ambient_temp(event.accuracy);
//					break;
//
//				case Sensor.TYPE_LIGHT:
//					instance.setLight(event.values[0]);
//					instance.setAc_light(event.accuracy);
//					break;
//
//				case Sensor.TYPE_PRESSURE:
//					instance.setPressure(event.values[0]);
//					instance.setAc_pressure(event.accuracy);
//					break;
//
//				case Sensor.TYPE_RELATIVE_HUMIDITY:
//					instance.setRelative_humidity(event.values[0]);
//					instance.setAc_relative_humidity(event.accuracy);
//					break;
//				case Sensor.TYPE_PROXIMITY:
//					instance.setProximity(event.values[0]);
//					instance.setAc_proximity(event.accuracy);
//					break;
//				case Sensor.TYPE_ACCELEROMETER:
//					instance.setAccelerometer(event.values[0], event.values[1], event.values[2]);
//					instance.setAc_Accelerometer(event.accuracy);
//					break;
//
//			}
//		}
//
//		@Override
//		public void onAccuracyChanged(Sensor sensor, int accuracy) {
//			switch (sensor.getType()) {
//				case Sensor.TYPE_AMBIENT_TEMPERATURE:
//					instance.setAc_ambient_temp(accuracy);
//					break;
//
//				case Sensor.TYPE_LIGHT:
//					instance.setAc_light(accuracy);
//					break;
//
//				case Sensor.TYPE_PRESSURE:
//					instance.setAc_pressure(accuracy);
//					break;
//
//				case Sensor.TYPE_RELATIVE_HUMIDITY:
//					instance.setAc_relative_humidity(accuracy);
//					break;
//				case Sensor.TYPE_PROXIMITY:
//					instance.setAc_proximity(accuracy);
//					break;
//				case Sensor.TYPE_ACCELEROMETER:
//					instance.setAc_Accelerometer(accuracy);
//					break;
//
//			}
//		}
//	};
//
//	private String getDeviceName() {
//		String manufacturer = Build.MANUFACTURER;
//		String model = Build.MODEL;
//		if (model.startsWith(manufacturer)) {
//			return capitalize(model);
//		} else {
//			return capitalize(manufacturer) + " " + model;
//		}
//	}
//
//
//	private String capitalize(String s) {
//		if (s == null || s.length() == 0) {
//			return "";
//		}
//		char first = s.charAt(0);
//		if (Character.isUpperCase(first)) {
//			return s;
//		} else {
//			return Character.toUpperCase(first) + s.substring(1);
//		}
//	}
//
//	private double getScreen_size() {
//
//		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//		Display display = wm.getDefaultDisplay();
//
//		int width;
//		int height;
//		int density;
//
//		if (Build.VERSION.SDK_INT >= 17){
//			//new pleasant way to get real metrics
//			DisplayMetrics realMetrics = new DisplayMetrics();
//			display.getRealMetrics(realMetrics);
//			width = realMetrics.widthPixels;
//			height = realMetrics.heightPixels;
//			density = realMetrics.densityDpi;
//
//		} else {
//			DisplayMetrics metrics = new DisplayMetrics();
//			display.getMetrics(metrics);
//			width = metrics.widthPixels;
//			height = metrics.heightPixels;
//			density = metrics.densityDpi;
//		}
//
//		DisplayMetrics dm = new DisplayMetrics();
//
//
//		display.getMetrics(dm);
//
//
//		double wi=(double)width/(double)density;
//		double hi=(double)height/(double)density;
//		double x = Math.pow(wi,2);
//		double y = Math.pow(hi, 2);
//
//		return Math.sqrt(x+y);
//	}
//
//	private long[] readCpuUsage() {
//
//
//		try {
//			BufferedReader br = new BufferedReader(new FileReader("/proc/stat"));
//			String load = br.readLine();
//			String[] toks = load.split(" +");  // Split on one or more spaces
//			br.close();
//
//			long[] result = new long[4];
//			result[0] = Long.parseLong(toks[1]);
//			result[1] = Long.parseLong(toks[2]);
//			result[2] = Long.parseLong(toks[3]);
//			result[3] = Long.parseLong(toks[4]);
//
//			return result;
//
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//
//		return null;
//	}
//
//	private int readN_cores() {
//		try {
//			BufferedReader br = new BufferedReader(new FileReader("/proc/stat"));
//			br.readLine();
//
//			int count = 0;
//			while(br.readLine().startsWith("cpu"))
//				count++;
//
//			return count;
//
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//
//		return -1;
//	}
//
//
//	private double[] readLoadAvg() {
//
//		try {
//			//Construct BufferedReader from InputStreamReader
//			BufferedReader br = new BufferedReader(new FileReader("/proc/loadavg"));
//
//			String load = br.readLine();
//			br.close();
//
//			String[] toks = load.split(" +");  // Split on one or more spaces
//
//			double[] result = new double[5];
//			result[0] = Double.parseDouble(toks[0]);
//			result[1] = Double.parseDouble(toks[1]);
//			result[2] = Double.parseDouble(toks[2]);
//
//			String[] now = toks[3].split("/");
//
//			result[3] = Double.parseDouble(now[0]);
//			result[4] = Double.parseDouble(now[1]);
//
//			return result;
//
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//
//		return null;
//	}
//
//	private double getCpuUsage(long running, long total, long prev_running, long prev_total)
//	{
//		return ((running - prev_running)/((double)(total - prev_total))) * 100;
//	}
//
//	public double getBatteryCapacity() {
//		Object mPowerProfile_ = null;
//
//
//		final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
//
//		try {
//			mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
//					.getConstructor(Context.class).newInstance(this);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			double batteryCapacity = (Double) Class
//					.forName(POWER_PROFILE_CLASS)
//					.getMethod("getAveragePower", String.class)
//					.invoke(mPowerProfile_, "battery.capacity");
//
//			return batteryCapacity;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return -1;
//	}
//
//    public Inst getInstance()
//    {
//    	return instance;
//    }
//
//	public void startCollecting()
//	{
//		if(collecting == state_not_collecting) {
//			mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_CELL_LOCATION | PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
//			mLocationManager.addGpsStatusListener(gpsListener);
//
//			registerReceiver(batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//			registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//
//			if (availableSensors.contains(Sensor.TYPE_AMBIENT_TEMPERATURE)) {
//				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE), SensorManager.SENSOR_DELAY_NORMAL);
//				sensorRegistered = true;
//			}
//			if (availableSensors.contains(Sensor.TYPE_PRESSURE)) {
//				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_NORMAL);
//				sensorRegistered = true;
//			}
//			if (availableSensors.contains(Sensor.TYPE_RELATIVE_HUMIDITY)) {
//				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY), SensorManager.SENSOR_DELAY_NORMAL);
//				sensorRegistered = true;
//			}
//			if (availableSensors.contains(Sensor.TYPE_LIGHT)) {
//				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
//				sensorRegistered = true;
//			}
//			if (availableSensors.contains(Sensor.TYPE_ACCELEROMETER)) {
//				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
//				sensorRegistered = true;
//			}
//			if (availableSensors.contains(Sensor.TYPE_PROXIMITY)) {
//				mSensorManager.registerListener(sensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
//				sensorRegistered = true;
//			}
//
//
//			mWifiManager.startScan();
//
//			handler.postDelayed(cpu_monitor, Constants.FIVE_SECONDS);
//
//			Toast.makeText(getApplicationContext(), "Collection will be started in 15 seconds", Toast.LENGTH_SHORT).show();
//
//			collecting = state_preparing_to_collecting;
//			handler.postDelayed(set_colleting_true, Constants.FIFTH_SECONDS);
//		}
//	}
//
//	public void stopCollecting()
//	{
//		if(collecting == state_preparing_to_collecting) {
//			handler.removeCallbacks(set_colleting_true);
//			collecting = state_not_collecting;
//			Toast.makeText(getApplicationContext(), "Collection will no longer be started", Toast.LENGTH_SHORT).show();
//		}
//		else if(collecting == state_collecting) {
//			mLocationManager.removeGpsStatusListener(gpsListener);
//			mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
//			unregisterReceiver(batteryInfoReceiver);
//			unregisterReceiver(wifiReceiver);
//
//			if (sensorRegistered) {
//				mSensorManager.unregisterListener(sensorListener);
//				sensorRegistered = false;
//			}
//
//			Toast.makeText(getApplicationContext(), "Collection stopped", Toast.LENGTH_SHORT).show();
//			collecting = state_not_collecting;
//		}
//	}
//
//	public int getState()
//	{
//		return collecting;
//	}
//}
