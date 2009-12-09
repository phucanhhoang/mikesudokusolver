/*
 * 
 */
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

// TODO: Auto-generated Javadoc
/**
 * The Class ParallelStrategySolver.
 */
public class ParallelStrategySolver {

	/** The board. */
	private SudokuBoard board;
	
	/** The possible values. */
	private boolean possibleValues[][][];

	/**
	 * Instantiates a new parallel strategy solver.
	 * 
	 * @param board2 the board2
	 */
	public ParallelStrategySolver(SudokuBoard board2) {
		board = board2;
	}

	/**
	 * Gets an array of possible values for the cell entered. Returns zero after all possibilities have been saved.
	 * 
	 * @param row the row
	 * @param col the col
	 * 
	 * @return the possible values cell
	 */
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

	/**
	 * Gets the value of least possible values cell.
	 * 
	 * @return the value of least possible values cell
	 */
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
	 * Initialize possible values.
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

	/**
	 * Loop through strategies.
	 * 
	 * @param counter the counter
	 * @param threadPool the thread pool
	 */
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
	 * @param threadPool the new up possible values
	 * @param counter the counter
	 */
	public void setupPossibleValues(CountingSemaphore counter,
			ThreadPoolExecutor threadPool) {

		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				if (board.getValueAt(i, j) != Constants.EMPTY_CELL) {
					Strategy temp = new FindPossibleValuesStrategy(counter, i,
							j, board, possibleValues);
					startThread(temp, threadPool, counter);
				}
			}
		}
		// System.out.println("Should equal number of squares filled :" +
		// counter);
		counter.waitForThreadsToFinish();
	}

	/**
	 * Solve.
	 * 
	 * @param counter the counter
	 * @param threadPool the thread pool
	 */
	public void solve(CountingSemaphore counter, ThreadPoolExecutor threadPool) {

		if (counter == null) {
			counter = new CountingSemaphore();
		}
		initializePossibleValues();

		setupPossibleValues(counter, threadPool);

		loopThroughStrategies(counter, threadPool);
	}

	/**
	 * Solve.
	 * 
	 * @param threadPool the thread pool
	 */
	public void solve(ThreadPoolExecutor threadPool) {
		solve(null, threadPool);
	}

	/**
	 * Start col thread.
	 * 
	 * @param counter the counter
	 * @param col the col
	 * @param threadPool the thread pool
	 */
	private void startColThread(CountingSemaphore counter, int col,
			ThreadPoolExecutor threadPool) {
		startThread(new OnePossibilityStrategy(counter, col, board,
				possibleValues, false), threadPool, counter);
	}

	/**
	 * Start row and column and square threads.
	 * 
	 * @param counter the counter
	 * @param threadPool the thread pool
	 */
	private void startRowAndColumnAndSquareThreads(CountingSemaphore counter,
			ThreadPoolExecutor threadPool) {
		for (int m = 0; m < Constants.BOARD_SIZE; m++) {
			startRowThread(counter, m, threadPool);

			startColThread(counter, m, threadPool);

			startSquareThread(counter, m, threadPool);
		}

	}

	/**
	 * Start row thread.
	 * 
	 * @param counter the counter
	 * @param row the row
	 * @param threadPool the thread pool
	 */
	private void startRowThread(CountingSemaphore counter, int row,
			ThreadPoolExecutor threadPool) {
		startThread(new OnePossibilityStrategy(counter, row, board,
				possibleValues, true), threadPool, counter);
	}

	/**
	 * Start single value for cell threads.
	 * 
	 * @param counter the counter
	 * @param threadPool the thread pool
	 */
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
						startThread(temp, threadPool, counter);
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

	/**
	 * Start square thread.
	 * 
	 * @param counter the counter
	 * @param square the square
	 * @param threadPool the thread pool
	 */
	private void startSquareThread(CountingSemaphore counter, int square,
			ThreadPoolExecutor threadPool) {
		startThread(new OnePossibilitySquareStrategy(counter, square, board,
				possibleValues), threadPool, counter);
	}

	/**
	 * Start thread.
	 * 
	 * @param temp the temp
	 * @param threadPool the thread pool
	 */
	private void startThread(Strategy temp, ThreadPoolExecutor threadPool, CountingSemaphore counter) {
		if (threadPool != null && counter.getValue() < Constants.NUM_BRANCHES) {
			threadPool.execute(temp);
		} else if (counter.getValue() < Constants.NUM_BRANCHES){
			new Thread(temp).start();
		} else
			temp.run();
	}
}
