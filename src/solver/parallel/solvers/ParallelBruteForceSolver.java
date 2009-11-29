package solver.parallel.solvers;

import gui.Constants;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import solver.Helper;
import solver.SudokuBoard;
import solver.parallel.solvers.strategies.semaphore.CountingSemaphore;

public class ParallelBruteForceSolver implements Runnable{
	
	private SudokuBoard board;
	private static SudokuBoard solvedBoard;
	private static boolean boardSolved = false;
	private int startRow;
	private int startCol;
	private CountingSemaphore counter;
	ThreadPoolExecutor threadPool;
	private boolean notBeenInRun = false;
	
	public ParallelBruteForceSolver() {
		super();
		counter = new CountingSemaphore();
	}
	
	public ParallelBruteForceSolver(SudokuBoard board2, ThreadPoolExecutor threadPool, CountingSemaphore counter, int startRow, int startCol) {
		this.board = board2;
		this.threadPool = threadPool;
		this.counter = counter;
		this.startRow = startRow;
		this.startCol = startCol;
		counter.incrementCounter();
		notBeenInRun = true;
	}

	@Override
	public void run() {
		boolean firstLayerOfRun = false;
		
		if (notBeenInRun) {
			firstLayerOfRun = true;
			notBeenInRun = false;
		}
		
		if (ParallelBruteForceSolver.boardSolved) {
			decrementCounterIfFirstLayerOfRun(firstLayerOfRun);
			return;
		}
		else if (board.isSolved()) {
			setSolvedBoard(board);
			decrementCounterIfFirstLayerOfRun(firstLayerOfRun);
			return;
		}
		
		ParallelStrategySolver solver = new ParallelStrategySolver(board);
		solver.solve(threadPool);
		
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
	 * @param firstLayerOfRun
	 */
	private void decrementCounterIfFirstLayerOfRun(boolean firstLayerOfRun) {
		if (firstLayerOfRun) {
			counter.decrementCounter();
			firstLayerOfRun = false;
		}
	}
	

	private boolean findNextEmptyCell() {
		int row = startRow;
		int col = startCol;
		
		while (board.getValueAt(row,col) != Constants.EMPTY_CELL) {
			if (row == Constants.BOARD_SIZE - 1) {
				row = 0;
				
				if (++col == Constants.BOARD_SIZE) 
					return false;
			} else {
				row++;
			}
		}
		startRow = row;
		startCol = col;
		return true;
	}

	private synchronized void setSolvedBoard(SudokuBoard board2) {
		if (boardSolved) {
			return;
		}
		System.out.println("BOARD SOLVED");
		boardSolved = true;
		solvedBoard = new SudokuBoard(Helper.copyCells(board2.getBoard()));
	}

	/**
	 * @param threadPool
	 * @param solver
	 */
	private void startBruteForceSolver(ThreadPoolExecutor threadPool,
			ParallelStrategySolver solver) {
		SudokuBoard tempBoard;
		SudokuBoard boardSave;
		int tempStartRow;
		int tempStartCol;
		for (int j:solver.getPossibleValuesCell(startRow, startCol)) {
			//System.out.println("Setting (" + startRow + " , " + startCol + ") to " + j);
			if (j == Constants.EMPTY_CELL || boardSolved) {
				break;
			}
			
			tempBoard = board.newChangedValue(startRow,startCol,j);

			if (!startThread(tempBoard)) {
				boardSave = board;
				this.board = tempBoard;
				tempStartRow = startRow;
				tempStartCol = startCol;
				run();
				if (board.isSolved())
					setSolvedBoard(board);
				board = boardSave;
				startRow = tempStartRow;
				startCol = tempStartCol;
				boardSave = null;
				tempBoard = null;
			}
		}
	}

	private synchronized boolean startThread(SudokuBoard tempBoard) {
		
		if (counter.getValue() < (Constants.NUM_THREADS)) {
			ParallelBruteForceSolver bruteSolver;
			bruteSolver = new ParallelBruteForceSolver(tempBoard, threadPool, counter, startRow, startCol);
			//new Thread(bruteSolver).start();
			threadPool.execute(bruteSolver);
			//bruteSolver.run();
			return true;
		}
		return false;
		
	}

	public SudokuBoard getSolvedBoard() {
		if (boardSolved)
			return solvedBoard;
		else
			return null;
	}

	public synchronized void clearSolved() {
		boardSolved = false;
		solvedBoard = null;
	}
}
