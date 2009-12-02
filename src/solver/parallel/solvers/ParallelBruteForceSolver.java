package solver.parallel.solvers;

import gui.Constants;

import java.util.concurrent.ThreadPoolExecutor;

import solver.Helper;
import solver.SudokuBoard;
import solver.parallel.solvers.strategies.semaphore.CountingSemaphore;

// TODO: Auto-generated Javadoc
/**
 * The Class ParallelBruteForceSolver.
 */
public class ParallelBruteForceSolver implements Runnable {

	/** The board. */
	private SudokuBoard board;
	
	/** The solved board. */
	private static SudokuBoard solvedBoard;
	
	/** The board solved. */
	private static boolean boardSolved = false;
	
	/** The start row. */
	private int startRow;
	
	/** The start col. */
	private int startCol;
	
	/** The counter. */
	private CountingSemaphore counter;
	
	/** The thread pool. */
	ThreadPoolExecutor threadPool;
	
	/** The not been in run. */
	private boolean notBeenInRun = false;

	/**
	 * Instantiates a new parallel brute force solver.
	 */
	public ParallelBruteForceSolver() {
		super();
		counter = new CountingSemaphore();
	}

	/**
	 * Instantiates a new parallel brute force solver.
	 * 
	 * @param board2 the board2
	 * @param threadPool the thread pool
	 * @param counter the counter
	 * @param startRow the start row
	 * @param startCol the start col
	 */
	public ParallelBruteForceSolver(SudokuBoard board2,
			ThreadPoolExecutor threadPool, CountingSemaphore counter,
			int startRow, int startCol) {
		// Initialize global variables.
		board = board2;
		this.threadPool = threadPool;
		this.counter = counter;
		this.startRow = startRow;
		this.startCol = startCol;
		counter.incrementCounter();
		notBeenInRun = true;
	}

	/**
	 * Clear solved.
	 */
	public synchronized static void clearSolved() {
		boardSolved = false;
		solvedBoard = null;
	}

	/**
	 * Decrement counter if first layer of run.
	 * 
	 * @param firstLayerOfRun the first layer of run
	 */
	private void decrementCounterIfFirstLayerOfRun(boolean firstLayerOfRun) {
		if (firstLayerOfRun) {
			counter.decrementCounter();
			firstLayerOfRun = false;
		}
	}

	/**
	 * Find next empty cell.
	 * 
	 * @return true, if successful
	 */
	private boolean findNextEmptyCell() {
		int row = startRow;
		int col = startCol;

		while (board.getValueAt(row, col) != Constants.EMPTY_CELL) {
			if (row == Constants.BOARD_SIZE - 1) {
				row = 0;

				if (++col == Constants.BOARD_SIZE) {
					return false;
				}
			} else {
				row++;
			}
		}
		startRow = row;
		startCol = col;
		return true;
	}

	/**
	 * Gets the solved board.
	 * 
	 * @return the solved board
	 */
	public SudokuBoard getSolvedBoard() {
		if (boardSolved) {
			return solvedBoard;
		} else {
			return null;
		}
	}

	/** 
	 * Only decrements counter on lowest layer of this method (It runs recurssively). 
	 * Then it checks if the board is solved before running the strategies to solve
	 * the board. If the strategies worked or there are no more empty cells it returns
	 * failure. Then it runs the bruteForceSolving method to break up ths board into all
	 * possible boards for the parallel solver to attempt again.
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		boolean firstLayerOfRun = false;

		/*
		 * If this is the first thim through this method in the
		 */
		if (notBeenInRun) {
			firstLayerOfRun = true;
			notBeenInRun = false;
		}

		if (ParallelBruteForceSolver.boardSolved) {
			decrementCounterIfFirstLayerOfRun(firstLayerOfRun);
			return;
		} else if (board.isSolved()) {
			setSolvedBoard(board);
			decrementCounterIfFirstLayerOfRun(firstLayerOfRun);
			return;
		}

		ParallelStrategySolver solver = new ParallelStrategySolver(board);
		solver.solve(null);

		if (board.isSolved() || boardSolved) {
			setSolvedBoard(board);
			decrementCounterIfFirstLayerOfRun(firstLayerOfRun);
			return;
		} else if (!findNextEmptyCell()) {
			decrementCounterIfFirstLayerOfRun(firstLayerOfRun);
			return;
		}

		startBruteForceSolver(threadPool, solver);
		decrementCounterIfFirstLayerOfRun(firstLayerOfRun);
	}

	/**
	 * Sets the solved board.
	 * 
	 * @param board2 the new solved board
	 */
	private synchronized void setSolvedBoard(SudokuBoard board2) {
		if (boardSolved) {
			return;
		}
		System.out.println("BOARD SOLVED");
		boardSolved = true;
		solvedBoard = new SudokuBoard(Helper.copyCells(board2.getBoard()));
	}

	/**
	 * Start brute force solver. Only uses the possible entries for the next possible
	 * cell, traverses board breadth first if there are available spots for threads or
	 * depth first if the threadPool is full.
	 * 
	 * @param threadPool the thread pool
	 * @param solver the solver
	 */
	private void startBruteForceSolver(ThreadPoolExecutor threadPool,
			ParallelStrategySolver solver) {
		SudokuBoard tempBoard;
		SudokuBoard boardSave;
		int tempStartRow;
		int tempStartCol;
		for (int j : solver.getPossibleValuesCell(startRow, startCol)) {
		 System.out.println("Setting (" + startRow + " , " + startCol +
		 ") to " + j);
			if (j == Constants.EMPTY_CELL || boardSolved) {
				break;
			}

			tempBoard = board.newChangedValue(startRow, startCol, j);

			if (!startThread(tempBoard)) {
				boardSave = board;
				board = tempBoard;
				tempStartRow = startRow;
				tempStartCol = startCol;
				run();
				if (board.isSolved()) {
					setSolvedBoard(board);
				}
				board = boardSave;
				startRow = tempStartRow;
				startCol = tempStartCol;
				boardSave = null;
				tempBoard = null;
			}
		}
	}

	/**
	 * Start thread.
	 * 
	 * @param tempBoard the temp board
	 * 
	 * @return true, if successful
	 */
	private synchronized boolean startThread(SudokuBoard tempBoard) {

		if (counter.getValue() < Constants.NUM_THREADS) {
			ParallelBruteForceSolver bruteSolver;
			bruteSolver = new ParallelBruteForceSolver(tempBoard, threadPool,
					counter, startRow, startCol);
			// new Thread(bruteSolver).start();
			threadPool.execute(bruteSolver);
			// bruteSolver.run();
			return true;
		}
		return false;

	}
}
