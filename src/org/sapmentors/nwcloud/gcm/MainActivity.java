package org.sapmentors.nwcloud.gcm;


import java.util.ArrayList;
import java.util.List;

import org.sapmentors.nwcloud.gcm.backend.AndroidDevice;
import org.sapmentors.nwcloud.gcm.backend.NWCloudBackend;
import org.sapmentors.nwcloud.gcm.backend.PushMessageExternal;
import org.sapmentors.nwcloud.gcm.model.PushMessageResponse;
import org.sapmentors.nwcloud.gcm.util.AndroidUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Main activity for the android app
 * 
 * On startup it registers the device for receiving GCM push messages
 * It also fetches all the devices from the NW cloud backend and provides
 * and interface for sending the users a message.
 * 
 * The message sent is sent to NW Cloud through a REST interface, which again
 * uses the GCM server library to send it via Google to the android device.
 * @author dagfinn.parnas
 *
 */
public class MainActivity extends Activity {
	protected static final String LOG_PREFIX = "NWCLOUD-GCM";

	Spinner spinnerEmailTo; 
	Button buttonSend;
	EditText editMessage; 
	ArrayAdapter<String> spinnerAdapter;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Register this device to Google Cloud Messaging for our API
        //Will also subsequently trigger storage of GCM registration key
        //for this devices in the NWCloud backend
        GCMIntentService.register(this);
        
        setContentView(R.layout.activity_main);
        
        //components in the view
        spinnerEmailTo = (Spinner)findViewById(R.id.spinner_emailto);
        buttonSend = (Button)findViewById(R.id.button_send);
        editMessage=(EditText)findViewById(R.id.edit_message);
        
        List<String> list = new ArrayList<String>();
    	list.add("Please wait... fetching recipients");
        
    	spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,list);
    	spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmailTo.setAdapter(spinnerAdapter);
        
        //disable the send button untill the recipient devices have been fetched
        buttonSend.setEnabled(false);
        
        NWCloudGetDevicesTask taskGetDevices = new NWCloudGetDevicesTask();
        taskGetDevices.execute();
    }
    
    /**
     * Populate the spinner with emails from registered
     * android devices (from NWCloud backend)
     * 
     * @param devices
     */
    private void setupSpinner(AndroidDevice[] devices){
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
    	informUserAboutMessage("Attempting to send message");
    	
    	PushMessageExternal pushMessageExternal = new PushMessageExternal();
    	
    	pushMessageExternal.setMessage(editMessage.getText().toString());
    	pushMessageExternal.setEmailFrom(AndroidUtils.getPrimaryAccountEmail(this));
    	
    	ArrayList<String> arEmailTo = new ArrayList<String>(10);
    	String strSelectedEmailTo = (String)spinnerEmailTo.getSelectedItem();
    	arEmailTo.add(strSelectedEmailTo);
    	
    	pushMessageExternal.setEmailTo(arEmailTo.toArray(new String[]{}));	
    	
    	NWCloudSendMessageTask taskSendMessage = new NWCloudSendMessageTask();
    	taskSendMessage.execute(pushMessageExternal);
    }
    
    
	public void informUserAboutMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		
	}
	
	public class NWCloudGetDevicesTask extends AsyncTask<Object, Object, AndroidDevice[]> {
		
		public NWCloudGetDevicesTask (){
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
	
	public class NWCloudSendMessageTask extends AsyncTask<PushMessageExternal, Object, PushMessageResponse> {
		
		public NWCloudSendMessageTask (){
		}
		
		@Override
		protected PushMessageResponse doInBackground(PushMessageExternal... arguments){
			PushMessageExternal pushMessage= arguments[0];
			Log.d(LOG_PREFIX, "About to send message to NWCloud backend. Msg;" + pushMessage.getMessage());
			PushMessageResponse pushmessageResponse = NWCloudBackend.sendMessage(pushMessage);
			return pushmessageResponse;
		}

		@Override
		protected void onPostExecute(PushMessageResponse pushmessageResponse) {
			informUserAboutMessage("Push message sent. Response code is " +pushmessageResponse.getResponseCode() +" ("+pushmessageResponse.getResponseMessage()+")");
		}
	}
}
