package solver.parallel.solvers.strategies;

import gui.Constants;
import solver.SudokuBoard;
import solver.parallel.solvers.strategies.semaphore.CountingSemaphore;

// TODO: Auto-generated Javadoc
/**
 * The Class SinglePossibleValueStrategy.
 */
public class SingleValueForCellStrategy extends Strategy {

	/** The row. */
	protected int row;

	/** The col. */
	protected int col;

	/**
	 * Instantiates a new single possible value strategy.
	 */
	public SingleValueForCellStrategy(CountingSemaphore instanceCounter) {
		super(instanceCounter);
	}

	/**
	 * Instantiates a new single possible value strategy.
	 * 
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @param board
	 *            the board
	 * @param possibleValues
	 *            the possible values
	 */
	public SingleValueForCellStrategy(CountingSemaphore instanceCounter, int i,
			int j, SudokuBoard board, boolean[][][] possibleValues) {
		super(instanceCounter, board, possibleValues);
		row = i;
		col = j;
	}

	/**
	 * Saves first possible value in the integer and breaks if second possible
	 * value is found with 0 as return value.
	 * 
	 * @return the only possible value for this cell
	 */
	private int getOnePossibleValue() {
		int possibleValue = 0;

		for (int i = 0; i < board.getSize(); i++) {
			if (possibleValue != 0 && possibleValues[row][col][i] == true) {
				possibleValue = 0;
				break;
			} else if (possibleValue == 0
					&& possibleValues[row][col][i] == true) {
				possibleValue = i + 1;
			}
		}
		return possibleValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see solver.parallel.strategies.Strategy#run()
	 */
	@Override
	public void runStrategy() {

		// Determine if there is only one possible value.
		int possibleValue = getOnePossibleValue();

		if (possibleValue != 0) {
			updateValue(row, col, possibleValue);
		}

		// updateBoardPossibleValues(possibleValues);
	}
}
