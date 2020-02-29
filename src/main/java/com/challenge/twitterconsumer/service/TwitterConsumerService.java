package com.challenge.twitterconsumer.service;

import java.util.List;

import com.challenge.twitterconsumer.domain.TweetData;

import twitter4j.Status;
import twitter4j.TwitterException;

public interface TwitterConsumerService {
	TweetData processTweet(Status status);
	boolean isPersistTweet(Status status);
	List<String> getTopHashRags();
	Iterable<TweetData> getAllTweets();
	TweetData getTweetById(long id);
	TweetData markTweetAsValid(long id);
	Iterable<TweetData> getValidatedTweetsByUserId(long userId);
	TweetData getTweetFromRequest(String statusRequest) throws TwitterException;
}
