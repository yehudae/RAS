package com.rafael.sdk.util;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class Randomizer.
 */
public class Randomizer {

	/** The numbers. */
	private int[] numbers = null;
	
	/** The random. */
	private Random random = null;
	
	/** The non repeat size. */
	private int nonRepeatSize;
	
	/**
	 * Instantiates a new randomizer.
	 *
	 * @param size the size
	 */
	public Randomizer(int size) {
	    numbers = new int[size];
	    
		for (int i=0; i<size; i++) {
	    	numbers[i] = i;
	    }
		
		nonRepeatSize = size;
		random = new Random(System.nanoTime());
	}

	/**
	 * Instantiates a new randomizer.
	 *
	 * @param numbers the numbers
	 */
	public Randomizer(int[] numbers) {
	    this.numbers = numbers;
	    nonRepeatSize = numbers.length;
		random = new Random(System.nanoTime());
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
	    return numbers.length;
	}

	/**
	 * Non repeat size.
	 *
	 * @return the int
	 */
	public int nonRepeatSize() {
	    return nonRepeatSize;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
	    String str = new String();

	    for (int i=0; i<numbers.length; i++) {
	        str += "[";
	        str += numbers[i];
	        str += "]";
	    }
	    
	    return str;
	}

	/**
	 * Next int.
	 *
	 * @return the int
	 */
	public int nextInt() {
		return random.nextInt();
	}
	
	/**
	 * Next bounded int.
	 *
	 * @return the int
	 */
	public int nextBoundedInt() {
		return numbers[random.nextInt(numbers.length)];
	}
	
	/**
	 * Next bounded non repeat int.
	 *
	 * @return the int
	 */
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

	/**
	 * Checks for next number.
	 *
	 * @return true, if successful
	 */
	boolean hasNextNumber() {
	    return (nonRepeatSize > 0);
	}

	/**
	 * Sets the seed.
	 *
	 * @param seed the new seed
	 */
	public void setSeed(long seed) {
	    random.setSeed(seed);
	}
}
