package edu.cmu.west.mysandbox;

import android.os.BatteryManager;
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
import java.util.List;
import android.os.AsyncTask;




public class MainActivity extends Activity implements SensorEventListener {
	
	public TextView hellotext;
	Intent batteryStatus;
	Context context;
	IntentFilter ifilter;
	SensorManager sensormanager;
	List<Sensor> sensorlist;
	Sensor humidity, pressure;
	int callcount;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context = getBaseContext();
        hellotext = (TextView)findViewById(R.id.hellotext);
        sensormanager = (SensorManager)getSystemService(SENSOR_SERVICE);
        pressure = sensormanager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        humidity = sensormanager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        sensormanager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL);
     //   sensormanager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL);
        callcount = 0;

    }
 
    public void onSensorChanged(SensorEvent event) {
    	callcount += 1;
    	String res = "callcount: " + Integer.toString(callcount);
  /*  	if (res.length() > 1000)
    	{
    		res = "Oops!";
    	}
    	
   */
    	res += "sensor name: " + event.sensor.getName();
    	for(float val : event.values) {
    		res +=  Float.toString(val);
    	}
    	hellotext.setText(res);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}