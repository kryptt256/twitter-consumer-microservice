package com.challenge.twitterconsumer.repository.tests;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import com.challenge.twitterconsumer.domain.TweetData;
import com.challenge.twitterconsumer.repository.TweetRepository;

@SpringBootTest
class TweetRepositoryTests {

	@Autowired
	TweetRepository<TweetData> repository;
	
	private Stream<TweetData> tweets;

	@Test
	@Order(1)
	void itShouldSaveTweet() {
		TweetData tweet = new TweetData();
		tweet.setId(454599L);
		tweet.setText("Prueba");		
		repository.save(tweet);
		assertThat(repository.findById(tweet.getId()), notNullValue());
	}
	
	@Test
	@Order(2)
	void itShouldSaveListOfTweets() {
		tweets = Stream.of(new TweetData(54547L, 100L, "hola mundo", false, "Spain"));
		repository.save(tweets.collect(Collectors.toList()));
		assertThat(StreamSupport.stream(repository.findAll().spliterator(), false)
			    .collect(Collectors.toList()), hasSize(3));
		
	}
	
	@Test
	@Order(3)
	void itShouldRetrieveValidatedTweetsByUserId() {
		tweets = Stream.of(new TweetData(54547L, 100L, "hola mundo", false, "Spain"),
				new TweetData(5454733L, 1001L, "Me gusta la pizza", true, "Francia"));
		repository.save(tweets.collect(Collectors.toList()));
		assertThat(StreamSupport.stream(repository.findAllValidated(1001L).spliterator(), false)
			    .collect(Collectors.toList()), hasSize(1));
	}

}
