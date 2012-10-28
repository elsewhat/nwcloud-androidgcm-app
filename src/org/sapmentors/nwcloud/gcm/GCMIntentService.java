package org.sapmentors.nwcloud.gcm;

import java.io.IOException;

import org.sapmentors.nwcloud.gcm.backend.NWCloudBackend;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
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
 * Receive a push message from the Cloud to Device Messaging (C2DM) service.
 * This class should be modified to include functionality specific to your
 * application. This class must have a no-arg constructor and pass the sender id
 * to the superclass constructor.
 */
public class GCMIntentService extends GCMBaseIntentService {
	private static final String LOG_PREFIX = "NWCLOUD-GCM";
	// Project_id is part of Google API Console URL of the project
	private static final String PROJECT_ID = "170560228105";

	/**
	 * Register the device for GCM.
	 * 
	 * @param mContext
	 *            the activity's context.
	 */
	public static void register(Context mContext) {
		Log.d(LOG_PREFIX, "Attempting to register to Google Cloud Messaging ");
		GCMRegistrar.checkDevice(mContext);
		GCMRegistrar.checkManifest(mContext);
		GCMRegistrar.register(mContext, PROJECT_ID);
	}

	public GCMIntentService() {
		super(PROJECT_ID);
	}

	/**
	 * Called on registration error. This is called in the context of a Service
	 * - no dialog or UI.
	 * 
	 * @param context
	 *            the Context
	 * @param errorId
	 *            an error message
	 */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(LOG_PREFIX, "onError " + errorId);
	}

	/**
	 * Called when a cloud message has been received.
	 */
	@Override
	public void onMessage(Context context, Intent intent) {
		String message = intent.getStringExtra("message");
		Log.i(LOG_PREFIX, "Google cloud message " + message);
		createNotification(message);
	}

	private void createNotification(String message) {
		Log.i(LOG_PREFIX, "createNotification with message " + message);

		int icon = android.R.drawable.ic_input_get; // icon from resources
		CharSequence tickerText = "Family sync quote"; // ticker-text
		long when = System.currentTimeMillis(); // notification time
		Context context = getApplicationContext(); // application Context
		CharSequence contentTitle = "Stuff Kids Say"; // message title
		CharSequence contentText = message; // message text

		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		// the next two lines initialize the Notification, using the
		// configurations above
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, notification);
	}

	
	/**
	 * Called when the device has been unregistered.
	 * 
	 * @param context
	 *            the Context
	 */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
	}
	
	/**
	 * Called when a registration token has been received.
	 * 
	 * @param context
	 *            the Context
	 */
	@Override
	public void onRegistered(Context context, String registrationKey) {
		Log.i(LOG_PREFIX, "onRegistered");
		// TODO
		// Persist registration key in nwcloud backend
		NWCloudBackend.persistDevice("dagfinn.parnas@gmail.com", registrationKey);
	}

	


}
