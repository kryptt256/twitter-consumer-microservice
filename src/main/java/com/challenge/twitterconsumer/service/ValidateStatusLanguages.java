/**
 * 
 */
package com.challenge.twitterconsumer.service;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import twitter4j.Status;

/**
 * @author vvmaster
 *
 */
@Component(value = "validateLang")
public class ValidateStatusLanguages implements PersistenceRule {

	@Value("#{'${service.persistence.valid-languages}'.split(',')}") 
	private List<String> languageList;
	
	@Override
	public boolean validate(Status tweet) {
		return languageList.stream()
				.anyMatch(lang -> new Locale(lang).equals( new Locale(tweet.getLang())));
	}

}
