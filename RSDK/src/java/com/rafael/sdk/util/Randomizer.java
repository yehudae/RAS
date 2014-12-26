package com.rafael.sdk.util;

import java.util.Random;

public class Randomizer {

	private int[] numbers = null;
	private Random random = null;
	private int nonRepeatSize;
	
	public Randomizer(int size) {
	    numbers = new int[size];
	    
		for (int i=0; i<size; i++) {
	    	numbers[i] = i;
	    }
		
		nonRepeatSize = size;
		random = new Random(System.nanoTime());
	}

	public Randomizer(int[] numbers) {
	    this.numbers = numbers;
	    nonRepeatSize = numbers.length;
		random = new Random(System.nanoTime());
	}

	public int size() {
	    return numbers.length;
	}

	public int nonRepeatSize() {
	    return nonRepeatSize;
	}

	public String toString() {
	    String str = new String();

	    for (int i=0; i<numbers.length; i++) {
	        str += "[";
	        str += numbers[i];
	        str += "]";
	    }
	    
	    return str;
	}

	public int nextInt() {
		return random.nextInt();
	}
	
	public int nextBoundedInt() {
		return numbers[random.nextInt(numbers.length)];
	}
	
	public int nextBoundedNonRepeatInt() {
	    if (nonRepeatSize == 0) {
	        nonRepeatSize = numbers.length;
	    }
	    
	    int index = random.nextInt(nonRepeatSize);
	    int temp = numbers[index];
	    numbers[index] = numbers[nonRepeatSize-1];
	    numbers[nonRepeatSize-1] = temp;
	    --nonRepeatSize;
	    return temp;
	}

	boolean hasNextNumber() {
	    return (nonRepeatSize > 0);
	}

	public void setSeed(long seed) {
	    random.setSeed(seed);
	}
}
