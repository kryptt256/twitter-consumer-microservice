package com.challenge.twitterconsumer.controller;

import org.springframework.http.ResponseEntity;

public interface TweetConsumer {

	ResponseEntity<?> consumeTweet(String statusRequest);

	ResponseEntity<Iterable<TweetDTO>> getTweets();

	ResponseEntity<TweetDTO> getTweetById(long tweetId);

	ResponseEntity<TweetDTO> setValid(long tweetId);

	ResponseEntity<Iterable<TweetDTO>> getValidatedTweetsByUserId(long userId);

	ResponseEntity<Iterable<String>> getTopHashtags();

}