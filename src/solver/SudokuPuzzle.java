package solver;

import gui.Constants;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * The Class SudokuHandler.
 */
public abstract class SudokuPuzzle {

	/** The board. */
	protected SudokuBoard board;

	/** The original board. */
	protected int[][] originalBoard;

	/**
	 * Instantiates a new sudoku handler.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public SudokuPuzzle() throws InterruptedException {
		generateBoard();
		originalBoard = Helper.copyCells(board.getBoard());
	}

	/**
	 * Create a new sudoku solver instance.
	 * 
	 * @param board
	 *            the board
	 */
	public SudokuPuzzle(int[][] board) {
		this.board = new SudokuBoard(board);
		originalBoard = Helper.copyCells(getBoard());
	}

	public SudokuPuzzle(SudokuBoard board) {
		this.board = board;
		originalBoard = Helper.copyCells(getBoard());
	}

	/**
	 * Deletes this instance of the board.
	 * 
	 * @throws Throwable
	 *             the throwable
	 */
	public void deleteInstance() throws Throwable {
		board = null;
		originalBoard = null;
		super.finalize();
	}

	/**
	 * Generate board by initializing a random board and solving it.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public void generateBoard() throws InterruptedException {
		initializeBoard(Constants.BOARD_SIZE);

		removeSquares(Constants.GEN_ATTEMPTS,Constants.BOARD_SIZE);
	}

	/**
	 * Gets the board.
	 * 
	 * @return the board
	 */
	public int[][] getBoard() {
		return board.getBoard();
	}

	/**
	 * Gets the original board.
	 * 
	 * @return the original board
	 */
	public int[][] getOriginalBoard() {
		return originalBoard;
	}

	/**
	 * Initializes a solved sudoku board that is random.
	 * 
	 * @throws InterruptedException
	 */
	private void initializeBoard(int size) throws InterruptedException {
		board = new SudokuBoard(size);

		if (solveRandom()) {
			System.out.println("board generated");
		}
	}

	public boolean isLegalMove(int lastClickedi, int lastClickedj, int num) {
		return board.legalMove(lastClickedi, lastClickedj, num);
	}

	/**
	 * @param gen
	 * @param missAttempts
	 * @return
	 * @throws InterruptedException
	 */
	private void removeSquares(int missAttempts, int size) throws InterruptedException {
		int lastX;
		int lastY;
		int lastVal;
		Random gen = new Random();
		// Randomly remove squares until non-unique solution is found.
		//
		do {
			lastX = gen.nextInt(size);
			lastY = gen.nextInt(size);
			lastVal = board.getValueAt(lastX, lastY);

			board.setValueAt(lastX, lastY, Constants.EMPTY_CELL);

			if (!uniqueSolution()) {
				missAttempts--;
				// Undo last remove.
				//
				board.setValueAt(lastX, lastY, lastVal);
			}
		} while (missAttempts > 0);
	}

	public void resetBoard() {
		board = new SudokuBoard(originalBoard);
	}

	/**
	 * Sets the board spot.
	 * 
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @param value
	 *            the value
	 */
	public void setBoardSpot(int i, int j, int value) {
		board.updateBoard(i, j, value);
	}

	/**
	 * Sets the original board.
	 * 
	 * @param originalBoard
	 *            the new original board
	 */
	public void setOriginalBoard(int[][] originalBoard) {
		this.originalBoard = originalBoard;
	}

	/**
	 * Solve.
	 * 
	 * @return true, if successful
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public abstract boolean solve() throws InterruptedException;

	/**
	 * Solve in opposite way to ensure that there is only a single solution when
	 * generating the sudoku.
	 * 
	 * @return true, if solve backwards
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public abstract boolean solveBackwards() throws InterruptedException;

	/**
	 * Solve in random way.
	 * 
	 * @return true, if solve random
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public abstract boolean solveRandom() throws InterruptedException;

	/**
	 * Unique solution to this sudoku.
	 * 
	 * @return true, if successful
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public boolean uniqueSolution() throws InterruptedException {
		SudokuBoard theBoard = board;
		// In order to solve the sudoku override the board in this class with a
		// temp value.
		//
		SudokuBoard board1 = new SudokuBoard(Helper.copyCells(theBoard
				.getBoard()));
		board = board1;
		boolean forwardSolved = solve();

		SudokuBoard board2 = new SudokuBoard(Helper.copyCells(theBoard
				.getBoard()));
		board = board2;
		boolean backwardSolved = solveBackwards();

		board = theBoard;

		// In order for the board to have a unique solution it must be solvable
		// in opposite ways to have one solution.
		//
		if (forwardSolved && backwardSolved) {
			return SudokuBoard.equalBoards(board1, board2);
		} else {
			return false;
		}
	}
}