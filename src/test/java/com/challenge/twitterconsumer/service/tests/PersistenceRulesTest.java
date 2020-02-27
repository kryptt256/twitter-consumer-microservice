/**
 * 
 */
package com.challenge.twitterconsumer.service.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import com.challenge.twitterconsumer.service.PersistenceRule;

import twitter4j.Status;
import twitter4j.User;

/**
 * @author vvmaster
 *
 */
@SpringBootTest
class PersistenceRulesTest {
	
	@Mock
	private Status status;
	
	@Mock
	private User user;
	
	@Autowired
	@Qualifier("validateFollowers")
	PersistenceRule numberOfFollowersRule;
	
	@Autowired
	@Qualifier("validateLang")
	PersistenceRule validateLanguages;
	
	@Test
	void numberOfFollowersNotValidTest() {
		Mockito.when(user.getFollowersCount()).thenReturn(20);
		Mockito.when(status.getUser()).thenReturn(user);
		assertTrue(!numberOfFollowersRule.validate(status));
	}
	
	@Test
	void numberOfFollowersValidTest() {
		Mockito.when(user.getFollowersCount()).thenReturn(2000);
		Mockito.when(status.getUser()).thenReturn(user);
		assertTrue(numberOfFollowersRule.validate(status));
	}
	
	@Test
	void textContainsAValidLanguageTest() {
		Mockito.when(status.getLang()).thenReturn("es");
		assertTrue(validateLanguages.validate(status));
	}

}

