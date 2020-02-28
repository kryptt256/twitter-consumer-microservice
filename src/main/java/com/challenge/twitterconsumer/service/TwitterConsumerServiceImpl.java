/**
 * 
 */
package com.challenge.twitterconsumer.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.challenge.twitterconsumer.domain.TweetData;
import com.challenge.twitterconsumer.repository.TweetRepository;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

/**
 * @author vvmaster
 *
 */
@Service
public class TwitterConsumerServiceImpl implements TwitterConsumerService{

	@Value("${service.hashtag.rank}")
	private int maxRankToShow;
	
	@Autowired
	private List<PersistenceRule> rules;

	@Autowired
	private TweetRepository<TweetData> repository;
	
	private Map<String, Integer> cacheRanking = new HashMap<>();
	
	private Comparator<Map.Entry<String, Integer>> rankComparator = 
			(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) -> {
				return o2.getValue().compareTo(o1.getValue());
			};
			
	@Override
	public boolean isPersistTweet(Status status) {
		return status.getLang() != null && rules.stream()
				.allMatch(r -> ((PersistenceRule)r).validate(status));
	}

	@Override
	public List<String> getTopHashRags() {
		return cacheRanking.entrySet().stream().sorted(rankComparator)
		.map(Map.Entry::getKey).limit(maxRankToShow).collect(Collectors.toList());
	}

	private void rankHashtag(HashtagEntity... hashtags) {
		Arrays.stream(hashtags).forEach(this::updateCacheRank);
	}
	
	private void updateCacheRank(HashtagEntity hashtag) {
		String text = hashtag.getText();
		Integer value = cacheRanking.get(text);
		if (value != null) {
			cacheRanking.put(text, ++value);
		} else {
			cacheRanking.put(text, 1);
		}
	}

	@Override
	public TweetData processTweet(Status status) {
		if (isPersistTweet(status)) {
			TweetData tweet = repository.save( TweetData.mapFromStatus(status) );
			rankHashtag(status.getHashtagEntities());
			return tweet;
		}
		return null;
	}

	@Override
	public Iterable<TweetData> getAllTweets() {
		return repository.findAll();
	}

	@Override
	public TweetData getTweetById(long id) {
		return repository.findById(id);
	}

	@Override
	public TweetData markTweetAsValid(long id) {
		TweetData tweet = repository.findById(id);
		tweet.setValid(true);
		repository.save(tweet);
		return tweet;
	}

	@Override
	public Iterable<TweetData> getValidatedTweetsByUserId(long userId) {
		return repository.findAllValidated(userId);
	}

	@Override
	public TweetData getTweetFromRequest(String statusRequest) {
		Status status = this.getStatusFromRequest(statusRequest)
				.orElseThrow(() -> new IllegalArgumentException("Tweet invalido"));
		
		return this.processTweet(status);
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

}
