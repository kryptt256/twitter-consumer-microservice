/**
 * 
 */
package com.challenge.twitterconsumer.controller;

import java.net.URI;

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
import com.challenge.twitterconsumer.repository.TweetRepository;
import com.challenge.twitterconsumer.service.TwitterConsumerService;

/**
 * @author vvmaster
 *
 */
@RestController
@RequestMapping("/twitterconsumer")
public class TweetConsumerController implements TweetConsumer {

	@Autowired
	private TweetRepository<TweetData> repository;
	
	@Autowired
	private TwitterConsumerService twitterService;
	
	public TweetConsumerController() {
	}
	
	@Override
	@RequestMapping(value="/tweet", method = {RequestMethod.POST, RequestMethod.PUT})
	public ResponseEntity<?> consumeTweet(@RequestBody String statusRequest) {
		
		TweetData tweet = twitterService.getTweetFromRequest(statusRequest);
		if (tweet == null) {
			return ResponseEntity.noContent().build();
		}
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(tweet.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@Override
	@GetMapping("/tweet")
	public ResponseEntity<Iterable<TweetData>> getTweets() {
		return ResponseEntity.ok(repository.findAll());
	}
	
	@Override
	@GetMapping("/tweet/{tweetId}")
	public ResponseEntity<TweetData> getTweetById(@PathVariable("tweetId") long tweetId) {
		return ResponseEntity.ok(repository.findById(tweetId));
	}
	
	@Override
	@PatchMapping("/tweet/{tweetId}")
	public ResponseEntity<TweetData> setValid(@PathVariable("tweetId") long tweetId) {
		TweetData tweet = repository.findById(tweetId);
		tweet.setValid(true);
		repository.save(tweet);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(tweet.getId()).toUri();
		return ResponseEntity.ok().header("Location", location.toString()).build();
	}
	
	@Override
	@GetMapping("/tweets/{userId}")
	public ResponseEntity<Iterable<TweetData>> getValidatedTweetsByUserId(@PathVariable("userId") long userId) {
		return ResponseEntity.ok(repository.findAllValidated(userId));
	}
	
	@Override
	@GetMapping("/tweet/topmost")
	public ResponseEntity<Iterable<String>> getTopHashtags() {
		return ResponseEntity.ok(twitterService.getTopHashRags());
	}
}
