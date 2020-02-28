/**
 * 
 */
package com.challenge.twitterconsumer.controller;

import java.net.URI;
import java.util.Optional;

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

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

/**
 * @author vvmaster
 *
 */
@RestController
@RequestMapping("/twitterconsumer")
public class TweetConsumerController {

	@Autowired
	private TweetRepository<TweetData> repository;
	
	@Autowired
	private TwitterConsumerService twitterService;
	
	public TweetConsumerController() {
	}
	
	@RequestMapping(value="/tweet", method = {RequestMethod.POST, RequestMethod.PUT})
	public ResponseEntity<?> consumeTweet(@RequestBody String StatusRequest) {
		Status status = this.getStatusFromRequest(StatusRequest)
				.orElseThrow(() -> new IllegalArgumentException("Tweet invalido"));
		
		TweetData tweet = twitterService.processTweet(status);
		
		if (tweet == null) {
			return ResponseEntity.noContent().build();
		}
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(tweet.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	private Optional<Status> getStatusFromRequest(String request) {
		Status result = null;
		
		try {
			result = TwitterObjectFactory.createStatus(request);
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		
		return Optional.ofNullable(result);
	}
	
	@GetMapping("/tweet")
	public ResponseEntity<Iterable<TweetData>> getTweets() {
		return ResponseEntity.ok(repository.findAll());
	}
	
	@GetMapping("/tweet/{id}")
	public ResponseEntity<TweetData> getTweetById(@PathVariable long id) {
		return ResponseEntity.ok(repository.findById(id));
	}
	
	@PatchMapping("/tweet/{id}")
	public ResponseEntity<TweetData> setValid(@PathVariable long id) {
		TweetData tweet = repository.findById(id);
		tweet.setValid(true);
		repository.save(tweet);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(tweet.getId()).toUri();
		return ResponseEntity.ok().header("Location", location.toString()).build();
	}
	
	@GetMapping("/tweets/user/{id}")
	public ResponseEntity<Iterable<TweetData>> getValidatedTweetsByUserId(@PathVariable long userId) {
		return ResponseEntity.ok(repository.findAllValidated(userId));
	}
	
	@GetMapping("/tweet/topmost")
	public ResponseEntity<Iterable<String>> getTopHashtags() {
		return ResponseEntity.ok(twitterService.getTopHashRags());
	}
}
