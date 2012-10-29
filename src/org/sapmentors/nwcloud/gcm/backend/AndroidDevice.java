package org.sapmentors.nwcloud.gcm.backend;

import com.google.api.client.util.Key;

public class AndroidDevice {

	@Key
	private String registrationKey;
	@Key
	private String email;
	
	

	public AndroidDevice(String email, String registrationKey) {
		this.registrationKey = registrationKey;
		this.email = email;
	}
	
	public AndroidDevice(){
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
}
