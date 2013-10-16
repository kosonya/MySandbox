package edu.cmu.west.mysandbox;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.telephony.CellInfo;
//import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import java.lang.Float;
import java.math.BigInteger;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;

import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;




public class MainActivity extends Activity implements SensorEventListener {
	
	public TextView pressureTV, humidityTV, accelerometerTV, temperatureTV,
					lightTV, longitudeTV, latitudeTV, altitudeTV, bearingTV, accuracyTV, callcountTV,
					batteryTV, cellcountTV, wifiTV, deviceidTV, locidTV, serveruriTV, issendingTV,
					sentcountTV;
	public EditText deviceidED, locidED, serveruriED;
	public Button togglesendingB, updatesettingsB;
	Intent batteryStatus;
	IntentFilter batteryintent;

	Context context;
	SensorManager sensormanager;
	List<Sensor> sensorlist;
	Sensor humidityS, pressureS, accelerometerS, temperatureS, lightS;
	BigInteger callcount;
	List<Float> pressurevals, humidityvals, accelerometervals, temperaturevals, lightvals;
	List<Double> gpsvals;
	LocationListener gpslistener;
	Boolean gps_is_enabled, packet_is_being_sent = false, is_sending = false;
	LocationManager locman;
	Integer batterylevel = -1;
	TelephonyManager telephonymanager;
	List<CellInfo> cellinfo;
//	List<NeighboringCellInfo> cellinfo;
	WifiManager wifimanager;
	List<ScanResult> wifipoints;
	String location_id, device_id, server_uri;
	int sent_count = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        batteryintent = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context = getBaseContext();
        pressureTV = (TextView)findViewById(R.id.pressureTV);
        humidityTV = (TextView)findViewById(R.id.humidityTV);
        accelerometerTV = (TextView)findViewById(R.id.accTV);
        temperatureTV = (TextView)findViewById(R.id.temperatureTV);
        lightTV = (TextView)findViewById(R.id.lightTV);
        latitudeTV = (TextView)findViewById(R.id.latitudeTV);
        longitudeTV = (TextView)findViewById(R.id.longitudeTV);
        altitudeTV = (TextView)findViewById(R.id.altitudeTV);
        bearingTV = (TextView)findViewById(R.id.bearingTV);
        accuracyTV = (TextView)findViewById(R.id.accuracyTV);
        callcountTV = (TextView)findViewById(R.id.callcountTV);
        batteryTV = (TextView)findViewById(R.id.batteryTV);
        cellcountTV = (TextView)findViewById(R.id.cellcountTV);
        wifiTV = (TextView)findViewById(R.id.wifiTV);

        deviceidTV = (TextView)findViewById(R.id.deviceidTV);
        locidTV = (TextView)findViewById(R.id.locationidTV);
        serveruriTV = (TextView)findViewById(R.id.serveruri2TV);
        issendingTV = (TextView)findViewById(R.id.issendingTV);
        sentcountTV = (TextView)findViewById(R.id.sentcountTV);
        
        
        
        
        deviceidED = (EditText)findViewById(R.id.deviceIDeditText);
        locidED = (EditText)findViewById(R.id.locationIDeditText);
        serveruriED = (EditText)findViewById(R.id.serveruriED);

        
        
        sensormanager = (SensorManager)getSystemService(SENSOR_SERVICE);
        pressureS = sensormanager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        humidityS = sensormanager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        accelerometerS = sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        temperatureS = sensormanager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        lightS = sensormanager.getDefaultSensor(Sensor.TYPE_LIGHT);

