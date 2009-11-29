package solver.arrayGeneration;

public class ForwardArray implements ArrayGeneratorStrategy {

	@Override
	public int[] generateArray(int size) {
		// Generates a array of all values from 1 to size in random order.
        //
        int[] vals = new int[size];
        
        for (int i = 0; i < size; i++)
            vals[i] = i+1;
        
        return vals;		
	}

}
