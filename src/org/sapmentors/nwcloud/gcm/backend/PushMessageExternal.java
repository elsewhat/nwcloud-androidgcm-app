package org.sapmentors.nwcloud.gcm.backend;

import com.google.api.client.util.Key;


/**
 * Represents the PushMessage in external state
 * This represents the JSON/XML payload that must be provided
 * to the REST API
 *  
 * @author dagfinn.parnas
 *
 */
public class PushMessageExternal {
	@Key
	protected String emailFrom; 
	@Key
	protected String[] emailTo; 
	@Key
	protected int messageType; 
	@Key
	protected String message;
	
	
	
	public PushMessageExternal(String emailFrom, String[] emailTo,
			int messageType, String message) {
		this.emailFrom = emailFrom;
		this.emailTo = emailTo;
		this.messageType = messageType;
		this.message = message;
	}
	
	public PushMessageExternal() {
	}
	
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
	public String[] getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String[] emailTo) {
		this.emailTo = emailTo;
	}
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
}
