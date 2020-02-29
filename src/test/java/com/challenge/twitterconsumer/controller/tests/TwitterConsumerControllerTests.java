package com.challenge.twitterconsumer.controller.tests;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.challenge.twitterconsumer.controller.TweetDTO;
import com.challenge.twitterconsumer.service.TwitterConsumerService;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TwitterConsumerControllerTests {

	@Autowired
	MockMvc mvc;
	
	@MockBean
	TwitterConsumerService service;
	
	private static final String urlPath = "/twitterconsumer/tweet";
	
	@Test
	void whenPostTweetThenCreateTweet() throws Exception {
		TweetDTO tweet = new TweetDTO(10L, 100L, "prueba", false, "Spain");
		given(service.getTweetFromRequest("{}")).willReturn(tweet);
		
		mvc.perform( post(urlPath).content("{}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect( status().isCreated());

	}
	
	@Test
	void retrieveTweetsTest( ) throws Exception {
		this.mvc.perform( get(urlPath))
		.andExpect(status().isOk())
		.andExpect( content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	void itShouldReturnTweetById() throws Exception {
		given(service.getTweetById(200L))
				.willReturn(new TweetDTO(200L, 300L, "Soy feliz", false, "Spain"));
		
		mvc.perform( get(urlPath + "/200").contentType(MediaType.APPLICATION_JSON))
		.andExpect( status().isOk());
	}
	
	@Test
	void itShouldMarkTweetAsValid() throws Exception{
		TweetDTO tweet = new TweetDTO(123L, 100L, "prueba", true, "Spain");
		given(service.markTweetAsValid(123L)).willReturn(tweet);

		mvc.perform( patch(urlPath + "/123"))
		.andExpect( status().isOk());
		
	}

}