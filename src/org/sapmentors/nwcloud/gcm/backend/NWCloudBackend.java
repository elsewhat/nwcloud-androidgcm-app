package org.sapmentors.nwcloud.gcm.backend;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.sapmentors.nwcloud.gcm.model.PushMessageResponse;
import org.sapmentors.nwcloud.gcm.util.AndroidUtils;

import android.content.Context;
import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

/**
 * This class handles the interface to the NWCloud backend.
 * 
 * The interface is REST based, and the code below uses the google-http-java-client
 * libraries
 * 
 * 
 * @author dagfinn.parnas
 *
 */
public class NWCloudBackend {
	protected static final String LOG_PREFIX = "NWCLOUD-GCM";
	protected static final String NWCLOUD_BACKEND_URL="https://androidgcmbackendp013234trial.nwtrial.ondemand.com/nwcloud-androidgcm-backend/";
	protected static final String LOCAL_BACKEND_URL = "http://192.168.1.6:8080/nwcloud-androidgcm-backend/";
	
	//Use this to switch which backend you want to use
	protected static final String BASE_BACKEND_URL= NWCLOUD_BACKEND_URL;
	
	
	protected static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	protected static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	
	/**
	 * Persist the device in the cloud backend
	 * 
	 * @param email
	 * @param registrationKey
	 */
	public static void persistDevice(String email, String registrationKey) {
		HttpRequestFactory requestFactory =
		        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
		            @Override
		          public void initialize(HttpRequest request) {
		            request.setParser(new JsonObjectParser(JSON_FACTORY));
		          }
		        });
		//the endpoint of the REST service
		GenericUrl url = new GenericUrl(BASE_BACKEND_URL +"api/mobiledevice/");
		
		//The data (to be converted to JSON)
		MobileDevice androidDevice = new MobileDevice(email, registrationKey, MobileDevice.ANDROID_MOBILE_PLATFORM);
		
		JsonHttpContent jsonContent = new JsonHttpContent(JSON_FACTORY, androidDevice);
	    
		HttpRequest request;
		try {
			request = requestFactory.buildPostRequest(url, jsonContent);
			HttpResponse response  = request.execute();
			String strResponse = response.parseAsString();
			Log.d(LOG_PREFIX, "Response code " + response.getStatusCode() + " " + response.getStatusMessage() + " response: "+strResponse);
		} catch (IOException e) {
			Log.e(LOG_PREFIX, "Failed to post JSON ", e);
		}
	    
	}
	
	/**
	 * Get all registered devices from the NWCloud backend
	 * 
	 * @return
	 */
	public static MobileDevice[] getRegisteredDevices() {
		HttpRequestFactory requestFactory =
		        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
		            @Override
		          public void initialize(HttpRequest request) {
		            request.setParser(new JsonObjectParser(JSON_FACTORY));
		          }
		        });
		//the endpoint of the REST service
		GenericUrl url = new GenericUrl(BASE_BACKEND_URL +"api/mobiledevice/");

		HttpRequest request;
		try {
			request = requestFactory.buildGetRequest(url);
			HttpResponse response = request.execute();
			MobileDevice[] androidDevices = response.parseAs(MobileDevice[].class);
			return androidDevices;
		} catch (IOException e) {
			Log.e(LOG_PREFIX, "Failed to parse JSON ", e);
		}
		return null;
	    
	}
	
	/**
	 * Send a push message to the REST based interface
	 * 
	 * @param pushMessage
	 * @return
	 */
	public static PushMessageResponse sendMessage(PushMessageExternal pushMessage){
		PushMessageResponse pushMessageResponse=null;;
		HttpRequestFactory requestFactory =
		        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
		            @Override
		          public void initialize(HttpRequest request) {
		            request.setParser(new JsonObjectParser(JSON_FACTORY));
		          }
		        });
		//the endpoint of the REST service
		GenericUrl url = new GenericUrl(BASE_BACKEND_URL +"api/messaging/");

		
		JsonHttpContent jsonContent = new JsonHttpContent(JSON_FACTORY, pushMessage);
	    
		HttpRequest request;
		try {
			request = requestFactory.buildPostRequest(url, jsonContent);
			HttpResponse response  = request.execute();
			pushMessageResponse = response.parseAs(PushMessageResponse.class);
			pushMessageResponse.setResponseCode(response.getStatusCode());
			pushMessageResponse.setResponseMessage(response.getStatusMessage());
			
			Log.d(LOG_PREFIX, "Response code " + response.getStatusCode() + " " + response.getStatusMessage() + " response: "+pushMessageResponse);
		} catch (IOException e) {
			Log.e(LOG_PREFIX, "Failed to post JSON ", e);
		}
		
		return pushMessageResponse;
	}



	/**
	 * Remove this device from the NWCloud
	 * 
	 * @param context
	 * @return
	 */
	public static boolean removeMyDevice(Context context) {
		try {
			String email = AndroidUtils.getPrimaryAccountEmail(context);
			email = URLEncoder.encode(email,"UTF-8");
			
			
			HttpRequestFactory requestFactory =
	        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
	            @Override
	          public void initialize(HttpRequest request) {
	            request.setParser(new JsonObjectParser(JSON_FACTORY));
	          }
	        });
			//the endpoint of the REST service
			GenericUrl url = new GenericUrl(BASE_BACKEND_URL +"api/mobiledevice/"+email);
					
			Log.i(LOG_PREFIX, "Attempting to delete device through URL " + url);
			
			HttpRequest request;
			
			request = requestFactory.buildDeleteRequest(url);
			HttpResponse response = request.execute();
			int responseCode = response.getStatusCode();
			Log.i(LOG_PREFIX, "Response to HTTP Delete " + responseCode+ " "+response.getStatusMessage());
			if(responseCode ==200){
				return true;
			}else {
				return false;
			}
		} catch (IOException e) {
			Log.e(LOG_PREFIX, "Error while sending HTTP DELETE request", e);
		}
				
		return false;
	}

	

	/** Feed of Google+ activities. */
	  public static class AndroidDeviceList {

	    /** List of Google+ activities. */
	    @Key()
	    private List<MobileDevice> devices;

	    public List<MobileDevice> getDevices() {
	      return devices;
	    }
	  }





	
	
}
