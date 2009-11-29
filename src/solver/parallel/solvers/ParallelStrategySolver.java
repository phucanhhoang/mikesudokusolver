package solver.parallel.solvers;

import gui.Constants;

import java.util.concurrent.ThreadPoolExecutor;

import solver.SudokuBoard;
import solver.parallel.solvers.strategies.FindPossibleValuesStrategy;
import solver.parallel.solvers.strategies.OnePossibilitySquareStrategy;
import solver.parallel.solvers.strategies.OnePossibilityStrategy;
import solver.parallel.solvers.strategies.SingleValueForCellStrategy;
import solver.parallel.solvers.strategies.Strategy;
import solver.parallel.solvers.strategies.semaphore.CountingSemaphore;

public class ParallelStrategySolver {

	private SudokuBoard board;
	/** The possible values. */
	private boolean possibleValues[][][];

	public ParallelStrategySolver(SudokuBoard board2) {
		board = board2;
	}

	public int[] getPossibleValuesCell(int row, int col) {
		int values[] = new int[Constants.BOARD_SIZE];
		int counter = 0;
		if (board.getValueAt(row, col) == Constants.EMPTY_CELL) {
			for (int i = 0; i < Constants.BOARD_SIZE; i++) {
				if (possibleValues[row][col][i]) {
					values[counter++] = i + 1;
				}
			}
		}

		return values;
	}

	public int getValueOfLeastPossibleValuesCell() {
		int minValues = Constants.BOARD_SIZE + 1;
		int cellValues;
		int row = minValues;
		int col = minValues;

		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				if (board.getValueAt(i, j) == Constants.EMPTY_CELL) {
					cellValues = 0;
					for (int k = 0; k < Constants.BOARD_SIZE; k++) {
						if (possibleValues[i][j][k]) {
							cellValues++;
						}
					}
					if (cellValues < minValues) {
						minValues = cellValues;
						row = i;
						col = j;
					}
				}
			}
		}

		if (row != Constants.BOARD_SIZE + 1 && col != Constants.BOARD_SIZE + 1) {
			System.out.println("Found cell at: " + row + " , " + col);
			return row + Constants.BOARD_SIZE * col;
		} else {
			return (Constants.BOARD_SIZE + 1) * (Constants.BOARD_SIZE + 1);
		}
	}

	/**
	 * 
	 */
	public void initializePossibleValues() {
		possibleValues = new boolean[Constants.BOARD_SIZE][Constants.BOARD_SIZE][Constants.BOARD_SIZE];

		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				boolean value = board.getValueAt(i, j) == Constants.EMPTY_CELL;
				for (int k = 0; k < Constants.BOARD_SIZE; k++) {
					possibleValues[i][j][k] = value;
				}
			}
		}
	}

	private void loopThroughStrategies(CountingSemaphore counter,
			ThreadPoolExecutor threadPool) {
		// ----------------------------
		// Solve squares with single values
		// ----------------------------
		// Get current value of the board.
		do {

			startSingleValueForCellThreads(counter, threadPool);

			startRowAndColumnAndSquareThreads(counter, threadPool);

			counter.waitForThreadsToFinish();

		} while (Strategy.seeChanges());
	}

	/**
	 * Sets the up possible values.
	 * 
	 * @param threadPool
	 *            the new up possible values
	 */
	public void setupPossibleValues(CountingSemaphore counter,
			ThreadPoolExecutor threadPool) {

		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				if (board.getValueAt(i, j) != Constants.EMPTY_CELL) {
					Strategy temp = new FindPossibleValuesStrategy(counter, i,
							j, board, possibleValues);
					startThread(temp, threadPool);
				}
			}
		}
		// System.out.println("Should equal number of squares filled :" +
		// counter);
		counter.waitForThreadsToFinish();
	}

	public void solve(CountingSemaphore counter, ThreadPoolExecutor threadPool) {

		if (counter == null) {
			counter = new CountingSemaphore();
		}
		initializePossibleValues();

		setupPossibleValues(counter, threadPool);

		loopThroughStrategies(counter, threadPool);
	}

	public void solve(ThreadPoolExecutor threadPool) {
		solve(null, threadPool);
	}

	private void startColThread(CountingSemaphore counter, int col,
			ThreadPoolExecutor threadPool) {
		startThread(new OnePossibilityStrategy(counter, col, board,
				possibleValues, false), threadPool);
	}

	private void startRowAndColumnAndSquareThreads(CountingSemaphore counter,
			ThreadPoolExecutor threadPool) {
		for (int m = 0; m < Constants.BOARD_SIZE; m++) {
			startRowThread(counter, m, threadPool);

			startColThread(counter, m, threadPool);

			startSquareThread(counter, m, threadPool);
		}

	}

	private void startRowThread(CountingSemaphore counter, int row,
			ThreadPoolExecutor threadPool) {
		startThread(new OnePossibilityStrategy(counter, row, board,
				possibleValues, true), threadPool);
	}

	private void startSingleValueForCellThreads(CountingSemaphore counter,
			ThreadPoolExecutor threadPool) {
		// Create the thread pool.
		for (int m = 0; m < Constants.BOARD_SIZE; m++) {
			for (int n = 0; n < Constants.BOARD_SIZE; n++) {
				if (board.getValueAt(m, n) == Constants.EMPTY_CELL) {
					try {
						// new Thread(new SingleValueForCellStrategy(m, n,
						// board, possibleValues)).start();
						Strategy temp = new SingleValueForCellStrategy(counter,
								m, n, board, possibleValues);
						startThread(temp, threadPool);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.err.println("CLASS NOT FOUND");
						System.exit(0);
					}
				}
			}
		}
		// System.out.println("Should equal number of empty spaces: " +
		// counter);
	}

	private void startSquareThread(CountingSemaphore counter, int square,
			ThreadPoolExecutor threadPool) {
		startThread(new OnePossibilitySquareStrategy(counter, square, board,
				possibleValues), threadPool);
	}

	private void startThread(Strategy temp, ThreadPoolExecutor threadPool) {
		if (threadPool != null) {
			threadPool.execute(temp);
		} else {
			temp.run();
		}
	}
}
