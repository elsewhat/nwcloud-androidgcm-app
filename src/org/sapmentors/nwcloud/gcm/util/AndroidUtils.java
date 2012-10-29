package org.sapmentors.nwcloud.gcm.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

public class AndroidUtils {
	private static final String LOG_PREFIX = "NWCloud-app";
	
	public static String getPrimaryAccountEmail(Context context){
	    AccountManager accountManager = AccountManager.get(context);
	    Account[] accounts = accountManager.getAccountsByType("com.google");
	    if(accounts==null || accounts.length==0){
	    	Log.w(LOG_PREFIX, "Could not find name of primary google account. Returning null" );
	    	return null;
	    }else {
	    	String name = accounts[0].name;
	    	Log.d(LOG_PREFIX, "Found "+accounts.length + " google accounts. Returning name:"+ name );
	    	return name;
	    }
	}
}
