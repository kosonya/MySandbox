package edu.cmu.west.mysandbox;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.Menu;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.widget.TextView;
import edu.mit.media.funf.FunfManager;
import edu.mit.media.funf.json.IJsonObject;
import edu.mit.media.funf.pipeline.BasicPipeline;
import edu.mit.media.funf.probe.Probe.DataListener;
import edu.mit.media.funf.probe.builtin.BatteryProbe;


public class MainActivity extends Activity implements DataListener {
	
	public TextView hellotext;
	public static final String PIPELINE_NAME = "default";
	public FunfManager funfManager;
	public BasicPipeline pipeline;
	public BatteryProbe batteryProbe;

	public ServiceConnection funfManagerConn = new ServiceConnection() {    
	    @Override
	    public void onServiceConnected(ComponentName name, IBinder service) {
            funfManager = ((FunfManager.LocalBinder)service).getManager();
	        
	        Gson gson = funfManager.getGson();
	        batteryProbe = gson.fromJson(new JsonObject(), BatteryProbe.class);
	        pipeline = (BasicPipeline) funfManager.getRegisteredPipeline(PIPELINE_NAME);
	       
	        funfManager.enablePipeline(PIPELINE_NAME);
	        pipeline = (BasicPipeline) funfManager.getRegisteredPipeline(PIPELINE_NAME); 
     	}

		public void onServiceDisconnected(ComponentName name) {
			funfManager = null;
		}
		
	};
	    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hellotext = (TextView)findViewById(R.id.hellotext);
        
        bindService(new Intent(this, FunfManager.class), funfManagerConn, BIND_AUTO_CREATE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	@Override
	public void onDataCompleted(IJsonObject arg0, JsonElement arg1) {
		batteryProbe.registerPassiveListener(this);
		
	}
	@Override
	public void onDataReceived(IJsonObject arg0, IJsonObject arg1) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		hellotext.setText(gson.toJson(arg0) + gson.toJson(arg1));
		
	}
}