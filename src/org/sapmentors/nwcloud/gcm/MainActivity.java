package org.sapmentors.nwcloud.gcm;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.sapmentors.nwcloud.gcm.backend.AndroidDevice;
import org.sapmentors.nwcloud.gcm.backend.NWCloudBackend;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	protected static final String LOG_PREFIX = "NWCLOUD-GCM";

	Spinner spinnerEmailTo; 
	Button buttonSend;
	ArrayAdapter<String> spinnerAdapter;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        GCMIntentService.register(this);
        setContentView(R.layout.activity_main);
        
        spinnerEmailTo = (Spinner)findViewById(R.id.spinner_emailto);
        buttonSend = (Button)findViewById(R.id.button_send);
        
        List<String> list = new ArrayList<String>();
    	list.add("Please wait... fetching");
        
    	spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
        
    	spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinnerAdapter.add("Please wait... fetching");
        spinnerEmailTo.setAdapter(spinnerAdapter);
        
        buttonSend.setEnabled(false);
        
       // NWCloudBackendTask taskGetDevices = new NWCloudBackendTask();
        //taskGetDevices.execute();
    
        
        setContentView(R.layout.activity_main);
    }
    
    public void setupSpinner(AndroidDevice[] devices){
    	spinnerAdapter.clear();
    	if(devices!=null){
        	spinnerAdapter.clear();
        	for (int i = 0; i < devices.length; i++) {
				AndroidDevice device =devices[i];
				spinnerAdapter.add(device.getEmail());		
			}
        	buttonSend.setEnabled(true);
        }else {
        	spinnerAdapter.add("No devices registered");
        	informUserAboutMessage("No devices registered in backend yet. Try again later");
        	buttonSend.setEnabled(false);
        }
    	spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void actionSendMessage(View view){
    	informUserAboutMessage("Send message");
    }
    
    
	public void informUserAboutMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		
	}
	
	public class NWCloudBackendTask extends AsyncTask<Object, Object, AndroidDevice[]> {
		
		public NWCloudBackendTask (){

		}
		
		@Override
		protected AndroidDevice[] doInBackground(Object... arg0){
			Log.d(LOG_PREFIX, "Before getting android devices from backend");
			AndroidDevice[] devices = NWCloudBackend.getRegisteredDevices();
			return devices;
		}

		@Override
		protected void onPostExecute(AndroidDevice[] result) {
			super.onPostExecute(result);
			setupSpinner(result);
		}
		
		
		
	}
}
