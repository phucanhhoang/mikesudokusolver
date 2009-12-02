package solver.parallel.solvers.strategies;

import gui.Constants;
import solver.SudokuBoard;
import solver.parallel.solvers.strategies.semaphore.CountingSemaphore;

// TODO: Auto-generated Javadoc
/**
 * The Class Strategy.
 */
public abstract class Strategy implements Runnable {

	/** The changed. */
	protected static boolean changed = false;

	public static int getOffset(int val, int size) {
		return Math.round(val
				/ (int) Math.round(Math.sqrt(size))
				* Math.round(Math.sqrt(size)));
	}

	/**
	 * See changes.
	 * 
	 * @return true, if successful
	 */
	public static boolean seeChanges() {
		boolean temp = changed;
		changed = false;
		return temp;
	}

	/** The board. */
	protected SudokuBoard board;

	/** The possible values. */
	protected boolean[][][] possibleValues;

	/** The counter. */
	protected CountingSemaphore instanceCounter;

	public Strategy() {
		this(null);
	}

	/**
	 * Instantiates a new strategy.
	 */
	public Strategy(CountingSemaphore instanceCounter) {
		super();
		if (instanceCounter == null) {
			this.instanceCounter = new CountingSemaphore();
		} else {
			this.instanceCounter = instanceCounter;
		}
		instanceCounter.incrementCounter();
	}

	/**
	 * Instantiates a new strategy.
	 * 
	 * @param board2
	 *            the board
	 * @param possibleValues
	 *            the possible values
	 */
	public Strategy(CountingSemaphore instanceCounter, SudokuBoard board2,
			boolean[][][] possibleValues) {
		this(instanceCounter);
		setup(board2, possibleValues);
	}

	public int getCounterValue() {
		return instanceCounter.getValue();
	}

	protected int readBoard(int row, int col) {
		return board.getValueAt(row, col);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		runStrategy();
		instanceCounter.decrementCounter();
	}

	public abstract void runStrategy();

	/**
	 * Sets the changed.
	 */
	protected synchronized void setChanged() {
		if (!changed) {
			changed = true;
		}
	}

	/**
	 * Setup.
	 * 
	 * @param board2
	 *            the board
	 * @param possibleValues
	 *            the possible values
	 */
	public void setup(SudokuBoard board2, boolean[][][] possibleValues) {
		board = board2;
		this.possibleValues = possibleValues;
	}

	/**
	 * Update board.
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the col
	 * @param val
	 *            the val
	 */
	private synchronized void updateBoard(int row, int col, int val) {
		board.setValueAt(row, col, val);
	}

	private void updateCellPossibleValues(int row, int col) {
		for (int i = 0; i < board.getSize(); i++) {
			updatePossibleValue(row, col, i);
		}
	}

	/**
	 * Update col possible values.
	 * 
	 * @param col
	 *            the col
	 * @param val
	 *            the val
	 */
	private void updateColPossibleValues(int col, int val) {
		for (int i = 0; i < board.getSize(); i++) {
			if (possibleValues[i][col][val]) {
				updatePossibleValue(i, col, val);
			}
		}
	}

	/**
	 * Update possible value.
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the col
	 * @param val
	 *            the val
	 */
	private synchronized void updatePossibleValue(int row, int col, int val) {
		possibleValues[row][col][val] = false;
	}

	/**
	 * Update possible values.
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the col
	 * @param val
	 *            the val
	 */
	protected void updatePossibleValues(int row, int col, int val) {
		updateCellPossibleValues(row, col);
		updateRowPossibleValues(row, val);
		updateColPossibleValues(col, val);
		updateSquarePossibleValues(row, col, val);
	}

	/**
	 * Update row possible values.
	 * 
	 * @param row
	 *            the row
	 * @param val
	 *            the val
	 */
	private void updateRowPossibleValues(int row, int val) {
		for (int i = 0; i < board.getSize(); i++) {
			if (possibleValues[row][i][val]) {
				updatePossibleValue(row, i, val);
			}
		}
	}

	/**
	 * Update square possible values.
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the col
	 * @param val
	 *            the val
	 */
	private void updateSquarePossibleValues(int row, int col, int val) {

		// Set beginning of square this cell is in.
		//
		int rowOffset = getOffset(row, board.getSize());
		int colOffset = getOffset(col, board.getSize());

		// Check square for repeated number.
		//
		for (int k = 0; k < Math.sqrt(board.getSize()); k++) {
			for (int m = 0; m < Math.sqrt(board.getSize()); m++) {
				if (possibleValues[k + rowOffset][m + colOffset][val]) {
					updatePossibleValue(k + rowOffset, m + colOffset, val);
				}
			}
		}
	}

	/**
	 * Update value.
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the col
	 * @param val
	 *            the val
	 */
	public void updateValue(int row, int col, int val) {
		// System.out.println("UPDATING VALUE");

		updatePossibleValues(row, col, val - 1);
		updateBoard(row, col, val);
		setChanged();
	}
}
