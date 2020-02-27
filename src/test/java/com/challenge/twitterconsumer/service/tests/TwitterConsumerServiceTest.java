/**
 * 
 */
package com.challenge.twitterconsumer.service.tests;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.challenge.twitterconsumer.domain.MyHashTagEntity;
import com.challenge.twitterconsumer.service.TwitterConsumerService;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.User;

/**
 * @author vvmaster
 *
 */
@SpringBootTest
class TwitterConsumerServiceTest {

	@Autowired
	TwitterConsumerService service;
	
	@Mock
	private Status status;
	
	
	@Mock
	private User user;
	
	@Test
	void itShouldPersistTweetTest() {
		Mockito.when(user.getFollowersCount()).thenReturn(5000);
		Mockito.when(status.getUser()).thenReturn(user);
		Mockito.when(status.getLang()).thenReturn("fr");
		assertTrue(service.isPersistTweet(status));
	}
	
	@Test
	void itShouldNotPersistTweetTest() {
		Mockito.when(user.getFollowersCount()).thenReturn(5000);
		Mockito.when(status.getUser()).thenReturn(user);
		Mockito.when(status.getLang()).thenReturn("en");
		assertTrue(!service.isPersistTweet(status));
		
	}
	
	@Test
	void itShouldSaveTweetTest() {
		Mockito.when(user.getFollowersCount()).thenReturn(5000);
		Mockito.when(user.getId()).thenReturn(123L);
		Mockito.when(user.getLocation()).thenReturn("Santo Domingo, Rep. Dom.");
		Mockito.when(status.getUser()).thenReturn(user);
		Mockito.when(status.getLang()).thenReturn("es");
		Mockito.when(status.getId()).thenReturn(12345L);
		Mockito.when(status.getText()).thenReturn("Spring Boot es excelente para Microservicios");
		Mockito.when(status.getHashtagEntities())
		.thenReturn(new HashtagEntity[] {new MyHashTagEntity(10, 40, "test hashtag")});
		assertThat(service.processTweet(status), notNullValue());
	}
	
	@Test
	void itShouldShowTopHashTags() {
		Mockito.when(user.getFollowersCount()).thenReturn(5000);
		Mockito.when(user.getId()).thenReturn(123L);
		Mockito.when(user.getLocation()).thenReturn("Santo Domingo, Rep. Dom.");
		Mockito.when(status.getUser()).thenReturn(user);
		Mockito.when(status.getLang()).thenReturn("es");
		Mockito.when(status.getId()).thenReturn(12345L);
		Mockito.when(status.getText()).thenReturn("Spring Boot es excelente para Microservicios");
		Mockito.when(status.getHashtagEntities())
		.thenReturn(new HashtagEntity[] {new MyHashTagEntity(10, 40, "bitcoin"),
				new MyHashTagEntity(10, 40, "bitcoin"),
				new MyHashTagEntity(10, 40, "love"),
				new MyHashTagEntity(10, 40, "dash"),
				new MyHashTagEntity(10, 40, "love"),
				new MyHashTagEntity(10, 40, "bitcoin"),
				new MyHashTagEntity(10, 40, "bitcoin")});
		
		service.processTweet(status);
		List<String> hashTagNames = service.getTopHashRags();
		assertThat(hashTagNames, hasItems("love"));
		
	}

}
