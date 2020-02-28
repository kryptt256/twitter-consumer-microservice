package com.challenge.twitterconsumer.controller;

import org.springframework.http.ResponseEntity;

import com.challenge.twitterconsumer.domain.TweetData;

public interface TweetConsumer {

	ResponseEntity<?> consumeTweet(String statusRequest);

	ResponseEntity<Iterable<TweetData>> getTweets();

	ResponseEntity<TweetData> getTweetById(long tweetId);

	ResponseEntity<TweetData> setValid(long tweetId);

	ResponseEntity<Iterable<TweetData>> getValidatedTweetsByUserId(long userId);

	ResponseEntity<Iterable<String>> getTopHashtags();

}