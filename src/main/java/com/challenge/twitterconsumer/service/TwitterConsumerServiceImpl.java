/**
 * 
 */
package com.challenge.twitterconsumer.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.challenge.twitterconsumer.controller.TweetDTO;
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
	public TweetDTO processTweet(Status status) {
		if (isPersistTweet(status)) {
			TweetDTO tweetDTO = this.mapDTOFromStatus(status);
			TweetData tweetData = this.mapDataFromDTO(tweetDTO);
			repository.save( tweetData );
			rankHashtag(status.getHashtagEntities());
			return tweetDTO;
		}
		return null;
	}

	@Override
	public Iterable<TweetDTO> getAllTweets() {
		return StreamSupport.stream(repository.findAll().spliterator(), false)
				.map(this::mapTweetDTOFromTweetData)
				.collect(Collectors.toList());
	}

	@Override
	public TweetDTO getTweetById(long id) {
		return mapTweetDTOFromTweetData( repository.findById(id) )  ;
	}

	@Override
	public TweetDTO markTweetAsValid(long id) {
		TweetData tweet = repository.findById(id);
		tweet.setValid(true);
		repository.save(tweet);
		return mapTweetDTOFromTweetData(tweet);
	}

	@Override
	public Iterable<TweetDTO> getValidatedTweetsByUserId(long userId) {
		return StreamSupport.stream(repository.findAllValidated(userId).spliterator(), false)
				.map(this::mapTweetDTOFromTweetData)
				.collect(Collectors.toList());
	}

	@Override
	public TweetDTO getTweetFromRequest(String statusRequest) throws TwitterException {
		Status status = this.getStatusFromRequest(statusRequest);
		return this.processTweet(status);
	}
	
	private Status getStatusFromRequest(String request) throws TwitterException {
		return TwitterObjectFactory.createStatus(request);
	}
	
	private TweetData mapDataFromDTO(TweetDTO tweetDTO) {
		TweetData tweetData = new TweetData();
		tweetData.setId(tweetDTO.getId());
		tweetData.setText(tweetDTO.getText());
		tweetData.setValid(false);
		tweetData.setUserId(tweetDTO.getUserId());
		tweetData.setLocation(tweetDTO.getLocation());
		return tweetData;
	}
	
	private TweetDTO mapDTOFromStatus(Status status) {
		TweetDTO tweetDTO = new TweetDTO();
		tweetDTO.setId(status.getId());
		tweetDTO.setText(status.getText());
		tweetDTO.setValid(false);
		tweetDTO.setUserId(status.getUser().getId());
		tweetDTO.setLocation(status.getUser().getLocation());
		return tweetDTO;
	}
	
	private TweetDTO mapTweetDTOFromTweetData(TweetData data) {
		return new TweetDTO(data.getId(), data.getUserId(), data.getText(), 
				data.isValid(), data.getLocation());
	}

}
