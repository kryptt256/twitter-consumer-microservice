package com.challenge.twitterconsumer.controller.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.challenge.twitterconsumer.domain.TweetData;
import com.challenge.twitterconsumer.service.TwitterConsumerService;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TwitterConsumerControllerTests {

	@Autowired
	MockMvc mvc;
	
	@MockBean
	TwitterConsumerService service;
	
	@Test
	void whenPostTweetThenCreateTweet() throws Exception {
		TweetData tweet = new TweetData(10L, 100L, "prueba", false, "Spain");
		given(service.processTweet(Mockito.any())).willReturn(tweet);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(tweet);
		
		mvc.perform( post("/twitterconsumer/tweet").contentType(MediaType.APPLICATION_JSON)
				.content(jsonStr)).andExpect( status().isCreated());

	}
	
	@Test
	void retrieveTweetsTest( ) throws Exception {
		this.mvc.perform( get("/twitterconsumer/tweet"))
		.andExpect(status().isOk())
		.andExpect( content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	void itShouldReturnTweetById() {
		given(this.service.getTweetById(200L))
		.willReturn(new TweetData(200L, 300L, "Soy feliz", false, "Spain"));
		assertThat(this.service.getTweetById(200L).getLocation()).isEqualTo("Spain");
	}
	
	@Test
	void itShouldMarkTweetAsValid() throws Exception{
		TweetData tweet = new TweetData(123L, 100L, "prueba", false, "Spain");
		given(service.processTweet(Mockito.any())).willReturn(tweet);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonStr = mapper.writeValueAsString(tweet);
		
		mvc.perform( post("/twitterconsumer/tweet").pathInfo("/123")
				.content(jsonStr))
		.andExpect( status().isCreated());
		
	}

}