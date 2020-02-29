/**
 * 
 */
package com.challenge.twitterconsumer.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.challenge.twitterconsumer.domain.TweetData;
import com.challenge.twitterconsumer.service.TwitterConsumerService;

import twitter4j.TwitterException;

/**
 * @author vvmaster
 *
 */
@RestController
@RequestMapping("/twitterconsumer")
public class TweetConsumerController implements TweetConsumer {

	private static Logger log = LoggerFactory.getLogger(TweetConsumerController.class);
	
	@Autowired
	private TwitterConsumerService twitterService;
	
	public TweetConsumerController() {
	}
	
	@Override
	@RequestMapping(value="/tweet", method = {RequestMethod.POST, RequestMethod.PUT})
	public ResponseEntity<?> consumeTweet(@RequestBody String statusRequest) {
		
		TweetData tweet;
		try {
			tweet = twitterService.getTweetFromRequest(statusRequest);
		} catch (TwitterException e) {
			StringBuilder sb = new StringBuilder(150);
			sb.append("Error: Tweet invalido!")
			.append("\n").append(e.getMessage());
			log.error(e.getMessage());
			
			throw new IllegalArgumentException(sb.toString());
		}
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(tweet.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@Override
	@GetMapping("/tweet")
	public ResponseEntity<Iterable<TweetData>> getTweets() {
		return ResponseEntity.ok(twitterService.getAllTweets());
	}
	
	@Override
	@GetMapping("/tweet/{tweetId}")
	public ResponseEntity<TweetData> getTweetById(@PathVariable("tweetId") long tweetId) {
		return ResponseEntity.ok(twitterService.getTweetById(tweetId));
	}
	
	@Override
	@PatchMapping("/tweet/{tweetId}")
	public ResponseEntity<TweetData> setValid(@PathVariable("tweetId") long tweetId) {
		TweetData tweet = twitterService.markTweetAsValid(tweetId);
		
		if (tweet != null) {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(tweet.getId()).toUri();
			return ResponseEntity.ok().header("Location", location.toString()).build();
		}
		
		throw new IllegalArgumentException("Error: tweet id no existe -> " + tweetId);
	}
	
	@Override
	@GetMapping("/tweets/{userId}")
	public ResponseEntity<Iterable<TweetData>> getValidatedTweetsByUserId(@PathVariable("userId") long userId) {
		return ResponseEntity.ok(twitterService.getValidatedTweetsByUserId(userId));
	}
	
	@Override
	@GetMapping("/tweet/topmost")
	public ResponseEntity<Iterable<String>> getTopHashtags() {
		return ResponseEntity.ok(twitterService.getTopHashRags());
	}
}
