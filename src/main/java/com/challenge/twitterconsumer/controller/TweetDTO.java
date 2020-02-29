/**
 * 
 */
package com.challenge.twitterconsumer.controller;

/**
 * @author vvmaster
 *
 */
public class TweetDTO {
	private long id;
	private long userId;
	private String text;
	private boolean valid;
	private String location;
	
	public TweetDTO() {
	}

	public TweetDTO(long id, long userId, String text, boolean valid, String location) {
		super();
		this.id = id;
		this.userId = userId;
		this.text = text;
		this.valid = valid;
		this.location = location;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
}
