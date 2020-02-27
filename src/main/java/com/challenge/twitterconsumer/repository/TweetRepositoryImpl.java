/**
 * 
 */
package com.challenge.twitterconsumer.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.challenge.twitterconsumer.domain.TweetData;


/**
 * @author vvmaster
 *
 */
@Repository
public class TweetRepositoryImpl implements TweetRepository<TweetData> {

	Map<Long, TweetData> dbTweets = new HashMap<>();
	
	@Override
	public TweetData save(TweetData tweet) {
		dbTweets.put(tweet.getId(), tweet);
		return dbTweets.get(tweet.getId());
	}

	@Override
	public Iterable<TweetData> save(Collection<TweetData> tweets) {
		tweets.forEach(this::save);
		return this.findAll();
	}

	@Override
	public TweetData findById(long id) {
		return dbTweets.get(id);
	}

	@Override
	public Iterable<TweetData> findAll() {
		return dbTweets.entrySet().stream().map(Map.Entry::getValue)
				.collect(Collectors.toList());
	}

	@Override
	public Iterable<TweetData> findAllValidated(long userId) {
		return dbTweets.entrySet().stream().map(Map.Entry::getValue)
				.filter(t -> t.getUserId() == userId && t.isValid())
				.collect(Collectors.toList());
	}

}
