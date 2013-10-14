package edu.cmu.west.mysandbox;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
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




public class MainActivity extends Activity implements SensorEventListener {
	
	public TextView pressureTV, humidityTV, accelerometerTV, temperatureTV,
					lightTV, longitudeTV, latitudeTV, altitudeTV, bearingTV, accuracyTV, callcountTV,
					batteryTV;
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
	Boolean gps_is_enabled;
	LocationManager locman;
	Integer batterylevel = -1;




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

        callcount = BigInteger.valueOf(0);

    }
 
    public void updateAllGUIFields() {
    	
    	callcountTV.setText("Updates received: " + callcount.toString());
    	
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
		
		updateAllGUIFields();
		
	}
	
	public void getBatteryLevel() {
		
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		float batteryPct = level / (float)scale;
		
		batterylevel = Integer.valueOf((int) (100*batteryPct));
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

}