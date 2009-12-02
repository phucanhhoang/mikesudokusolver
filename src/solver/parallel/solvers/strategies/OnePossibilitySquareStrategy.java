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
		rowOffset = (int) Math.round(Math.sqrt(board.getSize()))
				* (square % (int) Math.round(Math.sqrt(board.getSize())));
		colOffset = (int) Math.round(Math.sqrt(board.getSize()))
				* (square / (int) Math.round(Math.sqrt(board.getSize())));
	}

	private void countPossibleSpots() {
		for (int m = 0; m < Math.sqrt(board.getSize()); m++) {
			for (int n = 0; n < Math.sqrt(board.getSize()); n++) {
				if (readBoard(m + rowOffset, n + colOffset) == Constants.EMPTY_CELL) {
					for (int j = 0; j < board.getSize(); j++) {
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
				for (int m = 0; m < Math.sqrt(board.getSize()); m++) {
					for (int n = 0; n < Math.sqrt(board.getSize()); n++) {
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
