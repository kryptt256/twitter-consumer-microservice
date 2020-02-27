/**
 * 
 */
package com.challenge.twitterconsumer.domain;

import twitter4j.HashtagEntity;

/**
 * @author vvmaster
 *
 */
public class MyHashTagEntity implements HashtagEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3488086753277046829L;
	private String text;
	private int start;
	private int end;
	
	public MyHashTagEntity(int start, int end, String text) {
		this.start = start;
		this.end = end;
		this.text = text;
	}
	
	@Override
    public String getText() {
        return text;
    }

    @Override
    public int getStart() {
        return this.start;
    }

    @Override
    public int getEnd() {
        return this.end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyHashTagEntity that = (MyHashTagEntity) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "MyHashTagEntity{" +
                "text='" + text + '\'' +
                '}';
    }

}
