package solver.parallel.solvers.strategies;

import gui.Constants;
import solver.SudokuBoard;
import solver.parallel.solvers.strategies.semaphore.CountingSemaphore;

public class OnePossibilitySquareStrategy extends Strategy {

	private int rowOffset;
	private int colOffset;
	private int[] possibleSpots;

	public OnePossibilitySquareStrategy(CountingSemaphore instanceCounter) {
		super(instanceCounter);
	}

	public OnePossibilitySquareStrategy(CountingSemaphore instanceCounter,
			int square, SudokuBoard board, boolean[][][] possibleValues) {
		super(instanceCounter);
		setup(board, possibleValues);
		rowOffset = (int) Math.round(Math.sqrt(Constants.BOARD_SIZE))
				* (square % (int) Math.round(Math.sqrt(Constants.BOARD_SIZE)));
		colOffset = (int) Math.round(Math.sqrt(Constants.BOARD_SIZE))
				* (square / (int) Math.round(Math.sqrt(Constants.BOARD_SIZE)));
	}

	private void countPossibleSpots() {
		for (int m = 0; m < Math.sqrt(Constants.BOARD_SIZE); m++) {
			for (int n = 0; n < Math.sqrt(Constants.BOARD_SIZE); n++) {
				if (readBoard(m + rowOffset, n + colOffset) == Constants.EMPTY_CELL) {
					for (int j = 0; j < Constants.BOARD_SIZE; j++) {
						if (possibleValues[m + rowOffset][n + colOffset][j]) {
							possibleSpots[j]++;
						}
					}
				}
			}
		}
	}

	@Override
	public void runStrategy() {

		setupPossibleSpots();

		countPossibleSpots();

		updatePossibleSpots();
	}

	private void setupPossibleSpots() {
		possibleSpots = new int[Constants.BOARD_SIZE];
		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			possibleSpots[i] = 0;
		}
	}

	/**
	 * 
	 */
	private void updatePossibleSpots() {
		for (int j = 0; j < Constants.BOARD_SIZE; j++) {
			if (possibleSpots[j] == 1) {
				for (int m = 0; m < Math.sqrt(Constants.BOARD_SIZE); m++) {
					for (int n = 0; n < Math.sqrt(Constants.BOARD_SIZE); n++) {
						if (possibleValues[m + rowOffset][n + colOffset][j]) {
							updateValue(m + rowOffset, n + colOffset, j + 1);
							break;
						}
					}
					break;
				}
			}
		}
	}
}
