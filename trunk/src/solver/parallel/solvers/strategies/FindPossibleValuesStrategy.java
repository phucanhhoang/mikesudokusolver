package solver.parallel.solvers.strategies;

import solver.SudokuBoard;
import solver.parallel.solvers.strategies.semaphore.CountingSemaphore;

// TODO: Auto-generated Javadoc
/**
 * The Class FindPossibleValuesStrategy.
 */
public class FindPossibleValuesStrategy extends Strategy{
	
	/** The row. */
	int row;
	
	/** The col. */
	int col;

	/**
	 * Instantiates a new find possible values strategy.
	 */
	public FindPossibleValuesStrategy(CountingSemaphore instanceCounter) {
		super(instanceCounter);
	}

	/**
	 * Instantiates a new find possible values strategy.
	 * 
	 * @param row the row
	 * @param col the col
	 * @param board the board
	 * @param possibleValues the possible values
	 */
	public FindPossibleValuesStrategy(CountingSemaphore instanceCounter, int row, int col, SudokuBoard board,
			boolean[][][] possibleValues) {
		super(instanceCounter, board, possibleValues);
		this.row = row;
		this.col = col;
	}

	/* (non-Javadoc)
	 * @see solver.parallel.strategies.Strategy#run()
	 */
	@Override
	public void runStrategy() {
		updatePossibleValues(row, col, readBoard(row,col) - 1);
	}

}
