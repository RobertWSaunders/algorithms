import java.util.*;

// Robert Saunders
// 10194030

public class Collatz {

	/**
	 * Initialize a list to store the collatz values.
	 */
	static int[] list = new int[10000];

	/**
	 * Number to be run through the algorithm.
	 */
	static int num = 20;

	/**
	 * Main execution method.
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i = num; i >= 1; i--) {
			collatzArray(i);
		}
	}

	/**
	 * Implementation of collatz algorithm that uses previous values.
	 * @param n The number to run collatz on.
	 */
	public static void collatzArray(int n) {
		int start = n;
		int counter = 0;

		while (n != 1) {
			if (list[n] == 1) {
				break;
			} else {
				list[n] = 1;
			}
			if (n % 2 == 0) {
				n = n/2;
			}
			else {
				n = 3*n+1;
			}
			counter++;
		}

		System.out.println("Starting value: " + start + " Loop Ended at: " + n + " Steps Taken: " + counter + " Normal Method Steps Taken: " + collatz(start));
	}

	/**
	 * Runs the collatz algorithm.
	 * @param n The number to run the algorithm on.
	 */
	public static int collatz(int n) {
		if (n == 1) {
			return 0;
		} else if (n % 2 == 0) {
			return 1 + collatz(n / 2);
		} else {
			return 1 + collatz(n * 3 + 1);
		}
	}
}