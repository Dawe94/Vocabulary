package com.vocabulary;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class DiffrentNumbers {
	
	private final int MIN;
	private final int MAX;
	private final int QUANTITY;
	private final Random RANDOM = new Random();
	private final Set<Integer> nums = new HashSet<>();
	
	public DiffrentNumbers(int min, int max, int quantity) {
		this.MIN = min;
		this.MAX = (max - min);
		this.QUANTITY = quantity;
	}
	
	public int[] generate() {
		int[] array = new int[QUANTITY];
		int i = 0;
		while (i < QUANTITY) {
			if (isOverFlow()) {
				nums.clear();
			}
			int number = RANDOM.nextInt(MAX) + MIN;
			if (nums.add(number)) {
				array[i++] = number;
			}
		}
		return array;
	}
	
	public int generateAnotherOne() {
		Integer generatedNumber = null;
		while (generatedNumber == null) {
			if (isOverFlow()) {
				nums.clear();
			}
			int number = RANDOM.nextInt(MAX) + MIN;
			if (nums.add(number)) {
				generatedNumber = number;
			}
		}
		return generatedNumber;
	}
	
	private boolean isOverFlow() {
		return nums.size() >= MAX;
	}
	
}