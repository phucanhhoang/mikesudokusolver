package solver.recurssive.arrayGeneration;

public class BackwardArray implements ArrayGeneratorStrategy {

	@Override
	public int[] generateArray(int size) {
		// Generates a array of all values from 1 to size in random order.
		//
		int[] vals = new int[size];

		for (int i = 0; i < size; i++) {
			vals[i] = size - i;
		}

		return vals;
	}

}
