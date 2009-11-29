package solver.recurssive.arrayGeneration;

import java.util.Random;

public class RandomArray implements ArrayGeneratorStrategy {

	@Override
	public int[] generateArray(int size) {
		// Generates a array of all values from 1 to size in random order.
		//
		int[] vals = new int[size];
		int temp = 1;
		int spot = 1;
		Random gen = new Random();

		for (int i = 0; i < size; i++) {
			vals[i] = i + 1;
		}

		for (int i = 0; i < size; i++) {
			spot = gen.nextInt(size);
			temp = vals[spot];
			vals[spot] = vals[i];
			vals[i] = temp;
		}
		return vals;
	}

}
