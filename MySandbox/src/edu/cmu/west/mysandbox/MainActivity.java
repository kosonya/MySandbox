package edu.cmu.west.mysandbox;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import com.google.gson.Gson;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView hellotext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hellotext = (TextView)findViewById(R.id.hellotext);
        Gson gson = new Gson();
        ArrayList<Integer> ints = new ArrayList<Integer>();
        ints.add(10); ints.add(100); ints.add(42);
        
        hellotext.setText(gson.toJson(ints));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
