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
	Sensor humidity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context = getBaseContext();
        hellotext = (TextView)findViewById(R.id.hellotext);
        sensormanager = (SensorManager)getSystemService(SENSOR_SERVICE);
        humidity = sensormanager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensormanager.registerListener(this, humidity, SensorManager.SENSOR_DELAY_NORMAL);
    }
 
    public void onSensorChanged(SensorEvent event) {
    	String res = "";
    	for(float val : event.values) {
    		res += Float.toString(val) + "\n";
    	}
    	hellotext.setText(res);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}