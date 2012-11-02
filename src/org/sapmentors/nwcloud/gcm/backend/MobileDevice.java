package org.sapmentors.nwcloud.gcm.backend;

import com.google.api.client.util.Key;

/**
 * Represents a mobile device
 * 
 */
public class MobileDevice {
	public static final String ANDROID_MOBILE_PLATFORM="Android";
	public static final String IOS_MOBILE_PLATFORM="iOS";
	
	@Key
	private String registrationKey;
	@Key
	private String email;
	@Key 
	private String mobilePlatform;

	public MobileDevice(String email, String registrationKey,String mobilePlatform) {
		this.registrationKey = registrationKey;
		this.email = email;
		this.mobilePlatform=mobilePlatform;
	}
	
	public MobileDevice(){
	}

	public String getRegistrationKey() {
		return registrationKey;
	}

	public void setRegistrationKey(String registrationKey) {
		this.registrationKey = registrationKey;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMobilePlatform() {
		return mobilePlatform;
	}

	public void setMobilePlatform(String mobilePlatform) {
		this.mobilePlatform = mobilePlatform;
	}
	
	/**
	 * Is this an android device
	 * 
	 * @return
	 */
	public boolean isAndroidDevice(){
		if(ANDROID_MOBILE_PLATFORM.equalsIgnoreCase(mobilePlatform)){
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Is this an iOS/Apple device
	 * (note Apple Push Notification service not yet implemented)
	 * @return
	 */
	public boolean isIOS(){
		if(IOS_MOBILE_PLATFORM.equalsIgnoreCase(mobilePlatform)){
			return true;
		}else {
			return false;
		}
	}
	
}
