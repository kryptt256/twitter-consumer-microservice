/**
 * 
 */
package com.challenge.twitterconsumer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import twitter4j.Status;

/**
 * @author vvmaster
 *
 */
@Component(value = "validateFollowers")
public class ValidateNumberOfFollowers implements PersistenceRule {

	@Value("${service.persistence.followers}")
	private int persistingFollowers;
	
	@Override
	public boolean validate(Status tweet) {
		return tweet.getUser().getFollowersCount() > persistingFollowers;
	}
	
}
