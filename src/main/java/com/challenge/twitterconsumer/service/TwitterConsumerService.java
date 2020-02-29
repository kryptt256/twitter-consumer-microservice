package com.challenge.twitterconsumer.service;

import java.util.List;

import com.challenge.twitterconsumer.controller.TweetDTO;

import twitter4j.Status;
import twitter4j.TwitterException;

public interface TwitterConsumerService {
	TweetDTO processTweet(Status status);
	boolean isPersistTweet(Status status);
	List<String> getTopHashRags();
	Iterable<TweetDTO> getAllTweets();
	TweetDTO getTweetById(long id);
	TweetDTO markTweetAsValid(long id);
	Iterable<TweetDTO> getValidatedTweetsByUserId(long userId);
	TweetDTO getTweetFromRequest(String statusRequest) throws TwitterException;
}
