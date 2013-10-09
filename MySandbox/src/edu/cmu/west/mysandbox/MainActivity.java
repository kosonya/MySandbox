package edu.cmu.west.mysandbox;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import java.lang.Float;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;

import java.util.ArrayList;
import java.util.List;




public class MainActivity extends Activity implements SensorEventListener {
	
	public TextView pressureTV, humidityTV, accelerometerTV, temperatureTV, lightTV;
	Intent batteryStatus;
	Context context;
	IntentFilter ifilter;
	SensorManager sensormanager;
	List<Sensor> sensorlist;
	Sensor humidityS, pressureS, accelerometerS, temperatureS, lightS;
	int callcount;
	List<Float> pressurevals, humidityvals, accelerometervals, temperaturevals, lightvals;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context = getBaseContext();
        pressureTV = (TextView)findViewById(R.id.pressureTV);
        humidityTV = (TextView)findViewById(R.id.humidityTV);
        accelerometerTV = (TextView)findViewById(R.id.accTV);
        temperatureTV = (TextView)findViewById(R.id.temperatureTV);
        lightTV = (TextView)findViewById(R.id.lightTV);

        
        
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

        callcount = 0;

    }
 
    public void updateAllFields() {
    	if (pressurevals != null) {
	    	String text = "Pressure: ";
	    	for(Float val: pressurevals) {
	    		text += Float.toString(val) + "; ";
	    	}
	    	pressureTV.setText(text);
    	}
    	
    }
    
    public void onSensorChanged(SensorEvent event) {
    	callcount += 1;
    	if (event.sensor.getType() == Sensor.TYPE_PRESSURE) {
			pressurevals = new ArrayList<Float>();
    		for(float val: event.values) {
    			pressurevals.add(Float.valueOf(val));
    		}
    	}
    	if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
//    		humidityvals = new float[event.values.length];
//    		System.arraycopy(event.values, 0, humidityvals, 0, event.values.length);
    		String text = "Relative humidity: ";
    		for(float val: event.values) {
    			text += Float.toString(val) + "; ";
    		}
    		humidityTV.setText(text);   		
    		
    	}

    	if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
    		String text = "Acceleration: ";
    		for(float val: event.values) {
    			text += Float.toString(val) + "; ";
    		}
    		accelerometerTV.setText(text);   		
    		
    	}

    	if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
    		String text = "Temperature: ";
    		for(float val: event.values) {
    			text += Float.toString(val) + "; ";
    		}
    		temperatureTV.setText(text);   		
    		
    	}

    	if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
    		String text = "Light: ";
    		for(float val: event.values) {
    			text += Float.toString(val) + "; ";
    		}
    		lightTV.setText(text);   		
    		
    	}
		updateAllFields();
 
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}