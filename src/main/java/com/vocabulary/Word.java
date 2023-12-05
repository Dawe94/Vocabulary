package com.vocabulary;

import java.util.Arrays;

public class Word {
	
	private String word;
	private String[] meanings;
	private int hit;
	
	public Word(String word, String[] meanings, int hit) {
		super();
		this.word = word;
		this.meanings = meanings;
		this.hit = hit;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
	public String[] getMeanings() {
		return meanings;
	}

	public void setMeanings(String[] meanings) {
		this.meanings = meanings;
	}
	
	public int getHit() {
		return hit;
	}

	public void incrementHit() {
		hit++;
	}
	
	public String getMeaningsAsString() {
		int lineBreaker = 20;
		StringBuilder sb = new StringBuilder();
		for (String element : meanings) {
			if (sb.length() >= lineBreaker) {
				sb.delete(sb.length()-2, sb.length());
				sb.append("\n");
				lineBreaker += 50;
			}
			sb.append(element +", ");
		}
		sb.delete(sb.length()-2, sb.length());
		return sb.toString();
	}
	
	public String getHitAsString() {
		return Integer.toString(hit);
	}
	
	public boolean isThereMatch(String value) {
		for (String element : meanings) {
			if (element.trim().equalsIgnoreCase(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return word +": "+ Arrays.toString(meanings);
	}
	
	

}
