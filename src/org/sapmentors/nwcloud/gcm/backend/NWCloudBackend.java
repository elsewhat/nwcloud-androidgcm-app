package org.sapmentors.nwcloud.gcm.backend;

import java.io.IOException;
import java.util.List;


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
import com.google.api.client.util.GenericData;
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
	protected static final String BASE_BACKEND_URL = "http://192.168.1.6:8080/nwcloud-androidgcm-backend/";
	protected static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	protected static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	
	public static void persistDevice(String email, String registrationKey) {
		HttpRequestFactory requestFactory =
		        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
		            @Override
		          public void initialize(HttpRequest request) {
		            request.setParser(new JsonObjectParser(JSON_FACTORY));
		          }
		        });
		//the endpoint of the REST service
		GenericUrl url = new GenericUrl(BASE_BACKEND_URL +"api/androiddevice/");
		
		//The data (to be converted to JSON)
		AndroidDevice androidDevice = new AndroidDevice(registrationKey,email);
		
		JsonHttpContent jsonContent = new JsonHttpContent(JSON_FACTORY, androidDevice);
	    
		HttpRequest request;
		try {
			request = requestFactory.buildPostRequest(url, jsonContent);
			parseResponse(request.execute());
		} catch (IOException e) {
			Log.e(LOG_PREFIX, "Failed to post JSON ", e);
		}
	    
	}
	
	public static List<AndroidDevice> getRegisteredDevices() {
		HttpRequestFactory requestFactory =
		        HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
		            @Override
		          public void initialize(HttpRequest request) {
		            request.setParser(new JsonObjectParser(JSON_FACTORY));
		          }
		        });
		//the endpoint of the REST service
		GenericUrl url = new GenericUrl(BASE_BACKEND_URL +"api/androiddevice/");

		HttpRequest request;
		try {
			request = requestFactory.buildGetRequest(url);
			HttpResponse response = request.execute();
			AndroidDeviceList androidDeviceList = response.parseAs(AndroidDeviceList.class);
			return androidDeviceList.getDevices();
		} catch (IOException e) {
			Log.e(LOG_PREFIX, "Failed to post JSON ", e);
		}
		return null;
	    
	}


	private static void parseResponse(HttpResponse response) throws IOException{
		// TODO Auto-generated method stub
		String strResponse = response.parseAsString();
		
		Log.d(LOG_PREFIX, "Response code " + response.getStatusCode() + " " + response.getStatusMessage() + " response: "+strResponse);
	}


	/** Feed of Google+ activities. */
	  public static class AndroidDeviceList {

	    /** List of Google+ activities. */
	    @Key()
	    private List<AndroidDevice> devices;

	    public List<AndroidDevice> getDevices() {
	      return devices;
	    }
	  }


	
	
}
