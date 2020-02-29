package com.challenge.twitterconsumer.repository.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.challenge.twitterconsumer.domain.TweetData;
import com.challenge.twitterconsumer.repository.TweetRepository;

@SpringBootTest
class TweetRepositoryTests {

	private TweetRepository<TweetData> repository;
	
	private Stream<TweetData> tweets;

	@Autowired
	public TweetRepositoryTests(TweetRepository<TweetData> repository) {
		this.repository = repository;
	}

	@Test
	void itShouldSaveTweet() {
		TweetData tweet = new TweetData();
		tweet.setId(454599L);
		tweet.setText("Prueba");		
		repository.save(tweet);
		assertThat(repository.findById(tweet.getId()), notNullValue());
	}
	
	@Test
	void itShouldSaveListOfTweets() {
		tweets = Stream.of(new TweetData(54547L, 100L, "hola mundo", false, "Spain"));
		repository.save(tweets.collect(Collectors.toList()));
		assertThat(repository.findAll()).hasSize(3);
	}
	
	@Test
	void itShouldRetrieveValidatedTweetsByUserId() {
		tweets = Stream.of(new TweetData(54547L, 100L, "hola mundo", false, "Spain"),
				new TweetData(5454733L, 1001L, "Me gusta la pizza", true, "Francia"));
		repository.save(tweets.collect(Collectors.toList()));
		assertThat(repository.findAllValidated(1001L)).hasSize(1);
	}
}
