/**
 * 
 */
package com.challenge.twitterconsumer.controller.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author vvmaster
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TwitterConsumerControllerTestConsume {

	@Autowired
	MockMvc mvc;
	
	private static final String urlPath = "/twitterconsumer/tweet";
	
	@Test
	void itShouldReturnStatusCreated() throws Exception {
		File resource = new ClassPathResource("/statusrequest.json").getFile();
		String jsonStr = new String(Files.readAllBytes(resource.toPath()));
		
		mvc.perform( post(urlPath).contentType(MediaType.APPLICATION_JSON)
				.content(jsonStr)).andExpect( status().isCreated());
	}

}
