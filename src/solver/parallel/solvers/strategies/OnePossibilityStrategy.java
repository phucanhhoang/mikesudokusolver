package solver.parallel.solvers.strategies;

import gui.Constants;
import solver.SudokuBoard;
import solver.parallel.solvers.strategies.semaphore.CountingSemaphore;

// TODO: Auto-generated Javadoc
/**
 * The Class OnePossibilityStrategy.
 */
public class OnePossibilityStrategy extends Strategy {

	/** The possible spots. */
	protected int[] possibleSpots;

	/** The row or column this strategy is attempting to use. */
	protected int spot;

	/** The true for row. */
	protected boolean trueForRow;

	/**
	 * Instantiates a new one possibility strategy.
	 */
	public OnePossibilityStrategy(CountingSemaphore instanceCounter) {
		super(instanceCounter);
	}

	/**
	 * Instantiates a new one possibility strategy.
	 * 
	 * @param spot
	 *            the spot
	 * @param board
	 *            the board
	 * @param possibleValues
	 *            the possible values
	 * @param trueForRow
	 *            the true for row
	 */
	public OnePossibilityStrategy(CountingSemaphore instanceCounter, int spot,
			SudokuBoard board, boolean[][][] possibleValues, boolean trueForRow) {
		super(instanceCounter, board, possibleValues);
		this.spot = spot;
		this.trueForRow = trueForRow;
	}

	/**
	 * Counts number of positions each value can put in this row or col
	 */
	private void countPossibleSpots() {
		for (int i = 0; i < board.getSize(); i++) {
			if (readBoard(trueForRow ? spot : i, trueForRow ? i : spot) == Constants.EMPTY_CELL) {
				for (int j = 0; j < board.getSize(); j++) {
					if (possibleValues[trueForRow ? spot : i][trueForRow ? i
							: spot][j]) {
						possibleSpots[j]++;
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see solver.parallel.strategies.Strategy#run()
	 */
	@Override
	public void runStrategy() {
		setupPossibleSpots();

		countPossibleSpots();

		updatePossibleSpots();
	}

	/**
	 * Setup possible spots.
	 */
	public void setupPossibleSpots() {
		possibleSpots = new int[board.getSize()];
		for (int i = 0; i < board.getSize(); i++) {
			possibleSpots[i] = 0;
		}
	}

	/**
	 * 
	 */
	private void updatePossibleSpots() {
		for (int j = 0; j < board.getSize(); j++) {
			if (possibleSpots[j] == 1) {
				for (int i = 0; i < board.getSize(); i++) {
					if (possibleValues[trueForRow ? spot : i][trueForRow ? i
							: spot][j]) {
						updateValue(trueForRow ? spot : i, trueForRow ? i
								: spot, j + 1);
						break;
					}
				}
				break;
			}
		}
	}
}
