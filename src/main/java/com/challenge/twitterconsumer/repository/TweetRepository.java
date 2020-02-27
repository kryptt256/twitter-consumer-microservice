/**
 * 
 */
package com.challenge.twitterconsumer.repository;

import java.util.Collection;

/**
 * @author vvmaster
 *
 */
public interface TweetRepository<T> {
	T save(T tweet);
	Iterable<T> save(Collection<T> tweets);
	T findById(long id);
	Iterable<T> findAll();
	Iterable<T> findAllValidated(long userId);
}
