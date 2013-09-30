package edu.cmu.west.mysandbox;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import java.lang.Float;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import java.util.List;
import android.os.AsyncTask;




public class MainActivity extends Activity {
	
	public TextView hellotext;
	Intent batteryStatus;
	Context context;
	IntentFilter ifilter;
	SensorManager sensormanager;
	List<Sensor> sensorlist;
	
	protected class SensorReader extends AsyncTask<Object, Object, String> {
		protected String doInBackground(Object... args) {
			String result = "";
	        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
	        float batteryPct = level / (float)scale;
	        result = "Battery level: " + Float.toString(batteryPct);
	        for(Sensor sensor: sensorlist) {
	        	result += "\n" + sensor.toString();
	        }
			return result;
		}
		
		protected void onPostExecute(String result) {
			hellotext.setText(result);
		}
	}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context = getBaseContext();
        hellotext = (TextView)findViewById(R.id.hellotext);
        sensormanager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensorlist = sensormanager.getSensorList(Sensor.TYPE_ALL);
        batteryStatus = context.registerReceiver(null, ifilter);
        new SensorReader().execute();

        
    }

}