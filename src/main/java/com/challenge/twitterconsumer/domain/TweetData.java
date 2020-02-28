/**
 * 
 */
package com.challenge.twitterconsumer.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import twitter4j.Status;

/**
 * @author vvmaster
 *
 */
@JsonIgnoreProperties
public class TweetData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private long userId;
	private String text;
	private boolean valid;
	private String location;
	
	public TweetData() {
	}
	
	public TweetData(long id, long userId, String text, boolean valid, String location) {
		super();
		this.id = id;
		this.userId = userId;
		this.text = text;
		this.valid = valid;
		this.location = location;
	}

	public static TweetData mapFromStatus(Status status) {
		TweetData tweet = new TweetData();
		tweet.setId(status.getId());
		tweet.setText(status.getText());
		tweet.setValid(false);
		tweet.setUserId(status.getUser().getId());
		tweet.setLocation(status.getUser().getLocation());
		return tweet;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + (valid ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TweetData other = (TweetData) obj;
		if (id != other.id)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (userId != other.userId)
			return false;
		if (valid != other.valid)
			return false;
		return true;
	}

}
