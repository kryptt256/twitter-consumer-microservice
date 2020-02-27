package com.challenge.twitterconsumer.service;

import twitter4j.Status;

public interface PersistenceRule {
	boolean validate(Status tweet);
}
