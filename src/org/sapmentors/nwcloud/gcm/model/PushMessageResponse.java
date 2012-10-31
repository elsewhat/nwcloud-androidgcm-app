package org.sapmentors.nwcloud.gcm.model;

import com.google.api.client.util.Key;

/**
 * Represents the PushMessage in external state
 * This represents the JSON/XML payload that must be provided
 * to the REST API
 *  
 * @author dagfinn.parnas
 *
 */
public class PushMessageResponse {
	@Key
	protected String emailFrom; 
	@Key
	protected String[] emailSentTo; 
	@Key
	protected String[] emailFailed; 
	@Key
	protected int messageType;
	
	protected int responseCode;
	protected String responseMessage;
	
	
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
	public String[] getEmailSentTo() {
		return emailSentTo;
	}
	public void setEmailSentTo(String[] emailSentTo) {
		this.emailSentTo = emailSentTo;
	}
	public String[] getEmailFailed() {
		return emailFailed;
	}
	public void setEmailFailed(String[] emailFailed) {
		this.emailFailed = emailFailed;
	}
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	} 
	
	
}