        sensormanager.registerListener(this, humidityS, SensorManager.SENSOR_DELAY_NORMAL);
        sensormanager.registerListener(this, pressureS, SensorManager.SENSOR_DELAY_NORMAL);
        sensormanager.registerListener(this, accelerometerS, SensorManager.SENSOR_DELAY_NORMAL);
        sensormanager.registerListener(this, temperatureS, SensorManager.SENSOR_DELAY_NORMAL);
        sensormanager.registerListener(this, lightS, SensorManager.SENSOR_DELAY_NORMAL);
        gpslistener = new MyGPSListener();
        locman = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpslistener);
        batteryStatus = context.registerReceiver(null, batteryintent);
        
        telephonymanager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        wifimanager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        
        togglesendingB = (Button)findViewById(R.id.togglesendingB);
        togglesendingB.setOnClickListener(new onSendToggleClicked());
        
        updatesettingsB = (Button)findViewById(R.id.updatesettingsB);
        updatesettingsB.setOnClickListener(new onReadSettingsClicked());

        callcount = BigInteger.valueOf(0);
        
        readSettings();
        

    }
 
    public void readSettings()
    {
    	location_id = locidED.getText().toString();
    	device_id = deviceidED.getText().toString();
    	server_uri = serveruriED.getText().toString();  	
    }
    
    class onSendToggleClicked implements OnClickListener {

		@Override
		public void onClick(View v) {
			Button b = (Button)v;
			if (is_sending) {
				is_sending = false;
				b.setText("Start sending");
			}
			else {
				is_sending = true;
				b.setText("Stop sending");
			}
		}
    	
    }

    class onReadSettingsClicked implements OnClickListener {

		@Override
		public void onClick(View v) {
			readSettings();
			updateAllGUIFields();
		}
    	
    }
    
    
    
    public void updateAllGUIFields() {
    	 
    	if (locidED.hasFocus()) {
    		locidED.clearFocus();
    	}
    	
    	if (deviceidED.hasFocus()) {
    		deviceidED.clearFocus();
    	}
    	
    	if (serveruriED.hasFocus()) {
    		serveruriED.clearFocus();
    	}
    	
    	callcountTV.setText("Updates received: " + callcount.toString());
    	
    	if (device_id != null) {
    		deviceidTV.setText("Device ID: " + device_id);
    	}
    	if (location_id != null) {
    		locidTV.setText("Location ID: " + location_id);
    	}    	

    	if (server_uri != null) {
    		serveruriTV.setText("Server URI: " + server_uri);
    	}
    	
    	if(is_sending) {
    		issendingTV.setText("Sending to server is on");
    	}
    	else {
    		issendingTV.setText("Sending to server is off");
    	}
    	
    	if (batterylevel >= 0) {
    		batteryTV.setText("Battery level: " + Integer.toString(batterylevel) + "%");
    	}
    	
    	if (pressurevals != null) {
	    	String text = "Pressure: ";
	    	for(Float val: pressurevals) {
	    		text += Float.toString(val) + "; ";
	    	}
	    	pressureTV.setText(text);
    	}
    	if (humidityvals != null) {
	    	String text = "Humidity: ";
	    	for(Float val: humidityvals) {
	    		text += Float.toString(val) + "; ";
	    	}
	    	humidityTV.setText(text);
    	}

    	if (accelerometervals != null) {
	    	String text = "Accelerometer: ";
	    	for(Float val: accelerometervals) {
	    		text += Float.toString(val) + "; ";
	    	}
	    	accelerometerTV.setText(text);
    	}
 
    	if (temperaturevals != null) {
	    	String text = "Temperature: ";
	    	for(Float val: temperaturevals) {
	    		text += Float.toString(val) + "; ";
	    	}
	    	temperatureTV.setText(text);
    	}
 
    	if (lightvals != null) {
	    	String text = "Light: ";
	    	for(Float val: lightvals) {
	    		text += Float.toString(val) + "; ";
	    	}
	    	lightTV.setText(text);
    	}
    	if (gpsvals != null) {
    		latitudeTV.setText("Latitude: " + Double.toString(gpsvals.get(0)));
    		longitudeTV.setText("Longitude: " + Double.toString(gpsvals.get(1)));
    		altitudeTV.setText("Altitude: " + Double.toString(gpsvals.get(2)));
    		bearingTV.setText("Bearing: " + Double.toString(gpsvals.get(3)));
    		accuracyTV.setText("Accuracy: " + Double.toString(gpsvals.get(4)));
    	}
    	
    	if (cellinfo != null) {
    		String text = "";
    		text += "Cell count: " + cellinfo.size();
    		for (CellInfo cell: cellinfo) {
//    		for (NeighboringCellInfo cell: cellinfo) {
    			text += "\n" + cell.toString();
    		}
    		cellcountTV.setText(text);
    	}
    	
    	
    	if (wifipoints != null) {
    		String text = "Wifi points count: " + wifipoints.size();
    		for (ScanResult scanres: wifipoints) {
    			text += scanres.toString();
    		}
    		wifiTV.setText(text);
    	}
    	
    }
    
    public void onSensorChanged(SensorEvent event) {
    	callcount = callcount.add(BigInteger.valueOf(1));
    	
    	if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
			pressurevals = new ArrayList<Float>();
    		for(float val: event.values) {
    			pressurevals.add(Float.valueOf(val));
    		}
    	}
    	if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
			humidityvals = new ArrayList<Float>();
    		for(float val: event.values) {
    			humidityvals.add(Float.valueOf(val));
    		} 			
    	}

    	if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			accelerometervals = new ArrayList<Float>();
    		for(float val: event.values) {
    			accelerometervals.add(Float.valueOf(val));
    		}	
    	}

    	if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
			temperaturevals = new ArrayList<Float>();
    		for(float val: event.values) {
    			temperaturevals.add(Float.valueOf(val));
    		}
    	}

    	if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
			lightvals = new ArrayList<Float>();
    		for(float val: event.values) {
    			lightvals.add(Float.valueOf(val));
    		}
    	}
    	onSomethingChanged();
 
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	public void onSomethingChanged(){
		getBatteryLevel();
		getNeighboringCellInfo();
		getWifiListAndScan();
		updateAllGUIFields();
		
	}
	
	public void getNeighboringCellInfo() {
		cellinfo = telephonymanager.getAllCellInfo();
//		cellinfo = telephonymanager.getNeighboringCellInfo();
	}
	
	public void getBatteryLevel() {
		
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		float batteryPct = level / (float)scale;
		
		batterylevel = Integer.valueOf((int) (100*batteryPct));
	}
	
	public void getWifiListAndScan() {
		//new WifiGetter().execute();
		wifimanager.startScan();
		wifipoints = wifimanager.getScanResults();
	}
	
	public class MyGPSListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
	    	callcount = callcount.add(BigInteger.valueOf(1));
			gpsvals = new ArrayList<Double>();
			gpsvals.add(Double.valueOf(location.getLatitude()));
			gpsvals.add(Double.valueOf(location.getLongitude()));	
			gpsvals.add(Double.valueOf(location.getAltitude()));			
			gpsvals.add(Double.valueOf(location.getBearing()));
			gpsvals.add(Double.valueOf(location.getAccuracy()));
			onSomethingChanged();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	public class WifiGetter extends AsyncTask<Object, Object, Object> {

		@Override
		protected Object doInBackground(Object... params) {
			publishProgress();
			wifimanager.startScan();
			wifipoints = wifimanager.getScanResults();
			return null;
		}
		
		protected void onProgressUpdate(Object... params){
			wifiTV.setText("Wifi scanning has started");
		}
		
	}

	public class JSONSender extends AsyncTask<String, Object, Object>{

		@Override
		protected Object doInBackground(String... params) {
			String json_str = params[0];
			if (!packet_is_being_sent) {
				
			}
			return null;
		}
		
		protected void onProgressUpdate(Object... params) {
			packet_is_being_sent = true;
		}
		
		protected void onPostExecute(Object... params) {
			packet_is_being_sent = false;
		}
		
	}
	
}